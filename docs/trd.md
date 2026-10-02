# TRD — PeopleOps Technical Requirements
 
Version 1.0 · Created 2026-10-02 · Read with PRD.md (what) and ERD.md (data).
 
---
 
## 1. Stack
 
| Item | Choice |
|---|---|
| Language | Java 17 or 21 (LTS), whichever is installed; record it in `explainer.md` |
| Framework | Spring Boot 3.x (Initializr's current 3.x release) |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL, database `peopleops_db` |
| Build | Maven, packaging `jar` |
| API | REST, JSON |
| Group / artifact | `com.peopleops` / `peopleops` |
| Base package | `com.peopleops` |
| Main class | `PeopleOpsApplication` |
 
**Dependencies by week** (nothing else without approval)
 
| Week | Dependency |
|---|---|
| 1 | `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `postgresql` (runtime), `lombok`, `spring-boot-starter-test` |
| 3 | `spring-boot-starter-validation` (Phase 3.3), `springdoc-openapi-starter-webmvc-ui` (Phase 3.4) |
 
Pick the springdoc major version that matches the Spring Boot major version, and check its documentation when adding it.
 
## 2. Architecture
 
Simple layered design, grouped **by type** (not by feature):
 
```
controller  →  service  →  repository  →  PostgreSQL
   (DTO in/out)  (rules)     (JpaRepository)
```
 
```
com.peopleops
├── PeopleOpsApplication.java
├── controller/    XController
├── service/       XService          (concrete classes, no interfaces)
├── repository/    XRepository       (extends JpaRepository<X, Long>)
├── entity/        X
├── dto/           XRequestDTO, XResponseDTO
├── enums/         EmployeeStatus ...
├── exception/     (Week 3)
└── config/        (Week 3, OpenAPI)
```
 
| Layer | Does | Must not |
|---|---|---|
| Controller | Maps URLs, takes DTOs/params, calls one service method, returns `ResponseEntity` | Contain rules, touch repositories |
| Service | All business rules, DTO ↔ entity mapping, repository calls | Return entities to controllers |
| Repository | Interface with finder methods | Contain logic |
| Entity | Table mapping | Be returned from an API |
| DTO | Request/response shape | Contain logic |
 
## 3. Coding conventions
 
- Constructor injection written by hand (no field `@Autowired`, no `@RequiredArgsConstructor`).
- **Lombok is limited** to `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` on entities and DTOs. No `@Data`, no `@Builder`, no `@Slf4j`.
- One `XRequestDTO` (POST/PUT) and one `XResponseDTO` per entity. Special DTOs only where the API needs them: `HireRequestDTO`, `PayrollGenerateRequestDTO`.
- DTOs carry IDs for references (`departmentId`), never nested entities. They stay the same before and after Week 4.
- Mapping is done by private `toEntity` / `toResponse` methods inside each service.
- Dates: `LocalDate`, `LocalTime`, `LocalDateTime`. Money: `BigDecimal`. Day counts: `Integer` or `Double` as in ERD.
- Repository methods are added only when a task needs them.
- Class comments, naming and forbidden patterns follow `workflow.md` §7–8.
## 4. Entity conventions
 
```java
// Week 1 – plain ID column, no relationship annotation
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private Long departmentId;      // becomes a relationship in Week 4
    // ...
}
```
 
```java
// Week 4 – same column name, now a real relationship
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```
 
- Spring Boot's naming strategy maps `departmentId` → `department_id`, so Week 4 keeps the same column.
- Enums: `@Enumerated(EnumType.STRING)`.
- Composite uniques and column sizes follow ERD.md §3–4.
- Entities have getters/setters only; no business methods.
## 5. Configuration
 
`src/main/resources/application.properties`
 
```properties
spring.application.name=peopleops
server.port=8080
 
spring.datasource.url=jdbc:postgresql://localhost:5432/peopleops_db
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
 
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
 
- `ddl-auto=update` lets Hibernate create and adjust tables during development. Flyway is out of scope.
- Environment variables override the local defaults, so no real password is committed.
- Leave `spring.jpa.open-in-view` at its default so lazy relationships work during DTO mapping in Week 4.
- Week 3 adds log settings (Phase 3.1).
## 6. REST API catalog
 
Base path `/api`. `{id}` is a `Long`. Status changes use `PATCH` with a request parameter (`?value=` for the new status). "W4" items are added in Week 4.
 
| Module | Path | Endpoints |
|---|---|---|
| Departments | `/api/departments` | GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/active?value=` |
| Designations | `/api/designations` | GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Employees | `/api/employees` | GET (`?departmentId=&status=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/transfer?departmentId=`, PATCH `/{id}/promote?designationId=`; W4: GET `/{id}/skills`, POST `/{id}/skills/{skillId}`, DELETE `/{id}/skills/{skillId}` |
| Job openings | `/api/job-openings` | GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/status?value=` |
| Candidates | `/api/candidates` | GET (`?jobOpeningId=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/stage?value=`, POST `/{id}/hire` |
| Interviews | `/api/interviews` | GET (`?candidateId=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/result?value=&feedback=` |
| Onboarding tasks | `/api/onboarding-tasks` | GET (`?employeeId=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/complete` |
| Attendance | `/api/attendance` | GET (`?employeeId=&date=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/check-out?time=` |
| Leave types | `/api/leave-types` | GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Leave balances | `/api/leave-balances` | GET (`?employeeId=&year=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Leave requests | `/api/leave-requests` | GET (`?employeeId=&status=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/approve?decidedById=`, PATCH `/{id}/reject?decidedById=`, PATCH `/{id}/cancel` |
| Payrolls | `/api/payrolls` | GET (`?employeeId=`), GET `/{id}`, POST `/generate`, PATCH `/{id}/pay`, DELETE `/{id}` (no PUT: payroll is calculated, not typed in) |
| Performance reviews | `/api/performance-reviews` | GET (`?employeeId=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/submit` |
| Skills | `/api/skills` | GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Training programs | `/api/training-programs` | GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/status?value=`; W4: POST `/{id}/skills/{skillId}`, DELETE `/{id}/skills/{skillId}` |
| Training enrolments | `/api/training-enrollments` | GET (`?employeeId=`), GET `/{id}`, POST, DELETE `/{id}`, PATCH `/{id}/complete`, PATCH `/{id}/drop` (no PUT) |
| Assets | `/api/assets` | GET, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/retire` |
| Asset assignments | `/api/asset-assignments` | GET (`?employeeId=`), GET `/{id}`, POST (assign), PATCH `/{id}/return` |
| Employee documents | `/api/employee-documents` | GET (`?employeeId=`), GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/verify` |
| Offboardings | `/api/offboardings` | GET, GET `/{id}`, POST (initiate), PUT `/{id}`, DELETE `/{id}`, PATCH `/{id}/complete` |
 
**Status codes**
 
| Case | Code |
|---|---|
| GET, PUT, PATCH success | 200 |
| POST create / generate / hire / assign / initiate | 201 |
| DELETE success | 204 |
| ID not found (path or referenced in body) | 404 |
| Business rule broken | 400 |
| Duplicate (unique value) | 409 |
| Unexpected error | 500 |
 
## 7. Where each business rule lives
 
| Rule | Class | Notes |
|---|---|---|
| BR-01 | `CandidateService.hire` → `EmployeeService.createFromCandidate` | |
| BR-02 | `EmployeeService` (private method used by create paths) | Uses `OnboardingTaskRepository` |
| BR-03 | `EmployeeService.activateIfReady` | Called by `OnboardingTaskService.complete` and `EmployeeDocumentService.verify` |
| BR-04 | `AttendanceService` | |
| BR-05, BR-06 | `LeaveRequestService` | Uses balance and attendance repositories |
| BR-07 | `PayrollService` | Reads attendance and leave through repositories |
| BR-08 | `PerformanceReviewService` | |
| BR-09 | `TrainingEnrollmentService` | Skill linking added in Phase 4.7 |
| BR-10 | `AssetAssignmentService`, `AssetService` | |
| BR-11 | `EmployeeDocumentService` | |
| BR-12 | `OffboardingService` | Uses employee, asset-assignment, payroll repositories |
| BR-13 | `DepartmentService`, `DesignationService`, `EmployeeService` | |
| BR-14 | Each service, with small private existence checks | |
 
**Cross-module rule:** `EmployeeService` is the lifecycle hub. `CandidateService`, `OnboardingTaskService` and `EmployeeDocumentService` may call it. `EmployeeService` calls no other service. Every other cross-module need uses the other module's **repository**, never its service. This prevents circular dependencies and keeps the flow easy to explain.
 
**Business rules vs validation:** rules that depend on state or other records (BR-xx) are built in Week 2. Field-level checks (not blank, size, range, email format, rating 1–5) are built in Week 3 as Bean Validation.
 
## 8. Error handling
 
**Week 2:** services throw Spring's `ResponseStatusException` with the code from §6 and a clear message, for example `new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id " + id)`.
 
**Week 3:** replaced by custom exceptions and one `@RestControllerAdvice`.
 
| Exception | Status |
|---|---|
| `ResourceNotFoundException` | 404 |
| `BusinessRuleException` | 400 |
| `DuplicateResourceException` | 409 |
| `MethodArgumentNotValidException` | 400, with field errors |
| `HttpMessageNotReadableException`, `MethodArgumentTypeMismatchException` | 400 (bad JSON / bad enum value) |
| `DataIntegrityViolationException` | 409 |
| `Exception` (fallback) | 500, generic message |
 
Error body (`ErrorResponse`): `timestamp`, `status`, `error`, `message`, `path`, `fieldErrors` (map, only for validation).
 
## 9. Logging (Week 3)
 
- SLF4J with a plain logger line per service: `private static final Logger log = LoggerFactory.getLogger(X.class);`
- Services only, not controllers.
- `INFO` for lifecycle events (hire, activation, approval, payroll generated, exit); `WARN` for rejected business rules; `ERROR` for unexpected failures.
- Output to console and `logs/peopleops.log`; `logs/` is git-ignored.
## 10. Swagger / OpenAPI (Week 3)
 
- `/swagger-ui.html` and `/v3/api-docs`.
- One `OpenApiConfig` bean (title, description, version).
- `@Tag` on each controller; `@Operation(summary=...)` on lifecycle action endpoints only.
## 11. Testing
 
- Postman is the test tool. One folder per module in a single collection saved at `postman/PeopleOps.postman_collection.json`.
- Each phase ends with a "Postman test" step: the AI lists the requests, the user runs them and reports the result.
- Week 3 adds error and validation cases. Week 4 adds relationship and full-lifecycle runs.
- No extra automated test code is required. The `contextLoads` test from Initializr stays as is.
## 12. Build and run
 
```
mvn clean package
java -jar target/peopleops-0.0.1-SNAPSHOT.jar
```
 
PostgreSQL must be running. The JAR is built with `spring-boot-maven-plugin` from Initializr. Verified in Phase 4.9.
 
## 13. Repository layout
 
```
peopleops/
├── docs/                 workflow, PRD, TRD, ERD, implementation, explainer, benchmark
├── postman/
├── src/main/java/com/peopleops/...
├── src/main/resources/application.properties
├── pom.xml
├── README.md
└── .gitignore            target/, logs/, IDE files
```
 
Git rules are in `workflow.md` §10.
 
## 14. Decisions to confirm after the complexity benchmark
 
| Decision | Current choice | Revisit if the benchmark shows |
|---|---|---|
| Lombok (limited) | Yes | The reference project uses plain getters/setters |
| Separate Request/Response DTOs | Yes | The reference project uses one DTO per entity |
| 20 entities | Yes | The reference project is clearly smaller; trim by merging low-value modules |
| `PATCH` with request parameters | Yes | The reference project uses request bodies |
 
Changes are logged in `explainer.md` and applied to PRD/TRD/ERD only with approval.
 