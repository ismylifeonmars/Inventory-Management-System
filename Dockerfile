FROM openjdk:24-jdk
ARG JAR_FILE=target/*.jar
COPY ./target/Inventory-Management-System-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${PORT:-8080}
ENTRYPOINT ["java","-jar","/app.jar"]