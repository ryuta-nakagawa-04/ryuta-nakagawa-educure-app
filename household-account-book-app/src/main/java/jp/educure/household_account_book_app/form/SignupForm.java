package jp.educure.household_account_book_app.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * サインアップ画面のフォーム
 */
@Data
public class SignupForm {

    @NotBlank(message = "ユーザーネームを入力してください")
    @Size(max = 50, message = "ユーザーネームは50文字以内で入力してください")
    private String userName;

    @NotBlank(message = "メールアドレスを入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 6, max = 100, message = "パスワードは6〜100文字で入力してください")
    private String password;
}
