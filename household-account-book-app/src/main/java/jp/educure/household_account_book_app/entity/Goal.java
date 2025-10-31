package jp.educure.household_account_book_app.entity;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class Goal {
    private Integer id;              // 自動採番ID
    private Integer userId;          // users.id
    private String yearMonth;        // "yyyy-MM"（例: 2025-10）
    private Integer targetAmount;    // 月の目標（支出上限）
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
