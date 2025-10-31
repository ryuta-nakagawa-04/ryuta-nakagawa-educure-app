// AccountController.java
package jp.educure.household_account_book_app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jp.educure.household_account_book_app.security.UserPrincipal;
import jp.educure.household_account_book_app.service.UserService;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    // 退会確認ページ
    @GetMapping("/users/withdraw")
    public String showWithdraw() {
        return "users/withdraw";
    }

    // 「はい」押下 → 退会処理 → ログアウト → 完了ページへ
    @PostMapping("/users/withdraw")
    public String doWithdraw(@AuthenticationPrincipal UserPrincipal principal,
                             HttpServletRequest req, HttpServletResponse res) {
        userService.withdraw(principal.getId());           // deleted=true など
        new SecurityContextLogoutHandler().logout(req, res, null);
        return "redirect:/users/withdraw/done";
    }

    // 退会処理後フォーム（公開で見せる完了画面）
    @GetMapping("/users/withdraw/done")
    public String withdrawDone() {
        return "users/withdraw-done";
    }
}
