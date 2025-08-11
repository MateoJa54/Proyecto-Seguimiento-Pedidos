FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/tracking-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app.jar"]
