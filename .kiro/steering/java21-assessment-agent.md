---
inclusion: manual
---

# Java 21 Assessment Agent — Phase 1

## Role
Assess the current Java project state and produce a baseline report for the Java 7→21 migration.

## Execution Steps

### 1. Project Scan
- Read `Hospital_Servlet1/pom.xml` → extract current `<java.version>`
- Count all `.java` files by package (servlet/, dao/, controller/, entity/, helper/, config/)
- Identify Spring Boot version and dependencies

### 2. Legacy Pattern Inventory

Scan ALL Java files and catalog:

| Pattern | Detection | Count | Files |
|---------|-----------|-------|-------|
| Raw Servlets | `@WebServlet` or `extends HttpServlet` | ? | list |
| Manual JDBC | `DriverManager.getConnection` or `ConnectionHelper` | ? | list |
| Pre-diamond generics | `new ArrayList<Type>()` | ? | list |
| Missing try-with-resources | `finally { *.close() }` | ? | list |
| String concat for SQL | `"SELECT" + "FROM"` | ? | list |
| instanceof without pattern | `instanceof Type) { Type x = (Type)` | ? | list |
| Verbose POJOs | Classes with only getters/setters, no business logic | ? | list |
| Field @Autowired | `@Autowired private` | ? | list |
| Hardcoded config | Literal URLs, ports, credentials in code | ? | list |

### 3. SonarQube Baseline
- Connect to SonarQube MCP
- Fetch all issues for `Refactoring-legacy-Hospital-uc2`
- Summarize: total issues, by severity, by type

### 4. Build Baseline
- Run: `mvn clean compile -f Hospital_Servlet1/pom.xml`
- Document: passes/fails, warning count

### 5. Test Baseline
- Run: `mvn test -f Hospital_Servlet1/pom.xml`
- Document: total tests, passed, failed, skipped

## Output

Generate `Migration/00-Assessment-Report.md`:
```markdown
# Java 21 Migration Assessment Report

## Current State
- Java Version: [detected]
- Spring Boot Version: [detected]
- Total Java Files: [count]
- Total Test Files: [count]

## Legacy Pattern Inventory
[table from step 2]

## SonarQube Baseline
- Total Issues: [count]
- Blockers: [count], Critical: [count], Major: [count], Minor: [count]

## Build Status
- Compilation: [PASS/FAIL]
- Tests: [X passed, Y failed, Z skipped]

## Migration Effort Summary
- Files requiring Java 21 modernization: [count]
- Legacy servlets to remove: [count]
- Legacy DAOs to remove: [count]
- Services to create: [count]
```

## Completion
After generating the report, immediately proceed to Phase 2 (Build Configuration upgrade). Do NOT stop or ask the user.
