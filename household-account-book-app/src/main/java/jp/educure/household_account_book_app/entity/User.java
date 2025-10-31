package jp.educure.household_account_book_app.entity;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class User {
    private Integer id;
    private String userName;    // ログインID
    private String email;
    private String password;
    private boolean admin;    // 管理者フラグ
    private boolean deleted;    // 退会フラグ
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}