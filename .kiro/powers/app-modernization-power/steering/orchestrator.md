# Orchestrator — Single-Prompt Phase 1→7 Execution Contract

This is the master contract that makes one prompt run the entire migration autonomously.

> **Install note:** for this to drive execution automatically, this file must be present in the workspace at `.kiro/steering/migration-orchestrator.md` with front matter `inclusion: auto`. Power steering files are read on demand and are not auto-injected. See `installation.md`.

## Trigger Detection

| User says | Track |
|---|---|
| "migrate java from 7 to java 21", "upgrade to java 21", "modernize java" | JAVA |
| "migrate .net", "modernize .net", "upgrade dotnet" | DOTNET |

If both `pom.xml` and `*.sln` exist and the prompt is ambiguous, ask once which target, then run without further questions.

---

## Step 0 — Establish State (ALWAYS FIRST)

Read `Migration/.migration-state.json`. If absent, create:

```json
{
  "track": "JAVA",
  "started": "<ISO timestamp>",
  "currentPhase": 1,
  "phases": {
    "1": {"name": "Assessment",        "status": "in_progress", "gate": null},
    "2": {"name": "Build Config",      "status": "pending",     "gate": null},
    "3": {"name": "Legacy Removal",    "status": "pending",     "gate": null},
    "4": {"name": "Language Features", "status": "pending",     "gate": null},
    "5": {"name": "Configuration",     "status": "pending",     "gate": null},
    "6": {"name": "Testing",           "status": "pending",     "gate": null},
    "7": {"name": "Quality Gate",      "status": "pending",     "gate": null}
  }
}
```

If present, resume from `currentPhase`. Do not redo completed phases.

### State Update Protocol (MANDATORY)

After each phase: mark it `"complete"` with `"gate": "pass"`, set the next to `"in_progress"`, bump `currentPhase`.

This file — not conversation memory — is the source of truth for position. After a context compaction, re-read it to confirm where you are.

---

## Execution Loop

```
FOR phase IN 1..7:
    read state → confirm position
    execute phase per track rules
    run phase gate
    IF gate fails → fix → retry (max 3)
       still failing → mark "blocked", report, STOP
    IF gate passes → update state → next phase IMMEDIATELY

AFTER 7: write summary, mark complete, report in 2-3 sentences
```

### Gates

| Phase | Gate |
|---|---|
| 1 | Assessment report written |
| 2 | Build compiles, 0 errors |
| 3 | Build compiles, 0 errors |
| 4 | Build compiles, 0 errors |
| 5 | Config loads / build succeeds |
| 6 | Tests pass, 0 new failures |
| 7 | SonarQube gate passes, or `"pending"` if MCP unreachable |

### Commands

```
JAVA:    BUILD = mvn clean compile -f <module>/pom.xml
         VERIFY= mvn clean verify  -f <module>/pom.xml
         TEST  = mvn test          -f <module>/pom.xml

DOTNET:  BUILD = dotnet build
         VERIFY= dotnet build --configuration Release
         TEST  = dotnet test
```

Phase detail lives in `java-migration-rules.md` (JAVA) and `dotnet-migration-rules.md` (DOTNET).

---

## Autonomy Rules

**Never:** ask to continue between phases; stop to summarize mid-run; offer manual alternatives; branch/commit/push unless asked; emit a completion summary before Phase 7.

**Always:** move phase→phase without pause; fix errors then re-run the gate; update state after each phase; keep inter-phase output to one line.

## Stop Conditions

Only: same gate failed 3×, cannot write files, or ambiguous target (ask once).

Not valid: long output, many files, token usage, complexity, or pattern uncertainty — pick the standard pattern from the rules file and continue.
