package jp.educure.household_account_book_app.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.educure.household_account_book_app.entity.Category;

@Mapper
public interface CategoryMapper {
    // ユーザーのカテゴリ一覧
    List<Category> findAllByUserId(@Param("userId") Integer userId);

    // 追加（IDは自動採番）
    void insert(Category c);

    // 名称変更
    void updateName(@Param("id") Integer id,
                    @Param("userId") Integer userId,
                    @Param("name") String name);

    // 削除（今回は物理削除。必要なら論理削除に拡張）
    void deleteById(@Param("id") Integer id,
                    @Param("userId") Integer userId);
}
