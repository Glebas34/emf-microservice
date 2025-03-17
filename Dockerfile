FROM openjdk:latest
ARG JAR_FILE=emf_microservice/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]