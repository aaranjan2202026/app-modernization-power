---
inclusion: auto
---

# SonarQube MCP Integration (MANDATORY)

## SonarQube is NOT Optional

SonarQube scanning is a MANDATORY requirement for this migration workflow. At Phase 7, the agent MUST:
1. **ASK the user** for their SonarQube Server URL and Authentication Token
2. **Run the scanner** against the migrated code
3. **Get a definitive pass/fail** — indeterminate/pending are NOT valid final states
4. **Fix issues** if the gate fails (up to 3 attempts)

Do NOT skip Phase 7. Do NOT mark it as pending. Do NOT use stale/cached results.

## When to Use

When performing migration assessment (Phase 1) or quality validation (Phase 7), connect to SonarQube via MCP.

## Configuration

Two distinct URLs are involved — do not conflate them:

| Setting | Value | Purpose |
|---------|-------|---------|
| **MCP endpoint** | `https://sonarqube-mcp-server.azurewebsites.net/mcp` | What Kiro connects to. Set in `.kiro/settings/mcp.json` as `url`. |
| **SonarQube server** | `https://sonarqube-hub.azurewebsites.net` | The SonarQube instance itself, where projects and issues live. Referenced for context only. |

- **Java Project Key** (expected): `Refactoring-legacy-Hospital-uc2`
- **.NET Project Key** (expected): `Refactoring-legacy-DotNet-uc2`
- **Token**: configured in `.kiro/settings/mcp.json` under `headers.SONARQUBE_TOKEN`

### Always Verify the Project Key First

The MCP server warns that an incorrect project key **silently returns results from the wrong project** — no error is raised. Before trusting any SonarQube data:

1. Call `search_my_sonarqube_projects` and confirm the expected key above actually exists.
2. If it does not exist, use the key from the returned list that matches this workspace.
3. Never assume the hardcoded key is correct without this check.

Resolution order if the expected key is missing:
1. `.sonarlint/connectedMode.json` → `projectKey` field
2. `sonar.projectKey` in `pom.xml`, `sonar-project.properties`, `build.gradle`
3. `sonar.projectKey` in CI files (`.github/workflows/*.yml`, `azure-pipelines.yml`)
4. `search_my_sonarqube_projects` and match by name

## MCP Tools Available

| Tool | Purpose |
|------|---------|
| `search_my_sonarqube_projects` | List all projects |
| `search_sonar_issues_in_projects` | Fetch issues for a project |
| `get_project_quality_gate_status` | Check quality gate |
| `get_component_measures` | Get metrics (coverage, bugs, etc.) |
| `show_rule` | Get rule details |
| `get_system_health` | Check server status |
| `analyze_code_snippet` | Analyze code inline |

## Usage Pattern

### During Assessment (Phase 1)
```
1. search_my_sonarqube_projects → find project key
2. search_sonar_issues_in_projects(project=key) → get all issues
3. get_component_measures(project=key, metrics=bugs,vulnerabilities,code_smells,coverage)
4. Document baseline in assessment report
```

### During Quality Validation (Phase 7)
```
1. get_project_quality_gate_status(project=key) → check gate
2. search_sonar_issues_in_projects(project=key) → find new issues
3. Fix any new issues introduced during migration
4. Verify quality gate passes
```

## Rules
- Always discover projects first, never hardcode keys (use configured keys above as starting point)
- Query multiple metrics together for efficiency
- Document all findings in migration reports
- SonarQube is MANDATORY — if unreachable after 3 retries, STOP and report the blocker to the user
- Do NOT continue migration without a definitive SonarQube pass/fail result
