# Etapa de construcción
FROM openjdk:17-jdk-alpine as builder

WORKDIR /app

COPY ./pom.xml /app
COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

# Imagen final
FROM openjdk:17-jdk-alpine

WORKDIR /app
RUN mkdir ./logs

COPY --from=builder /app/target/orders-0.0.1-SNAPSHOT.jar .

# Expone el puerto de forma dinámica usando variable de entorno
EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "orders-0.0.1-SNAPSHOT.jar"]
