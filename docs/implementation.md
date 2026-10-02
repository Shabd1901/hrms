# implementation.md — PeopleOps Build Plan
 
Version 1.0 · Created 2026-10-02 · **196 steps · 36 phases · 23 commits · 4 weeks**
 
Step ID format: `week.phase.step` (for example `2.3.6`). A **step** is one small piece of work and one approval cycle. A **checkpoint** is the only moment you commit; there are 23 in total and each covers one or more phases. Rules for working through this file are in `workflow.md`. Progress is tracked in `explainer.md`, not here.
 
---
 
## How to read a step
 
| Step wording | Meaning |
|---|---|
| **Repo + DTOs** | Create `XRepository` (extends `JpaRepository<X, Long>`, no custom methods yet) and `XRequestDTO`, `XResponseDTO` |
| **GET + POST** | Create `XService` and `XController` with GET all (with filters from TRD §6), GET by id, and POST |
| **PUT + DELETE** | Add update and delete to the existing service and controller |
| **PATCH …** | Add the named PATCH endpoint(s) to the existing service and controller |
| **Postman test** | AI lists the requests for the phase (happy path plus the main rule failures). User runs them and reports. Results go in `explainer.md` |
| **Run & verify** | Start the app, check the tables in PostgreSQL |
 
Business rules (`BR-xx`) are in PRD.md §6. Endpoint paths are in TRD.md §6. Fields are in ERD.md.
 
**Gate:** `docs/complexity-benchmark.md` must be filled in before step **1.5.1**. If it still says PENDING, stop and tell the user.
 
---
 
## Commit checkpoints (23)
 
Commit only when a checkpoint is reached, never after every step.
 
| Checkpoint | Covers phases | Steps | Commit message |
|---|---|---|---|
| CP-01 | 1.1 | 3 | `docs(cp-01): add project documentation baseline` |
| CP-02 | 1.2, 1.3, 1.4 | 5 | `chore(cp-02): set up spring boot project and database connection` |
| CP-03 | 1.5, 1.6 | 11 | `feat(cp-03): add employee and recruitment entities` |
| CP-04 | 1.7, 1.8 | 10 | `feat(cp-04): add attendance leave payroll performance entities` |
| CP-05 | 1.9, 1.10 | 11 | `feat(cp-05): add training asset document offboarding entities` |
| CP-06 | 2.1, 2.2 | 14 | `feat(cp-06): add department designation employee apis` |
| CP-07 | 2.3 | 13 | `feat(cp-07): add recruitment apis` |
| CP-08 | 2.4 | 7 | `feat(cp-08): add onboarding and hire workflow` |
| CP-09 | 2.5 | 15 | `feat(cp-09): add attendance and leave apis` |
| CP-10 | 2.6, 2.7 | 10 | `feat(cp-10): add payroll and performance apis` |
| CP-11 | 2.8, 2.9 | 19 | `feat(cp-11): add training skill and asset apis` |
| CP-12 | 2.10, 2.11 | 11 | `feat(cp-12): add documents activation and offboarding apis` |
| CP-13 | 2.12 | 1 | `test(cp-13): complete week 2 api testing` |
| CP-14 | 3.1 | 7 | `feat(cp-14): add logging` |
| CP-15 | 3.2 | 9 | `feat(cp-15): add global exception handling` |
| CP-16 | 3.3 | 7 | `feat(cp-16): add request validation` |
| CP-17 | 3.4, 3.5 | 8 | `feat(cp-17): add swagger and complete week 3 testing` |
| CP-18 | 4.1, 4.2 | 11 | `feat(cp-18): map employee recruitment and onboarding` |
| CP-19 | 4.3, 4.4, 4.5 | 11 | `feat(cp-19): map attendance leave payroll training asset documents` |
| CP-20 | 4.6 | 3 | `feat(cp-20): add one-to-one mappings` |
| CP-21 | 4.7 | 4 | `feat(cp-21): add many-to-many mappings` |
| CP-22 | 4.8 | 2 | `test(cp-22): verify all mappings` |
| CP-23 | 4.9 | 4 | `chore(cp-23): build jar and complete project` |
 
Per week: Week 1 = 5 commits, Week 2 = 8, Week 3 = 4, Week 4 = 6.
 
---
 
# WEEK 1 — Requirements, ERD, Database, Setup, Entities (40 steps)
 
### Phase 1.1 — Requirements and documentation baseline (3)
| ID | Step |
|---|---|
| 1.1.1 | Create the repo folder and place `docs/` files |
| 1.1.2 | Add `.gitignore` (target, logs, IDE files) and a `README.md` skeleton |
| 1.1.3 | Requirement sign-off: user reviews PRD/ERD, changes are logged in `explainer.md` |
 
**Commit CP-01:** `docs(cp-01): add project documentation baseline`
 
### Phase 1.2 — PostgreSQL database creation (1)
| ID | Step |
|---|---|
| 1.2.1 | Create database `peopleops_db`, save the command in `docs/db-setup.sql`, confirm connection from psql or pgAdmin |
 
No commit here. Continues to CP-02 (phases 1.2, 1.3, 1.4).
 
### Phase 1.3 — Spring Boot project setup (2)
| ID | Step |
|---|---|
| 1.3.1 | Generate the Maven project from Spring Initializr (TRD §1 Week 1 dependencies, Java version recorded) |
| 1.3.2 | Confirm `mvn clean compile` passes and the main class is `PeopleOpsApplication` |
 
No commit here. Continues to CP-02 (phases 1.2, 1.3, 1.4).
 
### Phase 1.4 — Database connectivity (2)
| ID | Step |
|---|---|
| 1.4.1 | Configure `application.properties` (TRD §5) |
| 1.4.2 | Start the app and confirm it connects to `peopleops_db` |
 
**Commit CP-02:** `chore(cp-02): set up spring boot project and database connection`
 
### Phase 1.5 — Core entities: Department, Designation, Employee (5)
| ID | Step |
|---|---|
| 1.5.1 | Create package `enums` and `EmployeeStatus` |
| 1.5.2 | `Department` entity |
| 1.5.3 | `Designation` entity |
| 1.5.4 | `Employee` entity (`departmentId`, `designationId`, `managerId` as plain `Long`) |
| 1.5.5 | Run & verify: 3 tables created |
 
No commit here. Continues to CP-03 (phases 1.5, 1.6).
 
### Phase 1.6 — Recruitment and onboarding entities (6)
| ID | Step |
|---|---|
| 1.6.1 | Enums: `JobOpeningStatus`, `CandidateStage`, `InterviewResult`, `OnboardingTaskStatus` |
| 1.6.2 | `JobOpening` entity |
| 1.6.3 | `Candidate` entity (unique `jobOpeningId` + `email`) |
| 1.6.4 | `Interview` entity |
| 1.6.5 | `OnboardingTask` entity |
| 1.6.6 | Run & verify: 4 more tables |
 
**Commit CP-03:** `feat(cp-03): add employee and recruitment entities`
 
### Phase 1.7 — Attendance and leave entities (6)
| ID | Step |
|---|---|
| 1.7.1 | Enums: `AttendanceStatus`, `LeaveRequestStatus` |
| 1.7.2 | `Attendance` entity (table `attendance_records`, unique employee + date) |
| 1.7.3 | `LeaveType` entity |
| 1.7.4 | `LeaveBalance` entity (unique employee + type + year) |
| 1.7.5 | `LeaveRequest` entity |
| 1.7.6 | Run & verify: 4 more tables |
 
No commit here. Continues to CP-04 (phases 1.7, 1.8).
 
### Phase 1.8 — Payroll and performance entities (4)
| ID | Step |
|---|---|
| 1.8.1 | Enums: `PayrollStatus`, `ReviewStatus` |
| 1.8.2 | `Payroll` entity (unique employee + month + year) |
| 1.8.3 | `PerformanceReview` entity (unique employee + period) |
| 1.8.4 | Run & verify: 2 more tables |
 
**Commit CP-04:** `feat(cp-04): add attendance leave payroll performance entities`
 
### Phase 1.9 — Training and asset entities (7)
| ID | Step |
|---|---|
| 1.9.1 | Enums: `TrainingStatus`, `EnrollmentStatus`, `AssetStatus`, `AssignmentStatus` |
| 1.9.2 | `Skill` entity |
| 1.9.3 | `TrainingProgram` entity |
| 1.9.4 | `TrainingEnrollment` entity (unique program + employee) |
| 1.9.5 | `Asset` entity |
| 1.9.6 | `AssetAssignment` entity |
| 1.9.7 | Run & verify: 5 more tables |
 
No commit here. Continues to CP-05 (phases 1.9, 1.10).
 
### Phase 1.10 — Documents and offboarding entities, Week 1 wrap-up (4)
| ID | Step |
|---|---|
| 1.10.1 | Enums: `DocumentType`, `OffboardingStatus` |
| 1.10.2 | `EmployeeDocument` entity |
| 1.10.3 | `Offboarding` entity (unique employee) |
| 1.10.4 | Run & verify all 20 tables against ERD.md; Week 1 demo checklist in `explainer.md` |
 
**Commit CP-05:** `feat(cp-05): add training asset document offboarding entities`
 
---
 
# WEEK 2 — REST APIs (90 steps)
 
Errors use `ResponseStatusException` (TRD §8). No logging, validation annotations or Swagger yet.
 
### Phase 2.1 — Department and Designation APIs (8)
| ID | Step |
|---|---|
| 2.1.1 | Department: Repo + DTOs |
| 2.1.2 | Department: GET + POST (BR-14 unique name/code) |
| 2.1.3 | Department: PUT + DELETE |
| 2.1.4 | Department: PATCH active |
| 2.1.5 | Designation: Repo + DTOs |
| 2.1.6 | Designation: GET + POST |
| 2.1.7 | Designation: PUT + DELETE |
| 2.1.8 | Postman test |
 
No commit here. Continues to CP-06 (phases 2.1, 2.2).
 
### Phase 2.2 — Employee APIs (6)
| ID | Step |
|---|---|
| 2.2.1 | Employee: Repo + DTOs |
| 2.2.2 | Employee: GET + POST (BR-14: department, designation, manager exist, unique email, generated code `EMP-0001`, status `ONBOARDING`) |
| 2.2.3 | Employee: PUT + DELETE (BR-13: delete only while `ONBOARDING`) |
| 2.2.4 | Employee: PATCH transfer and promote (only for working employees) |
| 2.2.5 | Add in-use checks to Department and Designation delete (BR-13) |
| 2.2.6 | Postman test |
 
**Commit CP-06:** `feat(cp-06): add department designation employee apis`
 
### Phase 2.3 — Recruitment APIs (13)
| ID | Step |
|---|---|
| 2.3.1 | JobOpening: Repo + DTOs |
| 2.3.2 | JobOpening: GET + POST |
| 2.3.3 | JobOpening: PUT + DELETE |
| 2.3.4 | JobOpening: PATCH status |
| 2.3.5 | Candidate: Repo + DTOs |
| 2.3.6 | Candidate: GET + POST (opening must be `OPEN`, stage `APPLIED`, one application per email per opening) |
| 2.3.7 | Candidate: PUT + DELETE |
| 2.3.8 | Candidate: PATCH stage (forward moves only, `REJECTED` allowed from any open stage; `HIRED` only via hire) |
| 2.3.9 | Interview: Repo + DTOs |
| 2.3.10 | Interview: GET + POST (interviewer is a working employee; candidate at least `SHORTLISTED`) |
| 2.3.11 | Interview: PUT + DELETE |
| 2.3.12 | Interview: PATCH result (sets candidate `INTERVIEWED` when result is `PASSED`) |
| 2.3.13 | Postman test |
 
**Commit CP-07:** `feat(cp-07): add recruitment apis`
 
### Phase 2.4 — Onboarding and the hire workflow (7)
| ID | Step |
|---|---|
| 2.4.1 | OnboardingTask: Repo + DTOs |
| 2.4.2 | OnboardingTask: GET + POST (manual extra task) |
| 2.4.3 | OnboardingTask: PUT + DELETE |
| 2.4.4 | Auto-generate the 5 default tasks when an employee is created (BR-02); update employee delete to remove its tasks |
| 2.4.5 | Candidate: POST hire (BR-01, `HireRequestDTO`, `EmployeeService.createFromCandidate`) |
| 2.4.6 | OnboardingTask: PATCH complete (marks `DONE`, sets `completedOn`) |
| 2.4.7 | Postman test: apply → shortlist → interview → offer → hire → tasks exist |
 
**Commit CP-08:** `feat(cp-08): add onboarding and hire workflow`
 
### Phase 2.5 — Attendance and leave APIs (15)
| ID | Step |
|---|---|
| 2.5.1 | Attendance: Repo + DTOs |
| 2.5.2 | Attendance: GET + POST (BR-04) |
| 2.5.3 | Attendance: PUT + DELETE |
| 2.5.4 | Attendance: PATCH check-out (re-derives `PRESENT`/`HALF_DAY`) |
| 2.5.5 | LeaveType: Repo + DTOs |
| 2.5.6 | LeaveType: GET + POST |
| 2.5.7 | LeaveType: PUT + DELETE |
| 2.5.8 | LeaveBalance: Repo + DTOs |
| 2.5.9 | LeaveBalance: GET + POST |
| 2.5.10 | LeaveBalance: PUT + DELETE |
| 2.5.11 | LeaveRequest: Repo + DTOs |
| 2.5.12 | LeaveRequest: GET + POST (BR-05) |
| 2.5.13 | LeaveRequest: PUT + DELETE (only while `PENDING`) |
| 2.5.14 | LeaveRequest: PATCH approve, reject, cancel (BR-06) |
| 2.5.15 | Postman test |
 
**Commit CP-09:** `feat(cp-09): add attendance and leave apis`
 
### Phase 2.6 — Payroll APIs (5)
| ID | Step |
|---|---|
| 2.6.1 | Payroll: Repo + DTOs (including `PayrollGenerateRequestDTO`) |
| 2.6.2 | Payroll: GET |
| 2.6.3 | Payroll: POST generate (BR-07) |
| 2.6.4 | Payroll: PATCH pay + DELETE (only `GENERATED`) |
| 2.6.5 | Postman test: check the numbers by hand for one employee |
 
No commit here. Continues to CP-10 (phases 2.6, 2.7).
 
### Phase 2.7 — Performance APIs (5)
| ID | Step |
|---|---|
| 2.7.1 | PerformanceReview: Repo + DTOs |
| 2.7.2 | PerformanceReview: GET + POST (BR-08) |
| 2.7.3 | PerformanceReview: PUT + DELETE (only `DRAFT`) |
| 2.7.4 | PerformanceReview: PATCH submit |
| 2.7.5 | Postman test |
 
**Commit CP-10:** `feat(cp-10): add payroll and performance apis`
 
### Phase 2.8 — Training and skills APIs (11)
| ID | Step |
|---|---|
| 2.8.1 | Skill: Repo + DTOs |
| 2.8.2 | Skill: GET + POST |
| 2.8.3 | Skill: PUT + DELETE |
| 2.8.4 | TrainingProgram: Repo + DTOs |
| 2.8.5 | TrainingProgram: GET + POST |
| 2.8.6 | TrainingProgram: PUT + DELETE |
| 2.8.7 | TrainingProgram: PATCH status |
| 2.8.8 | TrainingEnrollment: Repo + DTOs |
| 2.8.9 | TrainingEnrollment: GET + POST (BR-09) |
| 2.8.10 | TrainingEnrollment: DELETE + PATCH complete and drop (no skill linking yet) |
| 2.8.11 | Postman test |
 
No commit here. Continues to CP-11 (phases 2.8, 2.9).
 
### Phase 2.9 — Asset APIs (8)
| ID | Step |
|---|---|
| 2.9.1 | Asset: Repo + DTOs |
| 2.9.2 | Asset: GET + POST |
| 2.9.3 | Asset: PUT + DELETE |
| 2.9.4 | Asset: PATCH retire (BR-10) |
| 2.9.5 | AssetAssignment: Repo + DTOs |
| 2.9.6 | AssetAssignment: GET + POST assign (BR-10) |
| 2.9.7 | AssetAssignment: PATCH return (BR-10) |
| 2.9.8 | Postman test |
 
**Commit CP-11:** `feat(cp-11): add training skill and asset apis`
 
### Phase 2.10 — Documents and employee activation (6)
| ID | Step |
|---|---|
| 2.10.1 | EmployeeDocument: Repo + DTOs |
| 2.10.2 | EmployeeDocument: GET + POST (BR-11) |
| 2.10.3 | EmployeeDocument: PUT + DELETE |
| 2.10.4 | EmployeeDocument: PATCH verify |
| 2.10.5 | `EmployeeService.activateIfReady`, called from onboarding complete and document verify (BR-03: status `ACTIVE`, leave balances created) |
| 2.10.6 | Postman test: full onboarding journey ends with an `ACTIVE` employee and leave balances |
 
No commit here. Continues to CP-12 (phases 2.10, 2.11).
 
### Phase 2.11 — Offboarding APIs (5)
| ID | Step |
|---|---|
| 2.11.1 | Offboarding: Repo + DTOs |
| 2.11.2 | Offboarding: GET + POST initiate (BR-12) |
| 2.11.3 | Offboarding: PUT + DELETE (only `INITIATED`, employee returns to `ACTIVE`) |
| 2.11.4 | Offboarding: PATCH complete (BR-12 clearance checks) |
| 2.11.5 | Postman test |
 
**Commit CP-12:** `feat(cp-12): add documents activation and offboarding apis`
 
### Phase 2.12 — Week 2 wrap-up (1)
| ID | Step |
|---|---|
| 2.12.1 | End-to-end Postman run (hire → exit), save the collection to `postman/`, update `explainer.md` |
 
**Commit CP-13:** `test(cp-13): complete week 2 api testing`
 
---
 
# WEEK 3 — Logging, Exceptions, Validation, Swagger, Testing (31 steps)
 
### Phase 3.1 — Logging (7)
| ID | Step |
|---|---|
| 3.1.1 | Log settings in `application.properties` (levels, file `logs/peopleops.log`) |
| 3.1.2 | Loggers in Department, Designation, Employee services |
| 3.1.3 | Loggers in Candidate, Interview, JobOpening, OnboardingTask services |
| 3.1.4 | Loggers in Attendance, LeaveType, LeaveBalance, LeaveRequest services |
| 3.1.5 | Loggers in Payroll and PerformanceReview services |
| 3.1.6 | Loggers in Skill, Training, Asset, Document, Offboarding services |
| 3.1.7 | Run a lifecycle flow and confirm the log file shows it |
 
**Commit CP-14:** `feat(cp-14): add logging`
 
### Phase 3.2 — Global exception handling (9)
| ID | Step |
|---|---|
| 3.2.1 | Custom exceptions: `ResourceNotFoundException`, `BusinessRuleException`, `DuplicateResourceException` |
| 3.2.2 | `ErrorResponse` DTO |
| 3.2.3 | `GlobalExceptionHandler` (TRD §8 table) |
| 3.2.4 | Replace `ResponseStatusException` in Department, Designation, Employee services |
| 3.2.5 | Replace in Recruitment and Onboarding services |
| 3.2.6 | Replace in Attendance and Leave services |
| 3.2.7 | Replace in Payroll and Performance services |
| 3.2.8 | Replace in Training, Asset, Document, Offboarding services |
| 3.2.9 | Postman test: 400, 404, 409 and bad enum / bad JSON all return the same JSON shape |
 
**Commit CP-15:** `feat(cp-15): add global exception handling`
 
### Phase 3.3 — Validation (7)
| ID | Step |
|---|---|
| 3.3.1 | Add validation dependency; handle `MethodArgumentNotValidException` in the handler |
| 3.3.2 | Annotations and `@Valid` for Department, Designation, Employee DTOs |
| 3.3.3 | Recruitment and Onboarding DTOs |
| 3.3.4 | Attendance and Leave DTOs |
| 3.3.5 | Payroll generate and Performance DTOs (rating 1–5, month 1–12) |
| 3.3.6 | Training, Asset, Document, Offboarding DTOs |
| 3.3.7 | Postman test: invalid input returns field errors |
 
**Commit CP-16:** `feat(cp-16): add request validation`
 
### Phase 3.4 — Swagger / OpenAPI (5)
| ID | Step |
|---|---|
| 3.4.1 | Add springdoc dependency, open `/swagger-ui.html` |
| 3.4.2 | `OpenApiConfig` (title, description, version) |
| 3.4.3 | `@Tag` on every controller |
| 3.4.4 | `@Operation` summaries on lifecycle action endpoints |
| 3.4.5 | Run a few calls from Swagger UI |
 
No commit here. Continues to CP-17 (phases 3.4, 3.5).
 
### Phase 3.5 — API testing and Week 3 wrap-up (3)
| ID | Step |
|---|---|
| 3.5.1 | Re-run the Week 2 collection, list and fix regressions |
| 3.5.2 | Add error and validation cases to the collection |
| 3.5.3 | Week 3 wrap-up in `explainer.md` |
 
**Commit CP-17:** `feat(cp-17): add swagger and complete week 3 testing`
 
---
 
# WEEK 4 — Mappings, JAR, Final Testing (35 steps)
 
Follow ERD.md §6. DTOs do not change except where stated. Each task replaces the plain `Long` field with the relationship and adjusts only the service lines that use it.
 
### Phase 4.1 — Employee core mappings (6)
| ID | Step |
|---|---|
| 4.1.1 | `Employee.department` many-to-one (R1) |
| 4.1.2 | `Employee.designation` many-to-one (R2) |
| 4.1.3 | `Employee.manager` self many-to-one (R3) |
| 4.1.4 | `Department.employees` one-to-many, `mappedBy` (R1) |
| 4.1.5 | Add `departmentName`, `designationTitle`, `managerName` to `EmployeeResponseDTO` |
| 4.1.6 | Test: Postman plus foreign keys visible in PostgreSQL |
 
No commit here. Continues to CP-18 (phases 4.1, 4.2).
 
### Phase 4.2 — Recruitment and onboarding mappings (5)
| ID | Step |
|---|---|
| 4.2.1 | `JobOpening` → Department, Designation (R4) |
| 4.2.2 | `Candidate.jobOpening` and `JobOpening.candidates` (R5) |
| 4.2.3 | `Interview` → Candidate, interviewer (R6) |
| 4.2.4 | `OnboardingTask.employee` and `Employee.onboardingTasks` with cascade and orphan removal (R7) |
| 4.2.5 | Test: hire workflow and employee delete |
 
**Commit CP-18:** `feat(cp-18): map employee recruitment and onboarding`
 
### Phase 4.3 — Attendance and leave mappings (4)
| ID | Step |
|---|---|
| 4.3.1 | `Attendance` → Employee (R8) |
| 4.3.2 | `LeaveBalance` → Employee, LeaveType (R9) |
| 4.3.3 | `LeaveRequest` → Employee, LeaveType, decidedBy (R10) |
| 4.3.4 | Test: leave request and approval flow |
 
No commit here. Continues to CP-19 (phases 4.3, 4.4, 4.5).
 
### Phase 4.4 — Payroll and performance mappings (3)
| ID | Step |
|---|---|
| 4.4.1 | `Payroll` → Employee (R11) |
| 4.4.2 | `PerformanceReview` → Employee, reviewer (R12) |
| 4.4.3 | Test |
 
No commit here. Continues to CP-19 (phases 4.3, 4.4, 4.5).
 
### Phase 4.5 — Training, asset and document mappings (4)
| ID | Step |
|---|---|
| 4.5.1 | `TrainingEnrollment` → TrainingProgram, Employee (R13) |
| 4.5.2 | `AssetAssignment` → Asset, Employee (R14) |
| 4.5.3 | `EmployeeDocument` → Employee (R15) |
| 4.5.4 | Test |
 
**Commit CP-19:** `feat(cp-19): map attendance leave payroll training asset documents`
 
### Phase 4.6 — One-to-One mappings (3)
| ID | Step |
|---|---|
| 4.6.1 | `Offboarding.employee` one-to-one (R16) |
| 4.6.2 | `Employee.candidate` one-to-one, set on hire (R17) |
| 4.6.3 | Test: offboarding and hire link |
 
**Commit CP-20:** `feat(cp-20): add one-to-one mappings`
 
### Phase 4.7 — Many-to-Many mappings (4)
| ID | Step |
|---|---|
| 4.7.1 | `Employee.skills` many-to-many and the employee skill endpoints (R18) |
| 4.7.2 | `TrainingProgram.skills` many-to-many and the program skill endpoints (R19) |
| 4.7.3 | Completing an enrolment adds the program's skills to the employee (BR-09 final) |
| 4.7.4 | Test |
 
**Commit CP-21:** `feat(cp-21): add many-to-many mappings`
 
### Phase 4.8 — Mapping verification (2)
| ID | Step |
|---|---|
| 4.8.1 | Check every foreign key and both join tables in PostgreSQL against ERD.md §6; deleting a parent that has children returns 409 |
| 4.8.2 | Full lifecycle regression in Postman; fix what fails |
 
**Commit CP-22:** `test(cp-22): verify all mappings`
 
### Phase 4.9 — JAR build and completion (4)
| ID | Step |
|---|---|
| 4.9.1 | `mvn clean package`, confirm the JAR is produced |
| 4.9.2 | Run with `java -jar`, check Swagger and one lifecycle flow |
| 4.9.3 | Final `README.md` (setup, run, endpoint overview, links to docs) |
| 4.9.4 | Final `explainer.md` review and viva notes |
 
**Commit CP-23:** `chore(cp-23): build jar and complete project`
 
---
 
## Totals
 
| Week | Phases | Steps | Commits |
|---|---|---|---|
| 1 | 10 | 40 | 5 |
| 2 | 12 | 90 | 8 |
| 3 | 5 | 31 | 4 |
| 4 | 9 | 35 | 6 |
| **Total** | **36** | **196** | **23** |