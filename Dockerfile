FROM maven:3.6.1-jdk-12 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/

# Manually install missing shared libs for Chromium.
RUN sh -c 'yum -y install libX11'

RUN mvn clean package -f pom.xml

FROM adoptopenjdk/openjdk12
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar app.jar
# Make sure to provide an environment variable `BOT_TOKEN`
ENTRYPOINT exec java -jar /app.jar $BOT_TOKEN