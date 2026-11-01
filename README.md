# Automated CI/CD Pipeline for TaskFlow

## Project Overview
TaskFlow is a Java-based task management application integrated with an automated DevOps pipeline. It demonstrates how software can be built, tested, containerized and published automatically using modern DevOps tools.

## Features
- Create, update status and delete tasks.
- REST API integration.
- PostgreSQL database persistence.
- Docker-based application containerization.
- Docker Compose for multi-container management.
- Automated testing using Maven.
- Continuous Integration and Continuous Deployment using GitHub Actions.
- Automated Docker image publishing to Docker Hub.

## Technology Stack

| Category | Technologies |
|---|---|
| Backend | Java 17, Spring Boot |
| Frontend | HTML, CSS, Thymeleaf |
| Database | PostgreSQL |
| Build Tool | Maven |
| Containerization | Docker, Docker Compose |
| Version Control | Git, GitHub |
| CI/CD | GitHub Actions |
| Container Registry | Docker Hub |

## CI/CD Workflow
1. Developer pushes code to GitHub.
2. GitHub Actions triggers automatically.
3. Maven builds the application and executes tests.
4. Docker builds the application image.
5. The image is published to Docker Hub.
6. The published image can be used for deployment.

## Running the Project

### Prerequisites
- Java 17
- Docker Desktop
- Git

### Run locally
```bash
.\mvnw.cmd spring-boot:run
```

Open http://localhost:8080.

### Run using Docker Compose
Configure your `.env` file using `.env.example`, then execute:

```bash
docker compose up -d
```

### Run tests
```bash
.\mvnw.cmd clean verify
```

## Project Outcome
The project demonstrates practical implementation of containerization, automated testing, CI/CD workflows, database persistence and Docker image publishing.

## Future Enhancements
- Cloud deployment.
- Application monitoring using Prometheus and Grafana.
- HTTPS and production security configuration.

## Author
Srihith
