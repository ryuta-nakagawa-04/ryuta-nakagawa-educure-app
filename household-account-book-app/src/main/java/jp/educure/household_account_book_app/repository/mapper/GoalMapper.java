package jp.educure.household_account_book_app.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.educure.household_account_book_app.entity.Goal;

@Mapper
public interface GoalMapper {
    Goal findByUserIdAndYearMonth(@Param("userId") Integer userId,
                                  @Param("yearMonth") String yearMonth);
    void upsert(Goal goal);  // 存在すれば更新、無ければ追加
    void delete(@Param("userId") Integer userId,
                @Param("yearMonth") String yearMonth); // 任意（必要なら）
}
