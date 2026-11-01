# TaskFlow — Automated CI/CD Task Manager

A Java Spring Boot task management web application with a GitHub Actions pipeline that tests the code, builds a Docker image, publishes it to Docker Hub, and deploys it to an AWS EC2 Linux server.

## Features
- Create tasks with title and optional description
- Mark tasks complete/incomplete
- Delete tasks
- Dashboard counts for total, completed and remaining tasks
- REST API under `/api/tasks`
- PostgreSQL persistence in Docker Compose
- H2-backed automated tests
- Container runs as a non-root user
- Automated CI/CD with GitHub Actions

## Architecture

`Developer → GitHub → GitHub Actions (test) → Docker Hub → AWS EC2 → TaskFlow`

The database runs as a separate PostgreSQL container with a persistent Docker volume.

## Requirements
- Java 17 and Maven 3.9+ for local development
- Docker Desktop / Docker Engine and Docker Compose
- Git and a GitHub account
- Docker Hub account
- AWS account and an Ubuntu EC2 instance for cloud deployment

## Run locally without Docker
```bash
mvn spring-boot:run
```
Open http://localhost:8080. The default local profile uses an in-memory H2 database.

Run tests:
```bash
mvn clean verify
```

## Run with Docker Compose
1. Copy `.env.example` to `.env`.
2. Set `DOCKERHUB_USERNAME` and a strong `DB_PASSWORD`.
3. Build the image locally and start the services:
```bash
docker build -t task-manager:local .
```
For a local image, edit `compose.yaml` image to `task-manager:local`, then run:
```bash
docker compose up -d
```
Open http://localhost:8080. Stop with `docker compose down`; data remains in the named volume. To delete the database volume too, use `docker compose down -v` (this permanently deletes stored tasks).

## Configure automated CI/CD

### 1. Create Docker Hub repository
Create a public repository named `task-manager` on Docker Hub. Create an access token with read/write permissions.

### 2. Create GitHub repository
Create an empty GitHub repository and push this project to its `main` branch:
```bash
git init
git add .
git commit -m "Initial TaskFlow application and CI/CD pipeline"
git branch -M main
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```

### 3. Add GitHub Actions secrets
In GitHub: **Settings → Secrets and variables → Actions → New repository secret**.

| Secret | Value |
|---|---|
| `DOCKERHUB_USERNAME` | Your Docker Hub username |
| `DOCKERHUB_TOKEN` | Docker Hub access token (not your account password) |
| `EC2_HOST` | Public IPv4 address or DNS of EC2 |
| `EC2_USERNAME` | Usually `ubuntu` for Ubuntu AMIs |
| `EC2_SSH_KEY` | Full private SSH key contents for the EC2 key pair |

Never commit tokens, private keys, passwords, or `.env` files to GitHub.

### 4. Prepare AWS EC2
Launch an Ubuntu EC2 instance. For a learning/demo deployment, a small instance may work; choose an instance size appropriate to your account's free-tier eligibility and workload. Configure its security group:
- SSH port 22: allow only your current public IP, not the whole internet.
- TCP port 8080: allow your IP for testing, or use a reverse proxy and HTTPS before public production use.

Connect to the instance:
```bash
ssh -i your-key.pem ubuntu@YOUR_EC2_PUBLIC_IP
```
Install Docker Engine and the Compose plugin using Docker's official Ubuntu installation instructions:
https://docs.docker.com/engine/install/ubuntu/

Then run:
```bash
mkdir -p ~/task-manager
cd ~/task-manager
```
Create `compose.yaml` on the EC2 server by copying the project's compose file. Create `.env`:
```bash
nano .env
```
Add:
```env
DOCKERHUB_USERNAME=your-dockerhub-username
IMAGE_TAG=latest
DB_PASSWORD=use-a-long-random-password-here
```
If the Docker Hub repository is private, authenticate on EC2 using `docker login` with a Docker Hub access token. A public repository does not need this.

Start the first deployment manually after the files are in place:
```bash
docker compose pull
docker compose up -d
docker compose ps
```
Check:
```bash
curl http://localhost:8080/actuator/health
```
The project includes Spring Boot Actuator. A healthy response should be returned by `curl http://localhost:8080/actuator/health`.
Open `http://YOUR_EC2_PUBLIC_IP:8080` in a browser.

### 5. Trigger the pipeline
Push a change to `main`. GitHub Actions will:
1. Run Maven tests.
2. Build and push `latest` and commit-SHA Docker tags.
3. Connect to EC2 over SSH.
4. Pull the latest image and recreate the app container.

The workflow expects `compose.yaml` and `.env` to already exist in `~/task-manager` on EC2. The database volume persists across app container updates.

## REST API
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/tasks` | List tasks |
| POST | `/api/tasks` | Create task |
| PATCH | `/api/tasks/{id}/toggle` | Toggle completion |
| DELETE | `/api/tasks/{id}` | Delete task |

Example create request:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Docker","description":"Build and run the container"}'
```

## Important production improvements
This is a college/demo project, not a hardened production service. Before exposing it to real users:
- Put Nginx or a cloud load balancer in front and configure HTTPS.
- Restrict inbound network access; do not expose PostgreSQL port 5432.
- Use AWS Secrets Manager or another secret manager for credentials.
- Add authentication/authorization and CSRF/security review for a multi-user deployment.
- Add database backup and restore testing.
- Pin third-party GitHub Actions to reviewed commit SHAs for a stricter supply-chain policy.
- Consider immutable image tags (the workflow also publishes the commit SHA tag) and deploy a chosen SHA for rollback.

## Troubleshooting
- **Pipeline cannot log in to Docker Hub:** verify the username and access token secrets.
- **SSH deployment fails:** verify EC2 is running, port 22 is allowed from GitHub-hosted runner IP ranges (or use a safer self-hosted runner / restricted access strategy), username and private key are correct.
- **App cannot connect to PostgreSQL:** check `docker compose logs db` and `docker compose logs app`; confirm the DB password is identical in `.env`.
- **Port 8080 is unreachable:** verify EC2 security group and `docker compose ps`.
- **Tasks disappear:** confirm the `postgres_data` volume exists and avoid `docker compose down -v`.

## Project structure
```text
task-manager/
├── .github/workflows/ci-cd.yml
├── src/
│   ├── main/java/com/srihith/taskmanager/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── main/resources/
│   │   ├── static/styles.css
│   │   ├── templates/index.html
│   │   └── application.properties
│   └── test/java/com/srihith/taskmanager/
├── Dockerfile
├── compose.yaml
└── pom.xml
```

## Author
Add your name, GitHub repository URL, deployment URL, and screenshots here after deployment.
