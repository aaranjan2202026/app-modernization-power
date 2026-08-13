---
inclusion: auto
---

# Java Migration Rules — 7/8 → 21

## Namespace Transformations (javax → jakarta)

| Legacy (javax.*) | Modern (jakarta.*) | Notes |
|------------------|-------------------|-------|
| `javax.servlet.*` | `jakarta.servlet.*` | All servlet APIs |
| `javax.persistence.*` | `jakarta.persistence.*` | JPA/Hibernate |
| `javax.validation.*` | `jakarta.validation.*` | Bean Validation |
| `javax.annotation.*` | `jakarta.annotation.*` | CDI annotations |
| `javax.inject.*` | `jakarta.inject.*` | DI annotations |
| `javax.ws.rs.*` | `jakarta.ws.rs.*` | JAX-RS |
| `javax.json.*` | `jakarta.json.*` | JSON Processing |
| `javax.mail.*` | `jakarta.mail.*` | JavaMail |
| `javax.activation.*` | `jakarta.activation.*` | Activation Framework |

### Exceptions — These Stay as javax.*
- `javax.sql.*` — Java SE (part of JDK)
- `javax.crypto.*` — Java SE (JCE)
- `javax.net.*` — Java SE (networking)
- `javax.security.auth.*` — Java SE (JAAS)
- `javax.swing.*` — Java SE (GUI)

---

## Removed APIs & Replacements

| Removed API | Replacement | Since |
|-------------|-------------|-------|
| `java.lang.Thread.stop()` | `thread.interrupt()` + check `isInterrupted()` | Java 11 |
| `java.lang.Thread.suspend()/resume()` | Use `Lock` + `Condition` | Java 11 |
| `java.security.AccessController` | Remove or use SecurityManager alternatives | Java 17 |
| `java.util.Date(int,int,int)` constructors | `LocalDate.of(year, month, day)` | Java 9 |
| `Runtime.getRuntime().exec(String)` | `ProcessBuilder` | Java 18 |
| `Finalization (finalize())` | `Cleaner` or try-with-resources | Java 18 |
| `Applet API` | Remove entirely | Java 17 |
| `SecurityManager` | Remove (deprecated for removal) | Java 17 |
| `RMI Activation` | Remove or use alternatives | Java 15 |

---

## Java 21 Language Features to Apply

### Records (Java 16+)
```java
// Replace verbose POJO DTOs:
public record DoctorDTO(int id, String name, String specialist, String email) {}
```
**When to use**: DTOs, value objects, configuration holders
**When NOT to use**: JPA entities (need mutable state), classes with complex logic

### Sealed Classes (Java 17+)
```java
public sealed interface ServiceResult<T> permits Success, Failure {}
public record Success<T>(T data) implements ServiceResult<T> {}
public record Failure<T>(String message, int code) implements ServiceResult<T> {}
```

### Pattern Matching instanceof (Java 16+)
```java
// BEFORE: if (obj instanceof String) { String s = (String) obj; use(s); }
// AFTER:
if (obj instanceof String s) { use(s); }
```

### Switch Expressions (Java 14+)
```java
String result = switch (status) {
    case "active" -> "Active User";
    case "inactive" -> "Inactive";
    case "banned" -> "Banned";
    default -> "Unknown";
};
```

### Text Blocks (Java 15+)
```java
String sql = """
    SELECT id, name, email
    FROM users
    WHERE status = ?
    ORDER BY name
    """;
```

### Virtual Threads (Java 21)
```yaml
# application.yml
spring:
  threads:
    virtual:
      enabled: true
```

### SequencedCollections (Java 21)
```java
list.getFirst();    // not list.get(0)
list.getLast();     // not list.get(list.size() - 1)
list.reversed();   // reversed view
```

### var (Java 10+)
```java
var doctors = repository.findAll();
var connection = dataSource.getConnection();
```

### Collection Factories (Java 9+)
```java
var items = List.of("a", "b", "c");          // immutable
var map = Map.of("key1", "val1", "key2", "val2");
```

---

## Build Configuration Changes

### Maven (pom.xml)
```xml
<properties>
    <java.version>21</java.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <release>21</release>
    </configuration>
</plugin>
```

### Gradle (build.gradle)
```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
```

---

## Spring Boot Compatibility

| Java Version | Minimum Spring Boot | Recommended |
|--------------|--------------------:|------------:|
| Java 17 | 3.0.0 | 3.4.x |
| Java 21 | 3.2.0 | 3.4.x |

Spring Boot 3.x requires:
- Jakarta EE 9+ (jakarta.* namespace)
- Servlet 6.0+
- JPA 3.1+
- Bean Validation 3.0+

---

## Common Migration Pitfalls

1. **javax.* imports in test code** — Tests also need jakarta.* migration
2. **Third-party libraries using javax** — Update to versions that support jakarta
3. **Reflection on internal APIs** — Add `--add-opens` JVM args or refactor
4. **web.xml** — Remove or migrate to Java config (@Configuration)
5. **EJB references** — Replace with Spring @Service/@Component
6. **JNDI lookups** — Replace with Spring dependency injection
