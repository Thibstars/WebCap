FROM maven:3.6.0-jdk-11-slim AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/

RUN mvn clean package -f pom.xml

FROM adoptopenjdk/openjdk11
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]