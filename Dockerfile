FROM maven:3.9.4-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.compiler.failOnError=false

FROM openjdk:21-jdk-slim
COPY --from=build /app/target/example_34-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "/app.jar"]