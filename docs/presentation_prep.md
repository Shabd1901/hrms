# PeopleOps HRMS — 15-Minute Viva & Presentation Master Cheat Sheet

> **Goal**: Read this in 15 minutes before your presentation/viva. Plain English, no fluff, direct answers to faculty questions, exact commands, and step-by-step demo flow.

---

## 1. What is PeopleOps HRMS? (Say this in first 30 seconds)
- **Elevator Pitch**: "PeopleOps is an enterprise Human Resource Management System backend built using **Spring Boot 3.2.5**, **Spring Data JPA**, and **PostgreSQL 15+**."
- **What it does**: Handles the full employee lifecycle:
  1. Department & Designation management
  2. Job Openings, Candidate Screening, Interviews & Automated Hiring
  3. Auto-generated Onboarding Tasks & Document Verification
  4. Daily Attendance tracking (clock-in, clock-out, work hours) & Leave balances
  5. Monthly Payroll generation & Performance reviews
  6. Company Assets assignment & Offboarding clearance

---

## 2. Project Architecture (Why folders are organized this way)

Faculty will open your project folder and ask: *"Explain your project structure."*

Point to `src/main/java/com/peopleops/` and explain the **4-Tier Layered Architecture**:

```text
Request (Browser / Postman / Swagger)
   │
   ▼
1. controller   --> Handles HTTP requests, paths, URLs, status codes (200, 201, 404).
   │
   ▼
2. service      --> Business logic, rules (validating dates, hours), and @Transactional.
   │
   ▼
3. repository   --> Spring Data JPA interfaces that talk to database (save, findById).
   │
   ▼
4. entity       --> Java classes mapped to PostgreSQL database tables using @Entity.
```

### Supporting Folders:
- **`dto` (Data Transfer Objects)**:
  - *Why?* We never expose database entities directly to the client for security and to avoid infinite JSON recursion. `RequestDTO` = input; `ResponseDTO` = output.
- **`responsebean`**:
  - *Why?* Standard classroom wrapper format containing `errorCode`, `errorDesc`, and `data`.
- **`exception` (`GlobalExceptionHandler`)**:
  - *Why?* Catches errors centrally across all controllers using `@RestControllerAdvice`. Returns clean JSON error responses instead of ugly server crashes.
- **`enums`**:
  - *Why?* Fixed statuses (`EmployeeStatus.ACTIVE`, `ReviewStatus.SUBMITTED`) so users can't type invalid random text.
- **`config` (`OpenApiConfig`)**:
  - *Why?* Configures Swagger UI documentation.

---

## 3. Top 5 Viva Questions (Memorize These!)

### Q1. Why did you use PostgreSQL instead of MySQL?
- **Answer**: "PostgreSQL has superior ACID compliance, enterprise concurrency (MVCC), native support for complex data types, and strict relational integrity constraints."

### Q2. Why do you need a Service layer if the Controller can call the Repository directly?
- **Answer**: "Separation of concerns. Controllers handle HTTP routing. Services contain business rules and `@Transactional` boundaries so multi-table operations (like hiring a candidate, creating 5 onboarding tasks, and initializing leave balance) succeed together or rollback completely if an error occurs."

### Q3. What is the difference between JPA and Hibernate?
- **Answer**: "JPA (Jakarta Persistence API) is just a **specification/interface** (the rules). Hibernate is the actual **implementation engine** that writes the SQL queries."

### Q4. What does `spring.jpa.hibernate.ddl-auto=update` do?
- **Answer**: "It tells Hibernate to automatically compare our Java `@Entity` classes against PostgreSQL on startup, creating missing tables and updating columns without deleting existing data."

### Q5. What is the difference between `@RestControllerAdvice` and `ResponseBean`?
- **Answer**: "`ResponseBean` is a manual envelope wrapper for responses. `@RestControllerAdvice` uses Spring AOP to automatically intercept exceptions globally and format standard HTTP error status codes (400, 404, 409)."

---

## 4. Live Demo Sheet (Commands & Order)

### Step A: Reset Database (if asked to show from scratch)
In pgAdmin or psql:
```sql
CREATE DATABASE peopleops_db;
```

### Step B: Run the Project
```powershell
cd c:\Users\praja\HomeSchool\PROJECT\hrms
mvn spring-boot:run
```
Point out in terminal:
1. HikariPool initialized.
2. Hibernate created all 20 tables.
3. Started on port `8080`.

### Step C: Show Swagger UI
Open browser:
`http://localhost:8080/swagger-ui/index.html`

### Step D: Run Postman Demo (Show 3 Best Flows)
1. **Create Department**: `POST /api/departments` -> returns `201 Created`.
2. **Auto-Hire Candidate**: `POST /api/candidates/1/hire` -> explain that this single click creates the employee, creates 5 onboarding tasks, and creates leave balance.
3. **Show Error Interception**: `GET /api/departments/99999` -> returns `404 Not Found` with structured JSON error.

---

## 5. Packaging & Deployment (JAR vs WAR)

Faculty will ask: *"What is JAR vs WAR and how did you build it?"*

### What is JAR?
- **Java ARchive**. Contains compiled classes, resources, AND an embedded Tomcat web server.
- Run it anywhere directly via: `java -jar filename.jar`.

### What is WAR?
- **Web Application ARchive**. Packaged for external servlet containers (e.g. standalone Apache Tomcat server in enterprise corporate environments).
- Enabled in our project via `<packaging>war</packaging>` in `pom.xml` and extending `SpringBootServletInitializer` in `PeopleOpsApplication.java`.

### Build Command:
```powershell
mvn clean package -DskipTests
```
Generates artifact in `hrms/target/peopleops-0.0.1-SNAPSHOT.war`.
