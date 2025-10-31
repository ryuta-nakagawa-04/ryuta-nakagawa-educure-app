// PasswordResetForm.java
package jp.educure.household_account_book_app.form;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PasswordResetForm {

    // hidden で持つ（画面には表示しない）
    @NotBlank(message = "ユーザーIDを入力してください")
    private String userName;

    @NotBlank(message = "新しいパスワードを入力してください")
    @Size(min = 6, max = 100, message = "パスワードは6〜100文字で入力してください")
    private String newPassword;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirm;
}
