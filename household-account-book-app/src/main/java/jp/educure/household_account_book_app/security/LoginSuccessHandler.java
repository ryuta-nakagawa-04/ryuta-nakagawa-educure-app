package jp.educure.household_account_book_app.security;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        // ログイン直後に呼ばれる。ここで行き先を権限で分ける
        UserPrincipal p = (UserPrincipal) authentication.getPrincipal(); // ログインユーザー情報を取得

        if (p.isAdmin()) {
            // 管理者はユーザー一覧へ
            response.sendRedirect("/admin/users");
        } else {
            // 一般ユーザーは家計簿トップへ
            response.sendRedirect("/dashboard");
        }
    }
}
