package jp.educure.household_account_book_app.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class Transaction {
    private Integer id;                 // 収支ID（自動採番）
    private Integer userId;             // users.id（今回は学習用に1固定で可）
    private Integer categoryId;         // categories.id（NULL可：カテゴリ削除時にNULLになるため）
    private Integer amount;             // 金額（0以上）
    private LocalDate txnDate;          // 取引日
    private String categoryName;        // カテゴリ名
    private String direction;           // "IN" or "OUT"
    private String memo;                // メモ
    private OffsetDateTime createdAt;   // 登録日時
    private OffsetDateTime updatedAt;   // 更新日時
    private OffsetDateTime deletedAt;   // 削除日時（NULL=未削除）
}
