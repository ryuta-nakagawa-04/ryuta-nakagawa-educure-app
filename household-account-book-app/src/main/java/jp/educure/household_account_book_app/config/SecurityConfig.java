package jp.educure.household_account_book_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jp.educure.household_account_book_app.security.UserPrincipal;
import jp.educure.household_account_book_app.security.LoginSuccessHandler;
import jp.educure.household_account_book_app.service.UserService;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        // DBの users から username(=user_name) で取得して UserPrincipal に変換
        return username -> {
            var u = userService.findByUserName(username);      // ユーザー検索
            if (u == null) throw new UsernameNotFoundException("User not found: " + username);
            return UserPrincipal.fromEntity(u);                // Security用ユーザーへ変換
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder encoder) {
        // 認証プロバイダ（DBのユーザー＋BCrypt照合）
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);    // ユーザー取得方法
        provider.setPasswordEncoder(encoder);                  // パスワードのハッシュ方式
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationProvider provider,
                                           LoginSuccessHandler successHandler) throws Exception {
        http
          .authenticationProvider(provider)                    // 自前の認証を使う
          .authorizeHttpRequests(auth -> auth
              // 誰でもOK（ログイン前に見せるもの）
              .requestMatchers("/css/**","/js/**","/images/**",
                               "/login", "/users/signup", "/password-reset", "/users/withdraw/done").permitAll()

              // 管理者だけ（/admin/** は ROLE_ADMIN 必須）
              .requestMatchers("/admin/**").hasRole("ADMIN")

              // 家計簿の一般画面は ROLE_USER 必須にする
              // ここにトップや取引/カテゴリ/目標など、一般向けURLを並べる
              .requestMatchers("/", "/transactions/**", "/categories/**", "/goals/**", "/settings")
                  .hasRole("USER")

              // その他はログインさえしていればOK（必要ならさらに絞ってOK）
              .anyRequest().authenticated()
          )
          .formLogin(login -> login
              .loginPage("/login")                            // ログイン画面のURL
              .loginProcessingUrl("/login")                   // フォームのPOST先（Securityが処理）
              .successHandler(successHandler)                 // 成功時に管理者/一般で分岐
              .failureUrl("/login?error")                     // 失敗時
              .permitAll()
          )
          .logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/login?logout")
              .permitAll()
          )
          .csrf(Customizer.withDefaults());                   // CSRFはデフォルト有効（フォームはトークン埋め込む）

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // パスワードは BCrypt で保存＆照合
        return new BCryptPasswordEncoder();
    }
}
