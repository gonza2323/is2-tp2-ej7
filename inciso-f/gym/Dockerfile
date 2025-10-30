FROM openjdk:17-jdk-slim
LABEL authors="agusolivares"
WORKDIR /app

ARG JAR_FILE=target/gimnasio-sport-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} gimnasio-sport.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gimnasio-sport.jar"]
