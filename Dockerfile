FROM openjdk:24-jdk
WORKDIR /app
COPY ./target/Inventory-Management-System-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]