---
inclusion: auto
---

# Migration Orchestrator — Single-Prompt Phase 1→8 Execution Contract

This is the master execution contract. When a migration trigger phrase is detected, follow this document to run all 8 phases autonomously in one continuous run.

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
    "7": {"name": "Quality Gate",      "status": "pending",     "gate": null},
    "8": {"name": "Audit & Push",      "status": "pending",     "gate": null}
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
FOR phase IN 1..8:
    1. Read Migration/.migration-state.json → confirm position
    2. Execute the phase per its track steering file
    3. Run the phase GATE (below)
    4. IF gate fails → fix → re-run gate (max 3 attempts)
       IF still failing after 3 → record "gate": "blocked", report, STOP
    5. IF gate passes → update state file → continue to next phase IMMEDIATELY

AFTER phase 8:
    Set state "currentPhase": "complete"
    Report completion in 2-3 sentences
```

### Phase Gates

| Phase | Gate condition | Output document |
|---|---|---|
| 1 | Assessment report written | `Migration/00-Assessment-Report.md` + `Orchestration/SONARQUBE-FIX-SUMMARY.md` |

Reference template for SonarQube Fix Summary: #[[file:steering/sonarqube-fix-summary-template.md]]
| 2 | `BUILD_CMD` compiles, 0 errors | `Migration/01-Migration_Plan.md` (task list generated) |
| 3 | `BUILD_CMD` compiles, 0 errors | — |
| 4 | `BUILD_CMD` compiles, 0 errors | — |
| 5 | App config loads (build succeeds; startup check if feasible) | — |
| 6 | `TEST_CMD` passes, 0 new failures | `Validation/VALIDATION-REPORT.md` (Gate G4 sign-off) |
| 7 | SonarQube scan MANDATORY — prompt for URL+token, run scanner, get fresh pass/fail | SonarQube gate result in state file |
| 8 | Audit report generated, code pushed to GitHub | `Migration/AUDIT-LOG.md` (finalized) + `Migration/FINAL-AUDIT-REPORT.md` |

### Audit Log — Continuous Throughout ALL Phases

`Migration/AUDIT-LOG.md` is **append-only and written throughout the run**, not just at Phase 8. Append an entry after:
- RUN_START (Step 0)
- Every GATE_CHECK (pass or fail)
- Every FILE_DELETE (with file list)
- Every VULN_REMEDIATE
- Every DEPENDENCY_REMOVE or DEPENDENCY_ADD
- RUN_COMPLETE

Format: `| <ISO timestamp> | <phase> | <EVENT_TYPE> | <detail> | <gate> | <files> |`

See `steering/audit-log.md` for the full event type catalog.

Reference template: #[[file:steering/audit-log-template.md]]

### Migration Plan — Generated at Phase 2

After Phase 1 assessment, generate `Migration/01-Migration_Plan.md` with:
- Current state analysis
- Numbered tasks: Category, Priority, Dependency, Description, Changes, Validation
- Execution order (dependency diagram)
- Success criteria checklist

Use these categories only: Config Externalization, Modularization, Deprecated API Replacement, Async/Reactive.

Mark each task as Completed when done during Phases 3-5.

Reference template: #[[file:steering/migration-plan-template.md]]

### Validation Report — Generated at Phase 6

After all tests pass, generate `Validation/VALIDATION-REPORT.md` with:
- Build results (compile, package, artifact)
- Test results table (suite, count, pass/fail/skip)
- Service layer coverage
- Modernization checklist (every criterion checked)
- Architecture before vs after
- File inventory (new, modified, deleted)
- Gate G4: APPROVED or BLOCKED

Reference template: #[[file:steering/validation-report-template.md]]

### Phase 7 — SonarQube (MANDATORY — AUTOMATIC)

SonarQube is NOT optional. The sonar-maven-plugin is pre-configured in pom.xml. The agent MUST:
1. **ASK the user** for SonarQube server URL and authentication token
2. **Commit and push** the migration code (scanner needs pushed code)
3. **Run the scanner automatically**:
   ```
   mvn sonar:sonar -Dsonar.projectKey=<key> -Dsonar.host.url=<URL> -Dsonar.token=<TOKEN> -f Hospital_Servlet1/pom.xml
   ```
   The sonar-maven-plugin (v4.0.0.4121) is already in pom.xml — no additional setup needed.
4. **Query the gate** via MCP tools: `get_project_quality_gate_status`
5. **Record pass or fail** — indeterminate/pending are NOT valid final states
6. If gate fails: fix issues, re-run scanner (max 3 attempts)

### Phase 8 — Audit Report & GitHub Push

After Phase 7:
1. Generate `Migration/FINAL-AUDIT-REPORT.md` covering ALL phases with SonarQube results
2. `git add -A && git commit -m "migration: Final audit report"`
3. `git push origin <branch>`
4. Verify with `git status` (clean tree)

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
- Emit a completion summary before Phase 8 finishes

**Git operations in Phase 7 and 8:**
- Commit and push ARE REQUIRED in Phase 7 (SonarQube needs pushed code to scan)
- Commit and push ARE REQUIRED in Phase 8 (final audit report push to GitHub)
- In Phases 1-6, git operations remain blocked

**SonarQube is MANDATORY:**
- Phase 7 MUST prompt the user for SonarQube URL and token
- Do NOT skip, defer, or mark as pending/indeterminate
- The scanner must run against the actual migrated code

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

Only these justify stopping before Phase 8:

| Condition | Action |
|---|---|
| Same gate fails 3 times | Record `"blocked"`, report the specific error, stop |
| Cannot write files (permissions) | Report and stop |
| Both Java and .NET present, target ambiguous | Ask once, then proceed |
| User does not provide SonarQube credentials | Report and stop — scan is mandatory |

Not valid reasons to stop: long output, many files, high token use, task complexity, server temporarily unreachable (retry), or uncertainty about a pattern choice (pick the standard pattern from the rules file and continue).

---

## Scope Boundary

In scope: language version upgrade, legacy code removal, service layer extraction, modern language features, configuration externalization, deprecated API replacement, test updates.

Out of scope: new features, UI redesign, database schema changes, unrelated refactoring, dependency upgrades not required by the migration.
