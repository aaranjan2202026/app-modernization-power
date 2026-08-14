# SonarQube Validation — Phase 7 Quality Gate (MANDATORY)

## IMPORTANT: SonarQube is NOT optional

The SonarQube scan is a **required** step in the migration workflow. The agent MUST:
1. **Ask the user** for the SonarQube server URL and authentication token
2. **Run the scanner** against the actual migrated code (not rely on stale results)
3. **Get a fresh gate result** (pass or fail — indeterminate/pending are not acceptable final states)
4. **Generate audit entries** for the scan results

If the user does not provide credentials, the migration STOPS. Do not mark as "pending" or "indeterminate" and move on.

## Credential Prompt (MANDATORY at Phase 7 start)

The agent must ask:
```
To proceed with the mandatory SonarQube scan, I need:
1. SonarQube Server URL (e.g., https://sonarqube-hub.azurewebsites.net)
2. SonarQube Authentication Token

Please provide both values.
```

These values are used ONLY for the current scan and are never persisted to files or environment variables.

## Verified server facts

Confirmed live against `https://sonarqube-mcp-server.azurewebsites.net/mcp`:

| | |
|---|---|
| Server | `sonarqube-mcp-server` v1.22.0.3040 |
| Protocol | `2025-11-25` |
| Tools | **19** |
| Auth | `SONARQUBE_TOKEN` request header (prompted at runtime, never stored) |
| Transport | Streamable HTTP, SSE-framed responses (`data:` lines) |

Note the MCP endpoint (`sonarqube-mcp-server.azurewebsites.net/mcp`) is a **different host** from the SonarQube server itself (`sonarqube-hub.azurewebsites.net`). Pointing `url` at the SonarQube server yields no tools.

## Tool inventory

Read-only — safe to auto-approve:

`search_my_sonarqube_projects` · `search_sonar_issues_in_projects` · `get_project_quality_gate_status` · `get_component_measures` · `search_security_hotspots` · `show_security_hotspot` · `show_rule` · `list_quality_gates` · `search_metrics` · `search_files_by_coverage` · `get_file_coverage_details` · `get_duplications` · `search_duplicated_files` · `list_branches` · `list_pull_requests` · `search_dependency_risks` · `analyze_code_snippet`

**Mutating — never auto-approve:**

| Tool | Effect |
|---|---|
| `change_sonar_issue_status` | Marks issues resolved/false-positive on the server |
| `change_security_hotspot_status` | Alters hotspot review state |

Both write to shared server state visible to the whole team. They are deliberately excluded from `autoApprove`.

`get_system_health` does **not** exist on this server. Listing it in `autoApprove` is harmless but misleading.

## Schema gotcha

`get_component_measures` takes `projectKey` and `metricKeys` — not `component` and `metrics`:

```powershell
# correct
@{ projectKey = $key; metricKeys = @('ncloc','bugs','coverage') }

# rejected with a schema validation error
@{ component = $key; metrics = @('ncloc') }
```

## Workflow

### Step 1 — verify the project key (mandatory)

The server warns: *an incorrect project key silently returns results from the wrong project*. No error is raised. Always call `search_my_sonarqube_projects` and confirm the key is present before trusting any figure.

Beware near-identical keys. This server hosts both:
- `Refactoring-legacy-Hospital-uc2` (present tense)
- `Refactored-legacy-Hospital-uc2` (past tense)

and the equivalent `Refactoring-` / `Refactored-` pair for DotNet. Picking the wrong one produces plausible but wrong numbers.

### Step 2 — staleness check (mandatory)

```
list_branches { projectKey }
```

Read `analysisDate` and the branch list, then ask: **does this describe the code I just changed?**

Gate results are only meaningful if a scanner ran against your current tree. If your work is uncommitted, unpushed, or on a branch SonarQube does not track, the gate describes different code and **cannot validate the migration**. Record Phase 7 as `indeterminate` in that case — not pass, not fail.

### Step 3 — gate and measures

```
get_project_quality_gate_status { projectKey }
get_component_measures         { projectKey, metricKeys }
```

## Gate outcomes

| Outcome | Condition | Phase 7 status |
|---|---|---|
| **pass** | Gate `OK` **and** analysis covers current code | `pass` |
| **fail** | Gate `ERROR` **and** analysis covers current code | `fail` — fix the new violations, re-scan |
| ~~indeterminate~~ | ~~Analysis predates the migration~~ | **NOT ACCEPTABLE** — must run fresh scan |
| ~~pending~~ | ~~Server unreachable~~ | **NOT ACCEPTABLE** — retry or fix connectivity |

Only `pass` or `fail` (after 3 fix attempts) are valid final Phase 7 states. The agent must always run a fresh scanner to produce a definitive result.

## Producing a real gate result (MANDATORY)

The gate cannot judge uncommitted work. The agent MUST execute these steps — they are not optional:

1. Commit and push the migration branch
2. Run the scanner using the **pre-configured sonar-maven-plugin** (v4.0.0.4121 in pom.xml):
   ```powershell
   $env:SONAR_PROJECT_KEY = "<project-key>"
   $env:SONAR_HOST_URL = "<user-provided-URL>"
   $env:SONAR_TOKEN = "<user-provided-token>"
   cmd /c ".\.tools\apache-maven-3.9.9\bin\mvn.cmd sonar:sonar -f Hospital_Servlet1/pom.xml 2>NUL"
   ```
   The pom.xml properties `sonar.projectKey`, `sonar.host.url`, and `sonar.token` resolve from these env vars automatically. No `-D` flags needed.
3. Wait for analysis to complete (check with `get_project_quality_gate_status`)
4. Record the result in state file and audit log
5. If gate fails: fix issues, re-scan, re-check (max 3 attempts)

**"Indeterminate" and "pending" are NOT acceptable final outcomes.** The agent must achieve a definitive pass or fail.

## Token handling

**Never store the token in an environment variable, a committed file, or shell history.** Prompt for it at the point of use.

| Where | How |
|---|---|
| `.kiro/settings/mcp.json` | Real token lives here, and the file is **gitignored** |
| `.kiro/settings/mcp.json.example` | Committed template with `REPLACE_WITH_YOUR_TOKEN` |
| Scripts | Prompt via `Read-Host -AsSecureString` at runtime |

Rationale: environment variables leak into child processes, `ps` output, CI logs, and crash dumps. A masked runtime prompt keeps the secret in process memory only, for the life of one command.

## Bundled script

For sessions where native MCP tools are unavailable:

```powershell
.\scripts\phase7-quality-gate.ps1 -ProjectKey Refactoring-legacy-Hospital-uc2
```

It prompts for the token (masked, not persisted), performs the handshake, enforces the project-key check, prints the staleness warning, then reports gate and measures. The plaintext copy is scrubbed from memory in a `finally` block.

## Common migration violations

| Rule | Issue | Fix |
|---|---|---|
| S2095 | Resource not closed | try-with-resources |
| S1148 | `printStackTrace()` | SLF4J logger |
| S106 | `System.out` / `System.err` | SLF4J logger |
| S1192 | Duplicated string literal | Extract a constant |
| S2068 | Hardcoded credentials | Env vars / secret store |
| S4507 | Debug feature in production | Remove or profile-gate |
