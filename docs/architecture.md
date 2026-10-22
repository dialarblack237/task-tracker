# Architecture

## System View (Deployment)

```mermaid
flowchart LR
    U[User<br/>browser] -->|opens| LP[Landing page<br/>GitHub Pages]
    U -->|opens| SW[Swagger UI<br/>Render]
    LP -->|links to| SW
    SW -->|HTTPS + JWT| API[Spring Boot API<br/>Render]
    API -->|JDBC + SSL| DB[(PostgreSQL<br/>Neon)]
    API -->|reads| FW[Flyway migrations<br/>on startup]
    UR[UptimeRobot] -->|pings /health every 5m| API