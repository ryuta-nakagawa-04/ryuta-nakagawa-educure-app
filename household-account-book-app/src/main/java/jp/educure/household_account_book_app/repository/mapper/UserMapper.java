package jp.educure.household_account_book_app.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.educure.household_account_book_app.entity.User;

@Mapper
public interface UserMapper {

    // 管理者の人数を数える（退会者は除外）
    int countAdmins();

    // 追加（自動採番でidを返す）
    void insert(User user);

    // 一覧取得（退会者は除外）
    List<User> findAllActive();

    // user_name で1件（退会者は除外：ログイン不可）
    User findByUserName(String userName);

    // idで1件
    User findById(Integer id);

    // パスワード更新
    void updatePassword(@Param("id") Integer id, @Param("password") String password);

    // 退会処理
    void markDeleted(@Param("id") Integer id);
}
