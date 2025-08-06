# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos los archivos necesarios del proyecto
COPY pom.xml .
COPY src ./src

# Construimos el proyecto y generamos el .jar
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con JDK para ejecutar el jar
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar el jar generado desde la etapa de construcción
COPY --from=build /app/target/client-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto del microservicio
EXPOSE 8081

# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]
