package jp.educure.household_account_book_app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.entity.User;
import jp.educure.household_account_book_app.repository.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;

    @Transactional
    public void save(User u) {
        // 既に管理者がいるか？
        boolean hasAdmin = mapper.countAdmins() > 0;

        // 最初の1人だけ管理者にする
        if (!hasAdmin) {
            u.setAdmin(true);
        }

        mapper.insert(u);
    }

    // 一覧取得
    public List<User> findAllActive() {
        return mapper.findAllActive();
    }

    // ログインID（user_name）から1件取得
    public User findByUserName(String userName) {
        return mapper.findByUserName(userName);
    }

    // IDで1件取得
    public User findById(Integer id) {
        return mapper.findById(id);
    }

    // パスワード更新（BCrypt済みの文字列を渡す）
    @Transactional
    public void changePassword(Integer id, String encodedPassword) {
        mapper.updatePassword(id, encodedPassword);
    }

    // 退会処理
    @Transactional
    public void withdraw(Integer userId) {
        mapper.markDeleted(userId);
    }
}
