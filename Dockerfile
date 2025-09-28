FROM maven:3.9-eclipse-temurin-23 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]