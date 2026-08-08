# Webtechnologien - Mail Application

It's a webmail application that has a backend made with Kotlin, Spring Boot and REST, and a modern web interface.

---

## How to Start the System

To build and start the whole application (the Backend and Frontend containers), enter the following command in the projects root directory:

```bash
docker compose up --build
```

Once the containers are running, you can access the application components:
- **Web Client (Frontend):** [http://localhost/](http://localhost/) (Port 80)
- **REST API (Backend):** [http://localhost:8080/](http://localhost:8080/) (Port 8080)
- **Swagger / OpenAPI Documentation:** [http://localhost:8080/api/v1/swagger-ui](http://localhost:8080/api/v1/swagger-ui)

---

## Initial User Credentials

When you first start the system, it automatically creates test user accounts in the database (using a file called `data.sql` in the directory `mail_backend/src/main/resources`).

You can use the following pre-created user credentials for testing:

### Primary Test User (Default)
* **Email:** `test@example.com`
* **Password:** `Password123!`

### Additional Pre-created Test Users
* **Email:** `max.mustermann@example.com` | **Password:** `Password123!`
* **Email:** `anna.schmidt@example.com` | **Password:** `Password123!`
* **Email:** `erika.musterfrau@example.com` | **Password:** `Password123!`
