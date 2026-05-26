# Use Case 2 Technical Design - .NET Refactor with Custom Agents

## 1. Solution

### 1.1 Objective
Refactor legacy .NET application code using GitHub Copilot custom agents to improve maintainability, reduce technical debt, and modernize architecture without functional regression.

### 1.2 Scope
In-scope refactoring:
- SonarQube issue remediation (bugs, vulnerabilities, code smells)
- Sync-to-async modernization
- Configuration externalization to appsettings
- Business logic modularization into services/repositories
- Unit test updates/additions for changed areas

Out of scope(What agents will NOT touch):
- New feature development
- UI redesign
- Large-scale domain redesign

### 1.3 External Platforms (not custom agents)
- GitHub Copilot API/Enterprise license
- SonarQube Server/Cloud
- Git repository (GitHub/Azure DevOps Repos)
- Azure DevOps pipelines
- Build agents

### 1.4 Expected Outcomes
- Improved code quality score- Reduced blocker/critical issues
- Reduced duplication and technical debt
- Zero functional regression through validation gates
- Traceable commits and documented refactoring approach

## 2. Agent Structure

### 2.1 Existing Custom Agents in Repo
- SonarQubeGenie
- dotnet-modernization-plan
- dotnet-modernization-developer

### 2.2 Proposed Runtime Roles (Operational Model)
The implementation will run with 5 logical roles. These can map to existing agents plus orchestration logic.

1. Orchestrator Role
- Starts workflow
- Enforces quality gates
- Routes handoffs between roles
- Triggers deployment pipeline

2. Assessment Role
- Pulls SonarQube findings
- Baselines quality metrics
- Produces prioritized issue list

3. Planning Role
- Converts findings into sequenced task plan
- Defines dependencies and acceptance checks per task

4. Refactoring Role
- Applies code changes task-by-task with Copilot
- Creates atomic commits
- Runs task-level build/test checks

5. Validation Role
- Runs full validation (build, tests, regression checks)
- Confirms acceptance criteria before deployment

### 2.3 Mapping to Current Agents
- Assessment: SonarQubeGenie
- Planning: dotnet-modernization-plan
- Refactoring: dotnet-modernization-developer
- Validation + Orchestrator: implemented as orchestration flow around existing agents (next implementation phase)

## 3. Sequence (Execution Flow)

### Phase 0: Intake
Input:
- Application path/repo
- SonarQube project key/report
- Refactor scope and acceptance criteria

Output:
- Workflow execution context

### Phase 1: Assess
Actions:
- Query SonarQube issues
- Classify by severity/type and affected module
- Capture baseline metrics

Gate G1:
- If no actionable issues: stop and report
- Else continue

Output:
- Assessment report + prioritized backlog

### Phase 2: Plan
Actions:
- Build task list grouped by refactor type
- Define dependency order
- Define validation checks per task

Gate G2:
- Plan completeness and dependency validity

Output:
- Refactor task plan

### Phase 3: Refactor
Actions:
- Execute tasks one-by-one
- Apply minimal safe change per task
- Commit with traceable task id

Gate G3 (per task):
- Build pass required
- Relevant tests pass required
- On failure: rollback task and retry/fix

Output:
- Refactored branch + change log

### Phase 4: Validate
Actions:
- Full build
- Full unit/integration test run (as available)
- Regression and coverage check for changed modules

Gate G4:
- No critical regression
- Acceptance criteria met

Output:
- Validation sign-off report

### Phase 5: Deploy
Actions:
- Trigger Azure DevOps deployment pipeline
- Run post-deploy smoke checks

Gate G5:
- Deployment success and application health
- On failure: rollback deployment

Output:
- Production-ready release status

## 4. Quality Gates (Short Definition)
- G1 Assess Gate: actionable findings exist
- G2 Plan Gate: task plan is complete and valid
- G3 Refactor Gate: task-level build/tests pass
- G4 Validation Gate: full QA/regression passes
- G5 Deploy Gate: deployment and health checks pass

## 5. Deliverables
- Assessment report
- Refactor task plan
- Refactored code branch with atomic commits
- Validation report
- Deployment result and rollback trace (if any)

## 6. Acceptance Criteria Mapping
- Target code areas identified/refactored: Phases 1-3
- Code quality improved: Phases 1 and 4 metric comparison
- No functional regression: Phases 3 and 4 gates
- Duplicate/inefficient code reduced: Phase 3 tasks + Phase 4 metrics
- Unit tests updated/added: Phase 3 and 4
- Code reviewed and committed: Phase 3 atomic commits + Phase 4 sign-off
- Approach documented: this technical design and generated reports


