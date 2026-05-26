# Agent Integration & Handoff Protocol
## Detailed Data Flow & Communication Specifications

**Document Purpose**: Technical specification for how custom agents integrate and hand off work  
**Audience**: Development team implementing agents  
**Status**: Design Document  

---

## TABLE OF CONTENTS

1. [Handoff Protocol Overview](#handoff-protocol-overview)
2. [Agent Communication Patterns](#agent-communication-patterns)
3. [Data Contract Specifications](#data-contract-specifications)
4. [Integration Touchpoints](#integration-touchpoints)
5. [Error Handling & Resilience](#error-handling--resilience)
6. [State Management](#state-management)
7. [Quality Gate Enforcement](#quality-gate-enforcement)

---

## HANDOFF PROTOCOL OVERVIEW

### Handoff Types

| Handoff Type | Trigger | From Agent | To Agent | Data Size | Latency |
|------------|---------|-----------|----------|-----------|---------|
| **Sequential** | Completion | Any | Next in sequence | Large | Minutes |
| **Parallel** | Independent tasks | Refactoring | Multiple | Medium | Real-time |
| **Event-Driven** | Error/milestone | Any | Orchestrator | Small | Immediate |
| **Fallback** | Failure | Any | Previous | Small | Immediate |

### Handoff Flow Pattern

```
Agent A Completes Work
    ↓
Validate Output Data
    ↓
Create Handoff Package
    ↓
Store in Repository
    ↓
Trigger Quality Gate
    ↓
Gate Passes?
├─ YES → Notify Agent B
│         ↓
│    Agent B Loads Data
│    ↓
│    Agent B Executes
└─ NO → Escalate to Orchestrator
```

---

## AGENT COMMUNICATION PATTERNS

### Pattern 1: Sequential Handoff (Assessment → Planning)

```
ASSESSMENT AGENT                          PLANNING AGENT
├─ Complete analysis                      
├─ Create report file
├─ Store in repo: 
│  └─ reports/assessment.json
├─ Update project state
└─ Signal completion
                                          ← QG1 GATE: Issues > 5?
                                          
                                          ← Load assessment report
                                          ├─ Parse JSON
                                          ├─ Extract metrics
                                          └─ Begin planning
```

### Pattern 2: Parallel Tasks (Multiple Refactoring Tasks)

```
REFACTORING AGENT
├─ Load migration plan
├─ Identify independent tasks
│  ├─ Task T001 (async/await)
│  ├─ Task T002 (config - parallel)
│  └─ Task T003 (async/await - depends on T001)
│
├─ Execute T001 ────────┐
├─ Execute T002 ────────┼─ Parallel (no dependency)
├─ Wait T001 Complete   │
├─ Execute T003 ────────┤ Sequential (depends on T001)
│                       │
└─ Merge results ←──────┘
```

### Pattern 3: Event-Driven Error Recovery

```
REFACTORING AGENT             ORCHESTRATOR              PREVIOUS STATE
├─ Build fails
├─ Create error event
└─ Emit: TASK_BUILD_FAILED ──→ [Receive Event]
                               ├─ Analyze failure
                               ├─ Check rollback plan
                               ├─ Trigger rollback
                               └─ Notify team
                                    
                               Rollback to:
                               [Load from Git] ←──── Get previous commit
```

### Pattern 4: Feedback Loop (Validation Rejection)

```
VALIDATION AGENT                          REFACTORING AGENT
├─ Complete QA
├─ Find issues
├─ Create feedback
└─ Emit: QA_FAILED
         ├─ Issue 1: Config not externalized
         ├─ Issue 2: 3 async methods still sync
         └─ Feedback: Return to Phase 3
         
                                          ← Receive feedback
                                          ├─ Parse issues
                                          ├─ Create fix tasks
                                          ├─ Execute fixes
                                          └─ Re-validate
```

---

## DATA CONTRACT SPECIFICATIONS

### Contract 1: Assessment → Planning Handoff

```json
{
  "handoff_id": "HO-001-ASS-TO-PLAN-20260521",
  "timestamp": "2026-05-21T10:30:00Z",
  "source_agent": "ASSESSMENT",
  "target_agent": "PLANNING",
  "status": "READY_FOR_HANDOFF",
  
  "payload": {
    "assessment_report": {
      "project_id": "pharmacy-orders-service",
      "analysis_date": "2026-05-21T10:00:00Z",
      "total_issues": 197,
      "issues_by_severity": {
        "blocker": 5,
        "critical": 42,
        "major": 89,
        "minor": 61
      },
      "issues_by_type": {
        "async_await": {
          "count": 47,
          "files": ["Controllers/OrderController.cs", "Services/OrderService.cs"],
          "pattern": "Sync I/O, .Result, .Wait()"
        },
        "config_externalization": {
          "count": 23,
          "files": ["appsettings.Development.json"],
          "pattern": "Hardcoded strings, connection strings"
        },
        "modularization": {
          "count": 38,
          "files": ["Business/OrderProcessor.cs"],
          "pattern": "Large classes, mixed concerns"
        },
        "deprecated_apis": {
          "count": 89,
          "files": ["*"],
          "pattern": "Old .NET Framework APIs"
        }
      },
      "metrics": {
        "code_quality_score": 45,
        "code_coverage": 0.45,
        "duplicated_lines_percentage": 15.2,
        "cyclomatic_complexity": "High",
        "technical_debt_hours": 240,
        "code_smells": 45
      },
      "patterns_detected": [
        {
          "pattern_id": "PAT-001",
          "name": "Async I/O Replacement",
          "affected_count": 47,
          "template": "File.ReadAllText() → File.ReadAllTextAsync()"
        },
        {
          "pattern_id": "PAT-002",
          "name": "Configuration Externalization",
          "affected_count": 23,
          "template": "string conn = 'Server=...' → appsettings.json"
        }
      ],
      "recommendations": [
        "Prioritize blocking/critical issues",
        "Apply async pattern first (high impact)",
        "Group config externalization tasks",
        "Batch similar transformations"
      ]
    }
  },
  
  "quality_gate": {
    "gate_id": "QG-1",
    "name": "Issues Identified",
    "criteria": "total_issues > 5",
    "result": "PASS",
    "passed_at": "2026-05-21T10:30:00Z"
  },
  
  "metadata": {
    "file_locations": {
      "report": "reports/assessment-20260521.json",
      "metrics": "reports/metrics-baseline-20260521.csv",
      "issue_export": "reports/sonarqube-export-20260521.json"
    },
    "next_agent": "PLANNING",
    "estimated_processing_time": "2 hours"
  }
}
```

### Contract 2: Planning → Refactoring Handoff

```json
{
  "handoff_id": "HO-002-PLAN-TO-REF-20260521",
  "timestamp": "2026-05-21T12:30:00Z",
  "source_agent": "PLANNING",
  "target_agent": "REFACTORING",
  "status": "READY_FOR_EXECUTION",
  
  "payload": {
    "migration_plan": {
      "plan_id": "MP-20260521-001",
      "project_id": "pharmacy-orders-service",
      "created_date": "2026-05-21T12:30:00Z",
      
      "phases": [
        {
          "phase_number": 1,
          "phase_name": "Quick Wins & Foundation",
          "priority": "P0",
          "tasks": [
            {
              "task_id": "T001",
              "title": "Replace File I/O with async in OrderController",
              "type": "async_await",
              "severity": "CRITICAL",
              "target_files": [
                "Controllers/OrderController.cs"
              ],
              "pattern_to_apply": "PAT-001",
              "acceptance_criteria": [
                "No File.ReadAllText() calls",
                "All I/O methods return Task or Task<T>",
                "All callers use await"
              ],
              "testing_strategy": "Unit tests for OrderController",
              "estimated_complexity": "MEDIUM",
              "dependencies": [],
              "rollback_commit": "abc123def456" 
            },
            {
              "task_id": "T002",
              "title": "Externalize database connection string",
              "type": "config_externalization",
              "severity": "MAJOR",
              "target_files": [
                "Data/OrderDbContext.cs",
                "appsettings.json"
              ],
              "pattern_to_apply": "PAT-002",
              "acceptance_criteria": [
                "Connection string in appsettings.json",
                "No hardcoded strings in code",
                "All environments have correct config"
              ],
              "testing_strategy": "Integration test for DB connection",
              "estimated_complexity": "LOW",
              "dependencies": [],
              "rollback_commit": "abc123def456"
            }
          ]
        },
        {
          "phase_number": 2,
          "phase_name": "Core Modularization",
          "priority": "P1",
          "tasks": []  // 18 tasks detailed similarly
        }
      ],
      
      "patterns": [
        {
          "pattern_id": "PAT-001",
          "name": "Async I/O Replacement",
          "code_template": {
            "before": "var content = File.ReadAllText(path);",
            "after": "var content = await File.ReadAllTextAsync(path);"
          },
          "affected_occurrences": 47,
          "files_involved": ["Controllers/", "Services/"],
          "validation_method": "Search for File. method calls"
        }
      ],
      
      "dependencies": {
        "task_graph": {
          "T001": [],      // No dependencies
          "T002": [],      // No dependencies
          "T003": ["T001"],    // Depends on T001
          "T004": ["T001", "T002"],  // Depends on multiple
          "T005": ["T003"]     // Depends on T003
        },
        "critical_path": ["T001", "T003", "T005"],
        "parallel_opportunities": [
          ["T001", "T002"],     // Can run together
          ["T006", "T007"]      // Can run together
        ]
      },
      
      "quality_gates": [
        {
          "gate_id": "QG-3",
          "name": "Build Success",
          "trigger": "after_each_task",
          "criteria": "dotnet build must pass with no errors",
          "failure_action": "ROLLBACK_AND_ALERT"
        },
        {
          "gate_id": "QG-4",
          "name": "Test Pass",
          "trigger": "after_each_phase",
          "criteria": "100% of tests must pass, coverage >= 70%",
          "failure_action": "HALT_PHASE"
        }
      ]
    }
  },
  
  "quality_gate": {
    "gate_id": "QG-2",
    "name": "Plan Valid",
    "criteria": "all_tasks_sequenced AND dependencies_valid",
    "result": "PASS",
    "passed_at": "2026-05-21T12:30:00Z"
  },
  
  "metadata": {
    "file_locations": {
      "plan": "migration/plan-20260521.json",
      "task_list": "migration/tasks-20260521.csv",
      "patterns": "migration/patterns-library-20260521.json"
    },
    "next_agent": "REFACTORING",
    "estimated_total_time": "40 hours",
    "task_count": 38,
    "execution_notes": "Execute Phase 1 first, then Phase 2, then Phase 3"
  }
}
```

### Contract 3: Refactoring → Validation Handoff

```json
{
  "handoff_id": "HO-003-REF-TO-VAL-20260521",
  "timestamp": "2026-05-21T16:45:00Z",
  "source_agent": "REFACTORING",
  "target_agent": "VALIDATION",
  "status": "READY_FOR_QA",
  
  "payload": {
    "refactoring_results": {
      "branch": "refactor/phase-1-pharmacy",
      "commits": [
        {
          "commit_hash": "a1b2c3d4e5f6",
          "task_id": "T001",
          "message": "T001: Replace sync I/O with async in OrderController",
          "files_changed": 1,
          "insertions": 15,
          "deletions": 8,
          "timestamp": "2026-05-21T13:00:00Z"
        },
        {
          "commit_hash": "f6e5d4c3b2a1",
          "task_id": "T002",
          "message": "T002: Externalize database connection to appsettings.json",
          "files_changed": 2,
          "insertions": 12,
          "deletions": 5,
          "timestamp": "2026-05-21T13:15:00Z"
        }
      ],
      
      "execution_summary": {
        "total_tasks": 38,
        "completed": 38,
        "failed": 0,
        "skipped": 0,
        "total_files_modified": 23,
        "total_insertions": 340,
        "total_deletions": 210,
        "execution_time_hours": 3.5
      },
      
      "change_log": [
        {
          "task_id": "T001",
          "status": "COMPLETED",
          "changes": "15 async I/O replacements in OrderController.cs",
          "test_results": "All unit tests pass (15/15)",
          "build_result": "SUCCESS"
        },
        {
          "task_id": "T002",
          "status": "COMPLETED",
          "changes": "Database config externalized",
          "test_results": "Integration test pass (5/5)",
          "build_result": "SUCCESS"
        }
      ],
      
      "code_quality_delta": {
        "before": {
          "quality_score": 45,
          "blocker_issues": 5,
          "critical_issues": 42,
          "major_issues": 89,
          "code_smells": 45,
          "duplications": "15.2%"
        },
        "after": {
          "quality_score": 68,
          "blocker_issues": 0,
          "critical_issues": 18,
          "major_issues": 52,
          "code_smells": 28,
          "duplications": "8.5%"
        },
        "improvement": {
          "quality_score_delta": 23,
          "blocker_reduction": "100%",
          "critical_reduction": "57%",
          "code_smells_reduction": "38%"
        }
      }
    }
  },
  
  "quality_gate": {
    "gate_id": "QG-3",
    "name": "Build Success",
    "criteria": "all_tasks_build_pass",
    "result": "PASS",
    "passed_at": "2026-05-21T16:45:00Z"
  },
  
  "metadata": {
    "file_locations": {
      "branch": "refactor/phase-1-pharmacy",
      "pull_request": "#425",
      "change_summary": "migration/refactoring-summary-20260521.json",
      "commit_log": "migration/commits-20260521.log"
    },
    "next_agent": "VALIDATION",
    "estimated_qa_time": "4 hours",
    "ready_for_deployment": true,
    "rollback_available": true,
    "rollback_command": "git reset --hard abc123"
  }
}
```

### Contract 4: Validation → Orchestrator Handoff

```json
{
  "handoff_id": "HO-004-VAL-TO-ORK-20260521",
  "timestamp": "2026-05-21T20:30:00Z",
  "source_agent": "VALIDATION",
  "target_agent": "ORCHESTRATOR",
  "status": "APPROVAL_DECISION_NEEDED",
  
  "payload": {
    "qa_report": {
      "report_id": "QA-20260521-001",
      "validation_date": "2026-05-21T20:30:00Z",
      "overall_result": "APPROVED",
      
      "code_review": {
        "reviewer": "VALIDATION_AGENT",
        "reviewed_files": 23,
        "critical_issues": 0,
        "major_issues": 2,
        "minor_issues": 5,
        "comments": [
          "Consider adding more comments to async methods",
          "One potential null reference in service method"
        ]
      },
      
      "test_results": {
        "unit_tests": {
          "passed": 156,
          "failed": 0,
          "skipped": 0,
          "success_rate": "100%",
          "execution_time": "2m 15s"
        },
        "integration_tests": {
          "passed": 45,
          "failed": 0,
          "skipped": 0,
          "success_rate": "100%",
          "execution_time": "5m 30s"
        },
        "e2e_tests": {
          "passed": 28,
          "failed": 0,
          "skipped": 0,
          "success_rate": "100%",
          "execution_time": "8m 45s"
        }
      },
      
      "coverage_analysis": {
        "before": {
          "coverage_percentage": 45,
          "covered_lines": 1200,
          "total_lines": 2667
        },
        "after": {
          "coverage_percentage": 72,
          "covered_lines": 1920,
          "total_lines": 2667
        },
        "delta": 27
      },
      
      "regression_analysis": {
        "no_regressions_detected": true,
        "breaking_changes": [],
        "performance_impact": "NONE",
        "memory_impact": "NEGLIGIBLE"
      },
      
      "acceptance_criteria_validation": {
        "all_passed": true,
        "details": [
          {
            "criteria": "Code quality score >= 75",
            "target": 75,
            "actual": 82,
            "status": "PASS"
          },
          {
            "criteria": "Coverage >= 70%",
            "target": 70,
            "actual": 72,
            "status": "PASS"
          },
          {
            "criteria": "Zero blocker/critical",
            "target": 0,
            "actual": 0,
            "status": "PASS"
          }
        ]
      }
    }
  },
  
  "quality_gate": {
    "gate_id": "QG-5",
    "name": "QA Approved",
    "criteria": "all_tests_pass AND coverage_sufficient AND no_regressions",
    "result": "PASS",
    "passed_at": "2026-05-21T20:30:00Z"
  },
  
  "deployment_recommendation": {
    "ready_for_deployment": true,
    "risk_level": "LOW",
    "deployment_strategy": "DIRECT",
    "rollback_plan": "Available (git reset --hard abc123)",
    "health_checks": [
      "Endpoint /health responds",
      "Database connectivity OK",
      "All services respond"
    ]
  },
  
  "metadata": {
    "file_locations": {
      "qa_report": "reports/qa-report-20260521.json",
      "test_results": "reports/test-results-20260521.xml",
      "coverage_report": "reports/coverage-20260521.html"
    },
    "next_step": "DEPLOY",
    "approved_by": "VALIDATION_AGENT",
    "approval_timestamp": "2026-05-21T20:30:00Z",
    "sign_off_token": "APPROVED-20260521-T20:30:00Z"
  }
}
```

---

## INTEGRATION TOUCHPOINTS

### Touchpoint 1: SonarQube Integration (Assessment Agent)

**Connection Type**: REST API / MCP  
**Frequency**: One-time per assessment  
**Timeout**: 30 minutes  
**Retry Strategy**: 3 retries with exponential backoff

**Request**:
```json
{
  "action": "get_project_issues",
  "project_key": "pharmacy-orders-service",
  "include": ["severity", "type", "file", "line"],
  "format": "json"
}
```

**Response**:
```json
{
  "issues": [
    {
      "key": "java:S1118",
      "type": "Code Smell",
      "severity": "MAJOR",
      "message": "Async method not awaited",
      "component": "Controllers/OrderController.cs",
      "line": 45,
      "effort": "10min"
    }
  ],
  "total": 197,
  "timestamp": "2026-05-21T10:00:00Z"
}
```

### Touchpoint 2: GitHub Copilot Integration (Refactoring Agent)

**Connection Type**: Copilot API  
**Frequency**: Per task execution  
**Timeout**: 2 minutes  
**Retry Strategy**: Single retry on timeout

**Request**:
```json
{
  "action": "analyze_and_suggest",
  "code_context": "...[current code]...",
  "transformation_type": "async_await_replacement",
  "file_path": "Controllers/OrderController.cs",
  "constraints": {
    "preserve_functionality": true,
    "add_error_handling": true,
    "follow_patterns": ["async/await", "try-catch"]
  },
  "model": "claude-opus-4",
  "temperature": 0.3
}
```

**Response**:
```json
{
  "suggestions": [
    {
      "original_code": "var data = _service.GetDataAsync().Result;",
      "suggested_code": "var data = await _service.GetDataAsync();",
      "confidence": 0.95,
      "explanation": "Replace .Result with await",
      "risks": "None - direct equivalent"
    }
  ],
  "timestamp": "2026-05-21T13:00:00Z"
}
```

### Touchpoint 3: Git Integration (All Agents)

**Connection Type**: Git CLI / GitHub API  
**Frequency**: Per task / per push  
**Timeout**: 5 minutes  
**Retry Strategy**: 2 retries for network issues

**Commands**:
```bash
# Create feature branch
git checkout -b refactor/phase-1-pharmacy

# Stage changes
git add Controllers/OrderController.cs

# Commit with task ID
git commit -m "T001: Replace sync I/O with async in OrderController"

# Push to remote
git push origin refactor/phase-1-pharmacy
```

### Touchpoint 4: Azure DevOps Pipeline Integration (Orchestrator)

**Connection Type**: REST API  
**Frequency**: Once after QA approval  
**Timeout**: 60 minutes  
**Retry Strategy**: No retry (manual intervention if failed)

**Request**:
```json
{
  "action": "run_pipeline",
  "pipeline_id": "DOTNET_DEMO_upgraded_AppMigratetoazure_UseCase2",
  "parameters": {
    "branch": "refactor/phase-1-pharmacy",
    "stage": "Deploy",
    "target_environment": "production"
  }
}
```

**Response** (Polling):
```json
{
  "run_id": "12345",
  "status": "Completed",
  "result": "Succeeded",
  "stages": [
    {
      "name": "Build",
      "status": "Completed",
      "duration": "2m 30s"
    },
    {
      "name": "Test",
      "status": "Completed",
      "duration": "5m 15s"
    },
    {
      "name": "Deploy",
      "status": "Completed",
      "duration": "3m 45s"
    }
  ],
  "deployment_url": "https://webapp-pharmacy-dotnet-demo-uc2.azurewebsites.net"
}
```

---

## ERROR HANDLING & RESILIENCE

### Error Taxonomy

```
LEVEL 1: Connection Errors
├─ SonarQube unavailable → Retry with backoff
├─ Copilot API timeout → Fallback to manual review
└─ Git authentication → Use cached credentials or manual intervention

LEVEL 2: Data Errors
├─ Invalid JSON format → Log and escalate
├─ Missing required fields → Validation error + retry
└─ Data integrity issues → Rollback transaction

LEVEL 3: Execution Errors
├─ Build failure → Rollback commit
├─ Test failure → Manual review required
└─ Deployment failure → Automatic rollback
```

### Retry Strategies

| Error Type | Max Retries | Backoff | Action on Final Failure |
|-----------|-------------|---------|------------------------|
| Network timeout | 3 | Exponential 2s, 4s, 8s | Escalate to Orchestrator |
| Build failure | 1 | None | Rollback + alert team |
| Test failure | 0 | None | Halt + manual review |
| API rate limit | 5 | Exponential | Queue for later |

### Resilience Mechanisms

1. **Atomic Commits**: Each task = one reversible commit
2. **State Checkpointing**: Save state at each gate
3. **Rollback Automation**: Automatic revert on failure
4. **Circuit Breaker**: Stop on repeated failures
5. **Fallback Patterns**: Manual review as last resort

---

## STATE MANAGEMENT

### Project State Machine

```
INIT
  ↓
ASSESSMENT_RUNNING
  ├─ (Success) → ASSESSMENT_COMPLETE ← QG1
  └─ (Failure) → ERROR_STATE
  
PLANNING_RUNNING
  ├─ (Success) → PLANNING_COMPLETE ← QG2
  ├─ (Failure) → PLANNING_RETRY
  └─ (Max Retries) → ERROR_STATE
  
REFACTORING_RUNNING
  ├─ (Per Task Success) → TASK_COMPLETE
  ├─ (Task Failure) → ROLLBACK → TASK_RETRY
  ├─ (All Tasks Complete) → REFACTORING_COMPLETE ← QG3
  └─ (Max Retries) → ERROR_STATE
  
VALIDATION_RUNNING
  ├─ (Success) → VALIDATION_APPROVED ← QG5
  ├─ (Failure) → VALIDATION_FAILED → REFACTORING_RUNNING
  └─ (Conditional) → MANUAL_REVIEW
  
DEPLOYMENT_RUNNING
  ├─ (Success) → DEPLOYMENT_SUCCESS ← QG6
  ├─ (Failure) → AUTO_ROLLBACK → DEPLOYMENT_FAILED
  └─ (Monitoring) → DEPLOYMENT_MONITORING
  
COMPLETE
  ↓
ARCHIVED
```

### State Persistence

**Storage Location**: Git repository + Project database

**State File Structure**:
```json
{
  "project_id": "pharmacy-orders-service",
  "current_state": "REFACTORING_RUNNING",
  "current_phase": 1,
  "current_task": "T003",
  "progress": {
    "total_tasks": 38,
    "completed_tasks": 2,
    "failed_tasks": 0,
    "completion_percentage": 5.3
  },
  "timestamps": {
    "started": "2026-05-21T11:00:00Z",
    "assessment_completed": "2026-05-21T11:30:00Z",
    "planning_completed": "2026-05-21T12:30:00Z",
    "refactoring_started": "2026-05-21T13:00:00Z",
    "current_task_started": "2026-05-21T13:15:00Z"
  },
  "artifacts": {
    "assessment_report": "reports/assessment-20260521.json",
    "migration_plan": "migration/plan-20260521.json",
    "refactoring_branch": "refactor/phase-1-pharmacy"
  }
}
```

---

## QUALITY GATE ENFORCEMENT

### Gate Enforcement Rules

```
QG1: Issues Identified
├─ Condition: total_issues > 5
├─ Enforcement: IF fails → ABORT, mark as "No Issues"
├─ Appeal Process: Override by CTO only
└─ Metrics: Count SonarQube issues

QG2: Plan Valid
├─ Condition: tasks_sequenced AND dependencies_valid
├─ Enforcement: IF fails → Replan (unlimited retries)
├─ Appeal Process: N/A
└─ Metrics: Task count, dependency graph integrity

QG3: Build Success
├─ Condition: dotnet build exit_code == 0
├─ Enforcement: IF fails → Rollback last commit
├─ Appeal Process: Manual review required (build logs)
└─ Metrics: Build result, compilation errors

QG4: Tests Pass
├─ Condition: test_pass_rate == 100% AND coverage >= 70%
├─ Enforcement: IF fails → Halt phase, alert team
├─ Appeal Process: Manual test run + review
└─ Metrics: Test results, coverage report

QG5: QA Approved
├─ Condition: all_acceptance_criteria_met
├─ Enforcement: IF fails → Return to Phase 3
├─ Appeal Process: Tech lead review + manual testing
└─ Metrics: QA report findings

QG6: Deployment Success
├─ Condition: health_checks_pass AND app_responding
├─ Enforcement: IF fails → Auto rollback
├─ Appeal Process: Manual rollback + investigation
└─ Metrics: Deployment logs, health check results
```

---

## SUMMARY TABLE: HANDOFFS & DATA CONTRACTS

| Handoff | From | To | Trigger | Data Size | Gate | Status |
|---------|------|-----|---------|-----------|------|--------|
| HO-001 | Assessment | Planning | Completion | 500KB | QG-1 | Designed |
| HO-002 | Planning | Refactoring | Completion | 200KB | QG-2 | Designed |
| HO-003 | Refactoring | Validation | Completion | 50MB | QG-3 | Designed |
| HO-004 | Validation | Orchestrator | Completion | 10MB | QG-5 | Designed |
| HO-005 | Orchestrator | DevOps | Approval | 5KB | QG-6 | Designed |

---

**Document Version**: 1.0  
**Status**: Ready for Implementation  
**Next Step**: Use this spec to implement inter-agent communication layer
