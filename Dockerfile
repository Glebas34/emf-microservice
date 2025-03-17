FROM openjdk:21-bullseye

WORKDIR /app

COPY emf_microservice/.mvn/ .mvn
COPY emf_microservice/mvnw emf_microservice/pom.xml ./
RUN ./mvnw dependency:go-offline

COPY emf_microservice/src ./src

CMD ["./mvnw", "spring-boot:run"]