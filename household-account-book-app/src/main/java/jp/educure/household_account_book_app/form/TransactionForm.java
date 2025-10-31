package jp.educure.household_account_book_app.form;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TransactionForm {

    @NotNull(message = "日付を入力してください")
    private LocalDate txnDate;

    @NotBlank(message = "収支区分を選択してください")
    @Pattern(regexp = "IN|OUT", message = "収支区分はINまたはOUTで入力してください")
    private String direction;

    @NotNull(message = "金額を入力してください")
    @Min(value = 0, message = "金額は0以上で入力してください")
    private Integer amount;

    @Size(max = 255, message = "メモは255文字以内で入力してください")
    private String memo;

    @NotNull(message = "カテゴリを選択してください")
    private Integer categoryId; // プルダウンから入る
}
