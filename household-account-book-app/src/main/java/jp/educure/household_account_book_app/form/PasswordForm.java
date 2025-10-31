package jp.educure.household_account_book_app.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * パスワード変更画面のフォーム
 */
@Data
public class PasswordForm {

    @NotBlank(message = "新しいパスワードを入力してください")
    @Size(min = 6, max = 100, message = "パスワードは6〜100文字で入力してください")
    private String next;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirm;
}
