FROM ghcr.io/graalvm/native-image-community:21 AS build

WORKDIR /app
COPY . .

RUN ./mvnw -Pnative -DskipTests native:compile

FROM debian:bookworm-slim
WORKDIR /app

COPY --from=build /app/target/porteiro-api app

ENTRYPOINT ["./app"]
