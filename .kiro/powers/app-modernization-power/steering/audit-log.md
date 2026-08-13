# Audit Log — Migration Event Tracking

## Purpose

Every migration run produces an append-only audit log at `Migration/AUDIT-LOG.md`. This provides a verifiable record of what was changed, when, by which phase, and what gate result was produced — essential for compliance, post-incident investigation, and demonstrating due diligence.

## When to write

Append an entry **after every gate check**, whether it passes or fails. Also append on:
- Run start (Step 0)
- Any file deletion (with file list)
- Any dependency addition or removal
- Any vulnerability remediation
- Run completion or stop

## Format

Each entry is a markdown table row appended to the log. The agent writes these; they are never edited retroactively.

```markdown
| Timestamp | Phase | Event | Detail | Gate | Files |
|-----------|-------|-------|--------|------|-------|
| 2026-08-11T10:00:00Z | 0 | RUN_START | Track=JAVA, Source=17, Target=21 | — | — |
| 2026-08-11T10:01:00Z | 1 | ASSESSMENT_COMPLETE | 37 main files, 11 test, 14 servlets, 4 legacy DAOs | pass | Migration/00-Assessment-Report.md |
| 2026-08-11T10:02:00Z | 2 | BUILD_CONFIG | java.version 17→21 | pass | Hospital_Servlet1/pom.xml |
| 2026-08-11T10:03:00Z | 3 | FILE_DELETE | 14 servlets, 4 DAOs, 1 ConnectionHelper | — | [19 files listed] |
| 2026-08-11T10:03:30Z | 3 | GATE_CHECK | compile 18 sources release 21 | pass | — |
| 2026-08-11T10:04:00Z | 4 | LANGUAGE_FEATURES | text blocks x5, getFirst x2, virtual threads | pass | DoctorRepository.java, AppointmentRepository.java, application.yml |
| 2026-08-11T10:05:00Z | 7 | QUALITY_GATE | SonarQube indeterminate: stale analysis on main | indeterminate | — |
| 2026-08-11T10:05:30Z | — | RUN_COMPLETE | All phases done. 49/49 tests. 0 errors. | — | Migration/Java21-Migration-Summary.md |
```

## Event types

| Event | Meaning |
|-------|---------|
| `RUN_START` | Migration initiated |
| `RUN_COMPLETE` | All phases finished |
| `RUN_STOPPED` | Stopped before completion (with reason) |
| `ASSESSMENT_COMPLETE` | Phase 1 done |
| `BUILD_CONFIG` | Phase 2 target version change |
| `FILE_DELETE` | Files removed from the project |
| `FILE_CREATE` | New files added |
| `FILE_MODIFY` | Existing files changed (list which) |
| `DEPENDENCY_ADD` | Package added |
| `DEPENDENCY_REMOVE` | Package removed (especially for vuln remediation) |
| `VULN_REMEDIATE` | Vulnerability addressed |
| `GATE_CHECK` | Phase gate evaluated |
| `GATE_RETRY` | Gate failed, attempting fix and re-run |
| `LANGUAGE_FEATURES` | Java 21 / C# 12 patterns applied |
| `HOSTING_MODERNIZE` | Startup→Program.cs consolidation |
| `CONFIG_EXTERNALIZE` | Configuration moved to YAML/appsettings |
| `TEST_CREATE` | Test project or test class added |
| `QUALITY_GATE` | SonarQube gate check |
| `SONAR_SCAN_START` | SonarQube scanner execution started (with URL, project key) |
| `SONAR_SCAN_COMPLETE` | SonarQube scanner finished (with pass/fail result) |
| `SONAR_CREDENTIALS_RECEIVED` | User provided SonarQube URL and token |
| `GIT_COMMIT` | Code committed to branch |
| `GIT_PUSH` | Code pushed to GitHub remote |
| `AUDIT_REPORT_GENERATED` | FINAL-AUDIT-REPORT.md created |
| `ERROR` | Unrecoverable error |

## Gate values

| Value | Meaning |
|-------|---------|
| `pass` | Gate condition met |
| `fail` | Gate condition not met, will retry |
| `blocked` | Failed 3× — run stops |
| `indeterminate` | Result cannot validate this code (stale analysis) |
| `pending` | Check could not run (server unreachable) |
| `n/a` | Phase skipped (already modern) |

## Rules for the agent

1. **Append-only.** Never edit or delete previous entries.
2. **Timestamp every entry** in ISO 8601 UTC.
3. **List affected files** for destructive operations (DELETE, DEPENDENCY_REMOVE).
4. **Include the reason** for any STOP, FAIL, or SKIP.
5. **Write the log even if the run fails.** A failed run with an audit trail is far more useful than one without.
6. **Create the file on RUN_START** if it doesn't exist; append if it does (supports resumed runs).

## File location

```
Migration/AUDIT-LOG.md
```

Committed with the rest of the migration output. Never gitignored.

## Integration with the orchestrator

The orchestrator contract (`migration-orchestrator.md`) already requires state-file updates after each phase. The audit log is a **second, human-readable, append-only** record of the same events — but with more detail and with file lists that the JSON state file omits.

Both are written; neither replaces the other.

## Example: .NET remediation run

```markdown
| Timestamp | Phase | Event | Detail | Gate | Files |
|-----------|-------|-------|--------|------|-------|
| 2026-08-11T12:00:00Z | 0 | RUN_START | Track=DOTNET, Source=net8.0 (already modern), Target=remediation | — | — |
| 2026-08-11T12:01:00Z | 1 | ASSESSMENT_COMPLETE | 96 source files, 0 legacy patterns, sln build FAILED (MSB4025) | pass | Migration/00-DotNet-Assessment-Report.md |
| 2026-08-11T12:02:00Z | 2 | FILE_CREATE | Test project at path sln already referenced | pass | pharmacy/tests/UnitTests/UnitTests.csproj |
| 2026-08-11T12:02:30Z | 2 | GATE_CHECK | Solution builds 0 errors | pass | — |
| 2026-08-11T12:03:00Z | 3 | HOSTING_MODERNIZE | Startup.cs → Program.cs minimal hosting | pass | pharmacy/src/Web/Program.cs |
| 2026-08-11T12:03:10Z | 3 | FILE_DELETE | Startup.cs (consolidated into Program.cs) | — | pharmacy/src/Web/Startup.cs |
| 2026-08-11T12:04:00Z | 4 | VULN_REMEDIATE | AutoMapper 9.0.0 (GHSA-rvv3-g6hj-g44x) REMOVED — unused | — | Web.csproj, Program.cs, AutoMapping.cs |
| 2026-08-11T12:04:10Z | 4 | FILE_DELETE | Empty AutoMapper Profile | — | pharmacy/src/Web/Extensions/AutoMapping.cs |
| 2026-08-11T12:04:20Z | 4 | FILE_MODIFY | IncomesController: inject ILogger, log swallowed exception | pass | pharmacy/src/Web/Controllers/IncomesController.cs |
| 2026-08-11T12:05:00Z | 6 | TEST_CREATE | 7 tests on MedicalItemsPaginatedSpecification | pass | pharmacy/tests/UnitTests/Specifications/MedicalItemsPaginatedSpecificationTests.cs |
| 2026-08-11T12:05:30Z | 7 | QUALITY_GATE | 0 vulnerabilities, 0 code errors, 2 warnings (CS8981 EF migration class names) | pass_local | — |
| 2026-08-11T12:05:45Z | — | RUN_COMPLETE | Solution builds, 7/7 tests pass, vuln cleared | — | Migration/DotNet-Migration-Summary.md |
```
