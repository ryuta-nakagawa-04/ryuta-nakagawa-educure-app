package jp.educure.household_account_book_app.dto;

import lombok.Data;

@Data
public class CategoryTotalRow {
    private String categoryName; // カテゴリ名（"食費"とか）
    private Integer totalAmount; // そのカテゴリの合計金額
}
