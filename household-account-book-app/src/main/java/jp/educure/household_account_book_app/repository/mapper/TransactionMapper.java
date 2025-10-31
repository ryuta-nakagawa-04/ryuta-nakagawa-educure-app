package jp.educure.household_account_book_app.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.educure.household_account_book_app.entity.Transaction;
import jp.educure.household_account_book_app.dto.CategoryTotalRow;

@Mapper
public interface TransactionMapper {

    // 既存：未削除一覧
    List<Transaction> findAllActiveByUserId(@Param("userId") Integer userId);

    // 既存：追加
    void insert(Transaction tx);

    // 追加：本人の1件取得
    Transaction findByIdForUser(@Param("id") Integer id, @Param("userId") Integer userId);

    // 追加：更新（影響行数を返す）
    int update(Transaction tx);

    // 追加：論理削除（影響行数を返す）
    int softDelete(@Param("id") Integer id, @Param("userId") Integer userId);

    // ユーザー + 年月 + 支出だけで、カテゴリごとの合計金額を出す
    List<CategoryTotalRow> sumByCategoryForMonth(
        @Param("userId") Integer userId,
        @Param("yearMonth") String yearMonth
    );
}
