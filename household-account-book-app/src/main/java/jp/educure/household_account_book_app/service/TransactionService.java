package jp.educure.household_account_book_app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.entity.Transaction;
import jp.educure.household_account_book_app.repository.mapper.TransactionMapper;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper mapper; // DBアクセス用（Mapper）

    // 未削除（deleted_at IS NULL）の取引をユーザーIDで全件取得
    public List<Transaction> listActive(Integer userId) {
        // Mapper側で userId と deleted_at IS NULL 条件を付けて取得
        return mapper.findAllActiveByUserId(userId);
    }

    // 1件取得（本人データのみ）。見つからない場合は null を返す簡易版
    public Transaction findById(Integer id, Integer userId) {
        // id と userId の両方で絞って “本人のレコードのみ” 取得
        return mapper.findByIdForUser(id, userId);
    }

    // 取引を1件登録
    @Transactional
    public void add(Transaction tx) {
        // INSERT を実行（useGeneratedKeys により tx.id に採番が入る想定）
        mapper.insert(tx);
    }

    // 取引を更新（本人のレコードのみ）
    @Transactional
    public boolean update(Transaction tx) {
        // WHERE id = #{id} AND user_id = #{userId} AND deleted_at IS NULL で更新
        // 影響行数を返して、0なら未更新（他人/削除済み/存在しない）
        int updated = mapper.update(tx);
        return updated > 0;
    }

    // 論理削除（deleted_at に now() を入れる／本人のレコードのみ）
    @Transactional
    public boolean softDelete(Integer id, Integer userId) {
        // WHERE id = #{id} AND user_id = #{userId} AND deleted_at IS NULL で更新
        int updated = mapper.softDelete(id, userId);
        return updated > 0;
    }
}
