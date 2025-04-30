FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]
