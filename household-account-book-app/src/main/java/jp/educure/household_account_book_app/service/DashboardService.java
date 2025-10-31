package jp.educure.household_account_book_app.service;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jp.educure.household_account_book_app.dto.CategoryTotalRow;
import jp.educure.household_account_book_app.entity.Goal;
import jp.educure.household_account_book_app.entity.Transaction;
import jp.educure.household_account_book_app.repository.mapper.TransactionMapper;
import jp.educure.household_account_book_app.repository.mapper.GoalMapper;
import lombok.RequiredArgsConstructor;
import lombok.Data;

/**
 * ダッシュボード画面に表示する集計情報をまとめるサービス
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionMapper transactionMapper; // 収支データ取得に使う
    private final GoalMapper goalMapper;               // 目標データ取得に使う

    /**
     * 指定したユーザーと年月の収入/支出/差額/目標をまとめて返す
     */
    public Summary buildSummary(Integer userId, String yearMonth) {
        // YearMonth型に変換
        YearMonth ym = YearMonth.parse(yearMonth);

        // 1) この月の全取引を取得（mapperでWHERE句に月を指定するようにしてもOK）
        List<Transaction> all = transactionMapper.findAllActiveByUserId(userId);

        // 2) 対象月だけをフィルタリング
        int totalIn = 0;
        int totalOut = 0;
        for (Transaction t : all) {
            // 取引日の月が一致するものだけ計算
            if (t.getTxnDate() != null &&
                YearMonth.from(t.getTxnDate()).equals(ym)) {

                if ("IN".equals(t.getDirection())) {
                    totalIn += t.getAmount();
                } else if ("OUT".equals(t.getDirection())) {
                    totalOut += t.getAmount();
                }
            }
        }

        int net = totalIn - totalOut; // 収支差額

        // 3) 目標額を取得（goalsテーブル）
        Goal goal = goalMapper.findByUserIdAndYearMonth(userId, yearMonth);

        // 4) まとめて返す
        Summary summary = new Summary();
        summary.setTotalIn(totalIn);
        summary.setTotalOut(totalOut);
        summary.setNet(net);

        if (goal != null) {
            summary.setTargetAmount(goal.getTargetAmount());
            summary.setTargetDiff(goal.getTargetAmount() - totalOut);
        }

        return summary;
    }

    // 円グラフ用データを返す
    public Map<String, Integer> buildCategoryTotals(Integer userId, String yearMonth) {

        // DBからカテゴリ別合計を取得
        List<CategoryTotalRow> rows =
            transactionMapper.sumByCategoryForMonth(userId, yearMonth);

        // Map<カテゴリ名, 合計金額> 形式にして返す
        Map<String, Integer> result = new LinkedHashMap<>();
        for (CategoryTotalRow row : rows) {
            result.put(row.getCategoryName(), row.getTotalAmount());
        }

        return result;
    }

    /**
     * ダッシュボード用の表示データをまとめた内部クラス
     */
    @Data
    public static class Summary {
        private int totalIn;         // 収入合計
        private int totalOut;        // 支出合計
        private int net;             // 差額（収入-支出）
        private Integer targetAmount; // 目標額
        private Integer targetDiff;   // 残り（目標 - 支出）
    }
}
