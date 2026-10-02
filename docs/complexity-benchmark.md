# complexity-benchmark.md

**Status: PENDING** — to be filled from the reference Java FSD project.

This file defines the complexity boundary for PeopleOps.

The reference project is a benchmark, not a template. Do not copy its domain, structure, naming, or implementation.

The AI must use this file to keep PeopleOps at a comparable level of technical and code complexity.

**IMPORTANT:**
- Do not modify this file unless explicitly instructed by the user.
- Do not artificially increase complexity to reach a number.
- Prefer the simplest implementation that satisfies the required functionality.
- Complexity should come primarily from the domain, relationships, workflows, and business rules.

---

## Reference Project

### Project Scope
- Name / domain:
- Major modules:
- Overall complexity:
- Main workflows:

### Architecture
- Architecture pattern:
- Number of entities:
- Number of controllers:
- Number of services / facades:
- Number of repositories:
- DTO approach:
- Other important architectural components:

### API
- Number of endpoints:
- CRUD vs business/workflow endpoints:
- Validation approach:
- Exception handling:
- Swagger / OpenAPI:

### Database
- Number of tables / entities:
- Relationships used (1-1, 1-N, N-N):
- Approx. relationship count:
- Enums / constraints / other notable features:

### Business Logic
- Business rules beyond CRUD:
- State/status workflows:
- Cross-entity operations:
- Calculations / derived values:
- Most complex modules:

### Code Style / Human Complexity
- Lombok used?:
- Approx. lines per service class:
- Typical method size:
- Interfaces used?:
- Design patterns used?:
- Generic / reflection-based logic?:
- DTO mappers used?:
- Notably simple implementations:
- Notably complex implementations:
- Anything that should NOT be replicated:

---

## PeopleOps Complexity Boundary

To be defined after analysing the reference project.

### Target Scope
- Major modules:
- Overall target complexity:
- Main workflows:

### Target Architecture
- Entities:
- Controllers:
- Services:
- Repositories:
- DTO approach:
- Other components:

### Target API
- Endpoint range:
- CRUD vs workflow operations:
- Validation:
- Exception handling:
- Swagger / OpenAPI:

### Target Database
- Entity range:
- Relationship range:
- Relationship types:
- Other constraints/features:

### Target Business Logic
- Required business workflows:
- Required business rules:
- Acceptable complexity:

### Target Code Style
- Expected method/class complexity:
- Abstractions allowed:
- Abstractions to avoid:
- Design patterns required:
- Design patterns to avoid:
- General implementation principle:

---

## Complexity Rules

1. Match the **level of complexity**, not the exact numbers.
2. Do not add entities, endpoints, classes, abstractions, or patterns only to increase project size.
3. Prefer readable and explainable code over clever code.
4. Every class and abstraction must have a clear purpose.
5. Business complexity is preferred over technical complexity.
6. The implementation must remain realistic for a student who is expected to explain every file during evaluation.