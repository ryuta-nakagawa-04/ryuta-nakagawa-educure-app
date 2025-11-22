# ビルド用ステージ
FROM gradle:8.2.1-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# 実行用ステージ
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
