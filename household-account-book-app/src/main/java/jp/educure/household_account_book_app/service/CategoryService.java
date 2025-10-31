package jp.educure.household_account_book_app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.entity.Category;
import jp.educure.household_account_book_app.repository.mapper.CategoryMapper;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper mapper;

    // 一覧（プルダウンや管理画面で使う）
    public List<Category> list(Integer userId) {
        return mapper.findAllByUserId(userId);
    }

    // 追加
    public void add(Integer userId, String name) {
        Category c = new Category();
        c.setUserId(userId);
        c.setName(name);
        mapper.insert(c);
    }

    // 名称変更
    public void rename(Integer userId, Integer id, String newName) {
        mapper.updateName(id, userId, newName);
    }

    // 削除
    public void delete(Integer userId, Integer id) {
        mapper.deleteById(id, userId);
    }
}
