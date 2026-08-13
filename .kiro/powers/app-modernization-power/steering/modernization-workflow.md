---
inclusion: auto
---

# Automated Migration Workflow — Java & .NET

## Single-Prompt Triggers

When the user says ANY of these phrases, execute the FULL automated migration workflow immediately without asking questions:

### Java Migration Triggers:
- "migrate java from 7 to java 21"
- "migrate java to 21"
- "upgrade to java 21"
- "java migration"
- "modernize java"
- "java 7 to 21"

### .NET Migration Triggers:
- "migrate .net"
- "modernize .net"
- "upgrade .net"
- ".net migration"
- "migrate dotnet"

---

## SonarQube Configuration

### Java Project
- SonarQube project name: `Refactoring-legacy-Hospital-uc2`
- SonarQube server: `https://sonarqube-hub.azurewebsites.net`

### .NET Project
- SonarQube dotnet project name: `Refactoring-legacy-DotNet-uc2`
- SonarQube server: `https://sonarqube-hub.azurewebsites.net`

### Token

**Never written in this file or any committed file.** The MCP endpoint
(`https://sonarqube-mcp-server.azurewebsites.net/mcp` — a different host from
the SonarQube server above) reads it from `.kiro/settings/mcp.json`, which is
gitignored. Scripts prompt for it at runtime via `Read-Host -AsSecureString`.

Do not export it as an environment variable: env vars leak into child
processes, `ps` output, CI logs, and crash dumps.

Verify the project key with `search_my_sonarqube_projects` before trusting any
result — a wrong key silently returns another project's data. Note the server
also hosts `Refactored-legacy-Hospital-uc2` and `Refactored-legacy-DotNet-uc2`,
one letter apart from the keys above.

---

## Project Type Auto-Detection

```
IF pom.xml or build.gradle found → Execute JAVA migration workflow
   BUILD_CMD = mvn clean verify -f Hospital_Servlet1/pom.xml
   TEST_CMD  = mvn test -f Hospital_Servlet1/pom.xml
   SOURCE    = Java 7 (legacy patterns)
   TARGET    = Java 21 (LTS) + Spring Boot 3.4.x

IF *.sln or *.csproj found → Execute .NET migration workflow
   BUILD_CMD = dotnet build
   TEST_CMD  = dotnet test
   SOURCE    = .NET Framework / Legacy .NET
   TARGET    = .NET 8+ with ASP.NET Core Minimal APIs
```

---

## EXECUTION MODE: FULLY AUTONOMOUS

- Execute ALL phases continuously without stopping
- NEVER ask "Would you like me to continue?" between phases
- NEVER offer manual alternatives — everything is automated
- NEVER stop mid-execution — work until 100% complete
- Fix errors automatically and continue
- Only stop for genuine unresolvable blockers (MCP down, can't write files)

---

## Branch Policy

Do **not** create or switch branches. All changes stay on the current local branch unless the user explicitly requests otherwise.

---

## Quality Gates

All quality gates must pass before reporting completion:
- **G1**: Assessment complete, issues cataloged
- **G2**: Build compiles with 0 errors after migration
- **G3**: All unit tests pass (0 failures)
- **G4**: SonarQube quality gate passes (or documented as pending if unreachable)

---

## Mandatory Output Documents

The workflow produces these files. They are NOT optional — each is generated at a specific phase.

| Document | Generated at | Location | Purpose |
|----------|-------------|----------|---------|
| **Migration Plan** | Phase 2 | `Migration/01-Migration_Plan.md` | Task list with dependencies, categories, priorities, validation criteria |
| **Audit Log** | ALL phases (append-only) | `Migration/AUDIT-LOG.md` | Timestamped event trail — every gate, deletion, fix, and decision |
| **Validation Report** | Phase 6 | `Validation/VALIDATION-REPORT.md` | Gate G4 sign-off: build, tests, coverage, architecture before/after |
| **SonarQube Fix Summary** | Phase 1 | `Orchestration/SONARQUBE-FIX-SUMMARY.md` | Issues before/after, fixes applied, vendor issues documented |
| **Assessment Report** | Phase 1 | `Migration/00-Assessment-Report.md` | Legacy pattern inventory, baseline metrics |
| **Migration State** | Step 0 | `Migration/.migration-state.json` | Phase position tracker (machine-readable) |

### Migration Plan format (`Migration/01-Migration_Plan.md`)

Must include:
- Current state analysis (architecture, patterns, debt)
- Numbered tasks with: Category, Priority, Dependency, Description, Changes, Validation
- Execution order diagram showing dependencies
- Success criteria checklist (checkboxes)

Categories: Config Externalization, Modularization, Deprecated API Replacement, Async/Reactive

### Audit Log format (`Migration/AUDIT-LOG.md`)

Append-only table. One row per event. See `steering/audit-log.md` for the full specification.

```markdown
| Timestamp | Phase | Event | Detail | Gate | Files |
```

Write entries at: RUN_START, every GATE_CHECK, every FILE_DELETE, every VULN_REMEDIATE, RUN_COMPLETE.

### Validation Report format (`Validation/VALIDATION-REPORT.md`)

Must include:
- Build validation (compile, package, artifact)
- Test results table (suite, tests, passed, failed, skipped)
- Service layer coverage table
- Modernization checklist (all criteria checked)
- Architecture before vs after
- Files modified/created/deleted summary
- Final gate decision: APPROVED or BLOCKED

### SonarQube Fix Summary format (`Orchestration/SONARQUBE-FIX-SUMMARY.md`)

Must include:
- Total issues before (by severity and type)
- Issues fixed (table: file, rule, description, action)
- Remaining issues (categorized: vendor/third-party vs fixable)
- Build validation result
- Gate G1 decision
