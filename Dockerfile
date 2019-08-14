FROM maven:3.6.1-jdk-12 AS MAVEN_TOOL_CHAIN

COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/

RUN mvn clean -Dmaven.test.skip=true -Dspring.profiles.active=linuxDeploy,default package -f pom.xml

FROM adoptopenjdk/openjdk12

RUN sh -c 'apt-get update'
RUN sh -c 'apt-get -y install chromium-browser'

COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar app.jar
# Make sure to provide an environment variable `BOT_TOKEN`
ENTRYPOINT exec java -Dspring.profiles.active=linuxDeploy,default -jar /app.jar $BOT_TOKEN