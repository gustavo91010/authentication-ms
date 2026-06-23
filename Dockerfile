# FROM maven:3.8.5-openjdk-17 AS build

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# FROM eclipse-temurin:17-jdk-jammy
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/porteiro-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
