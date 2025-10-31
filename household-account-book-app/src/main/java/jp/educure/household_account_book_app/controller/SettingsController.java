package jp.educure.household_account_book_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String settings() {
        // templates/settings.html を返す
        return "settings";
    }
}
