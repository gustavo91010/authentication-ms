FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/myapp.jar

# Criação do arquivo com conteúdo
RUN mkdir -p /root/.aws && \
    echo "[vm_007]" > /root/.aws/credentials && \
    echo "aws_access_key_id = REMOVED" >> /root/.aws/credentials  && \
    echo "aws_secret_access_key = 3lxxcGB2+21wvLMjBPHCVx3m8AU8BnXLgeP8QD/M" >> /root/.aws/credentials

ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]

# FROM maven:3.8.5-openjdk-17 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean install -DskipTests

# FROM openjdk:17-jdk-slim
# WORKDIR /app
# COPY --from=build /app/target/*.jar /app/myapp.jar
# ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]




# FROM openjdk:17-jdk-slim
# WORKDIR /app
# COPY target/*.jar /app/myapp.jar
# ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]

