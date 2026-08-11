---
inclusion: auto
---

# Migration Orchestrator — Single-Prompt Phase 1→7 Execution Contract

This is the master execution contract. When a migration trigger phrase is detected, follow this document to run all 7 phases autonomously in one continuous run.

## Trigger Detection

| User says | Run |
|---|---|
| "migrate java from 7 to java 21", "upgrade to java 21", "modernize java", "java migration" | JAVA track |
| "migrate .net", "modernize .net", "upgrade dotnet", ".net migration" | DOTNET track |

If BOTH `pom.xml` and `*.sln` exist and the prompt is ambiguous, ask which target once, then run without further questions.

---

## Step 0 — Establish State (ALWAYS FIRST)

Before Phase 1, read `Migration/.migration-state.json`.

**If it does not exist**, create it:

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

**If it already exists**, resume from `currentPhase`. Do not restart completed phases. Announce in one line: `Resuming migration at Phase N (<name>).`

### State Update Protocol (MANDATORY)

After finishing each phase, immediately rewrite the state file:
- Set that phase `status` to `"complete"` and `gate` to `"pass"`
- Set the next phase `status` to `"in_progress"`
- Update `currentPhase`

This file is the single source of truth for position. Never rely on conversation memory to know which phase you are in — after a context compaction, re-read this file.

---

## Execution Contract

```
FOR phase IN 1..7:
    1. Read Migration/.migration-state.json → confirm position
    2. Execute the phase per its track steering file
    3. Run the phase GATE (below)
    4. IF gate fails → fix → re-run gate (max 3 attempts)
       IF still failing after 3 → record "gate": "blocked", report, STOP
    5. IF gate passes → update state file → continue to next phase IMMEDIATELY

AFTER phase 7:
    Generate summary report
    Set state "currentPhase": "complete"
    Report completion in 2-3 sentences
```

### Phase Gates

| Phase | Gate condition |
|---|---|
| 1 | Assessment report written to `Migration/00-Assessment-Report.md` |
| 2 | `BUILD_CMD` compiles, 0 errors |
| 3 | `BUILD_CMD` compiles, 0 errors |
| 4 | `BUILD_CMD` compiles, 0 errors |
| 5 | App config loads (build succeeds; startup check if feasible) |
| 6 | `TEST_CMD` passes, 0 new failures |
| 7 | SonarQube gate passes, OR recorded as `"pending"` if MCP unreachable |

### Commands by Track

```
JAVA:    BUILD_CMD = mvn clean compile -f Hospital_Servlet1/pom.xml
         VERIFY    = mvn clean verify  -f Hospital_Servlet1/pom.xml
         TEST_CMD  = mvn test          -f Hospital_Servlet1/pom.xml

DOTNET:  BUILD_CMD = dotnet build
         VERIFY    = dotnet build --configuration Release
         TEST_CMD  = dotnet test
```

### Phase Detail Sources

| Track | Phases 1-7 detail |
|---|---|
| JAVA | `java21-migration.md`, patterns in `java21-patterns.md` |
| DOTNET | `dotnet-migration.md` |

---

## Autonomy Rules

**Never do these mid-run:**
- Ask "Would you like me to continue?" or "Shall I proceed to Phase N?"
- Stop after a phase to summarize progress
- Offer manual alternatives
- Create branches, commit, or push (unless the user explicitly asked)
- Emit a completion summary before Phase 7 finishes

**Always do these:**
- Move phase→phase with no pause
- Fix build/test errors immediately, then re-run the gate
- Update the state file after each phase
- Keep inter-phase output to one line: `Phase N complete. Starting Phase N+1.`

## Progress Reporting

During the run, output only phase transitions — one line each. No verbose narration, no per-file commentary. Detailed findings go into the report files, not chat.

At the end, report: what changed, build/test status, and where the summary lives. 2-3 sentences.

---

## Stop Conditions

Only these justify stopping before Phase 7:

| Condition | Action |
|---|---|
| Same gate fails 3 times | Record `"blocked"`, report the specific error, stop |
| Cannot write files (permissions) | Report and stop |
| Both Java and .NET present, target ambiguous | Ask once, then proceed |

Not valid reasons to stop: long output, many files, high token use, task complexity, or uncertainty about a pattern choice (pick the standard pattern from the rules file and continue).

---

## Scope Boundary

In scope: language version upgrade, legacy code removal, service layer extraction, modern language features, configuration externalization, deprecated API replacement, test updates.

Out of scope: new features, UI redesign, database schema changes, unrelated refactoring, dependency upgrades not required by the migration.
