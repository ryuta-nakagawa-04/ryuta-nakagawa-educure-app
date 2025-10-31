package jp.educure.household_account_book_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.service.UserService;

@Controller
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    // 管理者用：ユーザー一覧
    @GetMapping("/admin/users")
    public String list(Model model) {
        model.addAttribute("users", userService.findAllActive());
        return "admin/users"; // templates/admin/users.html を返す
    }
}
