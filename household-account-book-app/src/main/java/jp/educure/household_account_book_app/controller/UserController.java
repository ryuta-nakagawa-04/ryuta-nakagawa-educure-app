package jp.educure.household_account_book_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

import jp.educure.household_account_book_app.entity.User;
import jp.educure.household_account_book_app.security.UserPrincipal;
import jp.educure.household_account_book_app.service.UserService;
import jp.educure.household_account_book_app.form.SignupForm;
import jp.educure.household_account_book_app.form.PasswordForm; // ← これを追加

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    // --- サインアップ画面の表示 ---
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("form", new SignupForm());
        return "users/signup";
    }

    // --- サインアップ実行 ---
    @PostMapping("/signup")
    public String register(@Validated @ModelAttribute("form") SignupForm form,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "users/signup";
        }

        if (userService.findByUserName(form.getUserName()) != null) {
            result.rejectValue("userName", "duplicate", "このユーザーネームは既に使われています");
            return "users/signup";
        }

        User u = new User();
        u.setUserName(form.getUserName());
        u.setEmail(form.getEmail());
        u.setPassword(encoder.encode(form.getPassword()));
        u.setAdmin(false);
        u.setDeleted(false);
        userService.save(u);

        return "redirect:/login?signup=ok";
    }

    // --- パスワード変更画面 ---
    @GetMapping("/password-change")
    public String showPasswordForm(Model model) {
        model.addAttribute("form", new PasswordForm());
        return "users/password-change";
    }

    // --- パスワード変更実行 ---
    @PostMapping("/password-change")
    public String changePassword(@AuthenticationPrincipal UserPrincipal principal,
                                 @Validated @ModelAttribute("form") PasswordForm form,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "users/password-change";
        }

        // next と confirm が一致するかを手動チェック
        if (!form.getNext().equals(form.getConfirm())) {
            result.rejectValue("confirm", "mismatch", "確認用パスワードが一致しません");
            return "users/password-change";
        }

        // ログイン中のユーザーを取得
        User me = userService.findById(principal.getId());

        // パスワードをエンコードして保存
        String encoded = encoder.encode(form.getNext());
        userService.changePassword(me.getId(), encoded);

        redirectAttributes.addFlashAttribute("pwdChanged", true);

        // ログイン画面へリダイレクト
        return "redirect:/settings";
    }

}
