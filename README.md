家計簿アプリケーション

●概要
Java / Spring Boot / PostgreSQL の家計簿アプリ。
収支CRUD・カテゴリCRUD・月次目標・ダッシュボード（円グラフ） を提供。

Renderにてデプロイ。URLは以下
https://ryuta-nakagawa-educure-app.onrender.com

●主な機能
・収支の登録 / 編集 / 削除
・カテゴリの追加 / 編集 / 削除
・月次目標収支の設定（目標/実績をダッシュボードで可視化）
・カテゴリ別支出の円グラフ（Chart.js）
・操作結果をフラッシュメッセージで表示

●技術スタック
・Java 21 / Spring Boot 3.5.7（MVC, Security）
・MyBatis（XML Mapper）/ Lombok
・PostgreSQL 17
・Thymeleaf / HTML / CSS / Chart.js
・Gradle
