FROM maven:3.9.6-eclipse-temurin-22 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (to cache layers)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:24-jdk
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/Inventory-Management-System-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (Render will use this)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]