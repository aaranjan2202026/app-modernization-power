# App Modernization Power — Java & .NET

Single-prompt autonomous modernization. One message runs all seven phases: assess, upgrade build, strip legacy code, apply modern language features, externalize config, test, and validate quality.

## Keywords
migrate, java, dotnet, .net, java-21, java-17, net8, modernize, spring-boot, asp.net, jakarta, upgrade, sonarqube, quality-gate, refactor, legacy, migration, autonomous

## Usage

```
migrate java from 7 to java 21
migrate .net
```

That is the whole interaction. The run proceeds through all seven phases without further prompting, printing one line per phase transition.

> **Requires installation.** Power steering is loaded on demand, so the power alone cannot recognize the trigger phrase automatically. Copy the orchestrator and rules files into `.kiro/steering/` with `inclusion: auto`, and the hooks into `.kiro/hooks/`. See `installation.md`.

## The Seven Phases

| # | Phase | Gate |
|---|---|---|
| 1 | Assessment — inventory legacy patterns, SonarQube baseline | Report written |
| 2 | Build config — target Java 21 / net8.0 | Compiles, 0 errors |
| 3 | Legacy removal + service layer extraction | Compiles, 0 errors |
| 4 | Modern language features | Compiles, 0 errors |
| 5 | Configuration externalization | Config loads |
| 6 | Testing | Tests pass, 0 new failures |
| 7 | SonarQube quality gate | Gate passes, or `pending` if MCP unreachable |

A phase only advances when its gate passes. A gate failing three times stops the run with a specific error rather than continuing on a broken build.

## Resumability

Position is tracked in `Migration/.migration-state.json`, not in conversation memory. This matters because a seven-phase migration will outlive its context window — after a compaction the agent re-reads the file to confirm where it is instead of restarting or skipping ahead.

- Resume: say `continue the migration`
- Restart clean: delete the state file

## What It Changes

### Java (7/8 → 21)
Build config to Java 21 · `javax.*` → `jakarta.*` · removes raw servlets, legacy DAOs, static connection helpers · extracts a service layer with constructor injection · records, sealed interfaces, pattern matching, switch expressions, text blocks, virtual threads, sequenced collections · `application.yml` with profiles and env-var secrets

### .NET (Framework → 8)
SDK-style `.csproj` targeting `net8.0` · `System.Web` → ASP.NET Core · EF6 → EF Core · full async/await conversion · `Startup.cs` → minimal hosting `Program.cs` · records, primary constructors, collection expressions, raw string literals, nullable reference types · `appsettings.json` with the Options pattern · `IHttpClientFactory`

## Components

### Steering
| File | Loaded | Purpose |
|---|---|---|
| `orchestrator.md` | **auto** (after install) | Phase 1→7 execution contract, state protocol, gates |
| `java-migration-rules.md` | **auto** (after install) | Java 7→21 mappings and target patterns |
| `dotnet-migration-rules.md` | **auto** (after install) | .NET Framework→8 mappings and target patterns |
| `installation.md` | on demand | Wiring guide |
| `getting-started.md` | on demand | Phase-by-phase walkthrough |
| `sonarqube-validation.md` | on demand | Quality gate workflow |
| `hooks-reference.md` | on demand | Hook catalog |
| `autopilot.md` | on demand | Autonomy rules detail |

### Hooks
| Hook | Trigger | Action |
|---|---|---|
| `migration-write-guard` | preToolUse:write | Scope + namespace check, merged into one pass |
| `phase-gate-validation` | postTaskExecution | Runs gate, updates state file |
| `build-on-java-save` | fileEdited | `mvn compile` |
| `build-on-dotnet-save` | fileEdited | `dotnet build` |
| `sonarqube-post-task` | postTaskExecution | Quality spot-check |
| `git-safety-guard` | preToolUse:shell | Blocks branch/commit/push |

Exactly one `preToolUse:write` hook ships here on purpose — each one costs an agent round-trip per file write, so three overlapping guards on a 200-file migration would add 600 round-trips.

### MCP
| Server | Purpose | Required |
|---|---|---|
| `sonarqubemcp` | Baseline metrics, quality gate | No — Phases 1–6 run without it; Phase 7 records `pending` |

## Dependencies

None beyond Kiro's built-in tools. Transformations are file reads and writes; builds and tests run through the shell. This power does not call `aws-transform`, `java-migration-power`, or any other power.

| Requirement | For |
|---|---|
| JDK 21 + Maven 3.9 / Gradle 8 | Java track |
| .NET 8 SDK | .NET track |
| SonarQube MCP + token | Phase 7 only, optional |

## Supported Migrations

| Source | Target | Detection |
|---|---|---|
| Java 7/8 | Java 21 + Spring Boot 3.4.x | `pom.xml`, `build.gradle` |
| Java 11/17 | Java 21 | `pom.xml` `<java.version>` |
| .NET Framework 4.x | .NET 8 + ASP.NET Core | `*.sln`, legacy `.csproj` |
| .NET Core 3.1/5/6 | .NET 8 | SDK-style `.csproj` |

## Scope Boundary

**In:** language version upgrade, legacy code removal, service layer extraction, modern language features, configuration externalization, deprecated API replacement, test updates.

**Out:** new features, UI redesign, database schema changes, unrelated refactoring, dependency upgrades not required by the migration.

Enforced at write time by `migration-write-guard`.
