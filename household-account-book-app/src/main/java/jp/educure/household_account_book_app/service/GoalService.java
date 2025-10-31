package jp.educure.household_account_book_app.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.entity.Goal;
import jp.educure.household_account_book_app.repository.mapper.GoalMapper;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalMapper mapper;

    // 指定ユーザー・指定月の目標データをまるごと返す
    public Goal find(Integer userId, String yearMonth) {
        return mapper.findByUserIdAndYearMonth(userId, yearMonth);
    }

    // 指定ユーザー・指定月の目標金額（targetAmount）だけ返す
    public Integer findTargetAmount(Integer userId, String yearMonth) {
        Goal g = mapper.findByUserIdAndYearMonth(userId, yearMonth);
        if (g == null) {
            return null; // 未設定
        }
        return g.getTargetAmount();
    }

    // 目標を追加 or 更新
    public void upsert(Integer userId, String yearMonth, Integer targetAmount) {
        Goal g = new Goal();
        g.setUserId(userId);
        g.setYearMonth(yearMonth);
        g.setTargetAmount(targetAmount);
        mapper.upsert(g);
    }

    // 目標を削除
    public void delete(Integer userId, String yearMonth) {
        mapper.delete(userId, yearMonth);
    }
}
