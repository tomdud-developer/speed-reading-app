FROM openjdk:16-jdk-alpine
ARG JAR_FILE=build/libs/speed-reading-app-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]