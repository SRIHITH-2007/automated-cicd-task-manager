# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd --system --uid 1001 spring
COPY --from=build /workspace/target/task-manager-1.0.0.jar app.jar
USER 1001
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
