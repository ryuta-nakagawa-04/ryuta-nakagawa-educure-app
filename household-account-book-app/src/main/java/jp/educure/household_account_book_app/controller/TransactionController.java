package jp.educure.household_account_book_app.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import jp.educure.household_account_book_app.entity.Transaction;
import jp.educure.household_account_book_app.form.TransactionForm;
import jp.educure.household_account_book_app.security.UserPrincipal;
import jp.educure.household_account_book_app.service.TransactionService;
import jp.educure.household_account_book_app.service.CategoryService;
import jp.educure.household_account_book_app.service.DashboardService;
import jp.educure.household_account_book_app.form.GoalForm;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal principal,
                            @RequestParam(name="month", required=false) String month,
                            Model model) {

        Integer userId = principal.getId();
        String ym = (month == null || month.isBlank()) ? YearMonth.now().toString() : month;

        // ダッシュボード合計とか
        model.addAttribute("summary", dashboardService.buildSummary(userId, ym));
        model.addAttribute("selectedMonth", ym);

        // 直近の取引リスト
        List<Transaction> list = transactionService.listActive(userId);
        model.addAttribute("transactions", list);

        // 画面下部の簡易登録フォーム用
        TransactionForm form = new TransactionForm();
        form.setTxnDate(LocalDate.now());
        form.setDirection("OUT");
        model.addAttribute("form", form);
        model.addAttribute("categories", categoryService.list(userId));

        model.addAttribute("goalForm", new GoalForm());

        // ★カテゴリごとの合計金額（OUTのみ）
        // Map<カテゴリ名, 合計金額> をサービスからもらってる前提
        var totalsMap = dashboardService.buildCategoryTotals(userId, ym);

        // null ガード
        if (totalsMap == null || totalsMap.isEmpty()) {
            model.addAttribute("categoryLabels", java.util.List.of());
            model.addAttribute("categoryValues", java.util.List.of());
        } else {
            var labels = new java.util.ArrayList<String>(totalsMap.keySet());
            var values = new java.util.ArrayList<Integer>();
            for (String k : labels) {
                values.add(totalsMap.get(k));
            }
            model.addAttribute("categoryLabels", labels);
            model.addAttribute("categoryValues", values);
        }

        return "dashboard";
    }

    @GetMapping("/transactions") // 未削除一覧
    public String list(@AuthenticationPrincipal UserPrincipal principal,
                       Model model) {

        Integer userId = principal.getId();
        
        model.addAttribute("transactions", transactionService.listActive(userId));
        
        return "transactions/list";
    }

    @GetMapping("/transactions/new") // 新規フォーム
    public String newForm(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        Integer userId = principal.getId();
        TransactionForm form = new TransactionForm();
        form.setTxnDate(LocalDate.now());
        form.setDirection("OUT");
        model.addAttribute("form", form);
        model.addAttribute("categories", categoryService.list(userId));
        return "transactions/new";
    }

    @PostMapping("/transactions") // 追加
    public String create(@AuthenticationPrincipal UserPrincipal principal,
                         @Validated @ModelAttribute("form") TransactionForm form,
                         BindingResult result,
                         Model model,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Integer userId = principal.getId();

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.list(userId)); // エラー時もプルダウン再投入
            return "transactions/new";
        }

        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setTxnDate(form.getTxnDate());
        tx.setDirection(form.getDirection());
        tx.setAmount(form.getAmount());
        tx.setMemo(form.getMemo());
        tx.setCategoryId(form.getCategoryId());
        transactionService.add(tx);

        // 成功メッセージをFlashに積む
        redirectAttributes.addFlashAttribute("flashMessage", "収支を登録しました。");

        return "redirect:/transactions"; // 登録後は一覧へ（遷移図の矢印に合わせてOK）
    }

    @GetMapping("/transactions/{id}/edit") // 編集フォーム
    public String edit(@AuthenticationPrincipal UserPrincipal principal,
                       @PathVariable Integer id, Model model) {
        Integer userId = principal.getId();
        Transaction tx = transactionService.findById(id, userId);

        TransactionForm form = new TransactionForm();
        form.setTxnDate(tx.getTxnDate());
        form.setDirection(tx.getDirection());
        form.setAmount(tx.getAmount());
        form.setMemo(tx.getMemo());
        form.setCategoryId(tx.getCategoryId());

        model.addAttribute("form", form);
        model.addAttribute("id", id);
        model.addAttribute("categories", categoryService.list(userId));
        return "transactions/edit";
    }

    @PostMapping("/transactions/{id}") // 更新
    public String update(@AuthenticationPrincipal UserPrincipal principal,
                         @PathVariable Integer id,
                         @Validated @ModelAttribute("form") TransactionForm form,
                         BindingResult result,
                         Model model,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Integer userId = principal.getId();

        if (result.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("categories", categoryService.list(userId));
            return "transactions/edit";
        }

        Transaction tx = new Transaction();
        tx.setId(id);
        tx.setUserId(userId);
        tx.setTxnDate(form.getTxnDate());
        tx.setDirection(form.getDirection());
        tx.setAmount(form.getAmount());
        tx.setMemo(form.getMemo());
        tx.setCategoryId(form.getCategoryId());
        transactionService.update(tx);

        redirectAttributes.addFlashAttribute("flashMessage", "収支を編集しました。");

        return "redirect:/transactions";
    }

    @PostMapping("/transactions/{id}/delete") // 論理削除
    public String delete(@AuthenticationPrincipal UserPrincipal principal,
                         @PathVariable Integer id,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        transactionService.softDelete(id, principal.getId());

        redirectAttributes.addFlashAttribute("flashMessage", "収支を削除しました。");

        return "redirect:/transactions";
    }
}
