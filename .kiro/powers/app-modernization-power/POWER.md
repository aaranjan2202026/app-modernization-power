# App Modernization Power — Java & .NET

Single-prompt autonomous modernization. One message runs eight phases: assess, upgrade build, strip legacy code, apply modern language features, externalize config, test, SonarQube quality gate, and audit+push.

Java 7/8/11/17/21 → **21 or 25 (LTS)** · .NET Framework → **.NET 8+**

## Keywords
migrate, java, dotnet, .net, java-25, java-21, java-17, net8, modernize, spring-boot, asp.net, jakarta, javax, upgrade, sonarqube, quality-gate, refactor, legacy, migration, autonomous, lts

## Usage

```
migrate java from 7 to java 21
migrate java to 25
migrate .net
modernize java
upgrade dotnet
```

The run proceeds through all eight phases without further prompting, one line per phase transition.

---

## Power Structure — What Gets Installed

When this power is installed, the following files are placed into the user's workspace:

### Root Files (REQUIRED)

| File | Purpose |
|------|---------|
| `POWER.md` | This manifest — power identity, keywords, description |
| `README.md` | User-facing documentation |
| `mcp.json` | SonarQube MCP server configuration |

### Hooks (ALL REQUIRED — installed to `.kiro/hooks/`)

These hooks enforce quality gates and safety during migration:

| Hook File | Event | Action | Track |
|-----------|-------|--------|-------|
| `hooks/git-safety-guard.json` | preToolUse:shell | Blocks destructive git ops in Phases 1-6 | Both |
| `hooks/migration-write-guard.json` | preToolUse:write | Namespace ban + scope enforcement | Both |
| `hooks/phase-gate-validation.json` | postTaskExecution | Runs build gate, updates state | Both |
| `hooks/audit-log-entry.json` | postTaskExecution | Appends audit log entry | Both |
| `hooks/sonarqube-credential-prompt.json` | preTaskExecution | Prompts for SonarQube URL+token at Phase 7 | Both |
| `hooks/sonarqube-post-task.json` | postTaskExecution | Quick quality check after each task | Both |
| `hooks/final-audit-report.json` | postTaskExecution | Generates audit report + pushes to GitHub | Both |
| `hooks/build-on-java-save.json` | fileEdited:*.java | Auto Maven compile on save | Java |
| `hooks/build-on-dotnet-save.json` | fileEdited:*.cs | Auto dotnet build on save | .NET |

### Skills (installed to `.kiro/skills/`)

| Skill | Trigger Phrase | Track |
|-------|---------------|-------|
| `skills/java-migration.md` | "migrate java to 21" | Java |
| `skills/dotnet-migration.md` | "migrate .net" | .NET |
| `skills/sonarqube-check.md` | "run sonarqube check" | Both |

### Steering Files (installed to `.kiro/steering/` with `inclusion: auto`)

#### Core (REQUIRED for both tracks)

| File | Purpose | Inclusion |
|------|---------|-----------|
| `steering/orchestrator.md` | Master 8-phase execution contract | auto |
| `steering/modernization-workflow.md` | Trigger phrases, quality gates, output docs | auto |
| `steering/audit-log.md` | Event type catalog, append-only format | auto |
| `steering/autopilot.md` | Autonomy rules, decision framework | auto |
| `steering/toolchain-bootstrap.md` | Preflight check, no-admin install | auto |
| `steering/sonarqube-validation.md` | Phase 7 mandatory scan workflow | auto |

#### Java Track

| File | Purpose | Inclusion |
|------|---------|-----------|
| `steering/java-migration-rules.md` | javax→jakarta, removed APIs, Java 21 features | auto |
| `steering/java-version-matrix.md` | LTS versions, Spring Boot pairing | auto |

#### .NET Track

| File | Purpose | Inclusion |
|------|---------|-----------|
| `steering/dotnet-migration-rules.md` | Framework→8 mapping, async patterns, hosting | auto |

#### Templates (referenced by orchestrator via `#[[file:...]]`)

| File | Used At | Purpose |
|------|---------|---------|
| `steering/migration-plan-template.md` | Phase 2 | Migration plan format reference |
| `steering/sonarqube-fix-summary-template.md` | Phase 1 | SonarQube fix summary format |
| `steering/validation-report-template.md` | Phase 6 | Validation report format |
| `steering/audit-log-template.md` | All phases | Audit log entry format reference |

#### Documentation (on-demand, not auto-loaded)

| File | Purpose |
|------|---------|
| `steering/getting-started.md` | User walkthrough |
| `steering/installation.md` | Manual install guide |
| `steering/hooks-reference.md` | Hook documentation |

### Scripts

| File | Purpose |
|------|---------|
| `scripts/phase7-quality-gate.ps1` | SonarQube gate check (fallback when MCP unavailable) |
| `scripts/phase7-sonar-scan.ps1` | SonarQube scanner execution |

---

## The Eight Phases

| # | Phase | Gate |
|---|---|---|
| 1 | Assessment — legacy inventory, SonarQube baseline | Report written |
| 2 | Build config — target Java version / `net8.0` | Compiles, 0 errors |
| 3 | Legacy removal + service extraction | Compiles, 0 errors |
| 4 | Modern language features | Compiles, 0 errors |
| 5 | Configuration externalization | Config loads |
| 6 | Testing | 0 new failures |
| 7 | SonarQube quality gate (MANDATORY — prompts for URL + token) | pass / fail |
| 8 | Audit report generation + GitHub push | Report exists, code pushed |

A phase advances only when its gate passes. Three consecutive gate failures stop the run.

---

## SonarQube is MANDATORY

Phase 7 prompts the user for their SonarQube server URL and token, runs the scanner, and gets a definitive pass/fail. It does not accept stale results, skip the scan, or mark as pending.

## Audit and Push are MANDATORY

Phase 8 generates `Migration/FINAL-AUDIT-REPORT.md` covering all phases (including SonarQube results) and pushes the final code to GitHub.

---

## MCP Configuration

| Server | Purpose | Required |
|---|---|---|
| `sonarqubemcp` | Quality gate scan, metrics, issues | **Yes — Phase 7 is mandatory** |

The MCP endpoint (`sonarqube-mcp-server.azurewebsites.net/mcp`) is a **different host** from the SonarQube server. The token is prompted at runtime and never committed.

---

## Dependencies

| Requirement | For |
|---|---|
| JDK 21+ (25 OK, uses `--release 21`) + Maven 3.9 | Java track |
| .NET 8 SDK | .NET track |
| SonarQube MCP + token | **Phase 7 — MANDATORY** |
| Git + GitHub remote | **Phase 8 — MANDATORY** |

---

## Scope Boundary

**In:** language version upgrade, legacy code removal, service extraction, modern language features, configuration externalization, deprecated API replacement, test updates.

**Out:** new features, UI redesign, database schema changes, unrelated refactoring.

---

## Resumability

Position lives in `Migration/.migration-state.json`. After context compaction, the agent re-reads this file to confirm position. Say `continue the migration` to resume.
