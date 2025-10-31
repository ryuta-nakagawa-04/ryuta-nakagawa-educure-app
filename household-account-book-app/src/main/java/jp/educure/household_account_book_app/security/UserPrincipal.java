package jp.educure.household_account_book_app.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jp.educure.household_account_book_app.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final Integer id;           // DBの users.id
    private final String username;      // ログインID（users.user_name）
    private final String email;         // メール（任意）
    private final String password;      // ハッシュ済パスワード
    private final boolean isAdmin;      // 管理者フラグ
    private final boolean deleted;      // 退会フラグ

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 管理者は ROLE_ADMIN のみ付与（← 一般画面に入れないようにする）
        // 一般ユーザーは ROLE_USER のみ付与
        String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of((GrantedAuthority) () -> role);
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    // 退会済みは無効＝ログイン不可
    @Override public boolean isEnabled() { return !deleted; }

    // Entity → Security用ユーザーに変換（サービス/認証で使用）
    public static UserPrincipal fromEntity(User user) {
        return new UserPrincipal(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            user.getPassword(),
            Boolean.TRUE.equals(user.isAdmin()),
            Boolean.TRUE.equals(user.isDeleted())
        );
    }
}
