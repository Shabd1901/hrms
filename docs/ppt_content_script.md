# PeopleOps HRMS — Clean Backend Master PPT Script & Canva Prompt

> **How to use this:**
> Copy the prompt below into **ChatGPT** (with Canva integration) or directly into **Canva**.
> - **100% Backend Focus** (No frontend designs or web UI clutter).
> - **Warm Cream / Ivory Aesthetic** (Clean, elegant, non-dark, spacious).
> - **Project-Focused** (Explains what *you built in PeopleOps*, not abstract Java theory).
> - **Uncluttered & Light** (3–4 bullet points per slide; short, natural 2-sentence speech script for you).

---

```markdown
# MASTER PROMPT FOR CHATGPT + CANVA: Backend Engineering Presentation

**Role:** Expert Presentation Designer.
**Task:** Create a clean, elegant, uncluttered 7-slide presentation deck for a college project viva on a **Backend REST API Project**: "PeopleOps: Enterprise HRMS Backend".
**Author:** Shabdansh Prajapati (Roll No. 44 / IT-B-44)
**Project Guide:** Mrs. Monisha Linkesh (Assistant Professor, IT Dept, TCET)
**Institute:** Department of Information Technology, Thakur College of Engineering & Technology (TCET), Mumbai (A.Y. 2024–2025)

---

## Visual & Theme Styling (Warm Cream & Minimalist)
- **Aesthetic:** Minimalist, airy, modern, clean academic presentation. Not dark. Plenty of whitespace.
- **Color Palette:**
  - Background: Warm Cream / Soft Alabaster (`#FBF9F5` or `#F7F4EE`)
  - Content Cards: Pure White (`#FFFFFF`) with subtle warm border (`#EBE6DF`)
  - Main Headings & Text: Deep Charcoal Slate (`#22252A`)
  - Accent Color: Warm Terracotta / Amber Ochre (`#C26D45`) or Muted Forest Teal (`#3D7068`)
  - Subtext: Soft Muted Slate (`#5E6570`)
- **Typography:**
  - Headers: Clean, friendly Sans-Serif (`Plus Jakarta Sans`, `Inter`, or `Outfit`), Bold
  - Body: Readable Sans-Serif (`Inter` or `Roboto`), 14pt–16pt
- **Layout Rule:** Max 3 to 4 bullets per card. Clean spacing. No text clutter. Backend only.

---

## Slide-by-Slide Deck Content

---

### SLIDE 1: Title Slide (Clean & Warm)
- **Top Pill Tag:** SEMESTER 1 — FULL STACK DEVELOPMENT (BACKEND)
- **Main Title:** PeopleOps: Enterprise HRMS
- **Subtitle:** A 4-Tier Backend REST API Built with Spring Boot & PostgreSQL
- **Student Details:** Shabdansh Prajapati | Roll No. 44 (IT-B-44)
- **Project Guide:** Mrs. Monisha Linkesh (Assistant Professor, IT Department)
- **College:** Thakur College of Engineering & Technology (TCET), Mumbai
- **Design:** Centered or clean left-aligned warm cream layout with TCET crest.
- **Speech Script for You (What to say):**
  > "Good morning respected examiners. I am Shabdansh Prajapati, Roll No. 44. Today I am presenting PeopleOps, an enterprise Human Resource Management backend system that I built using Spring Boot and PostgreSQL. It manages all workforce operations through 123 tested REST API endpoints across 7 core modules."

---

### SLIDE 2: What is PeopleOps? (The 7 Modules I Built)
- **Slide Title:** Project Overview & Modules Built
- **Subtitle:** Comprehensive Backend System for Organizational Workforce Management
- **Content Layout (2 Warm Cards):**
  - **Card 1: Core Workforce Subsystems**
    * 🏢 **Organization & Core HR:** Departments, designations, employee directory, and document metadata.
    * ⏱️ **Attendance & Leaves:** Daily attendance logging, annual leave quotas, and request workflows.
    * 🤝 **Recruitment & Hiring:** Job openings, applicant screening, interview rounds, and onboarding tasks.
  - **Card 2: Operational & Governance Subsystems**
    * 💰 **Payroll Management:** Monthly compensation processing, Loss-of-Pay (LOP) calculations, and net pay generation.
    * 📈 **Performance & Appraisals:** Periodic ratings (1 to 5 stars) and managerial feedback reviews.
    * 🎓 **Skills & Training:** Master skill catalog, training courses, and employee enrollments.
    * 🛡️ **Asset Management:** Company hardware inventory and employee allocation tracking.
- **Speech Script for You:**
  > "PeopleOps handles the full lifecycle of an employee. Rather than building a basic two-table project, I developed 7 functional modules covering everything from job openings and interviews, to daily attendance, leave approvals, payroll generation, and hardware asset assignments."

---

### SLIDE 3: System Architecture (How the Backend Works)
- **Slide Title:** 4-Tier Backend Architecture
- **Subtitle:** Strict Separation of Concerns from Request to Database
- **Content Layout (Horizontal Flow Cards):**
  1. **REST Controllers (`/api/...`)**
     * Accepts incoming HTTP requests (GET, POST, PUT, DELETE).
     * Validates input data automatically using Jakarta Bean Validation.
  2. **Service Layer (`@Service`)**
     * Contains all the actual business logic and calculations.
     * Manages `@Transactional` boundaries so multi-step workflows stay safe.
  3. **Repository Layer (`JpaRepository`)**
     * Talks to the database using Spring Data JPA without boilerplate SQL.
  4. **PostgreSQL Database**
     * Stores relational data reliably with foreign keys and unique constraints.
- **Tech Stack Badges:** `Java 17` | `Spring Boot 3.2.5` | `Spring Data JPA` | `PostgreSQL 15+` | `Maven`
- **Speech Script for You:**
  > "I structured the backend using a clean 4-tier architecture. Controllers only handle incoming HTTP requests and validation. All business rules live inside the Service layer under transactions, and Spring Data JPA communicates directly with PostgreSQL. This keeps the code modular, maintainable, and clean."

---

### SLIDE 4: Entities & Relational Database Design
- **Slide Title:** Domain Entities & Relational Design
- **Subtitle:** 20 Domain Entities Structured in PostgreSQL
- **Content Layout (3 Feature Blocks):**
  - **Block 1: Primary Entities Used**
    * `Employee`: Central pivot entity linked to Department, Designation, and Manager.
    * `LeaveRequest` & `LeaveBalance`: Tracks requested leave days and remaining quotas.
    * `Candidate` & `JobOpening`: Manages recruitment stages from Applied to Hired.
    * `Payroll`: Generates net salary by factoring in attendance and deductions.
    * `Asset` & `AssetAssignment`: Tracks company laptops and handover status.
  - **Block 2: Relational Mappings Implemented**
    * **One-to-One (1:1):** Employee ↔ Offboarding clearance record.
    * **One-to-Many (1:N):** Department → Employees; Employee → Attendances, Leaves, Payrolls.
    * **Many-to-Many (N:M):** Employee ↔ Skills (via `employee_skills` join table).
  - **Block 3: Data Safety**
    * Enforced composite unique keys, foreign key constraints, and exact `BigDecimal` types for money.
- **Speech Script for You:**
  > "I designed 20 entities mapped to normalized tables in PostgreSQL. The Employee entity acts as the central pivot. I implemented all three core relationship types: One-to-One for offboarding, One-to-Many for departments and leaves, and Many-to-Many with dedicated join tables for employee skills."

---

### SLIDE 5: Entity-Relationship Diagram (Visual Slide)
- **Slide Title:** Entity Relationship Diagram
- **Subtitle:** Visual Representation of the 20 Entities and Table Associations
- **Center Area:** [Embed High-Resolution Diagram: `peopleops_erd.png`]
- **Key Summary Badges (Under Diagram):**
  * `20 Relational Tables`
  * `19 Relational Associations`
  * `2 Join Tables (employee_skills, training_program_skills)`
  * `15 Domain Enums for Status Management`
- **Speech Script for You:**
  > "This is the complete Entity-Relationship diagram for PeopleOps. It visually shows how the 20 entities connect across all 7 modules. All foreign keys enforce referential integrity with lazy loading to ensure high query performance."

---

### SLIDE 6: Key Business Workflows Built
- **Slide Title:** Key Business Workflows Built
- **Subtitle:** Intelligent Automation Beyond Simple CRUD Operations
- **Content Layout (3 Workflow Cards):**
  - **1. Leave Approval & Auto-Deduction**
    * Employee submits leave request with dates.
    * System validates available quota in `leave_balances`.
    * When manager approves, status updates to `APPROVED` and remaining balance decrements automatically in one transaction.
  - **2. Candidate-to-Employee Automated Onboarding**
    * Candidate moves through: Applied → Shortlisted → Interviewed → Hired.
    * Marking a candidate `HIRED` automatically creates the `Employee` record, decrements job openings, and generates 5 default onboarding tasks.
  - **3. Automated Monthly Payroll Calculation**
    * Reads employee base pay and allowances.
    * Automatically calculates Loss of Pay (LOP) days from attendance records.
    * Computes exact Net Salary: `(Gross Pay - LOP - Deductions)`.
- **Speech Script for You:**
  > "The backend does much more than basic CRUD. I built automated business workflows. For instance, when a manager approves a leave, the system automatically checks and deducts the employee's balance in an atomic transaction. Similarly, hiring a candidate automatically provisions an employee record and onboarding tasks."

---

### SLIDE 7: Backend Deliverables & Verification
- **Slide Title:** Deliverables & Practical Verification
- **Subtitle:** 100% Operational Backend Ready for Live Demonstration
- **Content Layout (4 Verification Points):**
  * 📡 **123 Tested REST Endpoints:** Complete coverage across 20 controllers with standard HTTP verbs and status codes (200, 201, 400, 404).
  * 📖 **Interactive Swagger UI:** Live API exploration and in-browser testing enabled via SpringDoc OpenAPI at `/swagger-ui.html`.
  * 📋 **Postman Test Collection:** Pre-built test collection to demonstrate business flows in sequence.
  * 📝 **SLF4J + Logback File Logging:** Real-time operational logs recorded to `logs/peopleops.log`.
  * 📦 **Production Packaging:** Built both executable JAR and servlet WAR (`peopleops-0.0.1-SNAPSHOT.war`, ~48 MB).
- **Speech Script for You:**
  > "To conclude, all 123 endpoints have been tested and verified with PostgreSQL. I can demonstrate live API calls right now using Swagger UI or Postman, show the real-time logs in `logs/peopleops.log`, and verify the database rows. Thank you, respected examiners. I am ready for the live demonstration and questions."
```
