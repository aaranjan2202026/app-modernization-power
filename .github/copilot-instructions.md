---
applyTo: "**"
---

# GitHub Copilot Instructions — .NET Modernization Workspace

## SonarQube Configuration

sonarqube project name = "Refactoring-legacy-Pharmacy-uc2"
sonarqube server = "https://sonarqube-hub.azurewebsites.net"

## Workflow

This workspace uses a 5-phase automated modernization workflow:

1. **Phase 1 — Assess**: `@SonarQubeGenie` fetches and fixes all SonarQube issues
2. **Phase 2 — Plan**: `@dotnet-modernization-plan` creates the refactoring task plan
3. **Phase 3 — Refactor**: `@dotnet-modernization-developer` executes all tasks
4. **Phase 4 — Validate**: `@dotnet-modernization-validator` enforces Gate G4
5. **Phase 5 — Deploy**: Orchestrator triggers Azure DevOps pipeline

## To Run the Full Workflow

```
@dotnet-modernization-orchestrator Start the full .NET modernization workflow
```

## To Run Individual Phases

```
@SonarQubeGenie Fix all SonarQube issues
@dotnet-modernization-plan Create modernization plan
@dotnet-modernization-developer Execute all modernization tasks
@dotnet-modernization-validator Run full validation
```

## Working Branch

All agents work on: `feature/dotnet-modernization`

## Agent Behavior Rules

- All agents operate in fully autonomous mode — no user confirmation needed between steps
- Quality gates (G1-G5) must pass before proceeding to next phase
- All fixes are committed atomically with traceable task IDs
- Never modify: new features, UI, domain redesign (out of scope)
