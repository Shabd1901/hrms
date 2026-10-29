# Multi-stage build for PeopleOps HRMS on Render / Cloud
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Lightweight JRE runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/peopleops-0.0.1-SNAPSHOT.war app.war
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh
EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]
