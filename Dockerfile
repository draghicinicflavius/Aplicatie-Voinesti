# Pasul 1: Construim aplicația folosind Maven
FROM maven:3.8.4-openjdk-17 as build
COPY . .
RUN mvn clean package -DskipTests

# Pasul 2: Luăm doar fișierul rezultat și îl pornim
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]