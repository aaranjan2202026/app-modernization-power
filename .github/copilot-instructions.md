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

All agents **auto-detect** whether the workspace contains a Java project (`pom.xml`/`build.gradle`) or a .NET project (`*.sln`/`*.csproj`) and adjust their commands and patterns accordingly.

This workspace uses a **local-first** 4-phase workflow. No GitHub Actions triggers, no branch creation, and no remote pushes are required. All changes are applied directly to the working directory.

1. **Phase 1 — Assess** (`@SonarQubeGenie`):
   - Connects to the SonarQube server using the project name defined above
   - Fetches all open issues (bugs, vulnerabilities, code smells) directly from the server — no CI/CD trigger needed
   - Produces a prioritised issue list before touching any file

2. **Phase 2a — SonarQube Fixes** (`@SonarQubeGenie`):
   - Fixes every fetched SonarQube issue directly inside the project source files (`Hospital_Servlet1/` or `pharmacy/`)
   - Reports which files were changed and which rule each fix addresses
   - Example output: _"Fixed S2095 (resource leak) in `DBConnection.java`"_

3. **Phase 2b — Modernization Plan** (`@modernization-plan`):
   - Analyses the codebase and produces a structured modernization task plan
   - Covers: async patterns, API upgrades, dependency versions, configuration externalisation, and other improvements
   - Plan is used by the next phase as the source of tasks to execute

4. **Phase 2c — Modernization** (`@modernization-developer`):
   - Executes every task from the plan produced in Phase 2b directly inside the project source files
   - Reports which files were changed and what modernization task each change addresses
   - Example output: _"Updated `pom.xml` — upgraded Spring Boot from 2.x to 3.x"_

5. **Phase 3 — Validate** (`@modernization-validator`):
   - Builds the project and runs all tests locally
   - Confirms that both SonarQube fixes and modernization changes do not break existing behaviour
   - Quality gates (G1-G4) must pass before completion is reported

> **Branch policy**: Do **not** create or switch branches. All changes stay on the current local branch. If the user explicitly asks to create a branch, do so at that point only.

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

## Agent Behavior Rules

- All agents operate in fully autonomous mode — no user confirmation needed between steps
- SonarQube issues are fetched directly from the server; no CI/CD workflow trigger is required
- All fixes are applied locally to the project source files on the current branch
- Do **not** create branches, commit, push, or trigger any remote workflow unless the user explicitly requests it
- Quality gates (G1-G4) must pass before reporting completion
- Never modify: new features, UI, domain redesign (out of scope)
