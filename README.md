## Reqs: 

- Java 21

- Maven

- Docker




Copy `.env.example` to `.env` and set the values for your machine or server.

`docker-compose` uses `POSTGRES_*` for the database container.
The app reads `DB_URL`, `DB_USER`, and `DB_PASSWORD` from the environment
(Docker `--env-file` on the server). `src/main/resources/database.properties`
is only a local fallback when those variables are not set.
