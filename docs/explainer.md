# explainer.md — PeopleOps Living Project Record

Personal scratch document. Updated by the AI after **every** step (rules: `workflow.md` §9). Written so each decision can be explained out loud.

Last updated: 2026-10-03 · Current step: **1.3.1** · Status: in progress

---

## 1. Overall project progress

| Measure | Value |
|---|---|
| Semester | 1 (Backend) |
| Weeks complete | 0 / 4 |
| Phases complete | 2 / 36 |
| Steps complete | 4 / 196 |
| Commits (checkpoints) made | 0 / 23 (CP-01 ready) |
| Overall | 2.0% |
| Complexity benchmark | PENDING (must be filled before step 1.5.1) |
| Java / Spring Boot / PostgreSQL versions | not recorded yet |

## 2. Weeks

| Week | Scope | Phases | Steps | Done | Commits | Status |
|---|---|---|---|---|---|---|
| 1 | Requirements, ERD, database, setup, entities | 10 | 40 | 4 | 0 / 5 | In progress |
| 2 | REST APIs | 12 | 90 | 0 | 0 / 8 | Not started |
| 3 | Logging, exceptions, validation, Swagger, API testing | 5 | 31 | 0 | 0 / 4 | Not started |
| 4 | Mappings, mapping tests, JAR, final testing | 9 | 35 | 0 | 0 / 6 | Not started |

## 3. Phases

| Phase | Name | Steps | Done | Checkpoint | Status |
|---|---|---|---|---|---|
| 1.1 | Requirements and documentation baseline | 3 | 3 | CP-01 | Completed |
| 1.2 | PostgreSQL database creation | 1 | 1 | CP-02 | Completed |
| 1.3 | Spring Boot project setup | 2 | 0 | CP-02 | In progress |
| 1.4 | Database connectivity | 2 | 0 | CP-02 | Not started |
| 1.5 | Core entities: Department, Designation, Employee | 5 | 0 | CP-03 | Not started |
| 1.6 | Recruitment and onboarding entities | 6 | 0 | CP-03 | Not started |
| 1.7 | Attendance and leave entities | 6 | 0 | CP-04 | Not started |
| 1.8 | Payroll and performance entities | 4 | 0 | CP-04 | Not started |
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
| 4.9 | JAR build and completion | 4 | 0 | CP-23 | Not started |

## 4. Counters

| Total steps | Completed | Remaining | Commits made |
|---|---|---|---|
| 196 | 4 | 192 | 0 / 23 |

Steps per week: W1 4/40 · W2 0/90 · W3 0/31 · W4 0/35
Commits per week: W1 0/5 · W2 0/8 · W3 0/4 · W4 0/6

## 5. Current and next steps

| | Step |
|---|---|
| Current | 1.3.1 — Generate Maven project from Spring Initializr |
| Next | 1.3.2 — Confirm `mvn clean compile` passes |
| After that | 1.4.1 — Configure `application.properties` |

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

## 8. Commit checkpoints

Commit only at a checkpoint. Fill the hash and date when you commit.

| Checkpoint | Covers phases | Planned commit message | Commit hash | Date |
|---|---|---|---|---|
| CP-01 | 1.1 | `docs(cp-01): add project documentation baseline including prd trd erd implementation plan explainer and project configuration skeleton files` | pending user commit | 2026-10-03 |
| CP-02 | 1.2, 1.3, 1.4 | `chore(cp-02): set up spring boot project and database connection` | | |
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
| CP-23 | 4.9 | `chore(cp-23): build jar and complete project` | | |

## 9. Change log (Learning & Teaching Record)

Newest entry first. Copy the template for every step.

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