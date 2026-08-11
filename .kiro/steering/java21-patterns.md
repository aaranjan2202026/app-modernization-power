---
inclusion: fileMatch
fileMatchPattern: "**/*.java, **/pom.xml"
---

# Java 21 Migration Patterns Reference

## Code Transformation Quick Reference

When modifying Java files during migration, apply these patterns:

### Records (replace verbose POJOs)
```java
// Replace classes with only data (DTOs, value objects)
public record DoctorDTO(int id, String name, String specialist, String email, String phone) {}
public record AppointmentDTO(int id, int userId, int doctorId, LocalDate date, String status) {}
```

### Pattern Matching instanceof
```java
// Every instanceof MUST use pattern matching
if (obj instanceof String s) { /* use s */ }
if (exception instanceof SQLException sqle) { logger.error("DB error: {}", sqle.getMessage()); }
```

### Switch Expressions
```java
// Replace if-else chains with switch expressions
return switch (userRole) {
    case "admin" -> handleAdmin(request);
    case "doctor" -> handleDoctor(request);
    case "patient" -> handlePatient(request);
    default -> ResponseEntity.status(403).build();
};
```

### Text Blocks (for SQL, HTML, JSON)
```java
String sql = """
    SELECT d.id, d.name, d.specialist
    FROM doctor d
    WHERE d.specialist = ?
    ORDER BY d.name ASC
    """;
```

### Virtual Threads (Java 21)
```yaml
# In application.yml — Spring Boot auto-uses virtual threads
spring:
  threads:
    virtual:
      enabled: true
```

### SequencedCollections (Java 21)
```java
list.getFirst()   // instead of list.get(0)
list.getLast()    // instead of list.get(list.size() - 1)
list.reversed()  // reversed view
```

### var for Local Variables
```java
var doctors = repository.findAllDoctors();
var response = restTemplate.getForEntity(url, String.class);
```

### Try-with-resources (enforce everywhere)
```java
try (var conn = dataSource.getConnection();
     var stmt = conn.prepareStatement(sql)) {
    stmt.setString(1, parameter);
    try (var rs = stmt.executeQuery()) {
        // process
    }
}
```

### Collection Factories (immutable)
```java
var roles = List.of("ADMIN", "DOCTOR", "PATIENT");
var config = Map.of("timeout", "5000", "retries", "3");
var uniqueIds = Set.of(1, 2, 3);
```

### Sealed Classes
```java
public sealed interface ServiceResult<T> permits Success, Failure {
    record Success<T>(T data) implements ServiceResult<T> {}
    record Failure<T>(String error, int code) implements ServiceResult<T> {}
}
```

### Constructor Injection (not field @Autowired)
```java
// REQUIRED pattern for all Spring beans:
@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository repository;
    private final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    public DoctorServiceImpl(DoctorRepository repository) {
        this.repository = repository;
    }
}
```

## Build Configuration Target
```xml
<properties>
    <java.version>21</java.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>
```

## Spring Boot Configuration Target
```yaml
spring:
  threads:
    virtual:
      enabled: true
  datasource:
    url: ${DB_URL:jdbc:sqlserver://localhost:1433;database=hospital}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```
