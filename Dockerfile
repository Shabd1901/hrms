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
EXPOSE 8080

# Convert any Render database URL (postgres:// or postgresql://) to jdbc:postgresql:// format
# and expand ${PORT} dynamically
ENTRYPOINT ["sh", "-c", "if [ -n \"$DATABASE_URL\" ]; then export SPRING_DATASOURCE_URL=$(echo \"$DATABASE_URL\" | sed 's|^postgres://|jdbc:postgresql://|' | sed 's|^postgresql://|jdbc:postgresql://|'); elif [ -n \"$SPRING_DATASOURCE_URL\" ]; then export SPRING_DATASOURCE_URL=$(echo \"$SPRING_DATASOURCE_URL\" | sed 's|^postgres://|jdbc:postgresql://|' | sed 's|^postgresql://|jdbc:postgresql://|'); fi; exec java -Dserver.port=${PORT:-8080} -jar app.war"]
