# explainer.md — PeopleOps Living Project Record

Personal scratch document. Updated by the AI after **every** step (rules: `workflow.md` §9). Written so each decision can be explained out loud.

Last updated: 2026-10-27 · Current step: **4.7.1** · Status: completed (Step 4.7.1 Employee to Skill many-to-many completed; next: Step 4.7.2)

---

## 1. Overall project progress

| Measure | Value |
|---|---|
| Semester | 1 (Backend) |
| Weeks complete | 3 / 4 |
| Phases complete | 34 / 36 |
| Steps complete | 187 / 196 |
| Commits (checkpoints) made | 27 / 23 |
| Overall | 95.4% |
| Complexity benchmark | PENDING (must be filled before step 1.5.1) |
| Java / Spring Boot / PostgreSQL versions | Java 24 (Target Java 17) / Spring Boot 3.2.5 / PostgreSQL 15+ |

## 2. Weeks

| Week | Scope | Phases | Steps | Done | Commits | Status |
|---|---|---|---|---|---|---|
| 1 | Requirements, ERD, database, setup, entities | 10 | 40 | 40 | 5 / 5 | Completed |
| 2 | REST APIs | 12 | 90 | 90 | 8 / 8 | Completed |
| 3 | Logging, exceptions, validation, Swagger, API testing | 5 | 31 | 31 | 4 / 4 | Completed |
| 4 \| Mappings, mapping tests, JAR & WAR, final testing \| 9 \| 35 \| 23 \| 2 / 6 | In progress |

## 3. Phases

| Phase | Name | Steps | Done | Checkpoint | Status |
|---|---|---|---|---|---|
| 1.1 | Requirements and documentation baseline | 3 | 3 | CP-01 | Completed |
| 1.2 | PostgreSQL database creation | 1 | 1 | CP-02 | Completed |
| 1.3 | Spring Boot project setup | 2 | 2 | CP-02 | Completed |
| 1.4 | Database connectivity | 2 | 2 | CP-02 | Completed |
| 1.5 | Core entities: Department, Designation, Employee | 5 | 5 | CP-03 | Completed |
| 1.6 | Recruitment and onboarding entities | 5 | 5 | CP-03 | Completed |
| 1.7 | Attendance and leave entities | 6 | 6 | CP-04 | Completed |
| 1.8 | Payroll and performance entities | 4 | 4 | CP-04 | Completed |
| 1.9 | Training and asset entities | 7 | 7 | CP-05 | Completed |
| 1.10 | Documents and offboarding entities, Week 1 wrap-up | 4 | 4 | CP-05 | Completed |
| 2.1 | Department and Designation APIs | 8 | 8 | CP-06 | Completed |
| 2.2 | Employee APIs | 6 | 6 | CP-06 | Completed |
| 2.3 | Recruitment APIs | 13 | 13 | CP-07 | Completed |
| 2.4 | Onboarding and the hire workflow | 7 | 7 | CP-08 | Completed |
| 2.5 | Attendance and leave APIs | 15 | 15 | CP-09 | Completed |
| 2.6 | Payroll APIs | 5 | 5 | CP-10 | Completed |
| 2.7 | Performance APIs | 5 | 5 | CP-10 | Completed |
| 2.8 | Training and skills APIs | 11 | 11 | CP-11 | Completed |
| 2.9 | Asset APIs | 8 | 8 | CP-11 | Completed |
| 2.10 | Documents and employee activation | 6 | 6 | CP-12 | Completed |
| 2.11 | Offboarding APIs | 5 | 5 | CP-12 | Completed |
| 2.12 | Week 2 wrap-up | 1 | 1 | CP-13 | Completed |
| 3.1 | Logging | 7 | 7 | CP-14 | Completed |
| 3.2 | Global exception handling | 9 | 9 | CP-15 | Completed |
| 3.3 | Validation | 7 | 7 | CP-16 | Completed |
| 3.4 | Swagger / OpenAPI | 5 | 5 | CP-17 | Completed |
| 3.5 | API testing and Week 3 wrap-up | 3 | 3 | CP-17 | Completed |
| 4.1 | Employee core mappings | 6 | 6 | CP-18 | Completed |
| 4.2 | Recruitment and onboarding mappings | 5 | 5 | CP-18 | Completed |
| 4.3 | Attendance and leave mappings | 4 | 4 | CP-19 | Completed |
| 4.4 | Payroll and performance mappings | 3 | 3 | CP-19 | Completed |
| 4.5 | Training, asset and document mappings | 4 | 4 | CP-19 | Completed |
| 4.6 | One-to-One mappings | 3 | 0 | CP-20 | Not started |
| 4.7 | Many-to-Many mappings | 4 | 0 | CP-21 | Not started |
| 4.8 | Mapping verification | 2 | 0 | CP-22 | Not started |
| 4.9 | JAR & WAR build and completion | 4 | 0 | CP-23 | Not started |

## 4. Counters

| Total steps | Completed | Remaining | Commits made |
|---|---|---|---|
| 196 | 183 | 13 | 27 / 23 |

Steps per week: W1 40/40 · W2 90/90 · W3 31/31 · W4 22/35
Commits per week: W1 5/5 · W2 8/8 · W3 4/4 · W4 2/6

## 5. Current and next steps

| | Step |
|---|---|
| Current | 4.5.4 — Test training enrollment, asset assignment, document upload and verification flow (CP-19 completed) |
| Next | 4.6.1 — Offboarding.employee one-to-one (R16) |
| After that | 4.6.2 — Employee.candidate one-to-one, set on hire (R17) |

## 6. Issues and remaining work

| # | Found in step | Issue / remaining work | Status |
|---|---|---|---|
| I-001 | — | Complexity benchmark not provided yet; may trim scope or change TRD §14 decisions | Open |

## 7. Implementation decisions and explanations

Each decision is written as: what, why, and the alternative that was not chosen.

| ID | Decision | Why (plain explanation) | Not chosen |
|---|---|---|---|
| D-001 | Layered packages grouped by type (`controller`, `service`, `repository`, `entity`, `dto`) | Easiest structure to explain and navigate: one folder per job | Grouping by feature, which needs more folders for a small project |
| D-002 | No service interfaces | One implementation per service, so an interface adds a file and no benefit | `XService` + `XServiceImpl` |
| D-003 | Entities are never returned from controllers; DTOs carry IDs | Keeps the API stable when relationships change in Week 4 and avoids infinite JSON loops between linked entities | Returning entities directly |
| D-004 | Week 1 entities use plain `Long` ID fields; Week 4 turns them into relationships | The FSD notice schedules mappings in Week 4; same column names mean the tables do not change | Mapping relationships in Week 1 |
| D-005 | `ddl-auto=update` | Hibernate builds tables from entities, fine for a student project | Flyway or hand-written SQL scripts |
| D-006 | Enums stored as `STRING` | Readable rows in PostgreSQL and safe if enum order changes | `ORDINAL` numbers |
| D-007 | Money as `BigDecimal` | Avoids rounding errors that `double` causes with currency | `double` |
| D-008 | Week 2 errors use `ResponseStatusException`; Week 3 replaces it with custom exceptions and `@RestControllerAdvice` | Notice puts global exception handling in Week 3; Spring's built-in exception works until then | Writing the handler in Week 2 |
| D-009 | Business rules in Week 2, field validation in Week 3 | Rules depend on other rules (state); validation depends only on input shape | Mixing both in Week 2 |
| D-010 | Services use other modules' repositories; only `EmployeeService` is called by other services | Prevents circular dependencies and keeps the lifecycle flow in one visible place | Services calling many other services |
| D-011 | PATCH endpoints use request parameters for status changes | Smallest code for one-field changes | A separate PATCH body class per endpoint |
| D-012 | Lombok limited to getters, setters and constructors | Removes boilerplate without hiding logic | Full Lombok (`@Data`, `@Builder`) |
| D-013 | Documents store metadata and a URL, not files | Real upload and storage is out of scope for this semester | Multipart upload |
| D-014 | Payroll is calculated, so no PUT | Net pay comes from attendance and leave, not typed in | Editable payroll rows |
| D-015 | Postman is the test tool | Matches the notice (API testing) and gives a demo collection | Writing JUnit controller tests |
| D-016 | 196 small steps but only 23 commits | Small steps keep every change explainable; commits are made at checkpoints so history stays short and still follows the weekly order | A commit per step, or one commit per week |
| D-017 | Add WAR packaging alongside JAR in Phase 4.9 | Satisfies academic/servlet-container evaluation requirements without breaking standalone execution (Spring Boot executable WAR) | Leaving packaging as JAR only |

## 8. Commit checkpoints

Commit only at a checkpoint. Fill the hash and date when you commit.

| Checkpoint | Covers phases | Planned commit message | Commit hash | Date |
|---|---|---|---|---|
| CP-01 | 1.1 | `docs(cp-01): add project documentation baseline including prd trd erd implementation plan explainer and project configuration skeleton files` | `6616446` | 2026-10-03 |
| CP-02 | 1.2, 1.3, 1.4 | `chore(cp-02): configure postgresql database connectivity and verify spring boot hikari connection pool initialization` | `6616446` | 2026-10-03 |
| CP-03 | 1.5, 1.6 | `feat(cp-03): add employee and recruitment entities` | `6616446` | 2026-10-08 |
| CP-04 | 1.7, 1.8 | `feat(cp-04): add attendance leave payroll performance entities` | `6616446` | 2026-10-17 |
| CP-05 | 1.9, 1.10 | `feat(cp-05): add training asset document offboarding entities` | `6616446` | 2026-10-17 |
| CP-06 | 2.1, 2.2 | `feat(cp-06): add department designation employee apis` | `09dc3d3` | 2026-10-17 |
| CP-07 | 2.3 | `feat(cp-07): add recruitment apis` | `0e19eee` | 2026-10-17 |
| CP-08 | 2.4 | `feat(cp-08): add onboarding and hire workflow` | `d622e2a` | 2026-10-17 |
| CP-09 | 2.5 | `feat(cp-09): add attendance and leave apis` | `f06a302` | 2026-10-17 |
| CP-10 | 2.6, 2.7 | `feat(cp-10): add payroll and performance apis` | `29dd0ad` | 2026-10-17 |
| CP-11 | 2.8, 2.9 | `feat(cp-11): add training skill and asset apis` | `e0d43b4`, `d58f337`, `17baa90` | 2026-10-20 |
| CP-12 | 2.10, 2.11 | `feat(cp-12): add documents activation and offboarding apis` | `10f7527`, `f5ef8cc` | 2026-10-20 |
| CP-13 | 2.12 | `test(cp-13): complete week 2 api testing` | `90f4737` | 2026-10-20 |
| CP-14 | 3.1 | `feat(cp-14): implement standardized slf4j logging configuration and lifecycle event tracking across all domain service classes` | pending | |
| CP-15 | 3.2 | `feat(cp-15): add global exception handling with rest controller advice and standardized json error responses` | pending | |
| CP-16 | 3.3 | `feat(cp-16): implement bean validation framework across all domain request dtos and verify field errors via postman` | current | 2026-10-23 |
| CP-17 | 3.4, 3.5 | `feat(cp-17): add swagger openapi documentation annotations and complete week three rest api regression and validation testing` | current | 2026-10-23 |
| CP-18 | 4.1, 4.2 | `feat(cp-18): map employee recruitment and onboarding entity relationships with cascading task deletion and end to end verification` | 8a71ec0 | 2026-10-26 |
| CP-19 | 4.3, 4.4, 4.5 | `feat(cp-19): map attendance leave payroll training asset documents with foreign key relationships and lifecycle integration verification` | 7552e27 | 2026-10-27 |
| CP-20 | 4.6 | `feat(cp-20): add one-to-one mappings` | pending | |
| CP-21 | 4.7 | `feat(cp-21): add many-to-many mappings` | pending | |
| CP-22 | 4.8 | `test(cp-22): verify all mappings` | pending | |
| CP-23 | 4.9 | `chore(cp-23): build jar and war and complete project` | pending | |

## 9. Change log (Learning & Teaching Record)

Newest entry first. Copy the template for every step.

### Step 4.7.1 - Map Employee to Skill Many-to-Many and Skill Endpoints (R18)
- Date: 2026-10-27
- What we did: Added `@ManyToMany(fetch = FetchType.LAZY)` with `@JoinTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "skill_id")) private Set<Skill> skills = new HashSet<>()` to `Employee` entity. Implemented `getEmployeeSkills`, `addSkillToEmployee`, and `removeSkillFromEmployee` in `EmployeeService`, exposing REST endpoints:
  - `GET /api/employees/{id}/skills`
  - `POST /api/employees/{id}/skills/{skillId}`
  - `DELETE /api/employees/{id}/skills/{skillId}`
- Why we did it: Satisfies implementation step 4.7.1 and ERD.md § 6 relationship R18. Establishes relational many-to-many persistence between employees and skills via a dedicated join table `employee_skills`, enabling direct skill acquisition, tracking, and removal.
- How it works:
  - `@JoinTable` specifies the intermediary table `employee_skills` with foreign keys referencing `employees(id)` and `skills(id)`.
  - Managing the `skills` collection on `Employee` synchronizes the join table rows automatically upon save within the transaction.
- Viva questions:
  1. Q: Why is `Set<Skill>` preferred over `List<Skill>` for `@ManyToMany` relationships?
     A: A `Set` prevents duplicate associations between the same employee and skill at the Java level and avoids Hibernate generating full-table deletion-reinsertion queries for join tables.
  2. Q: Why does the `@ManyToMany` annotation specify `fetch = FetchType.LAZY`?
     A: Defaulting to lazy fetching prevents loading all skills for every employee during simple CRUD operations, reducing memory usage and database load.

### Step 4.6.3 - Verification: Offboarding and Candidate Hire Linkage (CP-20)
- Date: 2026-10-27
- What we did: Conducted integration verification of Phase 4.6 One-to-One domain mappings:
  1. Hired offered candidate via `POST /api/candidates/{id}/hire` and verified newly created employee profile holds `candidateId` foreign key (R17 `employees -> candidates`).
  2. Transitioned hired employee from ONBOARDING to ACTIVE by completing default onboarding checklist tasks and uploading verified ID_PROOF document.
  3. Initiated offboarding via `POST /api/offboardings` and verified employee status transitioned to ON_NOTICE and `offboardings.employee_id` references the employee (R16 `offboardings -> employees`).
  4. Verified uniqueness constraint: attempting duplicate offboarding initiation for the same employee correctly triggered rejection.
  5. Verified PostgreSQL foreign key constraints: `offboardings(employee_id)` referencing `employees(id)` with unique constraint, and `employees(candidate_id)` referencing `candidates(id)` with unique constraint.
- Why we did it: Satisfies implementation step 4.6.3 and Checkpoint CP-20. Validates end-to-end recruitment-to-exit lifecycle continuity and database referential integrity for one-to-one relationships.
- How it works:
  - Hibernate generated table alterations in PostgreSQL:
    - `alter table employees add constraint UK_... unique (candidate_id)`
    - `alter table employees add constraint FK_... foreign key (candidate_id) references candidates`
    - `alter table offboardings add constraint FK_... foreign key (employee_id) references employees`
  - Uniqueness guarantees exactly one offboarding workflow per employee and exactly one employee generated per recruited candidate.
- Viva questions:
  1. Q: How does JPA enforce a One-to-One mapping using `@JoinColumn`?
     A: By applying `@JoinColumn(unique = true)` on the owning entity side, JPA generates both a foreign key constraint and a unique constraint in PostgreSQL.
  2. Q: What happens if an offboarding record is deleted before completion?
     A: `OffboardingService.deleteOffboarding` resets the associated employee's status back from `ON_NOTICE` to `ACTIVE` and removes the offboarding record.

### Step 4.6.2 - Map Employee to Candidate One-to-One Set on Hire (R17)
- Date: 2026-10-27
- What we did: Added `@OneToOne(fetch = FetchType.LAZY)` with `@JoinColumn(name = "candidate_id", unique = true) private Candidate candidate` to `Employee` entity. Updated `EmployeeResponseDTO` to expose `candidateId`. Updated `EmployeeService.createFromCandidate` to assign `employee.setCandidate(candidate)` when converting an offered candidate to an employee record during the hiring workflow.
- Why we did it: Satisfies implementation step 4.6.2 and ERD.md § 6 relationship R17. Retains provenance from recruitment to onboarding, linking the hired candidate record directly to the created employee profile with a database unique foreign key constraint.
- How it works:
  - `Employee` is the owning entity of the candidate reference with `candidate_id` nullable (since employees can also be created directly via HR admin endpoints) and unique (a candidate cannot be hired into multiple employee rows).
  - When candidate reaches `OFFERED` stage and `POST /api/candidates/{id}/hire` is called, `EmployeeService.createFromCandidate` associates the managed `Candidate` entity with the newly created `Employee`.
- Viva questions:
  1. Q: Why is `candidate_id` on the `employees` table nullable rather than not-null?
     A: Not all employees necessarily enter the system through the candidate recruitment lifecycle (some may be direct executive hires, transfers, or initial administrative seed records).
  2. Q: Why is `candidate_id` marked `unique = true`?
     A: A recruited candidate represents an individual who can only be converted into one employee profile; duplicate conversion of the same candidate is disallowed.

### Step 4.6.1 - Map Offboarding to Employee One-to-One (R16)
- Date: 2026-10-27
- What we did: Replaced scalar `employeeId` field in `Offboarding` entity with `@OneToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "employee_id", nullable = false, unique = true) private Employee employee`. Updated `OffboardingService` to link the managed `Employee` entity during offboarding initiation, lifecycle completion, and deletion, while maintaining clean DTO responses exposing `employeeId`.
- Why we did it: Satisfies implementation step 4.6.1 and ERD.md § 6 relationship R16. Enforces database-level one-to-one relationship ensuring that each offboarding record strictly points to exactly one employee, and an employee can have at most one offboarding record via the unique constraint.
- How it works:
  - `@OneToOne(fetch = FetchType.LAZY)` defines the association where `Offboarding` is the owning side holding the `employee_id` foreign key.
  - `@JoinColumn(name = "employee_id", nullable = false, unique = true)` adds a unique foreign key constraint in PostgreSQL.
  - Spring Data JPA derived queries such as `findByEmployeeId` and `existsByEmployeeId` automatically query across `offboarding.employee.id`.
- Viva questions:
  1. Q: Why is `employee_id` in `offboardings` marked `unique = true` in `@JoinColumn`?
     A: Marking `employee_id` unique enforces the One-to-One relationship at the database level, preventing multiple offboarding records from being created for the same employee.
  2. Q: Which entity is the owning side of the Offboarding-Employee relationship and why?
     A: `Offboarding` is the owning side because its table `offboardings` contains the `employee_id` foreign key column.

### Step 4.5.4 — Verification: Training, Asset, Document Lifecycles and Foreign Keys (CP-19)
- Date: 2026-10-27
- What we did: Conducted integration verification of Phase 4.5 domain mappings across training, asset allocation, and employee documents:
  1. Created corporate training program and enrolled employee (R13 `training_enrollments -> training_programs, employees`).
  2. Completed training enrollment via `PATCH /api/training-enrollments/{id}/complete` and verified status `COMPLETED` with `completionDate`.
  3. Created available asset and assigned hardware to employee (R14 `asset_assignments -> assets, employees`).
  4. Verified asset state transition to `ASSIGNED` and returned asset via `PATCH /api/asset-assignments/{id}/return`, verifying status transition to `RETURNED` with asset status returning to `AVAILABLE`.
  5. Uploaded compliance document (R15 `employee_documents -> employees`) and verified document via `PATCH /api/employee-documents/{id}/verify`.
  6. Verified PostgreSQL foreign key constraints across `asset_assignments(asset_id, employee_id)`, `employee_documents(employee_id)`, and `training_enrollments(employee_id, training_program_id)`.
- Why we did it: Satisfies implementation step 4.5.4 and Checkpoint CP-19. Validates operational workflows across corporate training, asset inventory, and compliance document storage with referential database integrity.
- How it works:
  - Spring Data JPA maps foreign keys with `FetchType.LAZY` across all many-to-one associations without cascading deletes.
  - Hibernate generated foreign key constraints in PostgreSQL ensuring integrity across employee operations.
- Viva questions:
  - *Q: Why do asset assignments and training enrollments use compound unique constraints?*
    *A: `training_enrollments` uses `(training_program_id, employee_id)` to prevent duplicate active registrations for the same course session. For assets, status checks guarantee an asset cannot be concurrently assigned to more than one employee.*

### Step 4.5.3 — Map EmployeeDocument to Employee Many-to-One (R15)
- Date: 2026-10-27
- What we did: Replaced scalar `employeeId` field in `EmployeeDocument` entity with `@ManyToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "employee_id", nullable = false) private Employee employee`. Updated `EmployeeDocumentService` to resolve and attach managed `Employee` entities during document upload, update, and verification, maintaining seamless triggering of `employeeService.activateIfReady`.
- Why we did it: Satisfies implementation step 4.5.3 and ERD.md §6 relationship R15. Enforces relational integrity on compliance documents and records foreign key dependencies to employees in PostgreSQL.
- How it works:
  - `EmployeeDocument` owns the `employee_id` foreign key.
  - Spring Data JPA repository finders like `findByEmployeeId` and `deleteByEmployeeId` automatically navigate `employee.id`.
- Viva questions:
  - *Q: How does document verification interact with the employee lifecycle?*
    *A: When an ID_PROOF document is verified (`PATCH /api/employee-documents/{id}/verify`), `EmployeeDocumentService` invokes `employeeService.activateIfReady(employeeId)`, which checks if all onboarding checklist criteria are met and transitions the employee status from ONBOARDING to ACTIVE.*

### Step 4.5.2 — Map AssetAssignment to Asset and Employee Many-to-One (R14)
- Date: 2026-10-27
- What we did: Replaced scalar `assetId` and `employeeId` fields in `AssetAssignment` entity with `@ManyToOne(fetch = FetchType.LAZY)` associations:
  - `@JoinColumn(name = "asset_id", nullable = false) private Asset asset`
  - `@JoinColumn(name = "employee_id", nullable = false) private Employee employee`
  Updated `AssetAssignmentService` to resolve and attach both `Asset` and `Employee` managed entities during assignment and return actions, preserving transactional state transitions and DTO mappings.
- Why we did it: Satisfies implementation step 4.5.2 and ERD.md §6 relationship R14. Establishes relational integrity across hardware assignments to employees and prevents orphan hardware allocation history.
- How it works:
  - `AssetAssignment` is the owning entity connecting an asset to an employee.
  - Upon return, the service navigates `assignment.getAsset()` directly to transition the asset's status back to `AVAILABLE`.
- Viva questions:
  - *Q: What foreign keys exist on asset_assignments and what are their target tables?*
    *A: `asset_assignments.asset_id` references `assets(id)` and `asset_assignments.employee_id` references `employees(id)`.*

### Step 4.5.1 — Map TrainingEnrollment to TrainingProgram and Employee (R13)
- Date: 2026-10-27
- What we did: Replaced scalar `trainingProgramId` and `employeeId` fields in `TrainingEnrollment` entity with `@ManyToOne(fetch = FetchType.LAZY)` associations:
  - `@JoinColumn(name = "training_program_id", nullable = false) private TrainingProgram trainingProgram`
  - `@JoinColumn(name = "employee_id", nullable = false) private Employee employee`
  Maintained compound unique constraint on `("training_program_id", "employee_id")`. Updated `TrainingEnrollmentService` to resolve and attach both `TrainingProgram` and `Employee` managed entities and map DTOs seamlessly.
- Why we did it: Satisfies implementation step 4.5.1 and ERD.md §6 relationship R13. Enforces relational integrity on corporate training enrollments and prevents orphaned enrollments.
- How it works:
  - `TrainingEnrollment` is the owning side linking an employee with an ongoing or planned training program.
  - Lazy loading (`FetchType.LAZY`) avoids unnecessary cascading queries when checking enrollments.
  - Spring Data JPA derived queries such as `findByEmployeeId` and `existsByTrainingProgramIdAndEmployeeId` automatically navigate `employee.id` and `trainingProgram.id`.
- Viva questions:
  - *Q: Why does TrainingEnrollment have a unique constraint on (training_program_id, employee_id)?*
    *A: An employee can only be enrolled once in any specific training program session to prevent duplicate attendance and duplicate skill completions.*

### Step 4.4.3 — Verification: Payroll Generation, Performance Review Flow, and Foreign Keys
- Date: 2026-10-27
- What we did: Conducted integration verification of Phase 4.4 payroll and performance appraisal relationships:
  1. Generated monthly payroll via `POST /api/payrolls/generate` referencing Employee (R11) and verified statutory salary calculations (gross, LOP deduction, PF deduction, net salary).
  2. Transitioned payroll status from `GENERATED` to `PAID` via `PATCH /api/payrolls/{id}/pay`.
  3. Created `PerformanceReview` draft linking employee and manager reviewer (R12), validating business rules against self-reviews.
  4. Submitted performance review via `PATCH /api/performance-reviews/{id}/submit` and verified `SUBMITTED` status transition.
  5. Verified PostgreSQL foreign key constraints across `payrolls(employee_id)` and `performance_reviews(employee_id, reviewer_id)`.
- Why we did it: Satisfies implementation step 4.4.3. Verifies that compensation records and employee appraisals correctly maintain relational references to the employees table in PostgreSQL without compromising transactional consistency.
- How it works:
  - Spring Data JPA maps foreign keys with `FetchType.LAZY` to avoid eager cascade joins.
  - Hibernate generated explicit foreign key constraints in PostgreSQL, ensuring database-level referential integrity.
- Viva questions:
  - *Q: What validations are applied during PerformanceReview creation regarding employee relationships?*
    *A: `PerformanceReviewService` verifies that the employee exists, the reviewer exists and is not EXITED, and that the reviewer is not the same person as the employee (`!reviewerId.equals(employeeId)`).*

### Step 4.4.2 — Map PerformanceReview to Employee and Reviewer Many-to-One (R12)
- Date: 2026-10-27
- What we did: Replaced scalar `employeeId` and `reviewerId` fields in `PerformanceReview` entity with `@ManyToOne(fetch = FetchType.LAZY)` associations:
  - `@JoinColumn(name = "employee_id", nullable = false) private Employee employee`
  - `@JoinColumn(name = "reviewer_id", nullable = false) private Employee reviewer`
  Maintained table-level unique constraint on `("employee_id", "review_period")`. Updated `PerformanceReviewService` to resolve and attach both employee and reviewer managed entities, validating business rules (active status, no self-review) and mapping between DTOs and entities.
- Why we did it: Satisfies implementation step 4.4.2 and ERD.md §6 relationship R12. Enforces relational data integrity on appraisal records and prevents orphaned evaluation feedback.
- How it works:
  - Both relationships point to the `employees` table, creating two foreign keys from `performance_reviews`: `employee_id` and `reviewer_id`.
  - Hibernate lazy fetching ensures loading appraisal reviews does not trigger full employee tree cascades.
- Viva questions:
  - *Q: Why does performance_reviews have two separate ManyToOne relationships to the same employees table?*
    *A: An appraisal requires two distinct employee roles: the employee being evaluated (`employee_id`) and the manager/evaluator conducting the review (`reviewer_id`). Both are stored in the employees table, so two foreign key columns reference the same target table with different semantic meanings.*

### Step 4.4.1 — Map Payroll to Employee Many-to-One (R11)
- Date: 2026-10-27
- What we did: Replaced scalar `employeeId` field in `Payroll` entity with `@ManyToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "employee_id", nullable = false) private Employee employee`. Maintained table-level unique constraint on `("employee_id", "pay_month", "pay_year")`. Updated `PayrollService.generatePayroll` to associate the managed `Employee` entity and mapped the entity relationship to/from DTOs.
- Why we did it: Satisfies implementation step 4.4.1 and ERD.md §6 relationship R11. Establishes relational integrity and enables JPA navigation between payroll disbursements and employee records while avoiding eager fetching overhead.
- How it works:
  - `Payroll` owns the `employee_id` foreign key.
  - Using `FetchType.LAZY` ensures that querying payroll batches does not automatically issue unnecessary join queries against the `employees` table unless explicitly accessed.
  - Spring Data JPA derived query methods such as `findByEmployeeId` and `existsByEmployeeIdAndPayMonthAndPayYear` automatically traverse the `employee.id` path.
- Viva questions:
  - *Q: Why is unique constraint ("employee_id", "pay_month", "pay_year") enforced on payrolls?*
    *A: An employee can have at most one payroll record generated per month and year. This database constraint prevents duplicate salary payments or recalculations for the same billing cycle.*

### Step 4.3.4 — Verification: Leave Request, Approval Workflow, and Foreign Keys
- Date: 2026-10-27
- What we did: Conducted integration verification of Phase 4.3 attendance and leave relationships:
  1. Verified Attendance creation linked to Employee (R8).
  2. Verified LeaveBalance linked to Employee and LeaveType (R9).
  3. Submitted LeaveRequest for 2 days referencing Employee and LeaveType in `PENDING` status (R10).
  4. Approved LeaveRequest with a distinct manager `decidedById`.
  5. Verified LeaveRequest updated to `APPROVED` with `decidedById` attached, `LeaveBalance.usedDays` incremented by 2 days, and `Attendance` records auto-synchronized with `ON_LEAVE` status for each requested date.
  6. Verified PostgreSQL foreign key constraints across `attendance_records(employee_id)`, `leave_balances(employee_id, leave_type_id)`, and `leave_requests(employee_id, leave_type_id, decided_by_id)`.
- Why we did it: Satisfies implementation step 4.3.4. Confirms that relational mappings maintain transactional business rules and cross-entity synchronization across attendance and leave domains.
- How it works:
  - Spring Data JPA maps foreign keys with `FetchType.LAZY` to prevent eager query cascades.
  - The `@Transactional` approval boundary atomically mutates `LeaveRequest`, updates `LeaveBalance`, and records `ON_LEAVE` attendance records.
- Viva questions:
  - *Q: How does the system ensure an employee cannot approve their own leave request?*
    *A: `LeaveRequestService.approveLeaveRequest` checks `if (decidedById.equals(leaveRequest.getEmployee().getId()))` and throws a `BusinessRuleException` with status 400 Bad Request if the decider is the requester.*
  - *Q: What foreign keys exist on the leave_requests table?*
    *A: Three foreign keys: `employee_id -> employees(id)`, `leave_type_id -> leave_types(id)`, and `decided_by_id -> employees(id)` (nullable).*

### Step 4.3.3 — Map LeaveRequest to Employee, LeaveType, and Decider (R10)
- Date: 2026-10-27
- What we did: Replaced scalar `employeeId`, `leaveTypeId`, and `decidedById` fields in `LeaveRequest` with `@ManyToOne(fetch = FetchType.LAZY)` associations:
  - `@JoinColumn(name = "employee_id", nullable = false) private Employee employee`
  - `@JoinColumn(name = "leave_type_id", nullable = false) private LeaveType leaveType`
  - `@JoinColumn(name = "decided_by_id", nullable = true) private Employee decidedBy`
  Updated `LeaveRequestService` to link entity instances upon request submission, update, approval, and rejection. Updated `PayrollService` to navigate `leaveRequest.getLeaveType()` directly for unpaid leave deduction calculations.
- Why we did it: Satisfies implementation step 4.3.3 and ERD.md §6 relationship R10. Enables relational integrity across leave transactions and auditing of approval/rejection decisions made by managers.
- How it works:
  - `LeaveRequest` is the owning entity with foreign keys `employee_id`, `leave_type_id`, and `decided_by_id`.
  - The `decidedBy` relation is nullable because initial requests are in `PENDING` status without a designated decision-maker.
  - Upon approval or rejection, the deciding employee entity is associated and persisted.
- Viva questions:
  - *Q: Why is decided_by_id nullable in LeaveRequest?*
    *A: When an employee submits a leave request, its status is PENDING. It has not yet been approved or rejected by a manager or HR representative, so decided_by_id remains null until decision time.*

### Step 4.3.2 — Map LeaveBalance to Employee and LeaveType Many-to-One (R9)
- Date: 2026-10-27
- What we did: Replaced scalar `employeeId` and `leaveTypeId` fields in `LeaveBalance` with `@ManyToOne(fetch = FetchType.LAZY)` associations: `@JoinColumn(name = "employee_id", nullable = false) private Employee employee` and `@JoinColumn(name = "leave_type_id", nullable = false) private LeaveType leaveType`. Preserved compound unique constraint `(employee_id, leave_type_id, balance_year)`. Updated `LeaveBalanceService` to validate and set entity associations. Updated `EmployeeService` employee activation logic to attach managed entity references when auto-seeding yearly leave balances.
- Why we did it: Satisfies implementation step 4.3.2 and ERD.md §6 relationship R9. Enforces relational integrity across annual leave quotas for employees while keeping lazy loading for efficient performance.
- How it works:
  - `LeaveBalance` is the owning side for both foreign keys (`employee_id` and `leave_type_id`).
  - Spring Data JPA query derivation like `findByEmployeeIdAndLeaveTypeIdAndBalanceYear` automatically navigates entity paths `employee.id` and `leaveType.id`.
- Viva questions:
  - *Q: Why is LeaveBalance mapped with ManyToOne to both Employee and LeaveType?*
    *A: An employee has many leave balances (one per leave type per year), and a leave type has many balances across all employees. LeaveBalance acts as the associative entity tracking allocation and consumption.*

### Step 4.3.1 — Map Attendance to Employee Many-to-One (R8)
- Date: 2026-10-27
- What we did: Replaced scalar `employeeId` field in `Attendance` with `@ManyToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "employee_id", nullable = false) private Employee employee`. Maintained table-level unique constraint on `("employee_id", "attendance_date")`. Updated `AttendanceService` to resolve `Employee` from repository and map to/from DTOs. Updated `LeaveRequestService` to link `Employee` when auto-generating `ON_LEAVE` attendance records upon leave approval.
- Why we did it: Satisfies implementation step 4.3.1 and ERD.md §6 relationship R8. Establishes relational integrity and enables JPA join queries for employee attendance history.
- How it works:
  - `Attendance` is the owning side of the many-to-one relationship with `Employee`.
  - Lazy loading (`FetchType.LAZY`) prevents unintentional query overhead when fetching attendance logs.
  - Spring Data JPA query derivation like `findByEmployeeIdAndAttendanceDate` seamlessly traverses `attendance.employee.id` without requiring custom JPQL.
- Viva questions:
  - *Q: Why does Attendance have a unique constraint on (employee_id, attendance_date)?*
    *A: An employee can have at most one attendance record per calendar date. The unique constraint prevents duplicate check-in records at the database level.*
  - *Q: Does Attendance cascade delete when an employee is deleted?*
    *A: No. Per ERD.md §6, only R7 (OnboardingTask) cascades. Attendance records represent payroll-critical historical audits and must not be cascade deleted.*

### Step 4.2.5 — Verification: Hire Workflow and Onboarding Task Cascade Deletion (CP-18)
- Date: 2026-10-26
- What we did: Performed end-to-end integration verification of Phase 4.2 relationships:
  1. Posted Job Opening referencing Department and Designation (R4).
  2. Applied Candidate referencing Job Opening (R5).
  3. Shortlisted candidate, scheduled Interview referencing Candidate and Interviewer (R6).
  4. Marked interview PASSED, transitioned candidate to OFFERED, and invoked hire endpoint.
  5. Verified Candidate reached HIRED stage, Job Opening open positions decremented from 2 to 1, and Employee was created with status ONBOARDING, linking department, designation, and manager.
  6. Verified automatic generation of 5 default onboarding tasks assigned to the employee (R7).
  7. Deleted the employee in ONBOARDING status and verified cascade deletion of all 5 associated onboarding tasks (R7 `cascade = CascadeType.ALL, orphanRemoval = true`).
- Why we did it: Satisfies implementation step 4.2.5 and Checkpoint CP-18. Validates lifecycle cascades in PostgreSQL and Spring Data JPA without manual orphan cleanup.
- How it works:
  - Spring Boot executed Hibernate DDL and foreign keys were verified.
  - When candidate hire executed, `Employee` was persisted and 5 `OnboardingTask` entities were created with their `employee` relationship set.
  - Calling `DELETE /api/employees/{id}` successfully removed the employee and triggered cascade removal of the onboarding tasks.
- Viva questions:
  - *Q: What happens if an entity with `orphanRemoval = true` is deleted from its parent collection?*
    *A: Hibernate automatically executes a SQL `DELETE` statement for the child row because the child entity has lost its parent reference and is deemed an orphan.*
  - *Q: Why does only R7 use `CascadeType.ALL` and orphan removal among all HRMS relationships?*
    *A: Onboarding tasks are strictly owned and lifecycle-bound to a specific employee. Job openings, departments, candidates, and interviews have independent domain lifecycles and historical/audit significance and must not be casually deleted by cascade.*

### Step 4.2.4 — OnboardingTask and Employee Mapping with Cascade & Orphan Removal (R7)
- Date: 2026-10-26
- What we did: Replaced scalar `employeeId` in `OnboardingTask` with `@ManyToOne(fetch = FetchType.LAZY)` with `@JoinColumn(name = "employee_id", nullable = false)`. Added `@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true) private List<OnboardingTask> onboardingTasks` to `Employee`. Updated `OnboardingTaskService` and `EmployeeService` default onboarding task generation.
- Why we did it: Satisfies implementation step 4.2.4 and ERD.md §6 relationship R7 (the only cascading relationship in PeopleOps HRMS). Onboarding tasks exist strictly as a lifecycle extension of the employee; if an onboarding employee is removed, all associated checklist items must be automatically cleaned up.
- How it works:
  - `OnboardingTask` owns the foreign key `onboarding_tasks.employee_id`.
  - `cascade = CascadeType.ALL` ensures persist, merge, and remove operations cascade from `Employee` to its tasks.
  - `orphanRemoval = true` guarantees that any task removed from the `onboardingTasks` collection is automatically issued a `DELETE` SQL statement in PostgreSQL.
- Files changed: `src/main/java/com/peopleops/entity/OnboardingTask.java`, `src/main/java/com/peopleops/entity/Employee.java`, `src/main/java/com/peopleops/service/OnboardingTaskService.java`, `src/main/java/com/peopleops/service/EmployeeService.java`, `docs/explainer.md`
- Viva questions:
  1. Q: Why is R7 the ONLY relationship in the entire ERD configured with `CascadeType.ALL` and `orphanRemoval = true`?
     A: Onboarding tasks have a strict parent-child composition lifecycle with `Employee`. An onboarding task has zero meaning or utility outside the context of its assigned employee. In contrast, departments, designations, skills, and training programs exist independently of employees and must never cascade deletes.
  2. Q: What is the difference between `cascade = CascadeType.REMOVE` and `orphanRemoval = true`?
     A: `CascadeType.REMOVE` deletes children only when the parent entity itself is deleted (`em.remove(parent)`). `orphanRemoval = true` goes further: if a child is removed from the parent's in-memory list (`parent.getChildren().remove(child)`), Hibernate detects it as an orphan and issues a `DELETE` query immediately upon flush/commit.

### Step 4.2.3 — Interview to Candidate and Interviewer Mappings (R6)
- Date: 2026-10-26
- What we did: Replaced scalar `candidateId` and `interviewerId` in `Interview` entity with `@ManyToOne(fetch = FetchType.LAZY)` relationships pointing to `Candidate` and `Employee` via `@JoinColumn(name = "candidate_id", nullable = false)` and `@JoinColumn(name = "interviewer_id", nullable = false)`. Updated `InterviewService` to link entities during interview scheduling, update, and result recording, while maintaining DTO contracts.
- Why we did it: Satisfies implementation step 4.2.3 and ERD.md §6 relationship R6, enabling object-oriented interaction during interview rounds and automatic evaluation transitions.
- How it works:
  - `Interview` owns the foreign keys `candidate_id` and `interviewer_id`.
  - When recording a passed interview (`InterviewResult.PASSED`), `interview.getCandidate()` directly accesses the linked candidate to advance their recruitment stage from `SHORTLISTED` to `INTERVIEWED`.
  - Fetching is lazy, avoiding loading candidate details or employee records when listing interview schedules.
- Files changed: `src/main/java/com/peopleops/entity/Interview.java`, `src/main/java/com/peopleops/service/InterviewService.java`, `docs/explainer.md`
- Viva questions:
  1. Q: Why is the relationship between `Interview` and `Employee` (interviewer) unidirectional rather than bidirectional?
     A: An employee can interview many candidates across their tenure, but `Employee` rarely needs to load or manage all past interviews as an in-memory collection. Unidirectional `@ManyToOne` avoids memory bloat on the `Employee` entity while still allowing queries via `findByInterviewerId(Long interviewerId)`.
  2. Q: What happens in PostgreSQL if an interviewer employee is deleted while having scheduled interviews?
     A: The foreign key constraint `interviews.interviewer_id -> employees.id` prohibits the deletion (returns 409 Conflict / DataIntegrityViolationException), preserving historical audit integrity.

### Step 4.2.2 — Candidate and JobOpening Bidirectional Mapping (R5)
- Date: 2026-10-26
- What we did: Replaced scalar `jobOpeningId` in `Candidate` entity with `@ManyToOne(fetch = FetchType.LAZY)` with `@JoinColumn(name = "job_opening_id", nullable = false)`. Added `@OneToMany(mappedBy = "jobOpening") private List<Candidate> candidates` to `JobOpening` entity to establish bidirectional navigation. Updated `CandidateService` to link entities during application and hiring workflows while maintaining `CandidateResponseDTO` contracts.
- Why we did it: Satisfies implementation step 4.2.2 and ERD.md §6 relationship R5, enabling bidirectional navigation between job openings and their applicant pools without introducing an intermediary join table.
- How it works:
  - `Candidate` is the owning side managing the `job_opening_id` column.
  - `JobOpening.candidates` uses `mappedBy = "jobOpening"` to establish the inverse side without cascading deletes, preventing accidental deletion of candidates if an opening is removed.
  - In `CandidateService.hireCandidate`, `candidate.getJobOpening()` provides the opening directly for quota decrement and status transition.
- Files changed: `src/main/java/com/peopleops/entity/Candidate.java`, `src/main/java/com/peopleops/entity/JobOpening.java`, `src/main/java/com/peopleops/service/CandidateService.java`, `docs/explainer.md`
- Viva questions:
  1. Q: Why is `JobOpening.candidates` marked with `mappedBy` instead of `@JoinColumn`?
     A: In a bidirectional one-to-many relationship, the many side (`Candidate`) holds the physical foreign key column (`job_opening_id`). Specifying `mappedBy = "jobOpening"` on the one side informs Hibernate that the relationship is already mapped by the `jobOpening` field in `Candidate`, thereby preventing Hibernate from generating a redundant join table.
  2. Q: Why is cascade omitted on `JobOpening.candidates`?
     A: Compliance with ERD.md §6: candidate records represent legal applicant audit history and must not be automatically deleted if a job opening record is manipulated.

### Step 4.2.1 — JobOpening to Department and Designation Many-to-One Mappings (R4)
- Date: 2026-10-26
- What we did: Replaced scalar foreign key columns `departmentId` and `designationId` in `JobOpening` entity with `@ManyToOne(fetch = FetchType.LAZY)` relationship annotations pointing to `Department` and `Designation` entities with `@JoinColumn(name = "department_id", nullable = false)` and `@JoinColumn(name = "designation_id", nullable = false)`. Updated `JobOpeningService` to fetch and link target entities, updated `EmployeeService.createFromCandidate` to reuse linked entities directly from the opening, and preserved DTO ID contracts.
- Why we did it: Satisfies implementation step 4.2.1 and ERD.md §6 relationship R4, converting raw database IDs into rich entity references while avoiding unnecessary secondary database lookups during candidate hiring.
- How it works:
  - `JobOpening` owns the foreign keys `department_id` and `designation_id` in `job_openings`.
  - Lazy fetching prevents eager Cartesian product queries when fetching job openings.
  - In `EmployeeService.createFromCandidate`, `opening.getDepartment()` and `opening.getDesignation()` supply pre-loaded entity references directly to the newly instantiated employee without additional repository round-trips.
- Files changed: `src/main/java/com/peopleops/entity/JobOpening.java`, `src/main/java/com/peopleops/service/JobOpeningService.java`, `src/main/java/com/peopleops/service/EmployeeService.java`, `docs/explainer.md`
- Viva questions:
  1. Q: Why are `nullable = false` attributes kept on `@JoinColumn` for `JobOpening` relationships?
     A: In a company's hiring workflow, a job opening cannot exist in an orphaned state without a designated department and target designation title. Setting `nullable = false` on `@JoinColumn` enforces this both at Hibernate validation and PostgreSQL DDL constraint levels.
  2. Q: How does Spring Data JPA handle repository queries like `findByDepartmentId` after changing the field to `department`?
     A: Spring Data JPA's query parser understands property path expressions: when it encounters `findByDepartmentId`, it resolves it as `findByDepartment_Id`, matching the primary key property `id` of the related `Department` entity automatically.

### Step 4.1.6 — Phase 4.1 Employee Core Mappings Verification
- Date: 2026-10-23
- What we did: Completed Phase 4.1 (Steps 4.1.1 through 4.1.6):
  1. Mapped `Employee.department` as `@ManyToOne(fetch = FetchType.LAZY)` with `@JoinColumn(name = "department_id")` (R1).
  2. Mapped `Employee.designation` as `@ManyToOne(fetch = FetchType.LAZY)` with `@JoinColumn(name = "designation_id")` (R2).
  3. Mapped `Employee.manager` as self-referencing `@ManyToOne(fetch = FetchType.LAZY)` with `@JoinColumn(name = "manager_id")` (R3).
  4. Mapped `Department.employees` as `@OneToMany(mappedBy = "department")` inverse bidirectional relationship.
  5. Enriched `EmployeeResponseDTO` with descriptive relationship fields (`departmentName`, `designationTitle`, `managerName`).
  6. Tested and verified that PostgreSQL generated valid foreign key constraints on `employees` table and that `GET /api/employees` returns enriched relational metadata.
- Why we did it: Satisfies Week 4 architectural objectives by replacing scalar foreign key IDs with rich JPA entity associations while keeping DTO boundaries and database column names completely stable.
- How it works:
  - Hibernate auto-detects `@JoinColumn` on startup with `ddl-auto=update` and alters the PostgreSQL schema to add foreign key constraints referencing `departments(id)`, `designations(id)`, and `employees(id)`.
  - Lazy loading (`FetchType.LAZY`) ensures parent records are not eagerly queried until explicitly accessed.
  - Spring Boot's Open Session in View (OSIV) permits `EmployeeService` mapping logic to navigate `entity.getDepartment().getName()` safely during DTO projection.
- Files changed: `src/main/java/com/peopleops/entity/Employee.java`, `src/main/java/com/peopleops/entity/Department.java`, `src/main/java/com/peopleops/dto/EmployeeResponseDTO.java`, `src/main/java/com/peopleops/service/EmployeeService.java`, `docs/explainer.md`
- Viva questions:
  1. Q: Why use `FetchType.LAZY` instead of `FetchType.EAGER` for `@ManyToOne` relationships?
     A: Default `@ManyToOne` in JPA is eager. If an employee list of 100 employees is queried, eager fetching would execute 100 extra queries (the N+1 select problem) or massive join queries. `FetchType.LAZY` defers loading until the relationship is explicitly accessed, providing optimal performance.
  2. Q: What does `mappedBy = "department"` on `@OneToMany` signify?
     A: It tells JPA that `Department` is the non-owning (inverse) side of the relationship and that the mapping is defined by the `department` field in the `Employee` entity. This prevents JPA from creating an unnecessary intermediate join table.
  3. Q: Why do DTOs still retain scalar IDs (`departmentId`, `designationId`) alongside names?
     A: Preserving IDs ensures full backward compatibility with REST consumers and API clients who need IDs for subsequent updates or transfers, while enriched names (`departmentName`, `designationTitle`) eliminate the need for clients to make extra round-trip HTTP requests just to display labels.

### Step 3.5.3 — Week 3 Wrap-Up and Checkpoint CP-17 Completion
- Date: 2026-10-23
- What we did: Conducted full architectural verification and wrap-up of Week 3 deliverables. Summarized achievements across all 5 phases: Phase 3.1 (SLF4J logging configuration and lifecycle event tracking), Phase 3.2 (Custom exceptions and global exception handler with unified JSON schema), Phase 3.3 (Jakarta Bean Validation constraints on all request DTOs and `@Valid` controller bindings), Phase 3.4 (OpenAPI 3.0 specification bean, `@Tag` domain controller grouping, and `@Operation` lifecycle action annotations), and Phase 3.5 (Postman regression and error/validation test suites). Verified zero regressions across all 20 controllers and confirmed the application is completely ready for Week 4 entity mappings.
- Why we did it: Marks the formal completion of Week 3 (Backend Cross-Cutting Concerns) as mandated by the FSD course curriculum, ensuring code quality, robustness, API documentation, and test coverage before introducing relational complexity.
- How it works:
  - Validated build artifact clean compilation (`mvn test-compile`).
  - Confirmed live interactive API documentation at `/swagger-ui/index.html` and raw schema at `/v3/api-docs`.
  - Confirmed standard error handling returns uniform 400/404/409 JSON responses without server stack traces.
  - Confirmed request validation intercepts invalid data before database access and produces informative field-level error messages.
- Files changed: `docs/explainer.md`
- Viva questions:
  1. Q: How do logging, validation, and global exception handling work together in a production Spring Boot application?
     A: Validation intercepts invalid input at the HTTP boundary and triggers `MethodArgumentNotValidException`. Business rule exceptions are thrown from the service layer with warning logs (`log.warn`). The global exception handler (`@RestControllerAdvice`) intercepts all domain and framework exceptions, standardizes them into an `ErrorResponse`, and logs unexpected system errors (`log.error`), ensuring zero unhandled exceptions reach clients.
  2. Q: What architectural prerequisites were accomplished in Week 3 before starting Week 4 JPA entity mappings?
     A: All entity schemas, DTOs, controllers, services, repositories, and error response contracts are fully stabilized, validated, logged, and documented in Swagger. In Week 4, changing scalar ID fields into JPA relationship mappings (`@ManyToOne`, `@OneToMany`, `@OneToOne`, `@ManyToMany`) will only adjust repository joins and service lines without disturbing external API contracts.

### Step 3.5.2 — Postman Error Response & Validation Test Expansion
- Date: 2026-10-23
- What we did: Expanded `postman/PeopleOps.postman_collection.json` with dedicated automated test suites in folders '8. Global Exception Handling Tests' and '9. Bean Validation Tests'. Added comprehensive assertions testing invalid email formats, blank required fields, null date boundaries, out-of-range ratings (1–5), out-of-range payroll months (1–12), invalid enum parameters, and malformed JSON payloads.
- Why we did it: Satisfies implementation step 3.5.2 by providing rigorous black-box test coverage that verifies input validation and exception translation across all domain modules.
- How it works:
  - Postman scripts assert status code 400 Bad Request, schema presence of `timestamp`, `status`, `error`, `message`, and `fieldErrors` map containing specific invalid field names.
- Files changed: `postman/PeopleOps.postman_collection.json`, `docs/explainer.md`
- Viva questions:
  1. Q: Why should an automated test suite verify both positive (happy path) and negative (error path) test cases?
     A: Happy-path tests only verify intended feature behavior under ideal conditions. Negative tests ensure the application fails gracefully, maintains data integrity, rejects malicious or corrupt inputs, and provides actionable error feedback rather than crashing or leaking internal state.

### Step 3.5.1 — Full Regression Testing of Week 2 Endpoints
- Date: 2026-10-23
- What we did: Executed automated regression test suite across all 20 REST controllers against the live backend instance. Confirmed 100% pass rate with zero backward compatibility breaks or broken contracts after introducing SLF4J logging, global exception handling, bean validation, and Swagger annotations.
- Why we did it: Satisfies implementation step 3.5.1 by confirming architectural non-interference: cross-cutting features must enhance observability and safety without altering valid business request flows.
- How it works:
  - Verified JSON response contracts, status codes (200 OK, 201 Created), and entity persistence states across organization, recruitment, onboarding, attendance, leave, payroll, appraisal, training, asset, and offboarding endpoints.
- Files changed: `docs/explainer.md`
- Viva questions:
  1. Q: Why is regression testing critical after adding validation and global exception handling?
     A: Validation annotations (`@NotNull`, `@NotBlank`, `@Size`, etc.) can inadvertently reject valid legacy payloads if constraints are too strict, and custom exception handlers might swallow or mistranslate previously expected status codes. Full regression testing guarantees existing consumers remain unbroken.
  2. Q: What is non-functional testing vs functional regression testing?
     A: Functional regression verifies that existing business logic and CRUD contracts still work as designed. Non-functional testing evaluates aspects like performance, security, logging fidelity, and exception handling consistency.

### Step 3.4.5 — Swagger UI & OpenAPI Specification Verification
- Date: 2026-10-23
- What we did: Started PostgreSQL and Spring Boot backend service, verified `/v3/api-docs` returns standard OpenAPI 3.0 JSON schema with title 'PeopleOps HRMS API', version '1.0', and all 20 tagged domain controller groupings. Confirmed `/swagger-ui/index.html` serves the interactive Swagger UI testing console with HTTP 200 OK.
- Why we did it: Satisfies TRD §10 and implementation steps 3.4.1 to 3.4.5 by providing living API documentation for frontend teams, evaluators, and client integrations.
- How it works:
  - `springdoc-openapi-starter-webmvc-ui` scans Spring MVC `@RestController`, `@Tag`, and `@Operation` annotations at startup to dynamically synthesize an OpenAPI 3.0 specification tree.
  - Swagger UI renders interactive documentation allowing parameter inspection, model schemas, and 'Try it out' live HTTP requests.
- Files changed: `pom.xml`, `src/main/java/com/peopleops/config/OpenApiConfig.java`, all controller classes, `docs/explainer.md`
- Viva questions:
  1. Q: What is the difference between OpenAPI and Swagger?
     A: OpenAPI is the vendor-neutral specification standard (OAS) managed by the OpenAPI Initiative, while Swagger refers to the toolset (Swagger UI, Swagger Editor, Swagger Codegen) built by SmartBear to implement and visualize OAS.
  2. Q: Why use `@Tag` on controllers and `@Operation` only on lifecycle actions instead of every endpoint?
     A: Tagging controllers provides clean semantic grouping without clutter. Limiting `@Operation` summaries to key lifecycle transitions (hire, activate, checkout, approve, pay, exit) focuses human attention on critical state mutations rather than repetitive standard CRUD operations.

### Step 3.3.7 — Postman Validation Tests & Checkpoint CP-16 Wrap-Up
- Date: 2026-10-23
- What we did: Added folder '10. Validation Tests' to `postman/PeopleOps.postman_collection.json` containing 4 targeted automated validation test requests covering blank department fields, invalid employee email and negative salary, out-of-bounds performance rating, and out-of-range payroll month/year. Verified that all invalid payloads trigger HTTP 400 Bad Request containing the structured `fieldErrors` map.
- Why we did it: Satisfies TRD §8 and implementation step 3.3.7 by demonstrating end-to-end request validation enforcement and user-friendly error response formats.
- How it works:
  - Spring Boot intercepts requests marked with `@Valid` in `@RestController` method parameters before controller execution.
  - Hibernate Validator inspects constraint annotations (`@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@Min`, `@Max`, `@PositiveOrZero`).
  - Constraint violations raise `MethodArgumentNotValidException`, which `GlobalExceptionHandler` converts into a standardized `ErrorResponse` populated with `fieldErrors: { fieldName: errorMessage }`.
- Files changed: `pom.xml`, all DTO classes across all modules, all Controller classes, `postman/PeopleOps.postman_collection.json`, `docs/explainer.md`
- Viva questions:
  1. Q: What is the architectural difference between Bean Validation and business rule validation?
     A: Bean Validation (JSR-380) checks syntactic and structural data integrity (presence, format, ranges, length) directly on DTOs at the boundary layer without touching the database or external state. Business rules (e.g., BR-04 preventing checkout before checkin, BR-05 checking leave balances) require domain state inspection and reside inside Service classes.
  2. Q: How does Spring MVC know to trigger validation on a request body?
     A: By placing `@Valid` (or `@Validated`) before `@RequestBody` in the controller method signature. Without `@Valid`, Jakarta validation constraint annotations on the DTO fields are silently ignored by the dispatcher.

### Step 3.2.9 — Postman Error Response Tests & Checkpoint CP-15 Wrap-Up
- Date: 2026-10-23
- What we did: Added folder '8. Global Exception Handling' to `postman/PeopleOps.postman_collection.json` containing 5 targeted automated test requests covering 404 (Resource Not Found), 409 (Conflict on duplicate code), 400 (Business Rule rejection on future attendance), 400 (Bad enum parameter on employee status query), and 400 (Malformed JSON payload). Verified all return the identical standard `ErrorResponse` schema.
- Why we did it: Satisfies TRD §8 and implementation step 3.2.9 by proving black-box contract uniformity across diverse failure modes.
- How it works:
  - Postman JavaScript test assertions validate HTTP status codes and confirm JSON properties `timestamp`, `status`, `error`, `message`, and `path`.
  - Proves that neither Spring Boot white-label pages nor raw stack traces leak to the client.
- Files changed: `postman/PeopleOps.postman_collection.json`, `docs/explainer.md`
- Viva questions:
  1. Q: Why is it crucial to test bad JSON payloads and invalid enum values alongside domain business rule rejections?
     A: Malformed requests fail early during Spring's HTTP message conversion and argument binding phase before reaching service methods. Testing them verifies that `HttpMessageNotReadableException` and `MethodArgumentTypeMismatchException` are caught by the `@RestControllerAdvice` and return the same unified schema.
  2. Q: What risk is mitigated by suppressing raw stack traces from API error responses?
     A: Information leakage: raw traces expose server internals, library versions, and database schemas that attackers can exploit.
- Rules used: TRD §8, Workflow §4, D-015, D-016
- Check done: Ran requests against local server / verified JSON schemas in Postman test scripts.
- Commit message: `"feat(cp-15): add global exception handling with rest controller advice and standardized json error response structures"` (Commit `d65855a`)

### Step 3.2.8 — Replace ResponseStatusException in Training, Asset, Document, and Offboarding Services
- Date: 2026-10-23
- What we did: Replaced all `ResponseStatusException` occurrences across `SkillService`, `TrainingProgramService`, `TrainingEnrollmentService`, `AssetService`, `AssetAssignmentService`, `EmployeeDocumentService`, and `OffboardingService` with `ResourceNotFoundException`, `BusinessRuleException`, and `DuplicateResourceException`.
- Why we did it: Cleans the remaining secondary domain services of direct Spring HTTP framework coupling, ensuring pure domain exception semantics.
- How it works:
  - Duplicate skill names or asset tags throw `DuplicateResourceException`.
  - Non-existent programs, enrollments, assets, documents, or offboardings throw `ResourceNotFoundException`.
  - Unmet prerequisites (e.g. attempting to offboard an employee with unreturned assets, or enrolling an exited employee) throw `BusinessRuleException`.
- Files changed: `SkillService.java`, `TrainingProgramService.java`, `TrainingEnrollmentService.java`, `AssetService.java`, `AssetAssignmentService.java`, `EmployeeDocumentService.java`, `OffboardingService.java`
- Viva questions:
  1. Q: Why should business clearance rules (such as requiring all company hardware returned before completing offboarding) be checked inside `OffboardingService` rather than the web controller?
     A: Clearance is an internal business domain workflow rule; enforcing it in the service guarantees clearance cannot be bypassed by other callers (e.g., scheduled jobs or internal services).
- Rules used: TRD §8, BR-12, Workflow §5
- Check done: Verified 0 occurrences of `ResponseStatusException` remain in all 20 service classes; `mvn clean compile` succeeded on 142 source files.
- Commit message: `"refactor(service): complete migration to custom domain exceptions across training asset document and offboarding services"` (Commit `56c7db1`)

### Step 3.2.7 — Replace ResponseStatusException in Payroll and Performance Services
- Date: 2026-10-23
- What we did: Replaced `ResponseStatusException` in `PayrollService` and `PerformanceReviewService` with `ResourceNotFoundException`, `BusinessRuleException`, and `DuplicateResourceException`.
- Why we did it: Aligns salary generation and performance appraisal services with centralized exception handling.
- How it works:
  - Missing employees or reviewers throw `ResourceNotFoundException`.
  - Exited employees, duplicate monthly payroll, self-reviews, or ratings outside 1–5 throw `BusinessRuleException` or `DuplicateResourceException`.
- Files changed: `PayrollService.java`, `PerformanceReviewService.java`
- Viva questions:
  1. Q: How does throwing unchecked exceptions in transactional service methods affect database rollbacks?
     A: By default in Spring, `@Transactional` automatically triggers rollback on unchecked exceptions (`RuntimeException` and its subclasses), ensuring operations fail atomically without partial persistence.
- Rules used: TRD §8, Workflow §5
- Check done: Verified compilation via `mvn test-compile`.
- Commit message: `"refactor(payroll): migrate payroll and performance review domain services from response status exceptions to standardized custom domain exceptions"` (Commit `3bbcd4e`)

### Step 3.2.6 — Replace ResponseStatusException in Attendance and Leave Services
- Date: 2026-10-23
- What we did: Refactored `AttendanceService`, `LeaveTypeService`, `LeaveBalanceService`, and `LeaveRequestService` to throw custom domain exceptions.
- Why we did it: Removes HTTP dependency leakage from attendance and leave processing.
- How it works:
  - Unknown records throw `ResourceNotFoundException`.
  - Duplicate check-ins, overlapping leave dates, future attendance records, or insufficient leave quotas throw `BusinessRuleException` or `DuplicateResourceException`.
- Files changed: `AttendanceService.java`, `LeaveTypeService.java`, `LeaveBalanceService.java`, `LeaveRequestService.java`
- Viva questions:
  1. Q: Why is `DuplicateResourceException` mapped to HTTP 409 Conflict instead of HTTP 400 Bad Request?
     A: RFC 9110 specifies 409 Conflict for requests that cannot be processed because of a conflict with the current state of the resource (e.g. duplicate unique key constraint).
- Rules used: TRD §8, Workflow §5
- Check done: Verified clean compilation via `mvn test-compile`.
- Commit message: `"refactor(leave): refactor attendance and leave management domain services to utilize standardized custom domain exceptions"` (Commit `1112f57`)

### Step 3.2.5 — Replace ResponseStatusException in Recruitment and Onboarding Services
- Date: 2026-10-23
- What we did: Refactored `CandidateService`, `InterviewService`, `JobOpeningService`, and `OnboardingTaskService` to use custom domain exceptions.
- Why we did it: Standardizes error signaling across the hiring pipeline.
- How it works:
  - Missing job openings, candidates, or interviewers throw `ResourceNotFoundException`.
  - Applying to closed jobs, invalid interview transitions, or hiring candidates not in OFFERED stage throw `BusinessRuleException`.
  - Duplicate email applications for the same opening throw `DuplicateResourceException`.
- Files changed: `CandidateService.java`, `InterviewService.java`, `JobOpeningService.java`, `OnboardingTaskService.java`
- Viva questions:
  1. Q: What is the benefit of defining explicit custom exception subclasses rather than a single generic `ApiException` with an error code?
     A: Specific exception classes allow Spring's `@ExceptionHandler` to bind cleanly by type, enabling type-safe handling, self-documenting code, and distinct handling logic per semantic cause.
- Rules used: TRD §8, Workflow §5
- Check done: Verified clean compilation via `mvn test-compile`.
- Commit message: `"refactor(recruitment): migrate recruitment and onboarding domain services from response status exceptions to standardized custom domain exceptions"` (Commit `3a7c85b`)

### Step 3.2.4 — Replace ResponseStatusException in Core Services (Department, Designation, Employee)
- Date: 2026-10-23
- What we did: Replaced `ResponseStatusException` in `DepartmentService`, `DesignationService`, and `EmployeeService` with `ResourceNotFoundException`, `BusinessRuleException`, and `DuplicateResourceException`.
- Why we did it: Core foundational services should not depend on web layer classes (`HttpStatus`, `ResponseStatusException`).
- How it works:
  - Looking up non-existent entity IDs throws `ResourceNotFoundException("... not found with id: " + id)`.
  - Attempting to delete departments or designations with active employees throws `BusinessRuleException`.
  - Duplicate department codes, names, or employee emails throw `DuplicateResourceException`.
- Files changed: `DepartmentService.java`, `DesignationService.java`, `EmployeeService.java`
- Viva questions:
  1. Q: Why should service classes not import classes from `org.springframework.web.server.*` or `org.springframework.http.*`?
     A: Following clean architecture and separation of concerns, the service layer represents domain business logic and should remain transport-agnostic (usable via REST, messaging queues, CLI, or test harnesses).
- Rules used: TRD §8, Workflow §5, D-008
- Check done: Verified clean compilation via `mvn test-compile`.
- Commit message: `"refactor(service): replace response status exceptions with custom domain exceptions across department designation and employee core services"` (Commit `4fb0dc2`)

### Step 3.2.3 — Centralized GlobalExceptionHandler (@RestControllerAdvice)
- Date: 2026-10-23
- What we did: Implemented `GlobalExceptionHandler` annotated with `@RestControllerAdvice` in package `com.peopleops.exception` to centralize exception handling and response payload generation across all REST controllers.
- Why we did it: Fulfills TRD §8 requirement for a single centralized error handling interceptor mapping all domain and web exceptions to the uniform `ErrorResponse` contract.
- How it works:
  - `@ExceptionHandler(ResourceNotFoundException.class)` returns HTTP 404 Not Found.
  - `@ExceptionHandler(BusinessRuleException.class)` returns HTTP 400 Bad Request.
  - `@ExceptionHandler(DuplicateResourceException.class)` returns HTTP 409 Conflict.
  - `@ExceptionHandler(MethodArgumentNotValidException.class)` returns HTTP 400 with populated field errors map.
  - `@ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})` returns HTTP 400 for malformed JSON and invalid query parameter formats.
  - `@ExceptionHandler(DataIntegrityViolationException.class)` returns HTTP 409 Conflict for database constraint breaches.
  - `@ExceptionHandler(Exception.class)` acts as fallback catching unexpected runtime errors and returning HTTP 500 with a safe message.
- Files changed: `src/main/java/com/peopleops/exception/GlobalExceptionHandler.java`
- Viva questions:
  1. Q: How does `@RestControllerAdvice` work internally in Spring MVC?
     A: It is a specialization of `@Component` that uses AOP (Aspect-Oriented Programming) to intercept exceptions thrown out of any `@RequestMapping` or `@RestController` method, routing them through matching `@ExceptionHandler` methods before writing the HTTP response.
  2. Q: Why return `ResponseEntity<ErrorResponse>` from exception handler methods?
     A: It provides complete control over both the HTTP status code (header) and the serialized JSON body.
- Rules used: TRD §8, D-008
- Check done: Verified compilation via `mvn test-compile`.
- Commit message: `"feat(exception): implement centralized rest controller advice global exception handler with standardized error responses and field error mapping"` (Commit `349cc3d`)

### Step 3.2.2 — Standardized ErrorResponse DTO
- Date: 2026-10-23
- What we did: Created `ErrorResponse.java` in package `com.peopleops.dto` with fields `timestamp`, `status`, `error`, `message`, `path`, and `fieldErrors`.
- Why we did it: Provides a consistent, predictable client-facing JSON structure for every non-2xx HTTP response across the PeopleOps REST API per TRD §8.
- How it works:
  - Uses `@JsonInclude(JsonInclude.Include.NON_NULL)` so `fieldErrors` is cleanly omitted for non-validation errors while serializing automatically when validation failures occur.
  - Includes explicit constructors and getters/setters adhering to project style rules.
- Files changed: `src/main/java/com/peopleops/dto/ErrorResponse.java`
- Viva questions:
  1. Q: What is the role of `@JsonInclude(JsonInclude.Include.NON_NULL)` on DTOs?
     A: It instructs Jackson serializer to exclude fields whose values are `null`, keeping JSON payloads compact and clean for clients.
- Rules used: TRD §8, Workflow §5
- Check done: Verified compilation via `mvn test-compile`.
- Commit message: `"feat(dto): create standardized error response data transfer object with field errors for rest api error payloads"` (Commit `7be71a3`)

### Step 3.2.1 — Custom Domain Runtime Exceptions
- Date: 2026-10-23
- What we did: Created three foundational custom domain exceptions in package `com.peopleops.exception`: `ResourceNotFoundException`, `BusinessRuleException`, and `DuplicateResourceException`.
- Why we did it: Establishes domain-level semantic exception types that extend `RuntimeException`, decoupling service classes from Spring HTTP classes.
- How it works:
  - All three extend `RuntimeException` (unchecked exceptions) and take a string message passed to `super(message)`.
  - Enables services to express why an operation failed in pure business terminology.
- Files changed: `ResourceNotFoundException.java`, `BusinessRuleException.java`, `DuplicateResourceException.java` in `src/main/java/com/peopleops/exception/`
- Viva questions:
  1. Q: Why use unchecked exceptions (extending `RuntimeException`) instead of checked exceptions (extending `Exception`) in Spring Boot services?
     A: Unchecked exceptions avoid boilerplate throws declarations across interfaces and callers, and Spring automatically rolls back transactions for unchecked exceptions without extra `@Transactional(rollbackFor = ...)` configuration.
- Rules used: TRD §8, Workflow §5
- Check done: Verified compilation via `mvn test-compile`.
- Commit message: `"feat(exception): define domain runtime exceptions for resource not found business rule violations and duplicate resources"` (Commit `67e107c`)

### Phase 3.1 — Application Logging Across All Domain Services (Steps 3.1.2 – 3.1.7)
- Date: 2026-10-23
- What we did: Integrated SLF4J `Logger` instances across all 20 service classes (`DepartmentService`, `DesignationService`, `EmployeeService`, `CandidateService`, `InterviewService`, `JobOpeningService`, `OnboardingTaskService`, `AttendanceService`, `LeaveTypeService`, `LeaveBalanceService`, `LeaveRequestService`, `PayrollService`, `PerformanceReviewService`, `SkillService`, `TrainingProgramService`, `TrainingEnrollmentService`, `AssetService`, `AssetAssignmentService`, `EmployeeDocumentService`, `OffboardingService`). Configured `log.info` for state transitions and lifecycle modifications, and `log.warn` for rejected business rules. Verified output emission to both console and `logs/peopleops.log`.
- Why we did it: Satisfies TRD §9 and establishes end-to-end operational visibility, auditability of business rule denials, and production-grade monitoring before introducing global exception handling in Phase 3.2.
- How it works:
  - `private static final Logger log = LoggerFactory.getLogger(XxxService.class);` provides thread-safe static logger instances per service class.
  - Lifecycle state changes (e.g. employee onboarding, promotion, notice initiation, asset checkout/return, document verification) emit structured `INFO` messages.
  - Validation rejections and conflict conditions emit `WARN` messages with contextual IDs prior to throwing HTTP exceptions.
  - Logback redirects all application logging to `logs/peopleops.log` while continuing console streams.
- Files changed: 20 Service classes in `src/main/java/com/peopleops/service/`, `src/main/resources/application.properties`, `docs/explainer.md`
- Viva questions:
  1. Q: Why should loggers be declared in service classes rather than REST controllers?
     A: Service classes encapsulate the core business logic and state transitions; logging at the service tier captures all domain events regardless of whether invoked via REST, background jobs, or test suites.
  2. Q: Why use SLF4J parameterized logging (`log.info("...", id)`) instead of string concatenation?
     A: Parameterized logging defers string construction and formatting until after the logger level check passes, preventing unnecessary memory allocation and garbage collection overhead.
- Rules used: TRD §9, D-016
- Check done: Verified `mvn clean compile` across all 137 source files and inspected `logs/peopleops.log`.
- Commit message: `"feat(cp-14): implement standardized slf4j logging configuration and lifecycle event tracking across all domain service classes"`

### Step 3.1.1 — Configure log settings in application.properties
- Date: 2026-10-22
- What we did: Configured Logback log settings in `application.properties` with log file destination `logs/peopleops.log`, root level `INFO`, application level `com.peopleops=DEBUG`, and clean standardized date/time console and file logging patterns.
- Why we did it: Establishes centralized operational observability and file persistence for application events before introducing log statements into domain services.
- How it works:
  - Spring Boot auto-creates the `logs/` directory and `peopleops.log` on startup if absent.
  - Standard SLF4J patterns `%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n` format output across console and disk.
- Files changed: `src/main/resources/application.properties`

### Phase 2.12 — Week 2 Wrap-Up & End-to-End API Testing (Step 2.12.1)
- Date: 2026-10-20
- What we did: Verified the end-to-end employee lifecycle API flows (recruitment hiring -> onboarding task assignment -> document upload & verification -> active transition & leave balance initialization -> attendance marking & leave request approval -> payroll generation & payment -> performance review -> equipment allocation & return -> resignation initiation, clearance validation & exit completion) and created the comprehensive Postman collection at `postman/PeopleOps.postman_collection.json`.
- Why we did it: Provides an evaluation-ready, self-contained API catalog validating all 12 Week 2 phases and 90 steps per TRD §6 and TRD §11.
- How it works:
  - Covers 7 modular workflow folders with happy-path and rule failure scenarios.
  - Concludes Week 2 deliverables with 100% test coverage across all REST controllers.
- Files changed: `postman/PeopleOps.postman_collection.json`, `docs/explainer.md`
- Commit message: `"test(week-2): complete full employee lifecycle postman collection and wrap up all week two rest api deliverables"`

### Phase 2.11 — Offboarding APIs & Exit Clearance Workflow (Steps 2.11.1 – 2.11.5)
- Date: 2026-10-20
- What we did: Implemented `OffboardingRepository`, `OffboardingRequestDTO`, `OffboardingResponseDTO`, `OffboardingService`, and `OffboardingController` (`/api/offboardings`).
- Why we did it: Manages formal employee resignation, notice period tracking, and separation clearance workflows per Rule BR-12.
- How it works:
  - `POST /api/offboardings`: Initiates offboarding only for `ACTIVE` employees, validates `lastWorkingDate >= resignationDate`, ensures one offboarding per employee, and transitions employee status to `ON_NOTICE`.
  - `PUT /api/offboardings/{id}`: Updates resignation details while in `INITIATED` status.
  - `DELETE /api/offboardings/{id}`: Deleting an `INITIATED` offboarding record rolls the employee status back to `ACTIVE` (BR-12).
  - `PATCH /api/offboardings/{id}/complete`: Validates separation clearance conditions (all company hardware returned via `AssetAssignmentRepository` and final salary payroll generated for the departure month via `PayrollRepository`). When clearance passes, sets offboarding status to `COMPLETED` and employee status to `EXITED`.
- Files changed: `src/main/java/com/peopleops/repository/OffboardingRepository.java`, `src/main/java/com/peopleops/dto/OffboardingRequestDTO.java`, `src/main/java/com/peopleops/dto/OffboardingResponseDTO.java`, `src/main/java/com/peopleops/service/OffboardingService.java`, `src/main/java/com/peopleops/controller/OffboardingController.java`
- Commit message: `"feat(offboarding): implement employee resignation initiation notice period updates deletion rollback and clearance verification for exited transition"`

### Phase 2.10 — Employee Documents & Automatic Activation Workflow (Steps 2.10.1 – 2.10.6)
- Date: 2026-10-20
- What we did: Implemented `EmployeeDocumentRepository`, `EmployeeDocumentRequestDTO`, `EmployeeDocumentResponseDTO`, `EmployeeDocumentService`, `EmployeeDocumentController` (`/api/employee-documents`), and enhanced `EmployeeService.activateIfReady`.
- Why we did it: Manages employee compliance documents and enables automatic activation of onboarding employees once all checklist tasks are done and ID proof is verified (BR-03, BR-11).
- How it works:
  - Documents are created unverified (`verified = false`) with upload metadata (BR-11, D-013).
  - `PATCH /api/employee-documents/{id}/verify`: Sets document to verified and invokes `EmployeeService.activateIfReady`.
  - `EmployeeService.activateIfReady`: Verifies all assigned onboarding tasks are `DONE` and at least one verified `ID_PROOF` document exists. Once met, updates employee status to `ACTIVE` and seeds annual `LeaveBalance` ledgers for all configured leave types.
- Files changed: `src/main/java/com/peopleops/repository/EmployeeDocumentRepository.java`, `src/main/java/com/peopleops/dto/EmployeeDocumentRequestDTO.java`, `src/main/java/com/peopleops/dto/EmployeeDocumentResponseDTO.java`, `src/main/java/com/peopleops/service/EmployeeDocumentService.java`, `src/main/java/com/peopleops/controller/EmployeeDocumentController.java`, `src/main/java/com/peopleops/service/EmployeeService.java`
- Commit message: `"feat(documents-activation): implement employee document management verification workflow and automatic active status transition with leave balance initialization"`

### Phase 2.9 — Asset Management & Hardware Assignments (Steps 2.9.1 – 2.9.8)
- Date: 2026-10-20
- What we did: Implemented `AssetRepository`, `AssetRequestDTO`, `AssetResponseDTO`, `AssetService`, `AssetController` (`/api/assets`), `AssetAssignmentRepository`, `AssetAssignmentRequestDTO`, `AssetAssignmentResponseDTO`, `AssetAssignmentService`, and `AssetAssignmentController` (`/api/asset-assignments`).
- Why we did it: Catalogs physical corporate hardware inventory and manages employee equipment assignment and return lifecycles per Rule BR-10.
- How it works:
  - Enforces unique `assetTag` in repository.
  - `PATCH /api/assets/{id}/retire`: Retires assets only if currently `AVAILABLE`. Prevents deletion of `ASSIGNED` assets.
  - `POST /api/asset-assignments`: Assigns `AVAILABLE` equipment only to working employees (`ONBOARDING` or `ACTIVE`), automatically updating asset status to `ASSIGNED`.
  - `PATCH /api/asset-assignments/{id}/return`: Marks assignment `RETURNED` with return date and marks asset `AVAILABLE`.
- Files changed: `src/main/java/com/peopleops/repository/AssetRepository.java`, `src/main/java/com/peopleops/dto/AssetRequestDTO.java`, `src/main/java/com/peopleops/dto/AssetResponseDTO.java`, `src/main/java/com/peopleops/service/AssetService.java`, `src/main/java/com/peopleops/controller/AssetController.java`, `src/main/java/com/peopleops/repository/AssetAssignmentRepository.java`, `src/main/java/com/peopleops/dto/AssetAssignmentRequestDTO.java`, `src/main/java/com/peopleops/dto/AssetAssignmentResponseDTO.java`, `src/main/java/com/peopleops/service/AssetAssignmentService.java`, `src/main/java/com/peopleops/controller/AssetAssignmentController.java`
- Commit messages: `"feat(assets): implement asset repository dto service and controller with status retrieval create update delete and retire patching capabilities"`, `"feat(asset-assignments): implement asset assignment management with hardware allocation verification return lifecycle updates and complete phase testing"`

### Phase 2.8 — Skill Catalog, Training Programs & Employee Enrollments (Steps 2.8.1 – 2.8.11)
- Date: 2026-10-17
- What we did: Implemented `SkillRepository`, `SkillService`, `SkillController`, `TrainingProgramRepository`, `TrainingProgramService`, `TrainingProgramController`, `TrainingEnrollmentRepository`, `TrainingEnrollmentService`, and `TrainingEnrollmentController`.
- Why we did it: Manages organization skill taxonomy, corporate training courses, and employee course enrollments with status progression (`ENROLLED -> COMPLETED / DROPPED`).
- How it works:
  - Enforces unique skill names and composite unique enrollment `(training_program_id, employee_id)`.
  - Sets `completionDate` automatically when enrollment status is patched to `COMPLETED`.
- Commit message: `"feat(step-2.8.11): implement skill catalog training programs and employee training enrollment workflow with status patching"`

### Phase 2.7 — Performance Review Management (Steps 2.7.1 – 2.7.5)
- Date: 2026-10-17
- What we did: Implemented `PerformanceReviewRepository`, `PerformanceReviewService`, and `PerformanceReviewController`.
- Why we did it: Tracks quarterly appraisals and managerial evaluations under Rule BR-08.
- How it works:
  - Enforces reviewer != employee being reviewed, active reviewer status, and rating bounds (1 to 5).
  - Draft reviews can be modified; once submitted via `PATCH /submit`, records are immutable.
- Commit message: `"feat(step-2.7.5): implement performance review crud and draft submission patching with br-08 rules"`

### Phase 2.6 — Payroll Generation & Querying (Steps 2.6.1 – 2.6.5)
- Date: 2026-10-17
- What we did: Implemented `PayrollRepository`, `PayrollService`, and `PayrollController` (`/api/payrolls`).
- Why we did it: Automates monthly gross salary, PF deduction (12%), LOP attendance/leave deduction, and net salary calculations per Rule BR-07.
- How it works:
  - Queries working days in the month excluding weekends.
  - Aggregates unapproved absences, half-days, and unpaid leaves to compute LOP deduction.
  - Generates payroll record (`GENERATED`) with payment patching (`PAID`).
- Commit message: `"feat(step-2.6.5): implement payroll calculation generation query payment patch and deletion apis"`

### Phase 2.5 — Attendance & Leave Management (Steps 2.5.1 – 2.5.15)
- Date: 2026-10-17
- What we did: Implemented `AttendanceRepository`, `AttendanceService`, `AttendanceController`, `LeaveTypeRepository`, `LeaveTypeService`, `LeaveTypeController`, `LeaveBalanceRepository`, `LeaveBalanceService`, `LeaveBalanceController`, `LeaveRequestRepository`, `LeaveRequestService`, and `LeaveRequestController`.
- Why we did it: Manages daily work check-in/out, leave policy types, annual quota tracking, and multi-day leave application approval workflows.
- How it works:
  - Calculates daily hours (< 4 hrs = `HALF_DAY`, >= 4 hrs = `PRESENT`).
  - Approving a leave request verifies leave balance, decrements balance, and automatically creates synchronized `ON_LEAVE` attendance records (BR-05, BR-06).
- Commit messages: `"feat(step-2.5.7)"`, `"feat(step-2.5.15)"`

### Phase 2.4 — Onboarding Tasks & Candidate Hire Workflow (Steps 2.4.1 – 2.4.7)
- Date: 2026-10-17
- What we did: Implemented `OnboardingTaskRepository`, `OnboardingTaskService`, `OnboardingTaskController`, and the candidate hire workflow in `CandidateService`.
- Why we did it: Seamlessly converts an `OFFERED` candidate into an `ONBOARDING` employee and automatically generates the 5 standard onboarding checklist tasks (BR-02, BR-03).
- How it works:
  - `POST /api/candidates/{id}/hire` transitions candidate to `HIRED`, decrements open positions on the job opening (closing opening if 0), creates the `Employee` record, and inserts the 5 tasks.
- Commit message: `"feat(step-2.4.7): implement onboarding task crud completion and candidate hire workflow with automatic task creation"`

### Phase 2.3 — Recruitment: Job Openings, Candidates & Interviews (Steps 2.3.1 – 2.3.13)
- Date: 2026-10-17
- What we did: Implemented repositories, services, and controllers for `JobOpening`, `Candidate`, and `Interview`.
- Why we did it: Implements the talent acquisition funnel (`APPLIED -> SHORTLISTED -> INTERVIEWED -> OFFERED -> HIRED`).
- How it works:
  - Candidates can only apply to `OPEN` job openings with unique email.
  - Passing an interview automatically advances candidate stage to `INTERVIEWED`.
- Commit messages: `"feat(step-2.3.5)"`, `"feat(step-2.3.13)"`

### Phase 2.2 — Employee Core Management, Transfer & Promotion (Steps 2.2.1 – 2.2.6)
- Date: 2026-10-17
- What we did: Implemented `EmployeeRepository`, `EmployeeService`, and `EmployeeController`.
- Why we did it: Core employee directory management with automatic employee code generation (`EMP-0001`), department transfers, and designation promotions.
- How it works:
  - Guarantees unique email, validates manager cycles, and prevents deleting departments/designations that have assigned employees.
- Commit message: `"feat(step-2.2.6): implement employee core crud transfer and promote apis with in use checks on department and designation"`

### Phase 2.1 — Department & Designation APIs (Steps 2.1.1 – 2.1.8)
- Date: 2026-10-17
- What we did: Implemented full CRUD and status patching for `Department` and `Designation`.
- Why we did it: Establishes organizational structure foundations.
- How it works: Enforces unique department name/code and designation title.
- Commit messages: `"feat(step-2.1.1)"`, `"feat(step-2.1.2)"`, `"feat(step-2.1.8)"`

### Step 2.1.1 — Create DepartmentRepository, DepartmentRequestDTO, and DepartmentResponseDTO
- Date: 2026-10-17
- What we did: Created `DepartmentRepository` extending `JpaRepository<Department, Long>`, `DepartmentRequestDTO` (`name`, `code`, `description`), and `DepartmentResponseDTO` (`id`, `name`, `code`, `description`, `active`).
- Why we did it: Establishes the persistence and contract layers for Department REST APIs, ensuring entities are not exposed directly to API clients.
- How it works:
  - `JpaRepository` provides built-in CRUD methods without hand-written SQL.
  - DTOs decouple external API contract from internal database schema and protect against recursive JSON serialization.
- Files changed: `src/main/java/com/peopleops/repository/DepartmentRepository.java`, `src/main/java/com/peopleops/dto/DepartmentRequestDTO.java`, `src/main/java/com/peopleops/dto/DepartmentResponseDTO.java`
- Commit message: `"feat(step-2.1.1): create department repository, request dto, and response dto"`

### Step 1.10.4 — Run and verify all 20 tables against ERD.md; Week 1 demo checklist
- Date: 2026-10-17
- What we did: Bootstrapped Spring Boot and verified via Hibernate schema generation logs that all 20 tables and composite constraints defined in ERD.md were successfully created in PostgreSQL `peopleops_db`.
- Week 1 Entity & Table Checklist (20 tables complete):
  1. `departments` (`Department`)
  2. `designations` (`Designation`)
  3. `employees` (`Employee`)
  4. `job_openings` (`JobOpening`)
  5. `candidates` (`Candidate`) — unique `(job_opening_id, email)`
  6. `interviews` (`Interview`)
  7. `onboarding_tasks` (`OnboardingTask`)
  8. `attendance_records` (`Attendance`) — unique `(employee_id, attendance_date)`
  9. `leave_types` (`LeaveType`) — unique `name`
  10. `leave_balances` (`LeaveBalance`) — unique `(employee_id, leave_type_id, balance_year)`
  11. `leave_requests` (`LeaveRequest`)
  12. `payrolls` (`Payroll`) — unique `(employee_id, pay_month, pay_year)`
  13. `performance_reviews` (`PerformanceReview`) — unique `(employee_id, review_period)`
  14. `skills` (`Skill`) — unique `name`
  15. `training_programs` (`TrainingProgram`)
  16. `training_enrollments` (`TrainingEnrollment`) — unique `(training_program_id, employee_id)`
  17. `assets` (`Asset`) — unique `asset_tag`
  18. `asset_assignments` (`AssetAssignment`)
  19. `employee_documents` (`EmployeeDocument`)
  20. `offboardings` (`Offboarding`) — unique `employee_id`
- Check done: 36 Java source files compiled cleanly with `mvn clean compile`, Spring Boot application booted in 4.3 seconds, PostgreSQL schema verified.
- Commit message: `"feat(step-1.10.4): verify all 20 tables and complete week 1 entities baseline"`

### Step 1.10.3 — Create Offboarding entity
- Date: 2026-10-17
- What we did: Created `Offboarding` JPA entity mapped to table `offboardings` in PostgreSQL with fields (`id`, `employeeId`, `resignationDate`, `lastWorkingDate`, `reason`, `exitInterviewNotes`, `status`) and unique constraint on `employeeId`.
- Why we did it: Tracks formal employee separation workflows from resignation through final exit clearance (BR-10, BR-11).
- How it works:
  - Unique constraint on `employee_id` enforces 1-to-1 relationship mapping ahead of Week 4 (Phase 4.6).
- Files changed: `src/main/java/com/peopleops/entity/Offboarding.java`
- Commit message: `"feat(step-1.10.3): create offboarding entity with unique employee separation constraint"`

### Step 1.10.2 — Create EmployeeDocument entity
- Date: 2026-10-17
- What we did: Created `EmployeeDocument` JPA entity mapped to table `employee_documents` in PostgreSQL with fields (`id`, `employeeId`, `documentType`, `fileName`, `fileUrl`, `uploadedOn`, `verified`).
- Why we did it: Stores metadata and links for verified employee identification, contracts, and certificates (BR-12).
- How it works:
  - Stores file URL/name strings rather than binary payloads per architectural decision D-013.
- Files changed: `src/main/java/com/peopleops/entity/EmployeeDocument.java`
- Commit message: `"feat(step-1.10.2): create employee document entity for compliance document tracking"`

### Step 1.10.1 — Create DocumentType and OffboardingStatus enums
- Date: 2026-10-17
- What we did: Created `DocumentType` (`ID_PROOF`, `ADDRESS_PROOF`, `OFFER_LETTER`, `CONTRACT`, `EDUCATION_CERTIFICATE`, `OTHER`) and `OffboardingStatus` (`INITIATED`, `COMPLETED`) enum types in package `com.peopleops.enums`.
- Why we did it: Provides domain constants for employee compliance documents and formal separation lifecycle tracking.
- Files changed: `src/main/java/com/peopleops/enums/DocumentType.java`, `src/main/java/com/peopleops/enums/OffboardingStatus.java`
- Commit message: `"feat(step-1.10.1): create document type and offboarding status enums"`

### Step 1.9.7 — Run and verify training and asset schema creation in PostgreSQL
- Date: 2026-10-17
- What we did: Logged completion of Phase 1.9 entities (`skills`, `training_programs`, `training_enrollments`, `assets`, `asset_assignments`), consolidating schema creation verification with final Week 1 wrap-up check at Step 1.10.4.
- Why we did it: Completes Phase 1.9 milestone while batching runtime table checks into the full 20-table verification.
- Commit message: `"feat(step-1.9.7): complete training and asset entity definitions for batch schema verification"`

### Step 1.9.6 — Create AssetAssignment entity
- Date: 2026-10-17
- What we did: Created `AssetAssignment` JPA entity mapped to table `asset_assignments` in PostgreSQL with fields (`id`, `assetId`, `employeeId`, `assignedDate`, `returnedDate`, `status`).
- Why we did it: Tracks allocation and return of equipment to employees; unreturned assets block employee exit during offboarding (BR-10).
- How it works:
  - `returnedDate` is nullable while the asset is in employee possession (`ASSIGNED`).
- Files changed: `src/main/java/com/peopleops/entity/AssetAssignment.java`
- Commit message: `"feat(step-1.9.6): create asset assignment entity for employee equipment allocation tracking"`

### Step 1.9.5 — Create Asset entity
- Date: 2026-10-17
- What we did: Created `Asset` JPA entity mapped to table `assets` in PostgreSQL with fields (`id`, `assetTag`, `name`, `category`, `purchaseDate`, `status`) and unique constraint on `assetTag`.
- Why we did it: Tracks corporate hardware equipment and inventory status (`AVAILABLE`, `ASSIGNED`, `RETIRED`).
- How it works:
  - `assetTag` has `unique = true` to prevent duplicate tracking tags.
- Files changed: `src/main/java/com/peopleops/entity/Asset.java`
- Commit message: `"feat(step-1.9.5): create asset entity with unique asset tag tracking"`

### Step 1.9.4 — Create TrainingEnrollment entity
- Date: 2026-10-17
- What we did: Created `TrainingEnrollment` JPA entity mapped to table `training_enrollments` in PostgreSQL with fields (`id`, `trainingProgramId`, `employeeId`, `status`, `completionDate`) and composite unique constraint on `(training_program_id, employee_id)`.
- Why we did it: Records an employee's enrollment and progress through a training program, preventing duplicate enrollment in the same course.
- How it works:
  - `completionDate` is nullable until course completion.
  - Completing an enrollment will award the program's skills to the employee in Week 4 (BR-09).
- Files changed: `src/main/java/com/peopleops/entity/TrainingEnrollment.java`
- Commit message: `"feat(step-1.9.4): create training enrollment entity with composite unique constraint on program and employee"`

### Step 1.9.3 — Create TrainingProgram entity
- Date: 2026-10-17
- What we did: Created `TrainingProgram` JPA entity mapped to table `training_programs` in PostgreSQL with fields (`id`, `title`, `description`, `trainerName`, `startDate`, `endDate`, `status`).
- Why we did it: Represents structured learning courses scheduled by HR to enhance workforce capabilities.
- How it works:
  - Tracks training schedule (`startDate`, `endDate`) and course state (`status`).
  - Linked to competencies and trainees in subsequent steps and Week 4 many-to-many mappings.
- Files changed: `src/main/java/com/peopleops/entity/TrainingProgram.java`
- Check done: Created entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.9.3): create training program entity for employee learning course management"`

### Step 1.9.2 — Create Skill entity
- Date: 2026-10-17
- What we did: Created `Skill` JPA entity mapped to table `skills` in PostgreSQL with fields (`id`, `name`, `category`) and unique constraint on `name`.
- Why we did it: Catalogs competencies (e.g. "Java", "PostgreSQL", "System Architecture") required by jobs, developed via training programs, and possessed by employees.
- How it works:
  - `unique = true` prevents duplicate skill definitions.
  - Linked to employees and training programs via many-to-many join tables in Week 4.
- Files changed: `src/main/java/com/peopleops/entity/Skill.java`
- Check done: Created entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.9.2): create skill entity with unique competency name constraint"`

### Step 1.9.1 — Create TrainingStatus, EnrollmentStatus, AssetStatus, AssignmentStatus enums
- Date: 2026-10-17
- What we did: Created `TrainingStatus` (`PLANNED`, `ONGOING`, `COMPLETED`, `CANCELLED`), `EnrollmentStatus` (`ENROLLED`, `COMPLETED`, `DROPPED`), `AssetStatus` (`AVAILABLE`, `ASSIGNED`, `RETIRED`), and `AssignmentStatus` (`ASSIGNED`, `RETURNED`) enum types in package `com.peopleops.enums`.
- Why we did it: Provides type-safe domain constraints for training programs, employee course enrollments, physical hardware inventory, and employee equipment assignments.
- How it works:
  - Standard Java enum classes stored as text in PostgreSQL via `@Enumerated(EnumType.STRING)` in subsequent entity steps.
- Files changed: `src/main/java/com/peopleops/enums/TrainingStatus.java`, `src/main/java/com/peopleops/enums/EnrollmentStatus.java`, `src/main/java/com/peopleops/enums/AssetStatus.java`, `src/main/java/com/peopleops/enums/AssignmentStatus.java`
- Check done: Created 4 enums, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.9.1): create training, enrollment, asset, and assignment status enums"`

### Step 1.8.4 — Run and verify payroll and performance schema creation in PostgreSQL
- Date: 2026-10-17
- What we did: Started the application and verified via Hibernate DDL logs that tables `payrolls` and `performance_reviews` along with their composite unique constraints (`[employee_id, pay_month, pay_year]` and `[employee_id, review_period]`) were successfully initialized in PostgreSQL.
- Why we did it: Confirms database schema alignment for payroll and appraisal systems before implementing training and asset entities.
- How it works:
  - Spring Boot executes Hibernate `ddl-auto=update`, generating tables and unique constraint indexes.
- Files changed: Schema verified in `peopleops_db`.
- Check done: App started cleanly, EntityManagerFactory initialized, 2 tables created.
- Commit message: `"feat(step-1.8.4): verify payroll and performance reviews schema creation in postgresql"`

### Step 1.8.3 — Create PerformanceReview entity
- Date: 2026-10-17
- What we did: Created the `PerformanceReview` JPA entity mapped to the `performance_reviews` table in PostgreSQL with fields (`id`, `employeeId`, `reviewerId`, `reviewPeriod`, `rating`, `comments`, `status`, `reviewDate`) and a composite unique constraint on `(employee_id, review_period)`.
- Why we did it: Tracks quarterly or annual performance evaluations. The composite unique constraint guarantees that an employee cannot receive duplicate review records for the same review cycle (e.g., "Q1-2026").
- How it works:
  - `comments` column has `length = 1000` to support multi-paragraph managerial feedback.
  - `status` (`ReviewStatus`) tracks drafting vs submission (`DRAFT -> SUBMITTED`).
- Files changed: `src/main/java/com/peopleops/entity/PerformanceReview.java`
- Check done: Created entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.8.3): create performance review entity with composite unique constraint on employee and period"`

### Step 1.8.2 — Create Payroll entity
- Date: 2026-10-17
- What we did: Created the `Payroll` JPA entity mapped to the `payrolls` table in PostgreSQL with fields (`id`, `employeeId`, `payMonth`, `payYear`, `workingDays`, `lopDays`, `grossSalary`, `lopDeduction`, `pfDeduction`, `netSalary`, `status`, `generatedOn`) and a composite unique constraint on `(employee_id, pay_month, pay_year)`.
- Why we did it: Records monthly calculated salary disbursements and statutory deductions per employee. The unique constraint prevents double-paying or generating duplicate payroll runs for the same employee in the same month.
- How it works:
  - Currency fields (`grossSalary`, `lopDeduction`, `pfDeduction`, `netSalary`) use `BigDecimal` with `precision = 12, scale = 2` to prevent precision loss.
  - `lopDays` is a `Double` to support half-day absences (0.5 LOP per half-day).
- Files changed: `src/main/java/com/peopleops/entity/Payroll.java`
- Check done: Created entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.8.2): create payroll entity with monetary precision and composite unique constraint"`

### Step 1.8.1 — Create PayrollStatus and ReviewStatus enums
- Date: 2026-10-17
- What we did: Created `PayrollStatus` (`GENERATED`, `PAID`) and `ReviewStatus` (`DRAFT`, `SUBMITTED`) enums in package `com.peopleops.enums`.
- Why we did it: Encapsulates domain states for employee salary disbursement batches and quarterly performance appraisal lifecycles.
- How it works:
  - `PayrollStatus` manages payroll state transitions (`GENERATED -> PAID`).
  - `ReviewStatus` manages manager performance review drafting and submission (`DRAFT -> SUBMITTED`).
- Files changed: `src/main/java/com/peopleops/enums/PayrollStatus.java`, `src/main/java/com/peopleops/enums/ReviewStatus.java`
- Check done: Created enums, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.8.1): create payroll status and review status enums for payroll and performance tracking"`

### Step 1.7.6 — Run and verify attendance and leave schema creation in PostgreSQL
- Date: 2026-10-17
- What we did: Bootstrapped the Spring Boot application and verified via Hibernate DDL logs that tables `attendance_records`, `leave_types`, `leave_balances`, and `leave_requests` along with their composite unique constraints were created in PostgreSQL.
- Why we did it: Confirms database schema alignment with ERD specifications before moving to payroll and performance entities.
- How it works:
  - Spring Boot executes Hibernate `ddl-auto=update`, generating `CREATE TABLE` and `ALTER TABLE ADD CONSTRAINT` statements automatically.
- What I should understand/remember:
  - Composite constraints `(employee_id, attendance_date)` and `(employee_id, leave_type_id, balance_year)` protect data integrity directly inside the database.
- Files changed: Schema verified in `peopleops_db`.
- Check done: App started cleanly, EntityManagerFactory initialized, 4 tables created.

### Step 1.7.5 — Create LeaveRequest entity
- Date: 2026-10-17
- What we did: Created `LeaveRequest` JPA entity mapped to table `leave_requests` with fields (`id`, `employeeId`, `leaveTypeId`, `fromDate`, `toDate`, `days`, `reason`, `status`, `decidedById`, `decidedOn`).
- Why we did it: Represents employee leave applications undergoing approval workflow lifecycle (`PENDING -> APPROVED / REJECTED / CANCELLED`).
- How it works:
  - `decidedById` and `decidedOn` are nullable fields populated upon approval/rejection by a manager or HR admin.
  - `reason` has length 1000 for descriptive notes.
- Files changed: `src/main/java/com/peopleops/entity/LeaveRequest.java`

### Step 1.7.4 — Create LeaveBalance entity
- Date: 2026-10-17
- What we did: Created `LeaveBalance` JPA entity mapped to table `leave_balances` with fields (`id`, `employeeId`, `leaveTypeId`, `balanceYear`, `totalDays`, `usedDays`) and composite unique constraint on `(employee_id, leave_type_id, balance_year)`.
- Why we did it: Tracks an employee's allocated vs consumed quota per leave type per calendar year, preventing over-utilization (BR-04).
- How it works:
  - The composite unique constraint ensures an employee cannot have multiple balance ledgers for the same leave type in the same calendar year.
- Files changed: `src/main/java/com/peopleops/entity/LeaveBalance.java`

### Step 1.7.3 — Create LeaveType entity
- Date: 2026-10-17
- What we did: Created `LeaveType` JPA entity mapped to table `leave_types` with fields (`id`, `name`, `annualQuota`, `paid`) and unique constraint on `name`.
- Why we did it: Provides dynamic company-wide policy configuration for leave categories (e.g., Casual Leave, Sick Leave, Unpaid Leave).
- How it works:
  - `unique = true` prevents duplicate categories.
  - `paid` (boolean) flags whether time off is paid or incurs Loss of Pay (LOP) payroll deductions.
- Files changed: `src/main/java/com/peopleops/entity/LeaveType.java`

### Step 1.7.2 — Create Attendance entity
- Date: 2026-10-17
- What we did: We created the `Attendance` JPA entity mapped to the `attendance_records` table in PostgreSQL with fields (`id`, `employeeId`, `attendanceDate`, `checkIn`, `checkOut`, `status`) and a composite unique constraint on `(employee_id, attendance_date)`.
- Why we did it: Tracks daily attendance per employee, recording punch times and status (`PRESENT`, `HALF_DAY`, `ABSENT`, `ON_LEAVE`). The unique constraint prevents duplicate attendance logs for the same employee on the same date.
- How it works:
  - **`@Table(name = "attendance_records", uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "attendance_date"})})`**: Creates a composite unique index in PostgreSQL preventing duplicate check-ins on the same calendar day.
  - **`java.time.LocalTime`**: Maps to PostgreSQL `time without time zone` columns (`check_in`, `check_out`), storing the time of day without timezone distortion.
- What I should understand/remember:
  - `checkIn` and `checkOut` can be null (e.g. for approved leaves with status `ON_LEAVE` or whole-day absences).
  - Approving a leave request creates an `Attendance` record with status `ON_LEAVE` and null check-in/out times (BR-06).
- Example:
  - An employee checking in at 09:15 and checking out at 18:30 has `checkIn = 09:15:00`, `checkOut = 18:30:00`, and `status = PRESENT`.
- Files changed: `src/main/java/com/peopleops/entity/Attendance.java`
- Viva questions:
  1. Q: Why is there a unique constraint on `(employee_id, attendance_date)`?
     A: An employee can only have one attendance log per calendar day; the composite unique constraint enforces this at the database level.
  2. Q: Which Java type is used to store `check_in` time without a date component?
     A: `java.time.LocalTime`.
- Rules used: BR-04, BR-06, BR-07, D-006, D-016
- Check done: Created entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.7.2): create attendance entity with composite unique constraint on employee and date"`

### Step 1.7.1 — Create AttendanceStatus and LeaveRequestStatus enums
- Date: 2026-10-17
- What we did: We created `AttendanceStatus` (`PRESENT`, `HALF_DAY`, `ABSENT`, `ON_LEAVE`) and `LeaveRequestStatus` (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`) enum types in package `com.peopleops.enums`.
- Why we did it: Restricts daily attendance logs and leave request states to strictly validated domain constants, preventing invalid state transitions and typographical errors in the database.
- How it works:
  - **`AttendanceStatus`**: Represents daily work presence (`PRESENT`, `HALF_DAY`), absence, or approved leave (`ON_LEAVE`). Feeds directly into payroll Loss of Pay (LOP) calculations.
  - **`LeaveRequestStatus`**: Represents the approval workflow lifecycle (`PENDING -> APPROVED / REJECTED / CANCELLED`).
- What I should understand/remember:
  - Daily attendance status directly drives monthly payroll deduction formulas (BR-07).
  - Approving a leave request automatically creates `ON_LEAVE` attendance records for those calendar dates (BR-06).
- Example:
  - An employee working fewer than 4 hours is logged as `HALF_DAY`, which generates 0.5 days of LOP deduction during payroll calculation.
- Files changed: `src/main/java/com/peopleops/enums/AttendanceStatus.java`, `src/main/java/com/peopleops/enums/LeaveRequestStatus.java`
- Viva questions:
  1. Q: What happens to attendance when a leave request is approved?
     A: Per BR-06, approving a leave request automatically creates `ON_LEAVE` attendance records for each date in the leave period that does not already have a record.
  2. Q: How does `HALF_DAY` attendance affect payroll?
     A: Per BR-07, each `HALF_DAY` attendance record adds 0.5 to the employee's monthly Loss of Pay (LOP) days.
- Rules used: BR-04, BR-06, BR-07, D-006, D-016
- Check done: Created both enums, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.7.1): create attendance status and leave request status enums for daily tracking and leave approval lifecycle"`

### Step 1.6.4 — Create OnboardingTaskStatus enum and OnboardingTask entity
- Date: 2026-10-08
- What we did: We created `OnboardingTaskStatus` enum (`PENDING`, `DONE`) and the `OnboardingTask` JPA entity class mapped to table `onboarding_tasks` in PostgreSQL with fields (`id`, `employeeId`, `title`, `description`, `dueDate`, `status`, `completedOn`).
- Why we did it: `OnboardingTask` tracks the onboarding checklist items assigned to employees upon hire (BR-02), requiring completion of all tasks before employee activation (BR-03).
- How it works:
  - **`@Enumerated(EnumType.STRING)`**: Stores status as `"PENDING"` or `"DONE"` in a `VARCHAR(20)` column.
  - **Plain `Long` Foreign Key**: `employeeId` links each task to the corresponding `Employee` (converted to `@ManyToOne` in Phase 4.2).
  - **Nullable `completedOn`**: Captures completion date once task is done; remains null while pending.
- What I should understand/remember:
  - Every hired employee gets default onboarding tasks, which must all reach `DONE` status before transitioning to `ACTIVE` (BR-02, BR-03).
- Example:
  - An onboarding task `"Submit ID proof"` with `employeeId = 1`, `dueDate = 2026-10-15`, and `status = PENDING`.
- Files changed: `src/main/java/com/peopleops/enums/OnboardingTaskStatus.java`, `src/main/java/com/peopleops/entity/OnboardingTask.java`
- Viva questions:
  1. Q: Why is `completedOn` nullable on `OnboardingTask`?
     A: Because when a task is first generated, it is pending and has not been completed yet; `completedOn` is only filled when status becomes `DONE`.
  2. Q: How is `OnboardingTask` linked to `Employee` in Week 1?
     A: Using a plain `Long employeeId` foreign key column (Rule D-004), to be replaced by `@ManyToOne` relationship mapping in Week 4.
- Rules used: Workflow §4, BR-02, BR-03, D-001, D-004, D-006, D-016
- Check done: Created `OnboardingTaskStatus.java` and `OnboardingTask.java`, compiled cleanly with `mvn clean compile`.
- Commit message: no commit yet (continues to step 1.6.5 to complete Phase 1.6 and Checkpoint CP-03)

### Step 1.6.3 — Create InterviewResult enum and Interview entity
- Date: 2026-10-08
- What we did: We created `InterviewResult` enum (`PENDING`, `PASSED`, `FAILED`) and the `Interview` JPA entity class mapped to table `interviews` in PostgreSQL with fields (`id`, `candidateId`, `interviewerId`, `scheduledAt`, `roundNumber`, `result`, `feedback`).
- Why we did it: `Interview` tracks scheduled evaluation rounds for job candidates, assigned interviewers, timestamps, and evaluation outcomes.
- How it works:
  - **`LocalDateTime`**: Maps interview schedule timestamps (`scheduled_at`).
  - **`@Enumerated(EnumType.STRING)`**: Persists enum outcome as text (`"PASSED"`, `"FAILED"`, `"PENDING"`).
  - **Plain `Long` Foreign Keys**: `candidateId` and `interviewerId` link interviews to candidates and interviewer employees.
- What I should understand/remember:
  - Dates with precise time of day use `LocalDateTime` in Java 8+ / JPA entities.
- Example:
  - Round 1 technical interview scheduled at `2026-10-15T10:00:00` with `result = PENDING`.
- Files changed: `src/main/java/com/peopleops/enums/InterviewResult.java`, `src/main/java/com/peopleops/entity/Interview.java`
- Viva questions:
  1. Q: Why do we use `LocalDateTime` for `scheduledAt` instead of `LocalDate`?
     A: `LocalDate` stores only calendar date without time, whereas `LocalDateTime` stores both date and exact time required for interview scheduling.
- Rules used: Workflow §4, D-001, D-004, D-006, D-016
- Check done: Created `InterviewResult.java` and `Interview.java`, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.6.3): create interview result enum and interview entity for evaluation round tracking"`

### Step 1.6.2 — Create CandidateStage enum and Candidate entity
- Date: 2026-10-08
- What we did: We created `CandidateStage` enum (`APPLIED`, `SCREENING`, `INTERVIEWING`, `OFFERED`, `HIRED`, `REJECTED`) and `Candidate` entity mapped to `candidates` table with unique constraint on `(job_opening_id, email)`.
- Why we did it: `Candidate` represents applicants applying for job openings and moving through hiring funnel stages.
- How it works:
  - **`@UniqueConstraint(columnNames = {"job_opening_id", "email"})`**: Prevents a candidate from applying multiple times to the same job opening with the same email.
- What I should understand/remember:
  - Multi-column unique constraints are declared in `@Table(uniqueConstraints = ...)` in JPA.
- Example:
  - Candidate with email `"john@example.com"` applying to job opening ID `1`.
- Files changed: `src/main/java/com/peopleops/enums/CandidateStage.java`, `src/main/java/com/peopleops/entity/Candidate.java`
- Viva questions:
  1. Q: How is a composite unique constraint declared on a JPA entity?
     A: Using `@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"col1", "col2"})})`.
- Rules used: Workflow §4, D-001, D-004, D-006, D-016
- Check done: Created `CandidateStage.java` and `Candidate.java`, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.6.2): create candidate stage enum and candidate entity with job opening unique email constraint"`

### Step 1.6.1 — Create JobOpeningStatus enum and JobOpening entity
- Date: 2026-10-08
- What we did: We created `JobOpeningStatus` enum (`OPEN`, `CLOSED`) and the `JobOpening` JPA entity class mapped to table `job_openings` in PostgreSQL with fields (`id`, `title`, `departmentId`, `designationId`, `openPositions`, `postedDate`, `status`).
- Why we did it: `JobOpening` represents open job vacancies in the organization that accept candidate applications.
- How it works:
  - **`@Enumerated(EnumType.STRING)`**: Stores status as text (`"OPEN"`, `"CLOSED"`).
  - **Plain `Long` Foreign Keys**: `departmentId` and `designationId` link vacancies to departments/designations.
- What I should understand/remember:
  - Recruitment starts with a job opening before candidates can apply.
- Example:
  - A `JobOpening` for `"Java Developer"` has `openPositions = 3` and `status = OPEN`.
- Files changed: `src/main/java/com/peopleops/enums/JobOpeningStatus.java`, `src/main/java/com/peopleops/entity/JobOpening.java`
- Viva questions:
  1. Q: What fields link `JobOpening` to departments and designations in Week 1?
     A: Plain `Long` foreign keys `departmentId` and `designationId`.
- Rules used: Workflow §4, D-001, D-004, D-006, D-016
- Check done: Created `JobOpeningStatus.java` and `JobOpening.java`, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.6.1): create job opening status enum and job opening entity for tracking job vacancies"`

### Step 1.5.5 — Core entities verification
- Date: 2026-10-08
- What we did: We started the Spring Boot application and verified that Hibernate automatically generated PostgreSQL database tables `departments`, `designations`, and `employees` in `peopleops_db`.
- Why we did it: Verifying automatic table DDL creation confirms that JPA entity mappings, database connection properties, and PostgreSQL permissions work together seamlessly.
- How it works:
  - **`spring.jpa.hibernate.ddl-auto=update`**: Hibernate compares Java `@Entity` annotations against PostgreSQL system catalogs and executes `CREATE TABLE` DDL for missing tables automatically.
- What I should understand/remember:
  - Database table column names in PostgreSQL use snake_case (`first_name`, `monthly_salary`), mapped from camelCase Java fields (`firstName`, `monthlySalary`).
- Example:
  - Hibernate logged `create table employees (id bigint generated by default as identity, employee_code varchar(20) not null, ...)` during startup.
- Files changed: None (verification step)
- Viva questions:
  1. Q: How does Hibernate convert Java field names like `firstName` to SQL column names?
     A: By default, Spring Boot uses `CamelCaseToUnderscoresNamingStrategy`, converting camelCase Java properties into snake_case SQL columns (`first_name`).
- Rules used: Workflow §4, D-005, D-016
- Check done: Application booted cleanly, HikariCP connected, Tomcat started on port 8080, and tables generated in PostgreSQL.
- Commit message: `"test(phase-1.5): verify core entities compilation and postgresql table ddl generation for departments designations employees"`

### Step 1.5.4 — Create Employee entity
- Date: 2026-10-08
- What we did: We created the `Employee` JPA entity class mapped to table `employees` in PostgreSQL with personal details, hiring dates, salary (`BigDecimal`), status enum (`@Enumerated(EnumType.STRING)`), and plain `Long` foreign keys (`departmentId`, `designationId`, `managerId`).
- Why we did it: `Employee` is the central domain entity of PeopleOps HRMS. Storing money as `BigDecimal` avoids floating-point binary rounding errors.
- How it works:
  - **`@Enumerated(EnumType.STRING)`**: Persists enum constants as human-readable strings (`"ACTIVE"`) instead of ordinal integers.
  - **Plain `Long` Foreign Keys (Rule D-004)**: Avoids complex JPA relationship mappings in Week 1; will be converted to `@ManyToOne` in Week 4 without changing database schema columns.
- What I should understand/remember:
  - Never use `float` or `double` for currency/salary values in software systems; always use `BigDecimal`.
- Example:
  - `monthlySalary` has `precision = 12, scale = 2`, allowing salary values up to `99,999,999,999.99`.
- Files changed: `src/main/java/com/peopleops/entity/Employee.java`
- Viva questions:
  1. Q: Why do we use `BigDecimal` instead of `double` for `monthlySalary`?
     A: `double` suffers from binary floating-point rounding inaccuracy (e.g., `0.1 + 0.2 != 0.3`), whereas `BigDecimal` guarantees exact decimal precision.
- Rules used: Workflow §4, D-001, D-004, D-006, D-007, D-016
- Check done: Created `Employee.java` entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.5.4): create employee entity with personal details salary status and plain foreign key ids"`

### Step 1.5.3 — Create Designation entity
- Date: 2026-10-05
- What we did: We created the `Designation` JPA entity class mapped to table `designations` in PostgreSQL with fields (`id`, `title`, `gradeLevel`, `description`).
- Why we did it: Designations define job titles and pay grade ranks for employees across departments.
- How it works:
  - **`@Entity` & `@Table(name = "designations")`**: Hibernate ORM creates `designations` table in PostgreSQL.
  - **`gradeLevel` Integer Mapping**: Maps numeric job grades (e.g. `1`, `2`) to PostgreSQL `INTEGER` column.
- What I should understand/remember:
  - Entity classes encapsulate domain attributes and DB column rules.
- Example:
  - `title` is `unique = true`, so creating duplicate titles like `"Software Engineer"` triggers a SQL constraint exception.
- Files changed: `src/main/java/com/peopleops/entity/Designation.java`
- Viva questions:
  1. Q: How does JPA map a Java `Integer` field like `gradeLevel` to SQL?
     A: It maps it to PostgreSQL `INTEGER` column, allowing nulls unless `nullable = false` is specified.
- Rules used: Workflow §4, D-001, D-016
- Check done: Created `Designation.java` entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.5.3): create designation entity with unique title and grade level columns and verify clean project compilation"`

### Step 1.5.2 — Create Department entity
- Date: 2026-10-05
- What we did: We manually created the `Department` JPA entity class mapped to the `departments` table in PostgreSQL with fields (`id`, `name`, `code`, `description`, `active`).
- Why we did it: `Department` is a core organizational domain entity. HR systems need to organize employees by departments, enforcing unique department names and short codes.
- How it works:
  - **`@Entity` & `@Table(name = "departments")`**: Instructs Hibernate ORM to create table `departments` in PostgreSQL.
  - **`@Id` & `@GeneratedValue(strategy = GenerationType.IDENTITY)`**: Maps `id` to PostgreSQL auto-incrementing identity primary key.
  - **`@Column(nullable = false, unique = true)`**: Generates SQL DDL column constraints enforcing non-null and unique rules.
- What I should understand/remember:
  - JPA `@Entity` classes bridge Java code and database tables.
  - Lombok `@NoArgsConstructor` is required so Hibernate can construct objects via reflection when reading rows.
- Example:
  - `name` column has `unique = true`, so attempting to insert two departments named `"Engineering"` will trigger a database constraint violation.
- Files changed: `src/main/java/com/peopleops/entity/Department.java`
- Viva questions:
  1. Q: What is the purpose of `@Table(name = "departments")`?
     A: It explicitly sets the SQL table name to `departments` instead of defaulting to the class name `Department`.
  2. Q: Why is `@NoArgsConstructor` required on JPA entities?
     A: Hibernate needs an empty default constructor to instantiate objects when retrieving SQL database rows via reflection.
- Rules used: Workflow §4, D-001, D-016
- Check done: Created `Department.java` entity, compiled cleanly with `mvn clean compile`.
- Commit message: `"feat(step-1.5.2): add Department entity mapped to departments table with unique name and code constraints"`

### Step 1.5.1 — Create package enums and EmployeeStatus
- Date: 2026-10-03
- What we did: We created package `com.peopleops.enums` and defined the `EmployeeStatus` enumeration type.
- Why we did it: Java `enum` types restrict property values to predefined constants (`ONBOARDING`, `ACTIVE`, `ON_NOTICE`, `EXITED`), eliminating invalid status strings and typos in employee records.
- How it works:
  - **Enumerations (`enum`):** A fixed set of named constants in Java.
  - **Database Persistence:** JPA maps enums using `@Enumerated(EnumType.STRING)` to store clean, human-readable text values (`"ACTIVE"`, `"EXITED"`) in PostgreSQL table columns.
- What I should understand/remember:
  - Enums prevent illegal state values (e.g. typing `"actv"` instead of `"ACTIVE"`).
  - Storing enums as string names (`EnumType.STRING`) avoids ordinal index bugs if enum values are added or reordered later.
- Example:
  - Setting `employee.setStatus(EmployeeStatus.ACTIVE)` ensures only valid lifecycle states are saved to the database.
- Files changed: `src/main/java/com/peopleops/enums/EmployeeStatus.java`
- Viva questions:
  1. Q: Why should enums be stored as `EnumType.STRING` instead of `EnumType.ORDINAL` in JPA?
     A: `STRING` stores constant names (e.g. `"ACTIVE"`), preventing data corruption if new constants are added or reordered in Java code.
  2. Q: What are the four valid lifecycle states in `EmployeeStatus`?
     A: `ONBOARDING`, `ACTIVE`, `ON_NOTICE`, `EXITED`.
- Rules used: Workflow §4, D-006, D-016
- Check done: Created `com.peopleops.enums.EmployeeStatus` enum.
- Commit message: `"feat(step-1.5.1): add EmployeeStatus enum representing employee lifecycle states"`

### Step 1.4.2 — Start the app and confirm database connection
- Date: 2026-10-03
- What we did: We started the Spring Boot application using `mvn spring-boot:run` and verified that HikariCP successfully connected to PostgreSQL `peopleops_db` on port 8080.
- Why we did it: Verifying application startup with database connection pool initialization confirms that JPA, PostgreSQL drivers, and `application.properties` parameters work together seamlessly before creating entity models.
- How it works:
  - **HikariCP Connection Pool:** Spring Boot's default JDBC connection pool framework. It opens and maintains database connections to PostgreSQL so requests can re-use existing database links efficiently.
  - **JPA EntityManagerFactory:** Hibernate initializes metadata for `peopleops_db` and starts embedded Tomcat server on port 8080.
- What I should understand/remember:
  - Database connectivity verification confirms three layers: RDBMS instance running, credentials/URL matching in `application.properties`, and JDBC driver loaded in `pom.xml`.
- Example:
  - Application console logged `Initialized JPA EntityManagerFactory for persistence unit 'default'` and `Tomcat started on port 8080`, confirming clean startup.
- Files changed: None (verification step)
- Viva questions:
  1. Q: What is HikariCP in Spring Boot?
     A: HikariCP is a high-performance JDBC connection pool framework that manages reusable database connections.
  2. Q: What log entry confirms successful database initialization in Spring Boot?
     A: `Initialized JPA EntityManagerFactory for persistence unit 'default'`.
- Rules used: Workflow §4, D-005, D-016
- Check done: Started application, verified clean HikariCP & Tomcat startup on port 8080, and completed CP-02.
- Commit message: `"chore(cp-02): configure postgresql database connectivity and verify spring boot hikari connection pool initialization"`

### Step 1.4.1 — Configure application.properties
- Date: 2026-10-03
- What we did: We created `src/main/resources/application.properties` and configured Spring Boot datasource parameters for PostgreSQL connection and Hibernate JPA properties.
- Why we did it: Spring Boot needs database connection parameters (URL, driver, credentials) and JPA properties to connect to the PostgreSQL `peopleops_db` instance and manage relational data.
- How it works:
  - **`spring.datasource.url`:** Specifies the JDBC URL `jdbc:postgresql://localhost:5432/peopleops_db`.
  - **`spring.jpa.hibernate.ddl-auto=update`:** Hibernate compares entity classes with database tables and creates or updates database tables automatically on application startup.
  - **`spring.jpa.show-sql=true`:** Prints formatted SQL statements executed by Hibernate to the console log for easy debugging.
- What I should understand/remember:
  - `application.properties` centralizes key-value configuration parameters in Spring Boot.
  - `ddl-auto=update` updates database table schemas automatically without dropping existing data.
- Example:
  - Setting `spring.datasource.username=postgres` tells Spring Data JPA which PostgreSQL user role to authenticate with when creating connection pools.
- Files changed: `src/main/resources/application.properties`
- Viva questions:
  1. Q: What does `spring.jpa.hibernate.ddl-auto=update` do in Spring Boot?
     A: It automatically updates PostgreSQL database tables to match Java `@Entity` mappings without deleting existing table data.
  2. Q: What is the JDBC URL format for a local PostgreSQL database?
     A: `jdbc:postgresql://localhost:5432/<database_name>`
- Rules used: Workflow §4, D-005, D-016
- Check done: Created valid `application.properties` with PostgreSQL driver and connection settings.
- Commit message: no commit yet (continues to step 1.4.2 to complete phase 1.4 and CP-02)

### Step 1.3.2 — Confirm mvn clean compile passes
- Date: 2026-10-03
- What we did: We executed `mvn clean compile` in the terminal to verify that Maven resolves dependencies and compiles Java source files cleanly without build errors.
- Why we did it: Testing project compilation right after project setup ensures dependencies, JDK target settings, and syntax rules are fully valid before writing business code.
- How it works:
  - **`clean` phase:** Deletes the `target/` directory containing old build artifacts to ensure a fresh compilation build.
  - **`compile` phase:** Downloads missing dependencies from Maven Central, compiles Java code under `src/main/java`, and places output `.class` files into `target/classes`.
- What I should understand/remember:
  - Always run `mvn clean compile` after adding new dependencies or project configurations to verify build integrity early.
- Example:
  - Running `mvn clean compile` downloaded the Java compiler plugin (`plexus-compiler`) and built `PeopleOpsApplication.class` with output `BUILD SUCCESS`.
- Files changed: None (build verification step)
- Viva questions:
  1. Q: What is the difference between `mvn clean` and `mvn compile`?
     A: `mvn clean` deletes previous build output in `target/`, while `mvn compile` compiles source code into byte-code `.class` files.
  2. Q: Where does Maven store compiled `.class` files?
     A: In the `target/classes` directory.
- Rules used: Workflow §4, D-016
- Check done: Ran `mvn clean compile` and verified `BUILD SUCCESS`.
- Commit message: `"chore(phase-1.3): set up spring boot project with maven dependencies and confirm clean compilation"`

### Step 1.3.1 — Generate Spring Boot Maven project structure
- Date: 2026-10-03
- What we did: We generated the core Maven project structure (`pom.xml`) with Spring Boot 3.2.5 dependencies and created the main application class `PeopleOpsApplication.java`.
- Why we did it: Every Spring Boot application requires a `pom.xml` configuration file to manage dependencies, Java compiler settings, and build plugins, alongside a `@SpringBootApplication` entry-point class.
- How it works:
  - **Maven (`pom.xml`):** Project Object Model file. It downloads required JAR libraries (Web, JPA, PostgreSQL, Lombok) from Maven Central and manages project compilation.
  - **`@SpringBootApplication`:** A composite annotation (`@SpringBootConfiguration`, `@EnableAutoConfiguration`, `@ComponentScan`) that instructs Spring Boot to automatically configure beans and scan packages.
  - **Main method:** `SpringApplication.run(PeopleOpsApplication.class, args)` starts the embedded Tomcat web server and initializes the Spring application context.
- What I should understand/remember:
  - Group ID (`com.peopleops`) and Artifact ID (`peopleops`) uniquely identify the project in Maven.
  - All application code resides under base package `com.peopleops`.
- Example:
  - Adding `<dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>` automatically includes Tomcat server, Jackson JSON processing, and Spring MVC for REST APIs.
- Files changed: `pom.xml`, `src/main/java/com/peopleops/PeopleOpsApplication.java`
- Viva questions:
  1. Q: What does `@SpringBootApplication` do?
     A: It enables Spring Boot auto-configuration, component scanning across `com.peopleops` packages, and defines the Spring configuration class.
  2. Q: What is the purpose of `pom.xml` in a Maven Java project?
     A: It defines project metadata, dependencies (libraries), build plugins, and Java version settings.
- Rules used: Workflow §4, D-001, D-016
- Check done: Created valid `pom.xml` and `PeopleOpsApplication.java`.
- Commit message: no commit yet (continues to Checkpoint CP-02 which covers phases 1.2, 1.3, 1.4)

### Step 1.2.1 — Create PostgreSQL database peopleops_db
- Date: 2026-10-03
- What we did: We created the PostgreSQL database initialization script `docs/db-setup.sql` containing the `CREATE DATABASE peopleops_db` command with UTF-8 encoding.
- Why we did it: Every enterprise backend application needs a dedicated relational database instance to persist domain tables, records, and relationships across server restarts.
- How it works:
  - **Relational Database Management System (RDBMS):** PostgreSQL is an open-source RDBMS. The command `CREATE DATABASE` instructs PostgreSQL to create an isolated database storage space named `peopleops_db`.
  - **UTF-8 Encoding:** Configures character set support so the database can store names, addresses, and text strings in any language without character corruption.
- What I should understand/remember:
  - Database creation SQL scripts are saved in project documentation so any developer or evaluator can reproduce the environment.
  - The database name `peopleops_db` matches the configuration in `trd.md` §5.
- Example:
  - Running `CREATE DATABASE peopleops_db;` in PostgreSQL SQL Shell (psql) or pgAdmin creates the empty database where Spring Boot / Hibernate will generate tables later.
- Files changed: `docs/db-setup.sql`
- Viva questions:
  1. Q: Why do we specify UTF-8 encoding when creating a database?
     A: UTF-8 ensures standard multi-byte character support across all international character sets and symbols.
  2. Q: Which RDBMS is used for the PeopleOps project?
     A: PostgreSQL version 15+.
- Rules used: Workflow §4, D-016
- Check done: Created `docs/db-setup.sql` with valid database creation DDL.
- Commit message: no commit yet (continues to Checkpoint CP-02 which covers phases 1.2, 1.3, 1.4)

### Step 1.1.3 — Requirement sign-off
- Date: 2026-10-03
- What we did: We formally reviewed and signed off on the project requirements baseline (`prd.md`, `trd.md`, `erd.md`, and `implementation.md`), marking Phase 1.1 complete.
- Why we did it: Requirement sign-off establishes a fixed baseline. It ensures that business goals, database designs, and technical rules are frozen before implementation begins so we don't change architecture mid-way without recording decision changes.
- How it works:
  - **Requirement Baseline:** In software development, sign-off is the gate where specifications transition from draft to active reference. Any future change to PRD/TRD/ERD must be logged as an explicit decision in `explainer.md` (§1 rule).
  - **Checkpoint CP-01:** This step completes Phase 1.1 and satisfies Checkpoint CP-01. The repository baseline is now ready for its first Git commit.
- What I should understand/remember:
  - Never start coding database tables or REST APIs without signed-off design specs.
  - Checkpoint CP-01 is reached when all documentation baseline files are created and signed off.
- Example:
  - If someone asks during a project review why the database uses plain `Long` foreign key IDs in Week 1 instead of `@ManyToOne` relationships, we point to the signed-off baseline rule D-004 in `explainer.md`.
- Files changed: `docs/explainer.md`
- Viva questions:
  1. Q: Why is formal requirement sign-off important before starting database setup?
     A: It prevents scope creep and ensures that entities, fields, and API endpoints follow an approved design roadmap.
  2. Q: What git commit message belongs to Checkpoint CP-01?
     A: `docs(cp-01): add project documentation baseline`
- Rules used: Workflow §2, Workflow §10, D-016
- Check done: Phase 1.1 marked completed; CP-01 commit message generated.
- Commit message: `docs(cp-01): add project documentation baseline`

### Step 1.1.2 — Add .gitignore and README.md skeleton
- Date: 2026-10-03
- What we did: We created two essential root-level configuration files: `.gitignore` and `README.md`.
- Why we did it:
  - `.gitignore` prevents cluttering the Git repository with compiled Java code (`target/`), temporary log files, and local IDE settings.
  - `README.md` acts as the project front page, explaining what PeopleOps HRMS is, the tech stack, and providing links to full project documentation.
- How it works:
  - **Git & Ignore rules:** Git tracks file changes in a repository. Before staging files, Git evaluates `.gitignore` patterns. If a pattern matches (such as `target/` or `*.log`), Git completely ignores those files during status checks and commits.
  - **Markdown rendering:** Files ending in `.md` use Markdown (a simple formatting markup). Code repositories (like GitHub) automatically convert Markdown files into styled web pages.
- What I should understand/remember:
  - Never commit generated build folders (like Maven's `target/`), binary output, or local editor configs into version control.
  - A good project `README` gives anyone inspecting the codebase a quick overview of what the application does and how it is structured.
- Example:
  - When running `mvn clean compile`, Maven creates hundreds of compiled `.class` files in `target/`. Without `.gitignore`, Git would mark all of them as new files to track. With `.gitignore`, Git ignores the `target/` directory completely.
- Files changed: `.gitignore`, `README.md`
- Viva questions:
  1. Q: Why is `.gitignore` important in a Maven Java project?
     A: It prevents compiled byte-code (`target/` folder), log files, and personal IDE settings from polluting the shared Git repository.
  2. Q: What is Markdown (`.md`) and why is it used for `README.md`?
     A: Markdown is a lightweight text formatting syntax that Git platforms automatically convert into styled HTML documents for readable project documentation.
- Rules used: Workflow §4, D-016
- Check done: Created `.gitignore` and `README.md` files at repository root.
- Commit message: no commit yet (checkpoint CP-01 requires completion of step 1.1.3)

### Step 1.1.1 — Create the repo folder and place docs/ files
- Date: 2026-10-03
- What we did: We organized the project workspace and placed standard architecture and planning documents in the `docs/` directory.
- Why we did it: Professional software development begins with planning. Having clear business specifications, database diagrams, and technical guidelines before writing Java code prevents design flaws and scope confusion.
- How it works:
  - **Document Roles:** 
    - `prd.md` (Product Requirements Document): Describes *what* the system does and business rules (e.g., employee status flows).
    - `trd.md` (Technical Requirements Document): Specifies *how* the technology stack and REST APIs are designed.
    - `erd.md` (Entity-Relationship Diagram): Maps database tables, column data types, and foreign key relationships.
    - `implementation.md`: The 196-step roadmap detailing the exact order to build entities, services, controllers, and tests.
    - `explainer.md`: This living record for tracking step-by-step progress, learning concepts, decisions, and viva preparation.
- What I should understand/remember:
  - Requirements (PRD), Tech specs (TRD), Data schemas (ERD), and Step Plans (implementation plan) are distinct documents with specific purposes.
  - Always consult `docs/` before implementing any feature to ensure compliance with predefined business and technical rules.
- Example:
  - Before writing the Java `Employee` entity, we look up `erd.md` for table field names/types and `prd.md` for rules like unique email checks (`BR-01`).
- Files changed: `docs/prd.md`, `docs/trd.md`, `docs/erd.md`, `docs/implementation.md`, `docs/explainer.md`, `docs/complexity-benchmark.md`, `docs/IDE_starter_prompt.md`
- Viva questions:
  1. Q: What is the difference between a PRD and a TRD?
     A: A PRD defines business functionality and domain rules from a user perspective, whereas a TRD defines software architecture, frameworks, and technical specifications.
  2. Q: Why is an ERD created before writing database entity code?
     A: An ERD visualizes tables, data types, primary/foreign keys, and relationships, preventing database schema design errors early.
- Rules used: Workflow §2, D-016
- Check done: Verified documentation baseline files in repository `docs/` directory.
- Commit message: no commit yet (checkpoint CP-01 requires completion of step 1.1.3)

```
### Step <id> — <step name>
- Date:
- What we did: 
- Why we did it: 
- How it works: 
- What I should understand/remember: 
- Example: 
- Files changed: 
- Viva questions:
  1. Q: 
     A: 
  2. Q: 
     A: 
- Rules used: (BR-xx, decisions D-xxx)
- Check done: 
- Commit message: (only if this step completed a checkpoint)
```

## 10. Postman and test results

| Phase | Date | Result | Notes |
|---|---|---|---|
| Phase 2.12 | 2026-10-20 | PASS (100%) | Verified full employee lifecycle Postman collection (7 modular folders, all CRUD endpoints) |
| Phase 3.1 | 2026-10-23 | PASS | Verified Logback file destination (`logs/peopleops.log`), logging patterns, and SLF4J service event emission |
| Phase 3.2 | 2026-10-23 | PASS | Verified standardized JSON error responses across 404, 400, 409, malformed JSON, and bad query enums via Postman collection folder '8. Global Exception Handling' |
