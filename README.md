# PeopleOps — Human Resource Management System (HRMS) Backend

PeopleOps is an enterprise-grade backend REST API application for managing core human resource operations, lifecycle processes, and talent management workflows. Built with **Java 17**, **Spring Boot 3.2.5**, and **PostgreSQL**, PeopleOps demonstrates clean layered software architecture, strict relational integrity, domain-driven business rules, automated validation, comprehensive logging, and interactive OpenAPI documentation.

---

## 1. Architectural Philosophy & Design Principles

PeopleOps adheres strictly to clean architectural principles:
- **Layered Structure**: Controller $\rightarrow$ Service $\rightarrow$ Repository. Controllers handle HTTP request/response semantics; services encapsulate transactional business rules; repositories provide Spring Data JPA persistence.
- **DTO Encapsulation**: Database entities are never leaked to API clients. Every incoming request is validated via dedicated Request DTOs, and every response is transformed into explicit Response DTOs.
- **Manual Mapping**: DTO $\leftrightarrow$ Entity mappings are implemented explicitly in private service helper methods, avoiding complex reflection libraries (such as MapStruct or ModelMapper) to maintain full code explainability.
- **Explicit Relational Mappings**: All entity relationships use `FetchType.LAZY` to avoid N+1 query storms. Cascade operations are restricted solely to ephemeral child tasks (`Employee.onboardingTasks` with `CascadeType.ALL, orphanRemoval = true`). All legal/audit-critical relationships (leave, attendance, payroll, reviews, assets, offboarding) prohibit cascading deletes, enforcing database-level foreign key protection.
- **Standardized Exception Handling**: A centralized `@RestControllerAdvice` translates domain exceptions (`ResourceNotFoundException` $\rightarrow$ 404, `DuplicateResourceException` $\rightarrow$ 409, `BusinessRuleException` $\rightarrow$ 400, `MethodArgumentNotValidException` $\rightarrow$ 400 with field errors, `DataIntegrityViolationException` $\rightarrow$ 409) into uniform JSON error envelopes.

---

## 2. Technology Stack

| Layer / Component | Technology | Version | Purpose |
|---|---|---|---|
| **Language** | Java | 17 (LTS) | Core enterprise language |
| **Framework** | Spring Boot | 3.2.5 | Application framework & dependency injection |
| **Persistence** | Spring Data JPA / Hibernate | 6.4.4 | ORM and relational database access |
| **Database** | PostgreSQL | 15+ | Relational persistence storage |
| **Validation** | Jakarta Bean Validation / Hibernate Validator | 3.0+ | Declarative request payload validation |
| **API Documentation** | Springdoc OpenAPI / Swagger UI | 2.3.0 | Automated OpenAPI v3 contract & interactive documentation UI |
| **Logging** | SLF4J / Logback | Built-in | Structured console & file logging (`logs/peopleops.log`) |
| **Build Tool** | Apache Maven | 3.8+ | Dependency management, compilation, and WAR/JAR packaging |

---

## 3. Database Setup & Configuration

### Prerequisites
- Java 17+ installed and set on `PATH`
- PostgreSQL 15+ running locally on port `5432`
- Maven 3.8+ installed

### Database Creation
Run the following commands in PostgreSQL (`psql` or pgAdmin):
```sql
CREATE DATABASE peopleops_db;
```

The database configuration in `src/main/resources/application.properties` connects using:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/peopleops_db
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

---

## 4. Build, Packaging, and Execution

### 1. Compile Code
```bash
mvn clean compile
```

### 2. Run in Development Mode
```bash
mvn spring-boot:run
```
The application boots up on `http://localhost:8080`.

### 3. Package Executable WAR / JAR Artifact
```bash
mvn clean package -DskipTests
```
This produces the deployable artifact in `target/`:
- `target/peopleops-0.0.1-SNAPSHOT.war` (Executable WAR with embedded Tomcat in `WEB-INF/lib-provided/`).

### 4. Run Packaged Artifact Standalone
```bash
java -jar target/peopleops-0.0.1-SNAPSHOT.war
```
To run on an alternate port:
```bash
java -jar target/peopleops-0.0.1-SNAPSHOT.war --server.port=8081
```

### 5. Deploy to External Servlet Container
Because `PeopleOpsApplication` extends `SpringBootServletInitializer`, the generated `.war` file can be copied directly into the `webapps/` folder of an external standalone Apache Tomcat or WildFly application server:
```bash
cp target/peopleops-0.0.1-SNAPSHOT.war $CATALINA_HOME/webapps/peopleops.war
```

---

## 5. API Documentation & Interactive Swagger UI

Once the application is running, access the interactive OpenAPI documentation:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI v3 Raw JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 6. Domain Modules & Endpoint Overview

PeopleOps provides 66 REST endpoints across 13 functional modules (20 tables):

| # | Module | Base Path | Key Endpoints & Operations |
|---|---|---|---|
| 1 | **Departments** | `/api/departments` | CRUD, active toggle (`PATCH /{id}/active`) |
| 2 | **Designations** | `/api/designations` | CRUD, grade level classification |
| 3 | **Employees** | `/api/employees` | CRUD, filter by status & dept, transfer (`PATCH /{id}/transfer`), promote (`PATCH /{id}/promote`), skills (`GET/POST/DELETE /{id}/skills/{skillId}`) |
| 4 | **Job Openings** | `/api/job-openings` | CRUD, status transition (`PATCH /{id}/status`) |
| 5 | **Candidates** | `/api/candidates` | CRUD, stage progression (`PATCH /{id}/stage`), hire candidate (`POST /{id}/hire`) |
| 6 | **Interviews** | `/api/interviews` | CRUD, record result & feedback (`PATCH /{id}/result`) |
| 7 | **Onboarding Tasks** | `/api/onboarding-tasks` | CRUD, checklist completion & activation hook (`PATCH /{id}/complete`) |
| 8 | **Attendance** | `/api/attendance` | CRUD, check-in, check-out (`PATCH /{id}/check-out`), daily work hours calculation |
| 9 | **Leave Types** | `/api/leave-types` | CRUD, quota & paid/unpaid status configuration |
| 10 | **Leave Balances** | `/api/leave-balances` | View & manage allocated, used, and remaining balances per employee |
| 11 | **Leave Requests** | `/api/leave-requests` | Apply, approve (`PATCH /{id}/approve`), reject (`PATCH /{id}/reject`), cancel (`PATCH /{id}/cancel`) |
| 12 | **Payrolls** | `/api/payrolls` | Generate monthly payroll (`POST /generate`), mark paid (`PATCH /{id}/pay`), delete draft |
| 13 | **Performance Reviews** | `/api/performance-reviews` | CRUD, rating (1–5) and feedback, submission (`PATCH /{id}/submit`) |
| 14 | **Skills** | `/api/skills` | Competency catalog CRUD |
| 15 | **Training Programs** | `/api/training-programs` | CRUD, status transition, curriculum skills (`GET/POST/DELETE /{id}/skills/{skillId}`) |
| 16 | **Training Enrollments** | `/api/training-enrollments` | Enroll employee, complete & auto-award skills (`PATCH /{id}/complete`), drop course (`PATCH /{id}/drop`) |
| 17 | **Assets** | `/api/assets` | Hardware/equipment inventory CRUD, retire (`PATCH /{id}/retire`) |
| 18 | **Asset Assignments** | `/api/asset-assignments` | Assign hardware, return hardware (`PATCH /{id}/return`) |
| 19 | **Employee Documents** | `/api/employee-documents` | Upload metadata, verify document & trigger activation (`PATCH /{id}/verify`) |
| 20 | **Offboardings** | `/api/offboardings` | Initiate resignation, clearance validation (assets & payroll), complete exit (`PATCH /{id}/complete`) |

---

## 7. Business Rules Enforced (BR-01 through BR-14)

- **BR-01 (Hire)**: Candidate must be in `OFFERED` stage; opening must be `OPEN`; hiring decrements open positions; closing opening when positions reach zero; links `candidate_id` to employee.
- **BR-02 (Onboarding Tasks)**: Hiring creates 5 standard onboarding checklist tasks (`PENDING`).
- **BR-03 (Activation)**: Employee transitions from `ONBOARDING` to `ACTIVE` when all onboarding tasks are `DONE` and an `ID_PROOF` document is verified. Initializes annual leave balances.
- **BR-04 (Attendance)**: One record per employee per date. Check-out time must be after check-in time. Automatically calculates total work hours.
- **BR-05 & BR-06 (Leaves)**: Leave request checks sufficient balance; approval deducts balance and records `ON_LEAVE` attendance; cancellation restores balance.
- **BR-07 (Payroll)**: Generated once per employee per month. Automatically computes LOP deduction from attendance/leaves and calculates PF deduction (12% of gross salary).
- **BR-08 (Performance Review)**: Reviewer must be an active employee different from the reviewee. One review per employee per period. Submitted reviews are immutable.
- **BR-09 (Training & Skills)**: Enrolls only active employees into open programs. Completing enrollment automatically awards the training program's syllabus skills to the employee.
- **BR-10 (Assets)**: Only `AVAILABLE` assets can be assigned to `ONBOARDING` or `ACTIVE` employees. Return sets assignment to `RETURNED` and restores asset to `AVAILABLE`.
- **BR-11 (Documents)**: Document upload records compliance metadata; verifying an `ID_PROOF` triggers the employee activation check (BR-03).
- **BR-12 (Offboarding)**: Initiation requires `ACTIVE` status; sets employee to `ON_NOTICE`. Completion requires zero unreturned assets and final month payroll generated; sets employee to `EXITED`.
- **BR-13 & BR-14 (Integrity & Deletions)**: Parents with active foreign key references cannot be deleted (returns HTTP 409 Conflict). Unique constraints enforced on email, employee code, asset tag, leave code, and department code.

---

## 8. Relational Schema & Entity Relationships

PostgreSQL database enforces 29 foreign key constraints and 2 join tables across 20 entity tables:
- **Many-to-One (N-1)**:
  - `employees` $\rightarrow$ `departments`, `designations`, `employees` (manager)
  - `job_openings` $\rightarrow$ `departments`, `designations`
  - `candidates` $\rightarrow$ `job_openings`
  - `interviews` $\rightarrow$ `candidates`, `employees` (interviewer)
  - `onboarding_tasks` $\rightarrow$ `employees` (Cascade ALL)
  - `attendance_records` $\rightarrow$ `employees`
  - `leave_balances` $\rightarrow$ `employees`, `leave_types`
  - `leave_requests` $\rightarrow$ `employees`, `leave_types`, `employees` (decided_by)
  - `payrolls` $\rightarrow$ `employees`
  - `performance_reviews` $\rightarrow$ `employees`, `employees` (reviewer)
  - `training_enrollments` $\rightarrow$ `training_programs`, `employees`
  - `asset_assignments` $\rightarrow$ `assets`, `employees`
  - `employee_documents` $\rightarrow$ `employees`
- **One-to-One (1-1)**:
  - `offboardings` $\rightarrow$ `employees` (`employee_id` unique FK)
  - `employees` $\rightarrow$ `candidates` (`candidate_id` unique FK)
- **Many-to-Many (N-N)**:
  - `employees` $\leftrightarrow$ `skills` via `employee_skills(employee_id, skill_id)`
  - `training_programs` $\leftrightarrow$ `skills` via `training_program_skills(training_program_id, skill_id)`

---

## 9. Documentation Directory Index

Detailed documentation files located in [`docs/`](docs/):
- [`docs/prd.md`](docs/prd.md) — Product Requirement Document & scope definition
- [`docs/trd.md`](docs/trd.md) — Technical Requirement Document & system architecture
- [`docs/erd.md`](docs/erd.md) — Entity-Relationship Diagram and schema specifications
- [`docs/implementation.md`](docs/implementation.md) — 196-step build plan and checkpoint roadmap
- [`docs/explainer.md`](docs/explainer.md) — Detailed living learning log, decisions record, and viva preparation questions
