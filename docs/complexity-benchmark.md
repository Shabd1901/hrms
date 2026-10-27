# complexity-benchmark.md

**Status: COMPLETED** — Calibrated against the reference Java FSD project (`student`).

This file defines the complexity boundary for PeopleOps (Enterprise Human Resource Management System) in comparison to the reference project.

The reference project serves as an architectural baseline for core Spring Boot concepts, while PeopleOps expands into a full 4-tier enterprise-grade system.

---

## Reference Project (Student Management System - `student`)

### Project Scope
- Name / domain: Student & Laptop Management (`student`) — Academic baseline project
- Major modules: Student, Laptop (2 basic modules)
- Overall complexity: Low (2-tier classroom demonstration)
- Main workflows: Basic CRUD for Students and Laptops, simple 1-to-1 hardware assignment

### Architecture
- Architecture pattern: 2-tier architecture (REST Controller directly injecting Spring Data JPA Repositories)
- Number of entities: 2 (`Student`, `Laptop`)
- Number of controllers: 2 (`StudentController`, `LaptopController`)
- Number of services / facades: 0 (No service layer; persistence logic embedded directly in controllers)
- Number of repositories: 2 (`StudentRepository`, `LaptopRepository`)
- DTO approach: None (Entities are directly returned in API responses or wrapped inside `ResponseBean`)
- Other important architectural components: `ResponseBean` helper POJO (`errorCode`, `errorDesc`, `data`) for packaging responses

### API
- Number of endpoints: ~10 endpoints (`/students`, `/resstudents`, `/excepstudents`, `/student/{id}`, etc.)
- CRUD vs business/workflow endpoints: 100% simple CRUD operations
- Validation approach: Ad-hoc manual `null` checks or absence of input validation
- Exception handling: Localized `try-catch` blocks inside individual controller methods; no centralized handler
- Swagger / OpenAPI: None configured

### Database
- Number of tables / entities: 2 tables (`student`, `laptop`)
- Relationships used (1-1, 1-N, N-N): 1 relationship: `@OneToOne` bidirectional (`Student` <-> `Laptop`)
- Approx. relationship count: 1
- Enums / constraints / other notable features: Basic foreign key column (`laptop_id` on student); Hibernate `ddl-auto=update`

### Business Logic
- Business rules beyond CRUD: None; direct entity reads and writes
- State/status workflows: None; entities have no lifecycle or status machine
- Cross-entity operations: Manual setting of laptop reference on student during assignment
- Calculations / derived values: None
- Most complex modules: `StudentController` (handling manual ResponseBean packaging and inline exception catches)

### Code Style / Human Complexity
- Lombok used?: No; standard Java POJOs with manual getters, setters, and constructors
- Approx. lines per service class: N/A (no service layer)
- Typical method size: 10–25 lines per controller method
- Interfaces used?: Only Spring Data `JpaRepository` interfaces
- Design patterns used?: Repository pattern (Spring Data), MVC Controller pattern
- Generic / reflection-based logic?: None
- DTO mappers used?: None
- Notably simple implementations: Controllers handle HTTP requests, entity queries, and response shaping in a single method
- Notably complex implementations: None
- Anything that should NOT be replicated: Mixing database persistence inside REST controllers, lack of DTO separation, and missing centralized exception handling

---

## PeopleOps Complexity Boundary

### Target Scope
- Major modules: 7 enterprise HR modules:
  1. Organization & Core HR (Department, Designation, Location, Employee, Document)
  2. Attendance & Leaves (Attendance, LeaveType, LeaveRequest, LeaveBalance, Holiday)
  3. Payroll & Compensation (SalaryStructure, PayrollRecord, BonusRecord, DeductionRule)
  4. Recruitment & Onboarding (JobOpening, Candidate, Interview)
  5. Performance & Appraisals (PerformanceReview)
  6. Learning & Development (Skill, TrainingProgram, TrainingEnrollment)
  7. Auditing & Compliance (AuditLog)
- Overall target complexity: High enterprise grade (4-tier architecture with clean separation of concerns)
- Main workflows: Employee lifecycle, Leave application & approval workflow with auto balance deduction, Payroll processing & net salary computation, Recruitment pipeline & interview scheduling, Skill management & training enrollment tracking, Audit trail logging

### Target Architecture
- Entities: 20 JPA domain entities
- Controllers: 20 REST controllers annotated with `@RestController` and `@RequestMapping("/api/...")`
- Services: 20 Service interfaces + 20 Service implementations (`@Service`) annotated with `@Transactional`
- Repositories: 20 Spring Data JPA repository interfaces extending `JpaRepository` with custom query methods
- DTO approach: Strict DTO separation. Dedicated Request and Response DTOs for all entities; 100% entity encapsulation with zero entity leakage to the presentation layer
- Other components: Centralized `GlobalExceptionHandler` (`@RestControllerAdvice`), standard `ResponseBean` compatibility package (`com.peopleops.responsebean`), SLF4J + Logback rolling file logging (`logs/peopleops.log`), `SpringBootServletInitializer` for dual JAR/WAR deployment

### Target API
- Endpoint range: 123 REST endpoints
- CRUD vs workflow operations: Full RESTful CRUD operations plus dedicated workflow endpoints (status transitions, leave approvals, payroll runs, skill attachments, search & filter queries)
- Validation: Declarative Bean Validation (Jakarta Validation: `@NotNull`, `@NotBlank`, `@Email`, `@Positive`, `@PastOrPresent`, `@FutureOrPresent`, `@Size`, etc.) enforced via `@Valid` on controller request bodies
- Exception handling: Centralized `@RestControllerAdvice` converting domain exceptions (`ResourceNotFoundException`, `DuplicateResourceException`, `InvalidStatusTransitionException`, `InsufficientLeaveBalanceException`, validation failures) into structured API error payloads
- Swagger / OpenAPI: Standard Spring Boot REST API documentation-ready structure

### Target Database
- Entity range: 20 tables + 2 join tables (`employee_skills`, `training_program_skills`)
- Relationship range: 19 relational mappings across entities
- Relationship types:
  - `@OneToOne`: Employee <-> SalaryStructure (managed 1:1 compensation profile)
  - `@ManyToOne` / `@OneToMany`: Department -> Employees, Designation -> Employees, Employee -> Attendances, Employee -> LeaveRequests, Employee -> LeaveBalances, Employee -> PayrollRecords, JobOpening -> Candidates, Candidate -> Interviews, etc.
  - `@ManyToMany`: Employee <-> Skill (via `employee_skills`), TrainingProgram <-> Skill (via `training_program_skills`)
- Other constraints/features: PostgreSQL 15+, foreign keys with cascade controls, unique constraints (e.g. employee code, department code, email), B-tree indexes, check constraints

### Target Business Logic
- Required business workflows:
  - Multi-state leave request approval/rejection with automated balance validation and balance deduction
  - Candidate hiring pipeline: `APPLIED` -> `SCREENING` -> `INTERVIEWING` -> `OFFERED` -> `HIRED` / `REJECTED`
  - Training program enrollment and status lifecycle: `ENROLLED` -> `IN_PROGRESS` -> `COMPLETED` / `DROPPED`
  - Automated net pay calculation: `Net Salary = Base + Allowances - Deductions - Tax`
  - Audit logging of critical employee and administrative actions
- Required business rules:
  - Overlap prevention for employee leave dates
  - Prevention of duplicate email or employee codes
  - Guard against negative leave balance deductions
  - Strict status state-machine validations
- Acceptable complexity: Service layer encapsulates all business logic; controllers remain thin and delegate directly to services

### Target Code Style
- Expected method/class complexity: Clean, readable methods (typically 15–40 lines per service method), highly cohesive classes with single responsibility
- Abstractions allowed: Service interfaces with concrete implementations, standard Spring Data repository abstractions, DTO mappers
- Abstractions to avoid: Overly complex generic reflection frameworks, premature dynamic proxying, unreadable metaprogramming
- Design patterns required:
  - Layered Architecture (Controller-Service-Repository-Entity)
  - Data Transfer Object (DTO) pattern
  - Repository pattern (Spring Data JPA)
  - Dependency Injection / Inversion of Control (Spring `@Autowired` / constructor injection)
  - Controller Advice / Interceptor pattern for global exception handling
  - Builder / Factory pattern in DTO mapping
- Design patterns to avoid: Over-engineered abstract factory hierarchies or complex CQRS/Event-Sourcing that would obscure clarity during academic evaluation
- General implementation principle: Professional enterprise readability and maintainability — every file and method has an unmistakable purpose that can be defended clearly in an oral viva examination

---

## Complexity Rules

1. Match the **level of complexity**, not the exact numbers.
2. Do not add entities, endpoints, classes, abstractions, or patterns only to increase project size.
3. Prefer readable and explainable code over clever code.
4. Every class and abstraction must have a clear purpose.
5. Business complexity is preferred over technical complexity.
6. The implementation must remain realistic for a student who is expected to explain every file during evaluation.
