# Migration Plan — Hospital_Servlet1

## Project: Hospital Management System
## Target: Java 17+ with Spring Boot 3.x best practices
## Date: 2026-08-11

---

## Current State Analysis

The application is a hospital management system using Spring Boot 3.4.1 with:
- Mixed architecture: Spring MVC Controllers + legacy HttpServlet classes
- Direct JDBC with raw SQL (no JPA/Spring Data)
- Hardcoded credentials in application.properties
- DAOs instantiated with `new` (not Spring-managed) in controllers
- Static DataSource hack in ConnectionHelper for servlet backward compatibility
- No service layer — controllers call DAOs directly
- No async processing
- Plain POJO entities (no records, no validation)

---

## Modernization Tasks

### Task 1: Config Externalization
**Category:** Config Externalization
**Priority:** High
**Dependency:** None
**Status:** Completed

### Task 2: Modularization — Service Layer Introduction
**Category:** Modularization
**Priority:** High
**Dependency:** Task 1
**Status:** Completed

### Task 3: Modularization — DAO to Spring-Managed Repository Pattern
**Category:** Modularization
**Priority:** High
**Dependency:** Task 2
**Status:** Completed

### Task 4: Deprecated API Replacement — Entity Modernization
**Category:** Deprecated API Replacement
**Priority:** Medium
**Dependency:** Task 2
**Status:** Completed

### Task 5: Async/Reactive — Asynchronous Appointment Processing
**Category:** Async/Reactive
**Priority:** Medium
**Dependency:** Tasks 2, 3
**Status:** Completed

### Task 6: Deprecated API Replacement — Remove Legacy Servlets
**Category:** Deprecated API Replacement
**Priority:** Medium
**Dependency:** Tasks 2, 3
**Status:** Completed

---

## Execution Order

```
Task 1 (Config) ✓
    ↓
Task 2 (Service Layer) ✓
    ↓
Task 3 (DAO Repository Pattern) ✓
    ↓
Task 4 (Entity Modernization) ✓   Task 5 (Async) ✓   Task 6 (Remove Servlets) ✓
```

---

## Success Criteria

- [x] All builds pass (`mvn clean verify`)
- [x] No hardcoded credentials in source code
- [x] Service layer exists between controllers and DAOs
- [x] All Spring beans use constructor injection
- [x] Legacy servlet classes removed
- [x] Java 17+ features used (records, LocalDate)
- [x] Async configuration in place
- [x] Test coverage >= 80% on service layer
