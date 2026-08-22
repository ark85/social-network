FROM maven:3.9-eclipse-temurin-26 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -U clean package -DskipTests

FROM eclipse-temurin:26-jre

RUN apt-get update && apt-get install -y curl

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
