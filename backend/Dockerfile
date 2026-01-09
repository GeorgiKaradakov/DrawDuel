# ---- Build Stage ----
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle clean :infrastructure:build -x test

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy ONLY the executable Spring Boot JAR (exclude "-plain.jar")
COPY --from=builder /app/infrastructure/build/libs/*[^plain].jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
