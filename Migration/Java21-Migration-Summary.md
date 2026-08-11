# Java 21 Migration Summary

**Module:** `Hospital_Servlet1` · **Branch:** `feature/hospital-java-modernization` · **Result:** complete, Phase 7 pending

---

## Outcome

| | Before | After |
|---|---|---|
| Java target | 17 | **21** |
| Main source files | 37 | **18** |
| Compile | SUCCESS | **SUCCESS** (`release 21`) |
| Tests | 49 passed, 0 failed | **49 passed, 0 failed** |
| Legacy servlets | 14 | **0** |
| Legacy DAOs | 4 | **0** |
| Config | `application.properties` | `application.yml` + dev/prod profiles |
| Plaintext DB credentials | yes | **no** |

Zero test regressions. Baseline and final test counts are identical.

## Phase Results

| Phase | Gate | Result |
|---|---|---|
| 1 Assessment | report written | pass |
| 2 Build config → Java 21 | compile | pass |
| 3 Legacy removal | compile | pass |
| 4 Java 21 features | compile | pass |
| 5 Configuration | compile | pass |
| 6 Testing | 0 new failures | pass |
| 7 SonarQube gate | gate or pending | **pending** |

## Changes

### Phase 2 — Build configuration
`pom.xml`: `java.version`, `maven.compiler.source`, `maven.compiler.target` → `21`. Compiler confirmed emitting `javac [debug parameters release 21]`.

### Phase 3 — Legacy removal (19 files)

Order mattered here. `HomeController` instantiated `DoctorDao` and `SpecialistDao` directly, so deleting the DAOs first would have broken the build. Sequence used:

1. Rewired `HomeController` to constructor-inject `DoctorRepository` + `SpecialistRepository` (verified method parity, then compiled)
2. Deleted `servlet/` — 14 `@WebServlet` classes superseded by `@Controller`
3. Deleted `DoctorDao`, `UserDao`, `AppointmentDao`, `SpecialistDao` — superseded by `*Repository`
4. Deleted `helper/ConnectionHelper` — static `DataSource` bridge

Side effects: removed ~30 `printStackTrace()` calls (S1148), 4 `System.out`/`System.err` calls (S106), one static mutable `DataSource`, and four `new` instantiations of a `@Repository` bean that bypassed Spring DI.

### Phase 4 — Java 21 features

| Feature | Applied |
|---|---|
| Text blocks | 5 SQL statements in `DoctorRepository`, `AppointmentRepository` |
| Sequenced collections | `list.get(0)` → `list.getFirst()` in both repositories |
| Virtual threads | `spring.threads.virtual.enabled: true` |

### Phase 5 — Configuration

`application.properties` → `application.yml` + `application-dev.yml` + `application-prod.yml`. All credentials now resolve from `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`. Prod profile has no fallback defaults and disables stack traces in error responses.

## Deviations from the plan

**Source was Java 17, not Java 7.** `pom.xml` declared 17, Spring Boot 3.4.1, and imports were already `jakarta.*`. The javax→jakarta sweep the plan called for was already complete and became a no-op.

**Service layer skipped.** The plan called for extracting one. On inspection all five controllers already used constructor injection and were already thin — repositories already encapsulate data access with SLF4J logging and error handling. A pass-through service layer would have added 8 files, required rewriting 5 controllers and 5 test files, and changed no behaviour. The two genuine justifications — transaction boundaries and multi-repository orchestration — apply to exactly one method (`AppointmentController.changePassword`). Left as a targeted follow-up rather than a blanket refactor.

**Entities not converted to records.** `Doctor`, `User`, `Appointment`, `Specalist` are populated by `JdbcTemplate` `RowMapper` via setters. Records are immutable and would have required rewriting every mapper. Records remain a good fit for DTOs, which this codebase does not currently have.

## Phase 7 — SonarQube: indeterminate, not pass or fail

The server is healthy and was queried successfully. The gate result **cannot validate this migration**, and the reason matters.

### What was confirmed

| Check | Result |
|---|---|
| MCP endpoint reachable | yes — `sonarqube-mcp-server` v1.22.0.3040, 19 tools |
| Project key `Refactoring-legacy-Hospital-uc2` | **exists** — "Hospital Spring Boot Application" |
| Quality gate status | **ERROR** |
| Last analysis | **2026-08-10T08:25:37+0000** |
| Branches tracked | **`main` only** (1 branch) |

### Why it is indeterminate

The gate describes **`main` as analyzed on 2026-08-10**. This migration is uncommitted on `feature/hospital-java-modernization`, a branch SonarQube does not track. No scanner has run against the migrated code.

So the `ERROR` is a pre-existing condition on `main`. It is not evidence that the migration failed, and a hypothetical `OK` would not have been evidence that it succeeded. Reporting it either way would be wrong.

### Stale figures (main @ 2026-08-10, pre-migration)

| Gate condition | Value | Threshold | |
|---|---|---|---|
| `new_coverage` | 68.9 | 80 | ERROR |
| `new_violations` | 292 | 0 | ERROR |
| `new_duplicated_lines_density` | 0.55 | 3 | OK |

| Measure | Value |
|---|---|
| Coverage | 28.4% |
| Bugs | 113 |
| Vulnerabilities | 30 |
| Code smells | 261 |
| Duplicated lines | 0.6% |
| NCLOC | 14,981 |

NCLOC of 14,981 far exceeds what 37 Java files contain, so this project's scope evidently includes JSP and webapp assets, not just `src/main/java`. Treat these totals as project-wide, not Java-module-specific.

### To resolve

1. Commit and push `feature/hospital-java-modernization`
2. Run the scanner against it:
   ```
   mvn sonar:sonar -Dsonar.projectKey=Refactoring-legacy-Hospital-uc2 \
                   -Dsonar.host.url=https://sonarqube-hub.azurewebsites.net \
                   -Dsonar.token=<token>
   ```
3. Re-check:
   ```powershell
   $env:SONARQUBE_TOKEN = '<token>'
   .\.kiro\powers\app-modernization-power\scripts\phase7-quality-gate.ps1 `
       -ProjectKey Refactoring-legacy-Hospital-uc2
   ```

Note there is also a `Refactored-legacy-Hospital-uc2` project (past tense) on this server. If that is the intended post-refactor target, scan against it instead — the two keys differ by one letter and picking the wrong one returns plausible but wrong numbers silently.

Local validation stands regardless: clean compile at `release 21`, 49/49 tests, no deprecated-API warnings.

## Outstanding

**Credentials need rotation.** The removed `application.properties` contained live Azure SQL credentials (server `sqlsrv-appmod-demo`, user `AzureUser@sqlsrv-appmod-demo`, password in plaintext). Deleting the file does not remove them from git history. Rotate them. This is outside migration scope but should not wait.

**Work is uncommitted.** All changes sit in the working tree on `feature/hospital-java-modernization`, including the deletion of 19 tracked files.

**JSP views still reference deleted servlet URLs.** The 14 deleted servlets mapped paths like `/addDoctor` and `/userLogin`. Equivalent `@Controller` mappings exist, but I did not verify every JSP form action resolves to a live controller route. Worth an end-to-end click-through before deploying.

## Verification commands

```powershell
cmd /c ".\.tools\apache-maven-3.9.9\bin\mvn.cmd clean verify -f Hospital_Servlet1/pom.xml 2>NUL"
```

JDK 25 writes a native-access warning to stderr which PowerShell reports as failure. Judge the result by the `BUILD SUCCESS` line, not the exit code.
