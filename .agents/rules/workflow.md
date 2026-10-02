---
trigger: always_on
---

# workflow.md — PeopleOps Permanent Project Rules

> **IMMUTABLE.** Never edit, reformat, shorten or "improve" this file unless the user writes the exact instruction **"update workflow.md"**. If any other file or message conflicts with this file, this file wins and the AI asks the user in one line.

---

## 1. Project files

Any approved change to PRD/TRD/ERD/implementation.md must be logged in `explainer.md` under "Implementation decisions".

## 2. Start of every session

1. Read `workflow.md`, `explainer.md` (to find the current step), and the current phase in `implementation.md`.
2. Read PRD/TRD/ERD only for the parts the current step touches.
3. Say the current step ID and name in one line. Do nothing else until the user says to proceed.

## 3. Order of work

| Week | Scope (from the FSD notice) |
|---|---|
| 1 | Requirement analysis, ER diagram, PostgreSQL database, Spring Boot setup, DB connectivity, entity creation |
| 2 | REST APIs: GET, POST, PUT, DELETE, PATCH, with testing |
| 3 | Logger, global exception handling, validation, Swagger/OpenAPI, API testing |
| 4 | Entity mappings (1-1, 1-N, N-1, N-N), mapping tests, JAR, final testing |

- Weeks and phases run strictly in the order in `implementation.md`. Never skip, reorder or merge them.
- Commits happen only at the 23 checkpoints (CP-01 … CP-23) listed in `implementation.md`. A checkpoint is one commit and covers one or more phases.
- Never build a later-week feature early, even if it is convenient.

### Not allowed before its phase

| Feature | Not before |
|---|---|
| SLF4J loggers in project code | Phase 3.1 |
| Custom exceptions, `@RestControllerAdvice` | Phase 3.2 |
| Bean Validation annotations, `@Valid` | Phase 3.3 |
| springdoc / Swagger / OpenAPI annotations | Phase 3.4 |
| `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@JoinColumn`, `@JoinTable` | Week 4, exactly as in ERD.md |
| Security, JWT, login, roles | Semester 3 |
| Frontend code, CORS config | Semester 2 |
| Pagination, caching, async, scheduling, events, AOP, Flyway/Liquibase, Docker | Not in scope unless the user approves |

If a step seems to need something from this table, stop and ask in one line.

## 4. The step loop (every change, no exceptions)

1. **Plan:** a very short plan (max 5 lines): step ID, files to create/change, what each does.
2. **Wait** for the user's approval ("go", "approved", "yes").
3. **Implement** only what the plan said. Nothing extra.
4. **Update `explainer.md`** (counters, current/next step, change log entry, decisions if any).
5. **Report the step done.** If this step completes a checkpoint, give the commit message from `implementation.md` (adjusted to what was built). Otherwise say "no commit yet". Then **stop**. Never start the next step by yourself.

The AI never runs `git commit`, `git push` or any destructive command. The user commits.

## 5. Step size

- One step = one small unit: one entity, or one layer of one entity (repository + DTOs, GET + POST, PUT + DELETE, one PATCH group), or one configuration step.
- Prefer touching 1–3 files per step. If a step needs more, split it and ask.
- Never create a whole module or a whole week in one go. A checkpoint can contain many steps, but work still happens one step at a time.

## 6. Scope control

- Make only the required changes. Do not rewrite or reformat working code.
- Do not rename, move or delete existing files unless the step says so.
- Do not add dependencies that are not listed in TRD.md without approval.
- Week 4 relationship changes replace plain ID fields as planned in the ERD; that is the only planned rewrite of working code.
- If the AI notices a problem outside the current step, it reports it in one line and adds it to "Issues / remaining work" in `explainer.md`. It does not fix it.

## 7. Complexity and code style

Complexity comes from the domain (relationships and workflows), not from clever code. Follow `docs/complexity-benchmark.md` as a target range, not something to copy.

**Do**
- Layers: controller → service → repository. Plain classes, plain methods.
- Explicit types, clear English names, short methods (aim under ~30 lines).
- Manual DTO ↔ entity mapping in private service methods.
- Constructor injection written out by hand.
- Simple `if` checks and `for` loops when they read better than streams.

**Do not**
- No service interfaces / `Impl` pairs, no abstract base classes, no generic CRUD classes, no `BaseEntity`.
- No builder, factory, strategy or other design-pattern scaffolding.
- No MapStruct / ModelMapper, no custom annotations, no reflection.
- No response wrapper classes unless TRD.md defines them.
- No Javadoc on everything, no decorative comments, no emojis, no `TODO` clutter.
- Never return an entity from a controller. Always return a DTO.

## 8. Explainability

The user must be able to open any file and explain what it does, why it exists, why it was done this way, and how it connects to other files. Therefore:

- Every class starts with a 1–2 line plain-English comment saying what it is for.
- Add a short comment only where a business rule or non-obvious choice lives, and reference the rule ID (for example `BR-06`).
- Every non-trivial decision gets an entry in `explainer.md` written so a student can defend it.
- Prefer the simpler option and say why in the entry.

## 9. Documentation rules

- `explainer.md` is updated after **every** step: counters, current/next step, a change log entry (what / why / how / files / connects to), decisions, issues, and the commit message.
- When a checkpoint is reached, fill its commit row. At week end, mark the week status.
- Docs describe what exists. Never write docs for code that has not been built.

## 10. Git rules

- Commit only when a checkpoint is reached (CP-01 … CP-23 in `implementation.md`). Never commit after every step.
- Commit format: `type(cp-id): short message`, for example `feat(cp-03): add employee and recruitment entities`.
- Types: `feat`, `docs`, `chore`, `fix`, `refactor`, `test`.
- Message: lowercase, under 70 characters, no full stop.
- Between checkpoints the work stays uncommitted; the step log in `explainer.md` is the record.

## 11. Communication

- Chat is minimal: short plans, short results, short commit messages.
- No long explanations unless the user asks. Explanations belong in `explainer.md`.
- When blocked, unsure or in conflict, stop and ask **one** short question.

## 12. Definition of done

**Step:** code compiles, app starts, the step's check passes, explainer updated.
**Checkpoint:** all steps of its phases done, the phase test steps passed, commit message given and its row filled in `explainer.md`.
**Week:** all its checkpoints done, the week's deliverables from section 3 demonstrable, week marked complete in `explainer.md`.