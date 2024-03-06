FROM maven:3.9.6-eclipse-temurin-21 AS MAVEN_BUILD
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn clean package

FROM openjdk:21
EXPOSE 8080
COPY --from=MAVEN_BUILD target/ApiConsumer-*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]