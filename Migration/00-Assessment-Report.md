# Java Migration Assessment Report — Phase 1

**Module:** `Hospital_Servlet1` · **Branch:** `feature/hospital-java-modernization`

---

## Correction to the stated scope

The request was "migrate java from 7 to java 21". The code is **not on Java 7**.

| | Declared / Observed |
|---|---|
| `pom.xml` `<java.version>` | **17** |
| Spring Boot parent | 3.4.1 |
| EE namespace | `jakarta.*` already (no `javax.*` EE imports) |
| Servlet API | Jakarta Servlet 6 |

This is a **17 → 21** migration. The javax→jakarta sweep and the Jakarta EE dependency swap are already complete, so that portion of the plan is a no-op. Remaining work is Java 21 language features, legacy layer removal, and service extraction.

## Toolchain

| Tool | Version | Notes |
|---|---|---|
| JDK | OpenJDK 25.0.1 Temurin | Targets 21 via `--release 21` |
| Maven | 3.9.9, workspace-local `.tools/` | Not on PATH; invoke by path |
| .NET SDK | absent | Blocks the separate .NET track only |

## Baseline (verified, pre-change)

| Check | Result |
|---|---|
| `mvn clean compile` | **BUILD SUCCESS** |
| `mvn test` | **49 tests, 0 failures, 0 errors** |

Baseline is green. Any test failure appearing later is migration-caused, not pre-existing.

## Inventory

48 `.java` files — 37 main, 11 test.

| Package | Files | Nature |
|---|---:|---|
| `servlet/` (admin, doctor, user) | 14 | **Legacy** — `@WebServlet` + `extends HttpServlet` |
| `dao/` — `*Dao` | 4 | **Legacy** — raw JDBC via `ConnectionHelper.getConObj()` |
| `dao/` — `*Repository` | 4 | Modern — `JdbcTemplate`, constructor injection, SLF4J, BCrypt |
| `controller/` | 7 | Spring MVC `@Controller` |
| `entity/` | 4 | Verbose POJOs, manual getters/setters |
| `config/` | 2 | `WebConfig`, `PasswordEncoderConfig` |
| `helper/ConnectionHelper` | 1 | **Legacy** — static `DataSource` bridge |
| `HospitalApplication` | 1 | Spring Boot entry point |

The codebase is **mid-refactor**: legacy and modern data layers coexist. Each `*Dao` has a `*Repository` counterpart already written and tested.

## Blocker found — invalidates the naive deletion plan

`HomeController` (a live `@Controller`) instantiates the legacy DAOs directly:

| Method | Legacy call |
|---|---|
| `userAppointment()` | `new DoctorDao()`, `new SpecialistDao()` |
| `adminDoctor()` | `new SpecialistDao()` |
| `adminViewDoctor()` | `new DoctorDao()` |

The steering assumed the `*Dao` classes were dead code superseded by `*Repository`. They are not — deleting them first would break compilation.

**Resolution:** rewire `HomeController` to constructor-inject `DoctorRepository` and `SpecialistRepository` *before* deleting anything. Method parity verified:

| Legacy | Modern | Signature match |
|---|---|---|
| `DoctorDao.getAllDoctors()` | `DoctorRepository.getAllDoctors()` | `List<Doctor>` ✓ |
| `SpecialistDao.getAllSpecialist()` | `SpecialistRepository.getAllSpecialist()` | `List<Specalist>` ✓ |

This also removes `new` instantiation of a `@Repository` bean, which bypassed Spring DI entirely.

## Quality issues

| Issue | Count | Rule |
|---|---:|---|
| `e.printStackTrace()` | ~30 | S1148 — all in legacy `*Dao` + servlets |
| `System.out` / `System.err` | 4 | S106 — `ConnectionHelper` |
| Static mutable state | 1 | `ConnectionHelper.staticDataSource` |
| Bean bypass via `new` | 4 sites | `HomeController` |
| Hardcoded DB credentials | 1 file | S2068 — `application.properties` |

Most resolve by deletion, since the offending files are the legacy layer.

## Java 21 opportunities

| Feature | Target |
|---|---|
| Records | `entity/` POJOs → DTOs; keep mutable entities for `RowMapper` |
| Text blocks | Concatenated SQL in `DoctorRepository`, `UserRepository` |
| Pattern matching | `instanceof` casts, `GlobalExceptionHandler` |
| Switch expressions | Role/status branching |
| Virtual threads | `spring.threads.virtual.enabled: true` |
| Sequenced collections | `getFirst()` / `getLast()` over index access |

## Configuration

Currently `application.properties`, single-profile, with **live Azure SQL credentials in plaintext** (server, user, password). Phase 5 converts to `application.yml` with dev/prod profiles and env-var indirection.

Note: the credentials are already committed to git history. Rotating them is outside migration scope but worth flagging separately.

## Revised Phase Plan

| Phase | Work | Gate |
|---|---|---|
| 2 | `pom.xml` → Java 21 | compile |
| 3 | **Rewire `HomeController` → repositories first**, then delete 14 servlets, 4 DAOs, `ConnectionHelper`; add service layer | compile |
| 4 | Java 21 features | compile |
| 5 | `application.yml` + profiles + virtual threads | starts |
| 6 | Tests — expect deletion of servlet/DAO-coupled tests | 0 new failures |
| 7 | SonarQube gate | pass or `pending` |

## Gate G1

**PASS** — assessment complete, baseline green, blocker identified with a verified resolution path.
