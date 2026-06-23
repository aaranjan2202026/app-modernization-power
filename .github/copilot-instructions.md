---
applyTo: "**"
---

# GitHub Copilot Instructions — Java Modernization Workspace

## SonarQube Configuration

### Java Project
sonarqube project name = "Refactoring-legacy-Hospital-uc2"
sonarqube server = "https://sonarqube-hub.azurewebsites.net"

### .NET Project
sonarqube dotnet project name = "Refactoring-legacy-DotNet-uc2"
sonarqube server = "https://sonarqube-hub.azurewebsites.net"

## Workflow

All agents **auto-detect** whether the workspace contains a Java project (`pom.xml`/`build.gradle`) or a .NET project (`*.sln`/`*.csproj`) and adjust their commands, branch, and patterns accordingly.

This workspace uses a 5-phase automated modernization workflow with SonarQube scanning:

**Phase 0 — Baseline Scan**:
- Java: Trigger `.github/workflows/sonarqube.yml`
- .NET: Trigger `.github/workflows/sonarqube-dotnet.yml`

1. **Phase 1 — Assess**: `@SonarQubeGenie` fetches and fixes all SonarQube issues
2. **Phase 2 — Plan**: `@modernization-plan` creates the refactoring task plan
3. **Phase 3 — Refactor**: `@modernization-developer` executes all tasks
4. **Phase 4 — Validate**: `@modernization-validator` enforces Gate G4
5. **Phase 5 — Deploy**: Orchestrator triggers Azure DevOps pipeline

**Phase 6 — Final Scan**: Re-trigger the appropriate workflow to show before/after metrics

## To Run the Full Workflow

```
# Java project:
@modernization-orchestrator Start the full Java modernization workflow

# .NET project:
@modernization-orchestrator Start the full .NET modernization workflow
```

## To Run Individual Phases

```
@SonarQubeGenie Fix all SonarQube issues
@modernization-plan Create modernization plan
@modernization-developer Execute all modernization tasks
@modernization-validator Run full validation
```

## Working Branches

- Java projects: `feature/java-modernization`
- .NET projects: `feature/dotnet-modernization`
- Branch is auto-selected by agents based on detected project type

## SonarQube Workflow Triggers

**Before Starting (Phase 0)**:
- Orchestrator automatically checkouts the correct branch based on project type
- Commits and pushes all agent configuration files to remote
- **Java**: Trigger `https://github.com/Application-Modernization/cca-app-mod-demo-java-refactor-custom-agents/actions/workflows/sonarqube.yml`
  - Or run: `gh workflow run sonarqube.yml --ref feature/java-modernization`
- **.NET**: Trigger `https://github.com/Application-Modernization/cca-app-mod-demo-java-refactor-custom-agents/actions/workflows/sonarqube-dotnet.yml`
  - Or run: `gh workflow run sonarqube-dotnet.yml --ref feature/dotnet-modernization`

**After Validation (Phase 6)**:
- `@modernization-validator` triggers the appropriate workflow after all tests pass
- Compares baseline metrics vs post-refactoring metrics
- Generates final quality report showing improvements

## Agent Behavior Rules

- All agents operate in fully autonomous mode — no user confirmation needed between steps
- Quality gates (G1-G5) must pass before proceeding to next phase
- All fixes are committed atomically with traceable task IDs
- Never modify: new features, UI, domain redesign (out of scope)
