---
applyTo: "**"
---

# GitHub Copilot Instructions — Java Modernization Workspace

## SonarQube Configuration

sonarqube project name = "Refactoring-legacy-Hospital-uc2"
sonarqube server = "https://sonarqube-hub.azurewebsites.net"

## Workflow

This workspace uses a 5-phase automated modernization workflow with SonarQube scanning:

**Phase 0 — Baseline Scan**: Trigger `.github/workflows/sonarqube.yml` to generate fresh SonarQube report

1. **Phase 1 — Assess**: `@SonarQubeGenie` fetches and fixes all SonarQube issues
2. **Phase 2 — Plan**: `@java-modernization-plan` creates the refactoring task plan
3. **Phase 3 — Refactor**: `@java-modernization-developer` executes all tasks
4. **Phase 4 — Validate**: `@java-modernization-validator` enforces Gate G4
5. **Phase 5 — Deploy**: Orchestrator triggers Azure DevOps pipeline

**Phase 6 — Final Scan**: Trigger `.github/workflows/sonarqube.yml` again to show before/after metrics (errors resolved vs remaining)

## To Run the Full Workflow

```
@java-modernization-orchestrator Start the full Java modernization workflow
```

## To Run Individual Phases

```
@SonarQubeGenie Fix all SonarQube issues
@java-modernization-plan Create modernization plan
@java-modernization-developer Execute all modernization tasks
@java-modernization-validator Run full validation
```

## Working Branch

All agents work on: `feature/java-modernization`

## SonarQube Workflow Triggers

**Before Starting (Phase 0)**:
- Orchestrator will automatically checkout `feature/java-modernization` branch
- Commits and pushes all agent configuration files to remote
- User must then manually trigger baseline scan at: `https://github.com/Application-Modernization/cca-app-mod-demo-java-refactor-custom-agents/actions/workflows/sonarqube.yml`
- Or run: `gh workflow run sonarqube.yml --ref feature/java-modernization` (GitHub CLI)
- This generates the baseline report for `@SonarQubeGenie` to fetch issues

**After Validation (Phase 6)**:
- `@java-modernization-validator` triggers the workflow after all tests pass
- Compares baseline metrics vs post-refactoring metrics
- Generates final quality report showing improvements

## Agent Behavior Rules

- All agents operate in fully autonomous mode — no user confirmation needed between steps
- Quality gates (G1-G5) must pass before proceeding to next phase
- All fixes are committed atomically with traceable task IDs
- Never modify: new features, UI, domain redesign (out of scope)
