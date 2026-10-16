# API Tests

Manual test requests for the Task Tracker API. Committed to the repo as
living documentation of what the API does.

## How to run

1. Install the **REST Client** extension in VS Code (by Huachao Mao).
2. Start the app: `mvn spring-boot:run`.
3. Insert a test user via the H2 console:
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:file:./data/task_tracker;AUTO_SERVER=TRUE`
   - User: `sa`, Password: (blank)
   - SQL: `INSERT INTO users (email, password, display_name, created_at, updated_at) VALUES ('test@example.com', 'x', 'Test User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);`
   - Then: `SELECT id FROM users;` — note the id (usually 1).
4. Open any `.http` file and click **Send Request** above a block.

## Files

| File | Purpose |
|---|---|
| `tasks.http` | Happy path for all task endpoints |
| `errors.http` | Error cases (validation, 404, malformed JSON) |
| `auth.http` | Register + login (added in Module 6) |

## Notes

- All requests use `userId=1` as a temporary stand-in for `@AuthenticationPrincipal`.
- Module 6 replaces the `userId` param with a JWT — this file will be updated at that time.