FROM gradle:jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 



FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar build/app.jar
WORKDIR /app/build
ENTRYPOINT java -jar app.jar
