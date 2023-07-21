FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/speed-reading-app-2.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]