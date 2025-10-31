package jp.educure.household_account_book_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.RequiredArgsConstructor;

import jp.educure.household_account_book_app.entity.Category;
import jp.educure.household_account_book_app.form.CategoryForm;
import jp.educure.household_account_book_app.security.UserPrincipal;
import jp.educure.household_account_book_app.service.CategoryService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    // 一覧画面
    @GetMapping
    public String list(@AuthenticationPrincipal UserPrincipal principal,
                       Model model) {
        Integer userId = principal.getId();
        model.addAttribute("categories", service.list(userId));
        return "categories/list";
    }

    // 追加フォーム表示 (GET /categories/new)
    @GetMapping("/new")
    public String showNewForm(@AuthenticationPrincipal UserPrincipal principal,
                              Model model) {
        model.addAttribute("form", new CategoryForm());
        return "categories/new";
    }

    // 追加処理 (POST /categories/new)
    @PostMapping("/new")
    public String create(@AuthenticationPrincipal UserPrincipal principal,
                         @ModelAttribute("form") CategoryForm form,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {

        Integer userId = principal.getId();

        // 追加画面では name が必須
        if (form.getName() == null || form.getName().isBlank()) {
            result.rejectValue("name", "required", "カテゴリ名を入力してください");
        }

        if (result.hasErrors()) {
            // 入力エラーがあれば同じフォームに戻す
            return "categories/new";
        }

        // DBに追加
        service.add(userId, form.getName());

        // フラッシュメッセージを設定
        String msg = "「" + form.getName() + "」カテゴリを追加しました。";
        redirectAttributes.addFlashAttribute("flashMessage", msg);

        // 設定画面へ戻る
        return "redirect:/settings";
    }

    // 編集フォーム表示（POST /categories/edit）
    @GetMapping("/edit")
    public String showEditPage(Model model) {
        // 空のフォームを渡す
        CategoryForm form = new CategoryForm();
        model.addAttribute("form", form);
        return "categories/edit";
    }

    // 編集実行（POST /categories/edit）
    @PostMapping("/edit")
    public String doEdit(@AuthenticationPrincipal UserPrincipal principal,
                        @ModelAttribute("form") CategoryForm form,
                        BindingResult result,
                        RedirectAttributes redirectAttributes,
                        Model model) {

        Integer userId = principal.getId();

        // 編集では beforeName と name が必須
        if (form.getBeforeName() == null || form.getBeforeName().isBlank()) {
            result.rejectValue("beforeName", "required", "編集前のカテゴリ名を入力してください");
        }
        if (form.getName() == null || form.getName().isBlank()) {
            result.rejectValue("name", "required", "編集後のカテゴリ名を入力してください");
        }

        if (result.hasErrors()) {
            model.addAttribute("form", form);
            return "categories/edit";
        }

        // beforeName のカテゴリを探す
        Category target = service.list(userId).stream()
            .filter(c -> c.getName().equals(form.getBeforeName()))
            .findFirst()
            .orElse(null);

        if (target == null) {
            result.rejectValue("beforeName", "notfound", "そのカテゴリが見つかりません");
            model.addAttribute("form", form);
            return "categories/edit";
        }

        // リネーム
        service.rename(userId, target.getId(), form.getName());

        redirectAttributes.addFlashAttribute(
            "flashMessage",
            "「" + form.getBeforeName() + "」を「" + form.getName() + "」に編集しました。"
        );

        return "redirect:/settings";
    }

    // 削除（GET /categories/delete）
    @GetMapping("/delete")
    public String showDeletePage(Model model) {
        CategoryForm form = new CategoryForm();
        model.addAttribute("form", form);
        return "categories/delete";
    }

    // ====== 削除実行（POST /categories/delete） ======
    @PostMapping("/delete")
    public String doDelete(@AuthenticationPrincipal UserPrincipal principal,
                       @ModelAttribute("form") CategoryForm form,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model) {

        Integer userId = principal.getId();

        // 削除では beforeName が必須
        if (form.getBeforeName() == null || form.getBeforeName().isBlank()) {
            result.rejectValue("beforeName", "required", "削除するカテゴリ名を入力してください");
        }

        if (result.hasErrors()) {
            model.addAttribute("form", form);
            return "categories/delete";
        }

        // beforeName でカテゴリ検索
        Category target = service.list(userId).stream()
            .filter(c -> c.getName().equals(form.getBeforeName()))
            .findFirst()
            .orElse(null);

        if (target == null) {
            result.rejectValue("beforeName", "notfound", "そのカテゴリが見つかりません");
            model.addAttribute("form", form);
            return "categories/delete";
        }

        // 削除
        service.delete(userId, target.getId());

        redirectAttributes.addFlashAttribute(
            "flashMessage",
            "「" + target.getName() + "」カテゴリを削除しました。"
        );

        return "redirect:/settings";
    }
}