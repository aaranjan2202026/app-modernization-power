# App Modernization Power — Java & .NET

Single-prompt autonomous modernization. One message runs seven phases: assess, upgrade build, strip legacy code, apply modern language features, externalize config, test, and validate quality.

Java 7/8/11/17/21 → **21 or 25 (LTS)** · .NET Framework → **.NET 8+**

## Keywords
migrate, java, dotnet, .net, java-25, java-21, java-17, net8, modernize, spring-boot, asp.net, jakarta, javax, upgrade, sonarqube, quality-gate, refactor, legacy, migration, autonomous, lts

## Usage

```
migrate java from 7 to java 25
migrate java to 21
migrate .net
```

The run proceeds through all seven phases without further prompting, one line per phase transition.

> **Requires installation.** Power steering loads on demand, so the power alone cannot recognize a trigger phrase. Copy the orchestrator, rules, and version matrix into `.kiro/steering/` with `inclusion: auto`, and the hooks into `.kiro/hooks/`. See `installation.md`.

## Pick the target pair, not just a version

A Java version with an incompatible framework produces a broken build. This is the single most common way a version-upgrade migration fails.

| Target | Requires | Status |
|---|---|---|
| Java 21 | Spring Boot 3.2+ | works, but all 3.x is **EOL** |
| **Java 25** | **Spring Boot 4.0+** | current supported pair |
| Java 25 + Spring Boot 3.4 | — | **unsupported — never pair** |

Every Spring Boot 3.x branch left OSS support in mid-2026. A project on 3.x gets no security fixes.

**If a project is on Spring Boot 3.x and the goal is Java 25, that is two migrations** — Java version *and* Boot 3.x → 4.x, which carries its own breaking changes. The power scopes these separately rather than silently bundling them. Full detail in `java-version-matrix.md`.

## The Seven Phases

| # | Phase | Gate |
|---|---|---|
| 1 | Assessment — legacy inventory, SonarQube baseline | Report written |
| 2 | Build config — target Java version / `net8.0` | Compiles, 0 errors |
| 3 | Legacy removal + service extraction | Compiles, 0 errors |
| 4 | Modern language features | Compiles, 0 errors |
| 5 | Configuration externalization | Config loads |
| 6 | Testing | 0 new failures |
| 7 | SonarQube quality gate (MANDATORY — prompts for URL + token) | pass / fail (no indeterminate/pending) |
| 8 | Audit report generation + GitHub push | Report exists, code pushed |

A phase advances only when its gate passes. Three consecutive gate failures stop the run with a specific error rather than continuing on a broken build.

**SonarQube is mandatory.** Phase 7 will prompt the user for their SonarQube server URL and token, run the scanner against the migrated code, and get a definitive pass/fail. It does not accept stale results or skip the scan.

**Audit and push are mandatory.** Phase 8 generates a comprehensive audit report covering all phases (including SonarQube results) and pushes the final code to GitHub.

## Resumability

Position lives in `Migration/.migration-state.json`, not conversation memory. A seven-phase migration outlives its context window; after a compaction the agent re-reads the file to confirm position instead of restarting or skipping ahead.

- Resume: `continue the migration`
- Restart clean: delete the state file

## What It Changes

### Java
Build config to the chosen LTS · `javax.*` → `jakarta.*` · removes raw servlets, legacy DAOs, static connection helpers · extracts a service layer where one is genuinely absent · records, sealed interfaces, pattern matching, switch expressions, text blocks, virtual threads, sequenced collections · `application.yml` with profiles and env-var secrets

Java 25 adds scoped values, module import declarations, flexible constructor bodies, compact source files.

**Preview features are never applied.** Structured concurrency, stable values, and primitive patterns are still preview in 25 despite frequent reports otherwise. Compiling production code with `--enable-preview` pins you to one JDK.

### .NET
SDK-style `.csproj` targeting `net8.0` · `System.Web` → ASP.NET Core · EF6 → EF Core · full async/await conversion · `Startup.cs` → minimal hosting `Program.cs` · records, primary constructors, collection expressions, raw string literals, nullable reference types · `appsettings.json` with Options pattern · `IHttpClientFactory`

## Components

### Steering
| File | Loaded | Purpose |
|---|---|---|
| `orchestrator.md` | **auto** | Phase 1→7 contract, state protocol, gates |
| `java-version-matrix.md` | **auto** | Version/framework pairing, feature availability |
| `java-migration-rules.md` | **auto** | `javax`→`jakarta`, removed APIs, target patterns |
| `dotnet-migration-rules.md` | **auto** | .NET Framework→8 mappings |
| `toolchain-bootstrap.md` | on demand | Preflight + no-admin Maven / .NET SDK install |
| `installation.md` | on demand | Wiring guide |
| `getting-started.md` | on demand | Phase walkthrough |
| `sonarqube-validation.md` | on demand | Quality gate workflow |
| `hooks-reference.md` | on demand | Hook catalog |
| `autopilot.md` | on demand | Autonomy rules |

### Hooks
| Hook | Trigger | Action |
|---|---|---|
| `migration-write-guard` | preToolUse:write | Scope + namespace check, one pass |
| `phase-gate-validation` | postTaskExecution | Runs gate, updates state |
| `build-on-java-save` | fileEdited | `mvn compile` |
| `build-on-dotnet-save` | fileEdited | `dotnet build` |
| `sonarqube-post-task` | postTaskExecution | Quality spot-check |
| `git-safety-guard` | preToolUse:shell | Blocks branch/commit/push |

Exactly one `preToolUse:write` hook ships here deliberately — each costs an agent round-trip per file write, so three overlapping guards on a 200-file migration would add 600 round-trips.

### Scripts
| Script | Purpose |
|---|---|
| `phase7-quality-gate.ps1` | SonarQube gate check when native MCP tools are unavailable. Prompts for the token, masked. |

### MCP
| Server | Purpose | Required |
|---|---|---|
| `sonarqubemcp` | Quality gate scan, metrics, issues | **Yes — Phase 7 is mandatory** |

Verified: `sonarqube-mcp-server` v1.22.0.3040, 19 tools. The MCP endpoint is a **different host** from the SonarQube server; pointing `url` at the SonarQube server returns zero tools.

## Secrets

The token is **prompted for at runtime** — never an environment variable, never committed.

| Where | How |
|---|---|
| `.kiro/settings/mcp.json` | Real token; **gitignored** |
| `.kiro/settings/mcp.json.example` | Committed template with placeholder |
| Scripts | `Read-Host -AsSecureString`, scrubbed in `finally` |
| Phase 7 runtime | Agent asks user directly for URL + token before scanning |

Env vars leak into child processes, `ps` output, CI logs, and crash dumps.

`autoApprove` covers read-only tools only. `change_sonar_issue_status` and `change_security_hotspot_status` write to shared server state and always require confirmation.

## Dependencies

None beyond Kiro's built-in tools. Transformations are file reads and writes; builds and tests run through the shell. This power does not call `aws-transform`, `java-migration-power`, or any other power.

| Requirement | For |
|---|---|
| JDK matching target + Maven 3.9 / Gradle 8 | Java track |
| .NET 8 SDK | .NET track |
| SonarQube MCP + token | **Phase 7 — MANDATORY** |
| Git + GitHub remote | **Phase 8 — MANDATORY for push** |

`toolchain-bootstrap.md` covers no-admin installation when no package manager is present.

## Scope Boundary

**In:** language version upgrade, legacy code removal, service extraction, modern language features, configuration externalization, deprecated API replacement, test updates.

**Out:** new features, UI redesign, database schema changes, unrelated refactoring, dependency upgrades not required by the migration.

Enforced at write time by `migration-write-guard`.

## Phase 7 is MANDATORY

The gate requires a **fresh** SonarQube scan. The agent must:
1. Prompt the user for SonarQube URL and token
2. Commit and push the migration code
3. Run `mvn sonar:sonar` (Java) or `dotnet sonarscanner` (.NET) with the provided credentials
4. Query the gate result via MCP tools
5. Record pass or fail — indeterminate/pending are not valid final states

If the scan reveals issues, the agent fixes them and re-scans (up to 3 attempts).

## Phase 8 — Audit & GitHub Push

After Phase 7, the agent:
1. Generates `Migration/FINAL-AUDIT-REPORT.md` covering all phases with SonarQube results
2. Commits the audit report
3. Pushes all code to GitHub on the migration branch

This ensures the repository contains both the modernized code and the complete audit trail.
