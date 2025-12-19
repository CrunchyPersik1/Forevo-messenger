# Используем официальный образ с OpenJDK 17
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и собираем зависимости (для кэша)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем весь исходный код
COPY src ./src

# Собираем JAR
RUN mvn clean package -DskipTests

# Второй этап: минимальный образ для запуска
FROM eclipse-temurin:17-jre-jammy

# Копируем JAR из первого этапа
COPY --from=builder /app/target/forevo-messenger-1.0-SNAPSHOT.jar /app.jar

# Открываем порт (Render по умолчанию смотрит 8080)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app.jar"]
