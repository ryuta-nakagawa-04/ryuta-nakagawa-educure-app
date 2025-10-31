package jp.educure.household_account_book_app.entity;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class Category {
    private Integer id;               // カテゴリID（自動採番）
    private Integer userId;           // 所有ユーザーID（users.id）
    private String name;              // カテゴリ名（例：食費）
    private OffsetDateTime createdAt; // 登録日時
    private OffsetDateTime updatedAt; // 更新日時
}
