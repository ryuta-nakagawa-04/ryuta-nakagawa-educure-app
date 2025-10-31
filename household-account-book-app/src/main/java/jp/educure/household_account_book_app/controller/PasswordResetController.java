// PasswordResetController.java
package jp.educure.household_account_book_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.service.UserService;
import jp.educure.household_account_book_app.entity.User;
import jp.educure.household_account_book_app.form.PasswordResetForm;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    // GET /password-reset?user=ryuta_nakagawa などで呼ばれる想定
    @GetMapping("/password-reset")
    public String showForm(@RequestParam(name = "user", required = false) String userName,
                           Model model) {
        PasswordResetForm form = new PasswordResetForm();
        if (userName != null && !userName.isBlank()) {
            form.setUserName(userName); // hidden に入れる
        }
        model.addAttribute("form", form);
        return "users/password-reset";
    }

    @PostMapping("/password-reset")
    public String reset(@Validated @ModelAttribute("form") PasswordResetForm form,
                        BindingResult result,
                        Model model) {
        // 入力バリデーション
        if (result.hasErrors()) {
            return "users/password-reset";
        }
        if (form.getUserName() == null || form.getUserName().isBlank()) {
            result.rejectValue("userName", "blank", "ユーザーが特定できません");
            return "users/password-reset";
        }
        if (!form.getNewPassword().equals(form.getConfirm())) {
            result.rejectValue("confirm", "mismatch", "確認用パスワードが一致しません");
            return "users/password-reset";
        }

        User user = userService.findByUserName(form.getUserName());
        if (user == null) {
            result.rejectValue("userName", "notfound", "該当するユーザーが見つかりません");
            return "users/password-reset";
        }

        String encoded = encoder.encode(form.getNewPassword());
        userService.changePassword(user.getId(), encoded);

        // 成功後はログインへリダイレクト（PRG）
        return "redirect:/login?reset=ok";
    }
}
