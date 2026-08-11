---
inclusion: manual
---

# Java 21 Refactoring Agent — Phases 3 & 4

## Role
Execute the structural refactoring (Phase 3) and Java 21 feature application (Phase 4) of the migration.

## Execution Mode
- Fully autonomous — no stopping between sub-steps
- Fix build errors immediately after each change
- Verify compilation after every major refactoring step

---

## Phase 3: Remove Legacy & Add Architecture

### Step 3.1: Remove Legacy Servlets
```
DELETE: src/main/java/com/org/servlet/admin/*.java
DELETE: src/main/java/com/org/servlet/doctor/*.java
DELETE: src/main/java/com/org/servlet/user/*.java
DELETE: src/main/java/com/org/servlet/ (entire directory)
```
Reason: Spring MVC controllers in `controller/` package already handle all routes.

### Step 3.2: Remove Legacy DAOs
```
DELETE: src/main/java/com/org/dao/AppointmentDao.java
DELETE: src/main/java/com/org/dao/DoctorDao.java
DELETE: src/main/java/com/org/dao/SpecialistDao.java
DELETE: src/main/java/com/org/dao/UserDao.java
```
Reason: `*Repository.java` classes with JdbcTemplate already replace these.

### Step 3.3: Remove ConnectionHelper
```
DELETE: src/main/java/com/org/helper/ConnectionHelper.java
```
Reason: Spring DataSource injection handles connectivity.

### Step 3.4: Create Service Layer

For each domain entity, create:
1. Service interface in `com.org.service/`
2. Service implementation with `@Service` and `@Transactional`

Pattern:
```java
public interface DoctorService {
    List<Doctor> findAll();
    Optional<Doctor> findById(int id);
    List<Doctor> findBySpecialist(String specialist);
    Doctor save(Doctor doctor);
    void deleteById(int id);
}

@Service
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository repository;

    public DoctorServiceImpl(DoctorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Doctor> findAll() {
        return repository.findAllDoctors();
    }

    @Override
    @Transactional
    public Doctor save(Doctor doctor) {
        return repository.save(doctor);
    }
    // ... other methods
}
```

### Step 3.5: Refactor Controllers
- Replace field `@Autowired` with constructor injection
- Replace direct repository calls with service calls
- Keep controllers thin — HTTP concerns only

**Verify**: `mvn clean compile -f Hospital_Servlet1/pom.xml`

---

## Phase 4: Apply Java 21 Features

### Step 4.1: Records
- Convert entity DTOs to records
- Convert request/response objects to records
- Keep JPA entities as classes (records can't be entities)

### Step 4.2: Pattern Matching
- Find all `instanceof` → add pattern variable
- Find all traditional `switch` → convert to switch expressions

### Step 4.3: Text Blocks
- Find all multi-line String concatenation (especially SQL) → text blocks
- Find all SQL in repository classes → text blocks

### Step 4.4: Modern Syntax
- Add `var` for local variables with obvious types
- Replace `new ArrayList<>()` with `List.of()` where immutable
- Add `getFirst()`/`getLast()` where `get(0)`/`get(size-1)` used
- Ensure all resource handling uses try-with-resources

### Step 4.5: Virtual Threads
- Add to application config:
  ```yaml
  spring:
    threads:
      virtual:
        enabled: true
  ```

### Step 4.6: Sealed Classes (where applicable)
- If domain has type hierarchies (roles, statuses), use sealed interfaces

**Verify**: `mvn clean compile -f Hospital_Servlet1/pom.xml`

---

## Completion
After both phases complete and build passes, immediately proceed to Phase 5 (Configuration). Do NOT stop.
