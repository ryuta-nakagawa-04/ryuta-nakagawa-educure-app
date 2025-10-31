package jp.educure.household_account_book_app.form;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GoalForm {
    @NotBlank(message = "対象月を指定してください")
    private String yearMonth;           // "yyyy-MM"

    @NotNull(message = "目標額を入力してください")
    @Min(value = 0, message = "目標額は0以上で入力してください")
    private Integer targetAmount;
}
