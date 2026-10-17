# explainer.md — PeopleOps Living Project Record

Personal scratch document. Updated by the AI after **every** step (rules: `workflow.md` §9). Written so each decision can be explained out loud.

Last updated: 2026-10-17 · Current step: **1.7.2** · Status: in progress

---

## 1. Overall project progress

| Measure | Value |
|---|---|
| Semester | 1 (Backend) |
| Weeks complete | 0 / 4 |
| Phases complete | 6 / 36 |
| Steps complete | 19 / 196 |
| Commits (checkpoints) made | 0 / 23 (CP-01, CP-02, CP-03 ready) |
| Overall | 9.7% |
| Complexity benchmark | PENDING (must be filled before step 1.5.1) |
| Java / Spring Boot / PostgreSQL versions | Java 24 (Target Java 17) / Spring Boot 3.2.5 / PostgreSQL 15+ |

## 2. Weeks

| Week | Scope | Phases | Steps | Done | Commits | Status |
|---|---|---|---|---|---|---|
| 1 | Requirements, ERD, database, setup, entities | 10 | 40 | 26 | 0 / 5 | In progress |
| 2 | REST APIs | 12 | 90 | 0 | 0 / 8 | Not started |
| 3 | Logging, exceptions, validation, Swagger, API testing | 5 | 31 | 0 | 0 / 4 | Not started |
| 4 | Mappings, mapping tests, JAR & WAR, final testing | 9 | 35 | 0 | 0 / 6 | Not started |

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
| 1.8 | Payroll and performance entities | 4 | 2 | CP-04 | In progress |
| 1.9 | Training and asset entities | 7 | 0 | CP-05 | Not started |
| 1.10 | Documents and offboarding entities, Week 1 wrap-up | 4 | 0 | CP-05 | Not started |
| 2.1 | Department and Designation APIs | 8 | 0 | CP-06 | Not started |
| 2.2 | Employee APIs | 6 | 0 | CP-06 | Not started |
| 2.3 | Recruitment APIs | 13 | 0 | CP-07 | Not started |
| 2.4 | Onboarding and the hire workflow | 7 | 0 | CP-08 | Not started |
| 2.5 | Attendance and leave APIs | 15 | 0 | CP-09 | Not started |
| 2.6 | Payroll APIs | 5 | 0 | CP-10 | Not started |
| 2.7 | Performance APIs | 5 | 0 | CP-10 | Not started |
| 2.8 | Training and skills APIs | 11 | 0 | CP-11 | Not started |
| 2.9 | Asset APIs | 8 | 0 | CP-11 | Not started |
| 2.10 | Documents and employee activation | 6 | 0 | CP-12 | Not started |
| 2.11 | Offboarding APIs | 5 | 0 | CP-12 | Not started |
| 2.12 | Week 2 wrap-up | 1 | 0 | CP-13 | Not started |
| 3.1 | Logging | 7 | 0 | CP-14 | Not started |
| 3.2 | Global exception handling | 9 | 0 | CP-15 | Not started |
| 3.3 | Validation | 7 | 0 | CP-16 | Not started |
| 3.4 | Swagger / OpenAPI | 5 | 0 | CP-17 | Not started |
| 3.5 | API testing and Week 3 wrap-up | 3 | 0 | CP-17 | Not started |
| 4.1 | Employee core mappings | 6 | 0 | CP-18 | Not started |
| 4.2 | Recruitment and onboarding mappings | 5 | 0 | CP-18 | Not started |
| 4.3 | Attendance and leave mappings | 4 | 0 | CP-19 | Not started |
| 4.4 | Payroll and performance mappings | 3 | 0 | CP-19 | Not started |
| 4.5 | Training, asset and document mappings | 4 | 0 | CP-19 | Not started |
| 4.6 | One-to-One mappings | 3 | 0 | CP-20 | Not started |
| 4.7 | Many-to-Many mappings | 4 | 0 | CP-21 | Not started |
| 4.8 | Mapping verification | 2 | 0 | CP-22 | Not started |
| 4.9 | JAR & WAR build and completion | 4 | 0 | CP-23 | Not started |

## 4. Counters

| Total steps | Completed | Remaining | Commits made |
|---|---|---|---|
| 196 | 26 | 170 | 0 / 23 |

Steps per week: W1 26/40 · W2 0/90 · W3 0/31 · W4 0/35
Commits per week: W1 0/5 · W2 0/8 · W3 0/4 · W4 0/6

## 5. Current and next steps

| | Step |
|---|---|
| Current | 1.8.3 — Create `PerformanceReview` entity |
| Next | 1.8.4 — Run & verify 2 more tables |
| After that | 1.9.1 — Enums: `TrainingStatus`, `EnrollmentStatus`, `AssetStatus`, `AssignmentStatus` |

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
| CP-01 | 1.1 | `docs(cp-01): add project documentation baseline including prd trd erd implementation plan explainer and project configuration skeleton files` | pending user commit | 2026-10-03 |
| CP-02 | 1.2, 1.3, 1.4 | `chore(cp-02): configure postgresql database connectivity and verify spring boot hikari connection pool initialization` | pending user commit | 2026-10-03 |
| CP-03 | 1.5, 1.6 | `feat(cp-03): add employee and recruitment entities` | | |
| CP-04 | 1.7, 1.8 | `feat(cp-04): add attendance leave payroll performance entities` | | |
| CP-05 | 1.9, 1.10 | `feat(cp-05): add training asset document offboarding entities` | | |
| CP-06 | 2.1, 2.2 | `feat(cp-06): add department designation employee apis` | | |
| CP-07 | 2.3 | `feat(cp-07): add recruitment apis` | | |
| CP-08 | 2.4 | `feat(cp-08): add onboarding and hire workflow` | | |
| CP-09 | 2.5 | `feat(cp-09): add attendance and leave apis` | | |
| CP-10 | 2.6, 2.7 | `feat(cp-10): add payroll and performance apis` | | |
| CP-11 | 2.8, 2.9 | `feat(cp-11): add training skill and asset apis` | | |
| CP-12 | 2.10, 2.11 | `feat(cp-12): add documents activation and offboarding apis` | | |
| CP-13 | 2.12 | `test(cp-13): complete week 2 api testing` | | |
| CP-14 | 3.1 | `feat(cp-14): add logging` | | |
| CP-15 | 3.2 | `feat(cp-15): add global exception handling` | | |
| CP-16 | 3.3 | `feat(cp-16): add request validation` | | |
| CP-17 | 3.4, 3.5 | `feat(cp-17): add swagger and complete week 3 testing` | | |
| CP-18 | 4.1, 4.2 | `feat(cp-18): map employee recruitment and onboarding` | | |
| CP-19 | 4.3, 4.4, 4.5 | `feat(cp-19): map attendance leave payroll training asset documents` | | |
| CP-20 | 4.6 | `feat(cp-20): add one-to-one mappings` | | |
| CP-21 | 4.7 | `feat(cp-21): add many-to-many mappings` | | |
| CP-22 | 4.8 | `test(cp-22): verify all mappings` | | |
| CP-23 | 4.9 | `chore(cp-23): build jar and war and complete project` | | |

## 9. Change log (Learning & Teaching Record)

Newest entry first. Copy the template for every step.

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
| | | | |