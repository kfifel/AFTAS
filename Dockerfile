
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/AFTAS-API-0.0.1-SNAPSHOT.war taskFlow.jar

EXPOSE 8080

CMD ["java", "-jar", "taskFlow.jar"]
