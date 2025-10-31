package jp.educure.household_account_book_app.form;

import lombok.Data;

@Data
public class CategoryForm {
    // 既存編集用にIDを使う（新規のときはnullでOK）
    private Integer id;

    // 画面上 “編集前のカテゴリ名” / “削除するカテゴリ”
    private String beforeName;

    // 画面上 “編集後のカテゴリ名”（編集時に変更入力する用）
    private String name;
}
