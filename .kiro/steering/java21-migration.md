---
inclusion: auto
---

# Java 7 → Java 21 Migration — Full Automated Workflow

## When This Applies

This workflow executes automatically when the user says:
- "migrate java from 7 to java 21"
- "upgrade to Java 21"
- "java migration"
- "modernize java"

## Project Context

- **Application**: Hospital Management System (`Hospital_Servlet1/`)
- **Source**: Java 7 legacy patterns (raw Servlets, manual JDBC, no modern syntax)
- **Target**: Java 21 (LTS) + Spring Boot 3.4.x + Jakarta EE
- **Build**: Maven (`Hospital_Servlet1/pom.xml`)
- **Database**: Azure SQL Server (mssql-jdbc)

---

## PHASE 1: Assessment & Baseline

**Agent Role: Assessor**

1. Scan `Hospital_Servlet1/pom.xml` — identify current `<java.version>` property
2. Inventory all `.java` files — count servlets, DAOs, entities, controllers, repositories
3. Identify legacy patterns:
   - Raw `HttpServlet` classes (`@WebServlet`) in `servlet/` package
   - Manual JDBC (`ConnectionHelper.getConObj()`, `DriverManager.getConnection()`)
   - Pre-diamond-operator generics
   - Missing try-with-resources (manual `finally { conn.close() }`)
   - String concatenation for SQL instead of text blocks
   - `instanceof` without pattern matching
   - Verbose POJOs with manual getters/setters
4. Connect to SonarQube MCP → fetch issues for `Refactoring-legacy-Hospital-uc2`
5. Capture baseline metrics

**Output**: `Migration/00-Assessment-Report.md`

**Gate**: Assessment complete → proceed immediately to Phase 2.

---

## PHASE 2: Upgrade Build Configuration

**Agent Role: Build Migrator**

1. Update `Hospital_Servlet1/pom.xml`:
   ```xml
   <java.version>21</java.version>
   <maven.compiler.source>21</maven.compiler.source>
   <maven.compiler.target>21</maven.compiler.target>
   ```
   And `<release>21</release>` in maven-compiler-plugin.

2. Verify Spring Boot parent ≥ 3.4.x (supports Java 21)
3. Add missing dependencies:
   - `spring-boot-starter-actuator`
   - `spring-boot-starter-validation` (if missing)
4. Remove deprecated or incompatible dependencies
5. Run: `mvn clean compile -f Hospital_Servlet1/pom.xml`
6. Fix ALL compilation errors immediately

**Gate**: `mvn clean compile` succeeds with 0 errors → proceed to Phase 3.

---

## PHASE 3: Remove Legacy Code & Establish Architecture

**Agent Role: Refactoring Developer**

### 3.1 Remove Legacy Servlets
- Delete ALL files in `src/main/java/com/org/servlet/` (admin/, doctor/, user/)
- These are superseded by Spring MVC `@Controller` classes already in `controller/`

### 3.2 Remove Legacy DAOs
- Delete `AppointmentDao.java`, `DoctorDao.java`, `SpecialistDao.java`, `UserDao.java`
- Replaced by modern `*Repository.java` classes using Spring JdbcTemplate

### 3.3 Remove ConnectionHelper
- Delete `ConnectionHelper.java` (static bridge pattern)
- Spring DataSource injection handles all DB connectivity

### 3.4 Add Service Layer
Create service interfaces + implementations in `com.org.service/`:
```
├── DoctorService.java (interface)
├── DoctorServiceImpl.java (@Service, @Transactional)
├── UserService.java (interface)
├── UserServiceImpl.java
├── AppointmentService.java (interface)
├── AppointmentServiceImpl.java
├── SpecialistService.java (interface)
└── SpecialistServiceImpl.java
```
- Move business logic from controllers into services
- Services call repositories via constructor injection
- Controllers become thin — only HTTP concerns

### 3.5 Refactor Controllers
- Use constructor injection (remove field `@Autowired`)
- Delegate ALL logic to services
- Return proper HTTP status codes

**Gate**: `mvn clean compile -f Hospital_Servlet1/pom.xml` passes → proceed to Phase 4.

---

## PHASE 4: Apply Java 21 Language Features

**Agent Role: Java 21 Modernizer**

### 4.1 Records (Java 16+)
Convert DTOs and simple value objects:
```java
// BEFORE: public class DoctorDTO { private int id; private String name; /* getters/setters */ }
// AFTER:
public record DoctorDTO(int id, String name, String specialist, String email, String phone) {}
```

### 4.2 Pattern Matching for instanceof (Java 16+)
```java
// BEFORE: if (obj instanceof String) { String s = (String) obj; }
// AFTER:  if (obj instanceof String s) { /* use s directly */ }
```

### 4.3 Switch Expressions (Java 14+)
```java
String view = switch (role) {
    case "admin" -> "admin/dashboard";
    case "doctor" -> "doctor/dashboard";
    case "user" -> "user/dashboard";
    default -> "error/403";
};
```

### 4.4 Text Blocks (Java 15+)
```java
// Replace ALL string-concatenated SQL with text blocks:
String sql = """
    SELECT d.id, d.name, d.specialist, d.email, d.phone
    FROM doctor d
    WHERE d.specialist = ?
    ORDER BY d.name
    """;
```

### 4.5 Sealed Classes (Java 17+)
```java
public sealed interface UserRole permits AdminRole, DoctorRole, PatientRole {}
public record AdminRole(String adminId) implements UserRole {}
public record DoctorRole(int doctorId, String specialization) implements UserRole {}
public record PatientRole(int patientId) implements UserRole {}
```

### 4.6 Virtual Threads (Java 21)
```yaml
spring:
  threads:
    virtual:
      enabled: true
```
Spring Boot 3.2+ auto-uses virtual threads for request handling when enabled.

### 4.7 SequencedCollections (Java 21)
```java
var doctors = repository.findAll();
var first = doctors.getFirst();  // not .get(0)
var last = doctors.getLast();    // not .get(size-1)
```

### 4.8 Local Variable Type Inference (var)
```java
var doctors = repository.findAllDoctors();
var connection = dataSource.getConnection();
```

### 4.9 Try-with-resources (enforce everywhere)
```java
try (var conn = dataSource.getConnection();
     var stmt = conn.prepareStatement(sql);
     var rs = stmt.executeQuery()) {
    // process results
}
```

### 4.10 Collection Factories
```java
var specialties = List.of("Cardiology", "Neurology", "Orthopedics");
var doctorMap = Map.of("id", 1, "name", "Dr. Smith");
```

**Gate**: `mvn clean compile` passes → proceed to Phase 5.

---

## PHASE 5: Configuration Modernization

**Agent Role: Config Migrator**

### 5.1 Convert to YAML
- Replace `application.properties` with `application.yml`
- Create `application-dev.yml` and `application-prod.yml`

### 5.2 Externalize Secrets
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:sqlserver://localhost:1433;database=hospital}
    username: ${DB_USERNAME:localuser}
    password: ${DB_PASSWORD:localpass}
  threads:
    virtual:
      enabled: true
server:
  port: ${SERVER_PORT:8080}
```

### 5.3 Configuration Properties Classes
```java
@ConfigurationProperties(prefix = "hospital")
public record HospitalProperties(
    int sessionTimeoutMinutes,
    String uploadDirectory,
    int maxAppointmentsPerDay
) {}
```

**Gate**: Application must start: `mvn spring-boot:run -f Hospital_Servlet1/pom.xml`

---

## PHASE 6: Testing & Validation

**Agent Role: Validator**

1. Update existing tests to use Java 21 patterns (records, var, text blocks)
2. Add missing unit tests for new service layer classes
3. Run: `mvn test -f Hospital_Servlet1/pom.xml`
4. Run: `mvn clean verify -f Hospital_Servlet1/pom.xml`
5. Verify: 0 test failures, 0 build errors, no deprecated API warnings

**Gate**: Build succeeds + all tests pass → proceed to Phase 7.

---

## PHASE 7: SonarQube Scan & Final Report

**Agent Role: Quality Gate**

1. Connect to SonarQube MCP → run analysis
2. Fix any NEW issues introduced during migration
3. Verify quality gate: `get_project_quality_gate_status`
4. Generate final report

**Output**: `Migration/Java21-Migration-Summary.md`

---

## Completion Criteria (ALL must be TRUE)

- [ ] `pom.xml` targets Java 21 (`<java.version>21</java.version>`)
- [ ] `mvn clean verify` succeeds with 0 errors
- [ ] All unit tests pass (0 failures)
- [ ] Legacy servlets removed (no files in `servlet/` package)
- [ ] Legacy DAOs removed (only `*Repository.java` remain)
- [ ] `ConnectionHelper.java` removed
- [ ] Service layer exists between controllers and repositories
- [ ] Records used for DTOs/value objects
- [ ] Pattern matching applied (instanceof, switch expressions)
- [ ] Text blocks used for multi-line strings/SQL
- [ ] Virtual threads enabled in config
- [ ] Configuration externalized to YAML with profiles
- [ ] SonarQube quality gate passes (or documented as pending)
- [ ] `Migration/Java21-Migration-Summary.md` generated

---

## Error Recovery

- Build fails → fix errors immediately, re-run, continue
- Tests fail → determine if pre-existing or migration-caused, fix migration issues
- SonarQube unreachable → skip Phase 7, document as "pending manual scan"
- Never stop for non-critical issues — document and continue
