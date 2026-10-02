# explainer.md — PeopleOps Living Project Record

Personal scratch document. Updated by the AI after **every** step (rules: `workflow.md` §9). Written so each decision can be explained out loud.

Last updated: 2026-10-02 · Current step: **1.1.1** · Status: not started

---

## 1. Overall project progress

| Measure | Value |
|---|---|
| Semester | 1 (Backend) |
| Weeks complete | 0 / 4 |
| Phases complete | 0 / 36 |
| Steps complete | 0 / 196 |
| Commits (checkpoints) made | 0 / 23 |
| Overall | 0% |
| Complexity benchmark | PENDING (must be filled before step 1.5.1) |
| Java / Spring Boot / PostgreSQL versions | not recorded yet |

## 2. Weeks

| Week | Scope | Phases | Steps | Done | Commits | Status |
|---|---|---|---|---|---|---|
| 1 | Requirements, ERD, database, setup, entities | 10 | 40 | 0 | 0 / 5 | Not started |
| 2 | REST APIs | 12 | 90 | 0 | 0 / 8 | Not started |
| 3 | Logging, exceptions, validation, Swagger, API testing | 5 | 31 | 0 | 0 / 4 | Not started |
| 4 | Mappings, mapping tests, JAR, final testing | 9 | 35 | 0 | 0 / 6 | Not started |

## 3. Phases

| Phase | Name | Steps | Done | Checkpoint | Status |
|---|---|---|---|---|---|
| 1.1 | Requirements and documentation baseline | 3 | 0 | CP-01 | Not started |
| 1.2 | PostgreSQL database creation | 1 | 0 | CP-02 | Not started |
| 1.3 | Spring Boot project setup | 2 | 0 | CP-02 | Not started |
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
| 196 | 0 | 196 | 0 / 23 |

Steps per week: W1 0/40 · W2 0/90 · W3 0/31 · W4 0/35
Commits per week: W1 0/5 · W2 0/8 · W3 0/4 · W4 0/6

## 5. Current and next steps

| | Step |
|---|---|
| Current | 1.1.1 — Create the repo folder and place `docs/` files |
| Next | 1.1.2 — Add `.gitignore` and `README.md` skeleton |
| After that | 1.1.3 — Requirement sign-off |

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
| D-009 | Business rules in Week 2, field validation in Week 3 | Rules depend on other records (state); validation depends only on input shape | Mixing both in Week 2 |
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
| CP-01 | 1.1 | `docs(cp-01): add project documentation baseline` | | |
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

## 9. Change log

Newest entry first. Copy the template for every step.

```
### Step <id> — <step name>
- Date:
- What changed: 
- Why: 
- How it works: 
- Files: 
- Connects to: 
- Rules used: (BR-xx, decisions D-xxx)
- Check done: 
- Commit message: (only if this step completed a checkpoint)
- Viva note: one sentence I can say if asked about this
```

*(No entries yet.)*

## 10. Postman and test results

| Phase | Date | Result | Notes |
|---|---|---|---|
| | | | |