# Task Tracker API

A Spring Boot REST API for managing personal tasks with JWT authentication and per-user ownership.

## Live Demo

| | |
|---|---|
| **API** | https://task-tracker-j9q5.onrender.com |
| **Swagger UI** | https://task-tracker-j9q5.onrender.com/swagger-ui.html |
| **Health** | https://task-tracker-j9q5.onrender.com/actuator/health |

> The free tier on Render sleeps after 15 minutes of inactivity. The first request after sleep takes ~30 seconds to wake up.

## Tech Stack

- **Java 21** + **Spring Boot 4**
- **PostgreSQL** (managed by [Neon](https://neon.tech) — free tier)
- **Flyway** for database migrations
- **Spring Security** + **JWT** for authentication
- **Docker** for deployment (multi-stage build)
- **GitHub Actions** for CI
- **Render** for hosting

## Features

- User registration and login with JWT
- Per-user task ownership (a user can only see/modify their own tasks)
- CRUD operations on tasks
- Task status (TODO / DONE) and due dates
- Global exception handling with structured error responses
- OpenAPI / Swagger UI documentation

## API Endpoints

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/v1/auth/register` | No | Register a new user |
| POST | `/api/v1/auth/login` | No | Log in, returns JWT |
| GET | `/api/v1/tasks` | Yes | List the caller's tasks |
| POST | `/api/v1/tasks` | Yes | Create a task |
| GET | `/api/v1/tasks/{id}` | Yes | Get one task (404 if not yours) |
| PUT | `/api/v1/tasks/{id}` | Yes | Update a task |
| DELETE | `/api/v1/tasks/{id}` | Yes | Delete a task |

## Quick Start (local)

### Prerequisites
- Java 21
- Maven (or use `./mvnw`)
- No database required — dev profile uses H2 in-memory

### Run

```bash
git clone https://github.com/dialarblack237/task-tracker.git
cd task-tracker
./mvnw spring-boot:run