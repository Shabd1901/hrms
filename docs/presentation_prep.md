# PeopleOps HRMS — 15-Minute Viva & Presentation Master Cheat Sheet

> **Goal**: Read this in 10 minutes before your presentation/viva. Plain English, no fluff, direct answers to faculty questions, exact commands, and step-by-step demo flow.

---

## ⚡ The "Learn in 10 Minutes" Ultra-Quick Viva Survival Sheet (Read This First!)

### 🎯 Your Golden Strategy:
1. **Present WHAT YOU BUILT, NOT textbook definitions!**
   - The faculty already knows what Java or an Entity is. They want to see **your project**: the 7 modules, the 20 entities, the 123 endpoints, and how leave approvals and hiring work.
2. **Do NOT volunteer deep technical theory on the slides!**
   - Keep your slides clean, warm, light, and focused on your project.
   - Keep the technical answers in your pocket. When the invigilator asks a question during viva, drop the exact 1-sentence answers below. You will look completely in control.

---

### 🧠 The Top 10 Viva Questions & 1-Sentence Answers (Memorize These!):

1. **"What is your project?"**
   > *"I built PeopleOps, an enterprise HRMS backend REST API using Spring Boot 3 and PostgreSQL 15+. It manages the entire employee lifecycle across 7 modules, 20 relational entities, and 123 tested endpoints."*

2. **"Why do you have 4 tiers (Controller, Service, Repository, Entity)?"**
   > *"Controllers only handle incoming HTTP requests and validation. Services contain all the business calculations and workflows. Repositories talk to PostgreSQL. This keeps every class clean, separated, and easy to maintain."*

3. **"Why use DTOs instead of returning Entities directly?"**
   > *"Two reasons: Security (to prevent users from hacking internal fields like salary or roles) and preventing circular JSON crashes when two tables are linked to each other."*

4. **"What does `@Transactional` do in your service methods?"**
   > *"It guarantees 'All-or-Nothing'. For example, when hiring a candidate, the system updates the candidate, creates an employee, and spawns 5 onboarding tasks. If any step fails, `@Transactional` automatically rolls back the entire operation so the database never gets corrupted."*

5. **"Why use `BigDecimal` for salaries instead of `double` or `float`?"**
   > *"`double` has binary floating-point rounding errors (like 0.1 + 0.2 = 0.300000004). For money and payroll, `BigDecimal` guarantees 100% exact math down to two decimal places."*

6. **"Why use PostgreSQL instead of MySQL?"**
   > *"PostgreSQL offers stricter ACID compliance, superior handling of foreign key constraints, and handled our 19 complex relational mappings and composite unique keys flawlessly."*

7. **"What is JAR vs WAR in your project?"**
   > *"A JAR includes its own embedded Tomcat web server so you can run it anywhere with `java -jar`. A WAR is designed to be deployed into external enterprise servers like Apache Tomcat. Our project supports both via `SpringBootServletInitializer`."*

8. **"How does logging work in your project?"**
   > *"We use SLF4J with Logback. It logs live requests to the console and automatically writes persistent audit logs into `logs/peopleops.log`."*

9. **"How do you handle errors across your 20 controllers?"**
   > *"We use `@RestControllerAdvice` (`GlobalExceptionHandler`). If an ID doesn't exist, instead of the server crashing with an ugly 500 error, it catches it and sends back a clean 404 JSON error response."*

10. **"Show me the live demo right now."**
    > *Open browser to `http://localhost:8080/swagger-ui.html`, expand `department-controller`, click `POST /api/departments`, click Try It Out, send JSON, get `201 Created`. Then show the log in terminal 2 (`logs/peopleops.log`)."*

---

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


---

## 6. Live Practical Demonstration Runbook (Show the Invigilator Live)

When the invigilator says: *"Okay, run it and show me right now,"* follow this exact 5-minute choreography.

### Step 1: Pre-Demo Setup Check
Open your development environment with 4 windows arranged:
1. **Window 1 (VS Code / IDE):** Showing the project file explorer.
2. **Window 2 (Terminal 1):** Running the Spring Boot application.
3. **Window 3 (Terminal 2):** Actively tailing the live log file.
4. **Window 4 (Browser / Postman):** Swagger UI and Postman collection ready.

---

### Step 2: Start the Backend & Point Out Key Startup Logs
Open Terminal 1:
```powershell
cd c:\Users\praja\HomeSchool\PROJECT\hrms
mvn spring-boot:run
```
**What to say out loud while it boots (15 seconds):**
> *"Here the Spring Boot 3.2.5 application is booting up on Java 17. You can see the HikariCP connection pool initializing its connection pool to PostgreSQL on localhost:5432. Hibernate 6 inspects our 20 `@Entity` classes and ensures all tables, foreign keys, and unique indexes exist. The embedded Tomcat web server is now bound and listening on port 8080."*

---

### Step 3: Demonstrate Live Logging in Action (Terminal 2)
Open Terminal 2 and run:
```powershell
cd c:\Users\praja\HomeSchool\PROJECT\hrms
Get-Content logs/peopleops.log -Wait -Tail 30
```
**What to say to the invigilator:**
> *"I have configured SLF4J with a Logback rolling file appender writing to `logs/peopleops.log`. As we trigger endpoints from Swagger or Postman, watch this terminal—every incoming request, transaction boundary, and business operation is logged in real-time with timestamps, thread IDs, and log levels."*

---

### Step 4: Live Swagger UI Interactive Demonstration
Open your browser to:
`http://localhost:8080/swagger-ui/index.html` (or `http://localhost:8080/swagger-ui.html`)

**What to perform in front of the invigilator:**
1. Point to the **20 tag groups** corresponding to our 20 REST controllers.
2. Expand the **`department-controller`** tag.
3. Click on `POST /api/departments` &rarr; click **"Try it out"**.
4. In the request body, paste:
   ```json
   {
     "name": "Cloud Engineering",
     "code": "CLOUD",
     "description": "Enterprise cloud architecture & DevOps",
     "active": true
   }
   ```
5. Click **"Execute"**. Point out:
   - Status Code: **`201 Created`**
   - Response Body: Returns the newly assigned `id` (e.g. `1`), confirming generation by PostgreSQL's `BIGSERIAL`.
6. Switch to Terminal 2 to show the examiner the live `INFO` log that was instantly written!

---

### Step 5: Live Business Workflow Demo in Postman
Open Postman and execute this 3-step sequence that proves real business logic (not just basic CRUD):

1. **Step A — Apply for Leave (`POST /api/leave-requests`):**
   - Submit a request for an employee for 3 days of Casual Leave.
   - Response: `201 Created` with status `PENDING`.
2. **Step B — Manager Approval & Auto-Deduction (`PUT /api/leave-requests/{id}/approve`):**
   - Send the approval request.
   - Response: `200 OK` with status `APPROVED`.
3. **Step C — Verify Automatic Balance Decrement (`GET /api/leave-balances/employee/{id}`):**
   - Query the employee's leave balance.
   - Point to the screen: *"Notice that `usedDays` increased from 0 to 3, and remaining days decreased automatically. This was executed inside a single `@Transactional` method to prevent balance discrepancies."*

---

### Step 6: Live pgAdmin / Database Verification
Open pgAdmin 4 or run in terminal:
```powershell
psql -U postgres -d peopleops_db -c "SELECT id, name, code, active FROM departments;"
```
Show the examiner the exact row created from the Swagger UI call, proving that the Java ORM successfully committed the transaction into the physical PostgreSQL relational engine.

---

## 7. File Structure Walkthrough & "Why I Did What I Did" (Code Block Justifications)

The invigilator will open your source code tree and point to specific classes, asking: *"Why did you write this code this way?"* Here are the exact justifications to deliver:

### File Tree Layout:
```text
hrms/src/main/java/com/peopleops/
├── PeopleOpsApplication.java       <-- Application Entry Point & WAR Initializer
├── config/                         <-- Swagger/OpenAPI Configuration
├── controller/                     <-- 20 REST Controllers (HTTP routing & @Valid)
├── dto/
│   ├── request/                    <-- 20 Request DTOs (Input validation bounds)
│   └── response/                   <-- 20 Response DTOs (Clean output projection)
├── entity/                         <-- 20 JPA Entities (PostgreSQL table schemas)
├── enums/                          <-- 15 Type-Safe Enums (Status state machines)
├── exception/                      <-- GlobalExceptionHandler & Domain Exceptions
├── repository/                     <-- 20 Spring Data JPA Repositories
├── responsebean/                   <-- Standard ResponseBean envelope wrapper
└── service/
    ├── impl/                       <-- 20 Service Implementations (@Transactional)
    └── [20 Service Interfaces]     <-- Decoupled business contracts
```

---

### Code Block 1: Why 4-Tier Layered Architecture instead of putting logic in Controllers?
- **Invigilator Question:** *"Why do you have separate Controller, Service, and Repository layers? Why not write the SQL query inside the Controller?"*
- **Rock-Solid Answer:**
  > *"Because of the **Separation of Concerns (SoC)** principle:
  > 1. **Controller Layer:** Strictly responsible for HTTP concerns (request deserialization, header parsing, HTTP response codes, and input validation via `@Valid`).
  > 2. **Service Layer:** Houses the business logic, validation rules, and `@Transactional` boundaries. If we ever switch from REST to GraphQL or a message queue like RabbitMQ, the business logic remains completely untouched.
  > 3. **Repository Layer:** Encapsulates data persistence. If we optimize our queries or migrate to another database, we only modify the persistence layer without risking business rules or breaking API contracts."*

---

### Code Block 2: Why strict DTO separation (`RequestDTO` & `ResponseDTO`)?
- **Invigilator Question:** *"Why didn't you just pass your `@Entity` class directly in `@RequestBody` and return it in `@ResponseBody`?"*
- **Rock-Solid Answer:**
  > *"Exposing Entities directly is an anti-pattern in enterprise systems for three critical reasons:
  > 1. **Circular Serialization Crash:** Bidirectional JPA relationships (like `Department` &rarr; `Employee` &rarr; `Department`) cause infinite recursion and `StackOverflowError` during Jackson JSON serialization.
  > 2. **Security & Mass-Assignment Vulnerabilities:** If an entity has sensitive internal fields like `passwordHash` or `isSuperAdmin`, a malicious client can inject them in the JSON payload and overwrite database columns. Request DTOs expose only the exact fields permitted for input.
  > 3. **Contract Stability:** If I rename an internal database column or split a table, I don't break existing mobile apps or frontend clients because the public DTO schema remains stable."*

---

### Code Block 3: Why declarative Jakarta Validation (`@Valid`, `@NotNull`, `@NotBlank`)?
- **Invigilator Question:** *"Why use annotations like `@NotBlank` and `@Positive` instead of simple `if-else` checks in Java?"*
- **Rock-Solid Answer:**
  > *"Declarative validation using Jakarta Bean Validation (Hibernate Validator) adheres to the **Fail-Fast** principle:
  > 1. It validates incoming payloads at the HTTP controller boundary before expensive business services or database connections are ever invoked.
  > 2. It keeps the code clean and readable, eliminating dozens of repetitive `if (dto.getName() == null)` checks.
  > 3. When combined with `@RestControllerAdvice`, validation failures automatically generate standardized error payloads detailing every violated field."*

---

### Code Block 4: Why `@RestControllerAdvice` (`GlobalExceptionHandler`)?
- **Invigilator Question:** *"How does error handling work in your project? Where are the `try-catch` blocks?"*
- **Rock-Solid Answer:**
  > *"We deliberately avoid scattered `try-catch` blocks across our 20 controllers by using **Spring AOP-based centralized exception handling** via `@RestControllerAdvice`:
  > 1. When a service throws `ResourceNotFoundException("Employee with id 5 not found")`, it bubbles up to `GlobalExceptionHandler`.
  > 2. The handler intercepts it and formats a standardized **RFC 7807 Problem Detail** JSON with timestamp, path, error message, and HTTP 404 status.
  > 3. This guarantees that API consumers always receive uniform JSON errors instead of unhandled 500 HTML stack traces."*

---

### Code Block 5: Why `@Transactional` on the Service Layer?
- **Invigilator Question:** *"What happens if an error occurs halfway through hiring a candidate?"*
- **Rock-Solid Answer:**
  > *"Every workflow method in our service layer is annotated with Spring's `@Transactional`. 
  > In the candidate hiring workflow, the system performs four operations: decrements open positions in `job_openings`, updates candidate stage to `HIRED`, inserts a new row in `employees`, and inserts 5 default tasks in `onboarding_tasks`.
  > If the task creation fails due to a database constraint, Spring automatically rolls back the entire transaction. Neither the employee nor the candidate status is committed, maintaining 100% database consistency under ACID rules."*

---

### Code Block 6: Why `BigDecimal` for Salaries instead of `double` or `float`?
- **Invigilator Question:** *"In your `Employee` and `Payroll` entities, why did you use `BigDecimal` for `monthlySalary` and `netSalary` instead of `double`?"*
- **Rock-Solid Answer:**
  > *"`float` and `double` are binary floating-point types defined by IEEE 754. They cannot accurately represent decimal fractions like `0.1` or `0.05`, leading to rounding errors (e.g., `0.1 + 0.2 = 0.30000000000000004`). In enterprise payroll systems, even a fractional cent rounding drift across thousands of employee salary calculations causes legal and accounting audits to fail. `BigDecimal` provides arbitrary-precision arithmetic with exact scale-2 control."*

---

### Code Block 7: Why `FetchType.LAZY` on Relational Associations?
- **Invigilator Question:** *"Why are your `@ManyToOne` and `@ManyToMany` mappings marked as `fetch = FetchType.LAZY`?"*
- **Rock-Solid Answer:**
  > *"To prevent the **N+1 Query Problem**. If `FetchType.EAGER` is used, fetching a list of 100 employees would trigger 1 query for employees, plus 100 separate queries for departments, 100 for designations, and 100 for managers—resulting in 301 database roundtrips. With `LAZY`, Hibernate retrieves only the employee columns, and fetches associated relations only when explicitly accessed."*

---

### Code Block 8: Why `@Enumerated(EnumType.STRING)`?
- **Invigilator Question:** *"Why did you specify `EnumType.STRING` for your enums?"*
- **Rock-Solid Answer:**
  > *"By default, JPA uses `EnumType.ORDINAL`, which stores enums as integer indexes (`0, 1, 2`). If a developer later inserts a new status like `ON_PROBATION` at the top of the enum file, all existing numeric values in PostgreSQL become corrupt. Storing the enum as `EnumType.STRING` persists the literal name (e.g. `'ACTIVE'`), ensuring database readability and zero risk of ordinal shifts."*

---

## 8. The Entity Justification Matrix (Why All 20 Entities Exist)

If the invigilator asks: *"Why do you have 20 entities? Could you have built this with 4 or 5?"*

**Opening Statement:**
> *"A simple toy project uses 2 or 3 entities with mixed attributes. PeopleOps is an enterprise HRMS where distinct business responsibilities must be normalized to eliminate data redundancy and support transactional workflows. Here is the operational necessity of our 20 entities across the 7 modules:"*

| # | Entity | Table Name | Business Justification (Why It Cannot Be Removed) |
|---|---|---|---|
| **1** | `Department` | `departments` | Models business units (e.g. Engineering, Sales). Prevents duplicating department codes across employees. |
| **2** | `Designation` | `designations` | Standardizes job titles and hierarchical grade levels (1-10) to determine organizational authority and pay bands. |
| **3** | `Employee` | `employees` | The central domain entity. Anchors identity, corporate email, salary, manager hierarchy, and department linkage. |
| **4** | `JobOpening` | `job_openings` | Tracks hiring demand, open headcounts, target department, and position lifecycle (`OPEN` vs `CLOSED`). |
| **5** | `Candidate` | `candidates` | Represents job applicants. Must be separate from `Employee` because applicants are not organizational employees until hired. |
| **6** | `Interview` | `interviews` | Models interview scheduling, round numbers (Technical, HR), interviewer assignments, and pass/fail evaluation. |
| **7** | `OnboardingTask` | `onboarding_tasks` | Automated checklist items assigned upon hiring (e.g. "Submit ID Proof", "Collect Laptop", "Sign NDA"). |
| **8** | `Attendance` | `attendance_records` | High-frequency daily punch logs tracking check-in/out timestamps and computing daily statuses (`PRESENT`, `HALF_DAY`, `ABSENT`). |
| **9** | `LeaveType` | `leave_types` | Defines organizational leave policies (Casual, Sick, Maternity) and statutory paid/unpaid rules. |
| **10** | `LeaveBalance` | `leave_balances` | Dynamic employee leave quota ledger. Must be separate from `LeaveType` because balances vary per employee per calendar year. |
| **11** | `LeaveRequest` | `leave_requests` | Captures individual leave applications, duration, reason, approver manager, and state machine (`PENDING` &rarr; `APPROVED`/`REJECTED`). |
| **12** | `Payroll` | `payrolls` | Monthly compensation record capturing gross earnings, Loss-of-Pay (LOP) deductions, statutory taxes, and net payable salary. |
| **13** | `PerformanceReview` | `performance_reviews` | Stores periodic appraisal scores (1-5), supervisor ratings, qualitative feedback, and quarterly developmental goals. |
| **14** | `Skill` | `skills` | Master catalog of technical and soft competencies (e.g. "Java", "Docker", "Public Speaking") shared across the organization. |
| **15** | `TrainingProgram` | `training_programs` | Corporate training courses with trainer details, curricula, dates, and target skills taught. |
| **16** | `TrainingEnrollment` | `training_enrollments` | Tracks which employees attend which training programs and records completion certificates (`ENROLLED`, `COMPLETED`, `DROPPED`). |
| **17** | `Asset` | `assets` | Physical company hardware inventory (laptops, monitors) with unique serial asset tags and availability statuses. |
| **18** | `AssetAssignment` | `asset_assignments` | Audit history of who was issued which hardware asset, checkout dates, return dates, and condition handovers. |
| **19** | `EmployeeDocument` | `employee_documents` | Digital compliance repository storing metadata and verified statuses for employee IDs, offer letters, and contracts. |
| **20** | `Offboarding` | `offboardings` | Formal exit lifecycle record tracking resignation date, notice period, exit interview feedback, and asset clearance before employee status becomes `EXITED`. |

---

## 9. Practical Technology Defense: Deep Dive

### 1. JAR vs WAR (Packaging & Deployment)
- **Question:** *"Why did you configure `<packaging>war</packaging>` instead of leaving it as jar?"*
- **Practical Demonstration:**
  1. Open `hrms/pom.xml` and highlight:
     ```xml
     <packaging>war</packaging>
     ```
  2. Open `PeopleOpsApplication.java` and highlight:
     ```java
     public class PeopleOpsApplication extends SpringBootServletInitializer {
         public static void main(String[] args) {
             SpringApplication.run(PeopleOpsApplication.java, args);
         }
     }
     ```
- **Explanation:**
  > *"By extending `SpringBootServletInitializer`, our application achieves **dual-deployment compatibility**:
  > - **As a WAR:** It can be dropped directly into an external production servlet container like Apache Tomcat or WildFly, which corporate IT departments frequently mandate.
  > - **As a Standalone Executable:** Because the `spring-boot-starter-tomcat` dependency is included, we can still run it standalone via `java -jar peopleops-0.0.1-SNAPSHOT.war` without needing any external server installed."*
- **Build Command:**
  ```powershell
  mvn clean package -DskipTests
  ```
  Points to the generated ~48 MB artifact in `hrms/target/peopleops-0.0.1-SNAPSHOT.war`.

---

### 2. Swagger / OpenAPI Interactive Documentation
- **Question:** *"How does Swagger work and how did you configure it?"*
- **Explanation:**
  > *"We integrated `springdoc-openapi-starter-webmvc-ui` version 2.5.0. At application startup:
  > 1. SpringDoc reflects upon all `@RestController` beans and scans annotations like `@GetMapping`, `@PostMapping`, `@PathVariable`, and `@RequestBody`.
  > 2. It inspects our DTO fields and Jakarta validation annotations (`@NotNull`, `@NotBlank`) to generate a dynamic OpenAPI 3.0 JSON specification at `/v3/api-docs`.
  > 3. It serves the interactive Swagger UI interface at `/swagger-ui/index.html`, allowing developers and QA engineers to execute live HTTP calls directly from the browser without needing Postman."*

---

### 3. SLF4J + Logback Production Logging
- **Question:** *"Why did you use SLF4J and how is it configured?"*
- **Explanation:**
  > *"SLF4J (Simple Logging Facade for Java) acts as an abstraction layer, while Logback serves as the high-performance implementation engine. In `application.properties`, we configured two distinct logging streams:
  > 1. **Console Appender:** Outputs human-readable, color-coded logs during local development.
  > 2. **Rolling File Appender (`logs/peopleops.log`):** Persists all logs to disk with time and size-based rolling policies. In production, this ensures zero loss of audit events even if the console restarts or the container terminates."*

---

### 4. Postman Integration & Verification
- **Question:** *"How did you verify all 123 endpoints?"*
- **Explanation:**
  > *"We structured an automated Postman collection organized by business modules. We verify endpoints using standard HTTP status codes:
  > - `200 OK`: Successful retrieval or update.
  > - `201 Created`: Successful entity creation (returns the newly generated ID).
  > - `204 No Content`: Successful deletion.
  > - `400 Bad Request`: Validation failure intercepted by Jakarta Validation.
  > - `404 Not Found`: Entity lookup failure intercepted by GlobalExceptionHandler.
  > - `409 Conflict`: Unique constraint violation (e.g. duplicate employee code)."*

---

## 10. Invigilator Rapid-Fire "Grill" Questions & Model Answers

### Q1: What is the difference between `@Component`, `@Service`, and `@Repository`?
- **Answer:** *"Technically, `@Service` and `@Repository` are meta-annotations with `@Component` inside them. However, they provide semantic specialization: `@Repository` enables automatic persistence exception translation (converting raw JDBC SQLExceptions into Spring's DataAccessException hierarchy), `@Service` clarifies that the bean holds business logic and transaction boundaries, and `@Component` is the generic stereotype for any Spring-managed bean."*

### Q2: How does Spring Boot's Dependency Injection (`@Autowired`) work behind the scenes?
- **Answer:** *"During application startup, Spring's `ApplicationContext` performs component scanning. It creates singleton instances (Spring Beans) of classes marked with stereotype annotations. When it encounters `@Autowired`, it inspects the bean registry and injects the matching implementation by type using reflection or constructor injection, ensuring loose coupling and inversion of control."*

### Q3: What is Hibernate "Dirty Checking"?
- **Answer:** *"When an entity is loaded within a `@Transactional` service method, Hibernate takes an internal snapshot of its state. If we modify entity properties in Java (e.g. `employee.setStatus(EmployeeStatus.ACTIVE)`), Hibernate automatically compares the modified entity against the initial snapshot at transaction commit time. If changes are detected ('dirty'), Hibernate automatically issues an `UPDATE` SQL statement without requiring us to explicitly call `repository.save()`."*

### Q4: How do you prevent SQL Injection in Spring Data JPA?
- **Answer:** *"Spring Data JPA completely prevents SQL injection by utilizing **parameterized queries** and PreparedStatements behind the scenes. When we invoke repository methods like `findByEmployeeCode(code)`, Hibernate binds `code` as a typed SQL parameter (`?1`) rather than concatenating raw strings into the SQL statement."*

### Q5: What is the difference between HTTP `PUT` and `PATCH`?
- **Answer:** *"`PUT` is idempotent and replaces the **entire** entity resource with the new representation provided in the request body. `PATCH` is designed for **partial updates**, modifying only the specific fields supplied (such as updating only an employee's phone number or marital status without resending their entire profile)."*

### Q6: Why did you use `LocalDate` instead of `java.util.Date`?
- **Answer:** *"`java.util.Date` is a legacy Java 1.0 class that is mutable, not thread-safe, and mixes date with time. `LocalDate` (introduced in Java 8 `java.time` package) is immutable, thread-safe, timezone-independent, and maps cleanly to PostgreSQL's native `date` column type via JDBC 4.2."*

### Q7: What does `spring.jpa.open-in-view=true` do?
- **Answer:** *"Open-Session-In-View (OSIV) keeps the Hibernate persistence context and database session open through the entire HTTP request lifecycle, including the presentation layer. This allows lazy-loaded associations to be fetched while rendering responses. While convenient in development, in ultra-high-throughput enterprise systems it is often disabled to release database connections back to the connection pool immediately after service method execution."*

### Q8: What is the role of HikariCP in your project?
- **Answer:** *"HikariCP is the default high-performance JDBC connection pool in Spring Boot 3. Instead of the massive overhead of opening and closing a physical TCP connection to PostgreSQL on every single API request, HikariCP maintains a pool of pre-established database connections, rapidly leasing them to active threads and retrieving them upon transaction completion."*

---

> **Final Examiner Takeaway:**
> *"PeopleOps HRMS is not a proof-of-concept; it is an architecturally sound, thoroughly documented, and defensively engineered enterprise backend that solves real workforce management problems using modern Java software engineering best practices."*
