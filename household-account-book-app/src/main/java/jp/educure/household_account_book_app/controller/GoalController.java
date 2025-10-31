package jp.educure.household_account_book_app.controller;

import java.time.YearMonth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.form.GoalForm;
import jp.educure.household_account_book_app.security.UserPrincipal;
import jp.educure.household_account_book_app.service.GoalService;

@Controller
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    // ① 目標設定画面の表示（GET /goals）
    @GetMapping("/goals")
    public String showGoalPage(@AuthenticationPrincipal UserPrincipal principal,
                               @RequestParam(name = "month", required = false) String month,
                               Model model) {

        Integer userId = principal.getId();

        // 表示対象の年月（指定がなければ今月）
        String ym = (month == null || month.isBlank())
                ? YearMonth.now().toString() // "2025-10" 形式
                : month;

        // いまDBに入ってる目標額（なければ null）
        Integer currentTarget = goalService.findTargetAmount(userId, ym);

        // 画面に表示する「現在の目標」用
        model.addAttribute("currentTarget", currentTarget);

        // 入力用フォームの初期データ
        GoalForm form = new GoalForm();
        form.setYearMonth(ym);
        form.setTargetAmount(currentTarget); // 既存の値を初期表示してあげる
        model.addAttribute("goalForm", form);

        return "goals"; // templates/goals.html
    }

    // ② 目標の登録・更新（POST /goals）
    @PostMapping("/goals")
    public String upsert(@AuthenticationPrincipal UserPrincipal principal,
                         @Validated @ModelAttribute("goalForm") GoalForm form,
                         BindingResult result,
                         Model model,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Integer userId = principal.getId();

        // 入力エラーがあった場合は、goals画面をそのまま再表示する
        if (result.hasErrors()) {
            // もう一度この月の現在値を画面に出せるように詰め直す
            Integer currentTarget = goalService.findTargetAmount(userId, form.getYearMonth());
            model.addAttribute("currentTarget", currentTarget);

            // form自体（goalForm）は @ModelAttribute で既に入ってるのでOK
            return "goals";
        }

        // DBに保存 or 更新
        goalService.upsert(userId, form.getYearMonth(), form.getTargetAmount());

        // ★ ダッシュボードに渡したいメッセージをFlashAttributeで積む
        redirectAttributes.addFlashAttribute("flashMessage", "目標収支を設定しました。");

        // ★ ダッシュボードに戻る（月も維持したいならクエリ付きで戻る）
        return "redirect:/dashboard?month=" + form.getYearMonth();
    }
}