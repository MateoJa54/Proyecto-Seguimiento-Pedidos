# Etapa de construcción
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

# Etapa runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Aquí debes copiar el jar correcto según tu pom.xml
COPY --from=builder /app/target/oauth2-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
