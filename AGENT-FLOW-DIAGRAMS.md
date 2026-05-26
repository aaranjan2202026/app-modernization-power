# Agent Orchestration Flow & Architecture Diagrams
## Visual Reference for Custom Refactoring Agent Platform

---

## 1. COMPLETE WORKFLOW ORCHESTRATION

```mermaid
graph TD
    A["🚀 START: User Input & Validation"] --> B["📋 ORCHESTRATOR Agent<br/>Receives Application Context<br/>Initializes Project State"]
    
    B -->|Handoff-1| C["🔍 ASSESSMENT Agent<br/>├─ Connect SonarQube<br/>├─ Analyze Code Quality<br/>├─ Generate Baseline<br/>└─ Identify Issues"]
    
    C -->|Output| C1["📊 Assessment Report<br/>├─ 47 Blocker/Critical Issues<br/>├─ 240 hrs Tech Debt<br/>├─ 45% Code Coverage<br/>├─ Priority Issue List<br/>└─ Patterns Detected"]
    
    C1 --> QG1{Quality Gate 1:<br/>Issues Found?}
    QG1 -->|NO| X["⛔ ABORT<br/>No Refactoring Needed"]
    QG1 -->|YES| D
    
    D["📊 PLANNING Agent<br/>├─ Design Strategy<br/>├─ Sequence Tasks<br/>├─ Analyze Dependencies<br/>└─ Create Roadmap"]
    
    D -->|Output| D1["📋 Migration Plan<br/>├─ Phase 1: Quick Wins (12 tasks)<br/>├─ Phase 2: Complex (18 tasks)<br/>├─ Phase 3: Validation (8 tasks)<br/>├─ Dependencies Map<br/>└─ Patterns Library"]
    
    D1 --> QG2{Quality Gate 2:<br/>Plan Valid?}
    QG2 -->|NO| D_RETRY["↩️ Return to Planning<br/>Reanalyze & Refine"]
    D_RETRY --> D
    QG2 -->|YES| E
    
    E["🔧 REFACTORING Agent<br/>├─ Load Plan<br/>├─ Execute Tasks<br/>├─ Apply Copilot Suggestions<br/>├─ Manage Git<br/>└─ Track Progress"]
    
    E -->|Per Task| E1["⚙️ Task Execution<br/>├─ Code Analysis<br/>├─ Copilot Prompt Generation<br/>├─ Apply Transformation<br/>├─ Run Tests<br/>├─ Commit Changes<br/>└─ Move to Next"]
    
    E1 --> QG3{Quality Gate 3:<br/>Build Success?}
    QG3 -->|NO| ROLLBACK["🔄 ROLLBACK<br/>Revert Commit<br/>Alert Team<br/>Manual Fix Required"]
    ROLLBACK --> E
    QG3 -->|YES| F
    
    E -->|Output| F["📝 Refactored Code<br/>├─ 38 Atomic Commits<br/>├─ 23 Files Modified<br/>├─ +340/-210 Lines<br/>├─ Branch: refactor/phase-1<br/>└─ Change Log"]
    
    F --> QG4{Quality Gate 4:<br/>All Tests Pass?}
    QG4 -->|NO| FAIL1["❌ TEST FAILURE<br/>Halt & Investigate"]
    FAIL1 --> E
    QG4 -->|YES| G
    
    G["✅ VALIDATION Agent<br/>├─ Code Review<br/>├─ Run Test Suites<br/>├─ Check Coverage<br/>├─ Detect Regressions<br/>└─ Generate QA Report"]
    
    G -->|Code Review| G1["📋 Code Review<br/>├─ 2 Minor Suggestions<br/>├─ Pattern Compliance<br/>└─ Best Practices Check"]
    
    G -->|Testing| G2["🧪 Test Execution<br/>├─ Unit: 156 pass<br/>├─ Integration: 45 pass<br/>└─ E2E: All pass"]
    
    G -->|Metrics| G3["📊 Metrics Validation<br/>├─ Coverage: 72% ↑27%<br/>├─ Blocker: 0 ↓47<br/>├─ Tech Debt: 145h ↓95h<br/>└─ Code Smells: 28 ↓17"]
    
    G1 --> QG5{Quality Gate 5:<br/>QA Approved?}
    QG5 -->|NO| FAIL2["❌ QA FAILED<br/>Return to Refactoring"]
    FAIL2 --> E
    QG5 -->|YES| H
    
    G2 --> G3
    G3 --> QG5
    
    G -->|Output| G4["✅ QA Report<br/>├─ All Gates: PASS<br/>├─ No Regressions<br/>├─ Deployment Ready<br/>└─ Sign-off: APPROVED"]
    
    G4 --> H["☁️ ORCHESTRATOR (Final)<br/>├─ Approve Deployment<br/>├─ Trigger CI/CD<br/>├─ Monitor Deployment<br/>└─ Verify Health"]
    
    H -->|Deploy| I["🚀 Azure DevOps Pipeline<br/>├─ Build<br/>├─ Test<br/>├─ Deploy to App Service<br/>└─ Health Check"]
    
    I --> QG6{Deployment<br/>Success?}
    QG6 -->|NO| ROLLBACK_PROD["🔄 AUTO ROLLBACK<br/>Restore Previous Version<br/>Alert Team<br/>Investigation"]
    ROLLBACK_PROD --> END_FAIL["❌ DEPLOYMENT FAILED"]
    QG6 -->|YES| J
    
    J["🎉 COMPLETION<br/>├─ Update Metrics<br/>├─ Team Notification<br/>├─ Documentation Updated<br/>├─ Knowledge Recorded<br/>└─ Project Closed"]
    
    J --> END_SUCCESS["✅ SUCCESS<br/>Application Refactored<br/>Quality Improved<br/>Ready for Next Phase"]
    
    style A fill:#90EE90
    style B fill:#87CEEB
    style C fill:#FFB6C1
    style E fill:#FFD700
    style G fill:#DDA0DD
    style H fill:#87CEEB
    style I fill:#FFA500
    style END_SUCCESS fill:#90EE90
    style END_FAIL fill:#FF6B6B
    style X fill:#FF6B6B
    style ROLLBACK fill:#FF6B6B
    style ROLLBACK_PROD fill:#FF6B6B
```

---

## 2. AGENT INTERACTION MATRIX

```mermaid
graph LR
    ORK["🎯 ORCHESTRATOR<br/>(Coordinator)"]
    ASS["🔍 ASSESSMENT<br/>(Analyzer)"]
    PLAN["📋 PLANNING<br/>(Strategist)"]
    REF["🔧 REFACTORING<br/>(Executor)"]
    VAL["✅ VALIDATION<br/>(QA Gate)"]
    
    ORK -->|Trigger| ASS
    ORK -->|Trigger| PLAN
    ORK -->|Trigger| REF
    ORK -->|Trigger| VAL
    ORK -->|Monitor & Coordinate| ORK
    
    ASS -->|Assessment Report| PLAN
    PLAN -->|Migration Plan| REF
    REF -->|Refactored Code| VAL
    VAL -->|QA Report| ORK
    
    ASS -.->|Get Issues| SQ["🔌 SonarQube"]
    PLAN -.->|Read Baseline| ASS
    REF -.->|Get Suggestions| COP["🤖 Copilot"]
    REF -.->|Commit Changes| GIT["📦 Git"]
    VAL -.->|Run Tests| TEST["🧪 Tests"]
    ORK -.->|Trigger Pipeline| PIPE["⚙️ DevOps"]
    
    style ORK fill:#87CEEB,stroke:#000,stroke-width:3px
    style ASS fill:#FFB6C1
    style PLAN fill:#FFD700
    style REF fill:#DDA0DD
    style VAL fill:#98FB98
    style SQ fill:#F0F0F0
    style COP fill:#F0F0F0
    style GIT fill:#F0F0F0
    style TEST fill:#F0F0F0
    style PIPE fill:#F0F0F0
```

---

## 3. DATA FLOW ARCHITECTURE

```mermaid
graph TB
    INPUT["📥 User Input<br/>Application Path<br/>Target Framework<br/>Acceptance Criteria"]
    
    INPUT --> ORK["ORCHESTRATOR<br/>Initializes Project"]
    
    ORK --> ASS["ASSESSMENT Agent<br/>Phase 1: Discover & Assess"]
    
    ASS -->|Query| SONAR["SonarQube MCP<br/>Project Issues<br/>Metrics Export"]
    SONAR -->|Issues Data| ASS_OUT["Assessment Output<br/>├─ Issues by Category<br/>├─ Baseline Metrics<br/>├─ Priority List<br/>└─ Risk Matrix"]
    
    ASS_OUT --> PLAN["PLANNING Agent<br/>Phase 2: Plan Refactoring"]
    
    PLAN -->|Analyze| ARCH["Application Architecture<br/>Dependencies<br/>Modules<br/>Patterns"]
    ARCH -->|Input| PLAN_OUT["Planning Output<br/>├─ Migration Plan<br/>├─ Task Sequence<br/>├─ Quality Gates<br/>└─ Pattern Library"]
    
    PLAN_OUT --> REF["REFACTORING Agent<br/>Phase 3: Execute Transformation"]
    
    REF -->|For Each Task| COPILOT["GitHub Copilot API<br/>Code Analysis<br/>Suggestions<br/>Transformations"]
    COPILOT -->|Suggestions| REF
    
    REF -->|Version Control| GIT["Git Repository<br/>Create Branch<br/>Commit Changes<br/>Push Updates"]
    GIT -->|Refactored Code| REF_OUT["Refactoring Output<br/>├─ Modified Files<br/>├─ Commit History<br/>├─ Feature Branch<br/>└─ Change Log"]
    
    REF_OUT --> VAL["VALIDATION Agent<br/>Phase 4: Review & Validate"]
    
    VAL -->|Run| TESTS["Test Framework<br/>Unit Tests<br/>Integration Tests<br/>E2E Tests"]
    TESTS -->|Results| VAL
    
    VAL -->|Check| METRICS["SonarQube Metrics<br/>Coverage<br/>Code Smells<br/>Technical Debt"]
    METRICS -->|Data| VAL
    
    VAL -->|Output| VAL_OUT["Validation Output<br/>├─ QA Report<br/>├─ Test Results<br/>├─ Coverage Report<br/>└─ Sign-off"]
    
    VAL_OUT --> DEPLOY["ORCHESTRATOR<br/>Phase 5: Deploy"]
    
    DEPLOY -->|Trigger| PIPELINE["Azure DevOps Pipeline<br/>├─ Build Stage<br/>├─ Test Stage<br/>└─ Deploy Stage"]
    
    PIPELINE -->|Deploy To| APP["Azure App Service<br/>Production Application<br/>Live Environment"]
    
    APP -->|Final Output| SUCCESS["✅ SUCCESS<br/>Refactored Application<br/>Metrics Improved<br/>Ready for Use"]
    
    style INPUT fill:#90EE90
    style ORK fill:#87CEEB
    style ASS fill:#FFB6C1
    style PLAN fill:#FFD700
    style REF fill:#DDA0DD
    style VAL fill:#98FB98
    style DEPLOY fill:#87CEEB
    style SUCCESS fill:#90EE90
    style SONAR fill:#F0F0F0
    style COPILOT fill:#F0F0F0
    style GIT fill:#F0F0F0
    style TESTS fill:#F0F0F0
    style METRICS fill:#F0F0F0
    style PIPELINE fill:#F0F0F0
```

---

## 4. QUALITY GATES CHECKPOINT FLOW

```mermaid
graph TD
    A["Start Workflow"] --> B["Phase 1: Assessment"]
    
    B --> QG1{"QG1: Issues<br/>Identified?"}
    QG1 -->|NO| ABORT1["⛔ ABORT<br/>No refactoring needed"]
    QG1 -->|YES| C["Phase 2: Planning"]
    
    C --> QG2{"QG2: Valid<br/>Plan?"}
    QG2 -->|NO| REPLAN["↩️ Replan<br/>Adjust strategy"]
    REPLAN --> C
    QG2 -->|YES| D["Phase 3: Refactoring"]
    
    D --> LOOP1["Execute Task Loop<br/>For Each Task:"]
    LOOP1 --> EXEC["Execute Task<br/>with Copilot"]
    EXEC --> BUILD{"QG3: Build<br/>Success?"}
    BUILD -->|NO| RB1["🔄 Rollback<br/>Previous Commit"]
    RB1 --> EXEC
    BUILD -->|YES| TEST{"QG4: Tests<br/>Pass?"}
    TEST -->|NO| HALT1["⏸️ HALT<br/>Manual fix required"]
    HALT1 --> EXEC
    TEST -->|YES| NEXT{"More<br/>Tasks?"}
    NEXT -->|YES| EXEC
    NEXT -->|NO| E
    
    E["Phase 4: Validation"] --> CODE["Code Review"]
    E --> TESTS_FULL["Full Test Suite"]
    E --> COVERAGE["Coverage Check"]
    CODE --> QG5{"QG5: QA<br/>Pass?"}
    TESTS_FULL --> QG5
    COVERAGE --> QG5
    
    QG5 -->|FAIL| FAIL["❌ Return to<br/>Refactoring"]
    FAIL --> D
    QG5 -->|PASS| F
    
    F["Phase 5: Deployment<br/>Trigger CI/CD Pipeline"] --> DEPLOY{"QG6: Deploy<br/>Success?"}
    DEPLOY -->|NO| RB2["🔄 Auto Rollback<br/>Previous Version"]
    RB2 --> FAIL_DEPLOY["❌ Deployment Failed<br/>Investigation Required"]
    DEPLOY -->|YES| SUCCESS["✅ SUCCESS<br/>Refactored & Deployed"]
    
    style ABORT1 fill:#FF6B6B
    style REPLAN fill:#FFD700
    style RB1 fill:#FF6B6B
    style HALT1 fill:#FF6B6B
    style FAIL fill:#FF6B6B
    style RB2 fill:#FF6B6B
    style FAIL_DEPLOY fill:#FF6B6B
    style SUCCESS fill:#90EE90
    style QG1 fill:#FFB6C1
    style QG2 fill:#FFB6C1
    style BUILD fill:#FFB6C1
    style TEST fill:#FFB6C1
    style QG5 fill:#FFB6C1
    style DEPLOY fill:#FFB6C1
```

---

## 5. AGENT STATE MACHINE & TRANSITIONS

```mermaid
stateDiagram-v2
    [*] --> INIT: User Submits Request
    
    INIT --> ORK_VALIDATE: ORCHESTRATOR Initializes
    ORK_VALIDATE --> ASS_READY: Setup Complete
    
    ASS_READY --> ASS_RUNNING: Assessment Agent Starts
    ASS_RUNNING --> ASS_COMPLETE: SonarQube Analysis Done
    ASS_COMPLETE --> ASS_GATE: QG1 - Issues Found?
    
    ASS_GATE --> ABORT: NO Issues ⛔
    ASS_GATE --> PLAN_READY: YES Issues ✓
    
    PLAN_READY --> PLAN_RUNNING: Planning Agent Starts
    PLAN_RUNNING --> PLAN_COMPLETE: Migration Plan Created
    PLAN_COMPLETE --> PLAN_GATE: QG2 - Plan Valid?
    
    PLAN_GATE --> PLAN_RUNNING: NO - Retry
    PLAN_GATE --> REF_READY: YES - Valid ✓
    
    REF_READY --> REF_RUNNING: Refactoring Agent Starts
    REF_RUNNING --> TASK_EXEC: Execute First Task
    TASK_EXEC --> BUILD_TEST: Run Build & Tests
    
    BUILD_TEST --> BUILD_GATE: QG3 - Build OK?
    BUILD_TEST --> TEST_GATE: QG4 - Tests OK?
    
    BUILD_GATE --> TASK_ROLLBACK: NO ⛔
    BUILD_GATE --> COMMIT: YES ✓
    
    TEST_GATE --> HALT: NO - Manual Fix ⛔
    TEST_GATE --> COMMIT: YES ✓
    
    TASK_ROLLBACK --> TASK_EXEC: Retry Task
    HALT --> TASK_EXEC: Retry Task
    
    COMMIT --> MORE_TASKS: Commit Changes
    MORE_TASKS --> TASK_EXEC: Next Task
    MORE_TASKS --> REF_COMPLETE: No More Tasks
    
    REF_COMPLETE --> VAL_READY: Refactoring Done
    VAL_READY --> VAL_RUNNING: Validation Agent Starts
    VAL_RUNNING --> VAL_REVIEW: Code Review
    VAL_RUNNING --> VAL_TESTS: Test Execution
    VAL_RUNNING --> VAL_COVERAGE: Coverage Check
    
    VAL_REVIEW --> VAL_GATE: QG5 - QA Pass?
    VAL_TESTS --> VAL_GATE
    VAL_COVERAGE --> VAL_GATE
    
    VAL_GATE --> REF_RUNNING: NO - Return ⛔
    VAL_GATE --> VAL_COMPLETE: YES - Approved ✓
    
    VAL_COMPLETE --> DEPLOY_GATE: Validation Complete
    DEPLOY_GATE --> DEPLOY_RUNNING: Trigger DevOps Pipeline
    
    DEPLOY_RUNNING --> DEPLOY_SUCCESS: QG6 - Deploy OK?
    
    DEPLOY_SUCCESS --> DEPLOY_FAIL: NO - Rollback ⛔
    DEPLOY_SUCCESS --> COMPLETE: YES - Success ✓
    
    DEPLOY_FAIL --> FAILURE: Deployment Failed
    COMPLETE --> SUCCESS: Application Refactored
    
    ABORT --> SKIP: No Changes Needed
    SKIP --> [*]
    SUCCESS --> [*]
    FAILURE --> [*]
    
    note right of TASK_EXEC
        Atomic per task
        Version control
        Copilot-assisted
    end note
    
    note right of VAL_RUNNING
        Comprehensive QA
        No regressions
        Coverage validated
    end note
```

---

## 6. INTEGRATION ARCHITECTURE OVERVIEW

```mermaid
graph TB
    subgraph AGENTS["Custom Agents Platform"]
        ORK["🎯 Orchestrator<br/>(Central Hub)"]
        ASS["🔍 Assessment"]
        PLAN["📋 Planning"]
        REF["🔧 Refactoring"]
        VAL["✅ Validation"]
    end
    
    subgraph EXTERNAL["External Systems & Services"]
        SQ["SonarQube/Cloud<br/>Code Quality Analysis"]
        COP["GitHub Copilot<br/>AI Refactoring"]
        GIT["Git Repo<br/>Version Control"]
        TESTS["Test Framework<br/>Unit/Integration/E2E"]
        DEVOPS["Azure DevOps<br/>CI/CD Pipeline"]
    end
    
    subgraph OUTPUTS["Deliverables & Reports"]
        ASS_RPT["Assessment Report"]
        PLAN_RPT["Migration Plan"]
        REF_CODE["Refactored Code"]
        QA_RPT["QA Report"]
        DEPLOY_RPT["Deployment Status"]
    end
    
    ORK -->|Coordinate| ASS
    ORK -->|Coordinate| PLAN
    ORK -->|Coordinate| REF
    ORK -->|Coordinate| VAL
    ORK -->|Trigger| DEVOPS
    
    ASS -->|Query Issues| SQ
    ASS -->|Generate| ASS_RPT
    
    PLAN -->|Consume| ASS_RPT
    PLAN -->|Generate| PLAN_RPT
    
    REF -->|Consume| PLAN_RPT
    REF -->|Get Suggestions| COP
    REF -->|Commit| GIT
    REF -->|Generate| REF_CODE
    
    VAL -->|Consume| REF_CODE
    VAL -->|Run Tests| TESTS
    VAL -->|Check Metrics| SQ
    VAL -->|Generate| QA_RPT
    
    DEVOPS -->|Deploy| GIT
    DEVOPS -->|Generate| DEPLOY_RPT
    
    style ORK fill:#87CEEB,stroke:#000,stroke-width:3px
    style ASS fill:#FFB6C1
    style PLAN fill:#FFD700
    style REF fill:#DDA0DD
    style VAL fill:#98FB98
    style SQ fill:#E8E8E8
    style COP fill:#E8E8E8
    style GIT fill:#E8E8E8
    style TESTS fill:#E8E8E8
    style DEVOPS fill:#E8E8E8
    style ASS_RPT fill:#90EE90
    style PLAN_RPT fill:#90EE90
    style REF_CODE fill:#90EE90
    style QA_RPT fill:#90EE90
    style DEPLOY_RPT fill:#90EE90
```

---

## 7. TASK EXECUTION LOOP (Refactoring Agent Detail)

```mermaid
graph TD
    START["🎬 Start Task<br/>Load: Task ID, Files,<br/>Acceptance Criteria"]
    
    START --> LOAD["📂 Load Code<br/>Read target files<br/>Parse AST<br/>Build context"]
    
    LOAD --> ANALYZE["🔍 Analyze Code<br/>Detect patterns<br/>Identify issues<br/>Find opportunities"]
    
    ANALYZE --> PROMPT["💬 Generate Copilot Prompt<br/>├─ Current code<br/>├─ Pattern to apply<br/>├─ Transformation rules<br/>└─ Constraints"]
    
    PROMPT --> COPILOT["🤖 Copilot Analysis<br/>├─ Suggestion 1<br/>├─ Suggestion 2<br/>└─ Alternative 3"]
    
    COPILOT --> REVIEW["👁️ Review Suggestions<br/>Validate against:<br/>├─ Pattern fit<br/>├─ Functional impact<br/>└─ Best practices"]
    
    REVIEW --> SELECT["✅ Select Best<br/>Transform"]
    
    SELECT --> APPLY["🔧 Apply Changes<br/>├─ Replace code<br/>├─ Update imports<br/>├─ Format code<br/>└─ Validate syntax"]
    
    APPLY --> BUILD["🔨 Build Check<br/>dotnet build<br/>--no-restore"]
    
    BUILD --> BUILD_OK{Build<br/>Success?}
    BUILD_OK -->|NO| ROLLBACK1["↩️ Revert<br/>Back to original"]
    ROLLBACK1 --> MANUAL["🚨 Manual Review<br/>Task Failed<br/>Requires Manual Fix"]
    BUILD_OK -->|YES| TEST["🧪 Run Tests<br/>├─ Unit tests<br/>├─ Affected tests<br/>└─ Build artifact"]
    
    TEST --> TEST_OK{Tests<br/>Pass?}
    TEST_OK -->|NO| ROLLBACK2["↩️ Revert<br/>Back to original"]
    ROLLBACK2 --> MANUAL
    TEST_OK -->|YES| VALIDATE["✅ Validate Criteria<br/>├─ Acceptance met<br/>├─ No regressions<br/>└─ Pattern applied"]
    
    VALIDATE --> CRITERIA_OK{Criteria<br/>Met?}
    CRITERIA_OK -->|NO| MODIFY["🔄 Modify Approach<br/>Try alternative<br/>or manual fix"]
    MODIFY --> COPILOT
    CRITERIA_OK -->|YES| COMMIT["💾 Commit Changes<br/>git commit<br/>-m 'T###: Description'"]
    
    COMMIT --> LOG["📝 Log Result<br/>Task ID: T###<br/>Status: COMPLETE<br/>Files: 3 modified<br/>Commit: abc123"]
    
    LOG --> NEXT["➡️ Next Task<br/>Return to<br/>Task Loop"]
    
    style START fill:#90EE90
    style BUILD_OK fill:#FFB6C1
    style TEST_OK fill:#FFB6C1
    style CRITERIA_OK fill:#FFB6C1
    style MANUAL fill:#FF6B6B
    style COMMIT fill:#90EE90
    style NEXT fill:#87CEEB
```

---

## 8. ERROR HANDLING & RECOVERY FLOW

```mermaid
graph TD
    ERROR["⚠️ Error Detected"]
    
    ERROR --> CLASSIFY{"Error<br/>Type?"}
    
    CLASSIFY -->|Build Failure| BUILD_ERR["🔴 Build Error<br/>├─ Log error<br/>├─ Identify issue<br/>└─ Rollback commit"]
    BUILD_ERR --> RETRY_BUILD{"Retry?"}
    RETRY_BUILD -->|YES| TASK_RETRY["↩️ Retry Task<br/>Apply alternative<br/>approach"]
    RETRY_BUILD -->|NO| MANUAL_BUILD["🚨 Manual Fix Required<br/>Alert team lead"]
    TASK_RETRY --> RESOLVE
    MANUAL_BUILD --> RESOLVE
    
    CLASSIFY -->|Test Failure| TEST_ERR["🟠 Test Failure<br/>├─ Identify test<br/>├─ Check regression<br/>└─ Analyze code change"]
    TEST_ERR --> ANALYZE_TEST{"Root Cause?"}
    ANALYZE_TEST -->|Code Bug| FIX_CODE["🔧 Fix Code<br/>Apply Copilot fix<br/>or manual patch"]
    ANALYZE_TEST -->|Test Issue| FIX_TEST["🧪 Fix Test<br/>Update test<br/>or assertion"]
    FIX_CODE --> RETRY_TEST["🔄 Retry Test"]
    FIX_TEST --> RETRY_TEST
    RETRY_TEST --> RESOLVE
    
    CLASSIFY -->|QA Failure| QA_ERR["🟡 QA Rejection<br/>├─ Review comments<br/>├─ Analyze impact<br/>└─ Plan fix"]
    QA_ERR --> DECIDE_QA{"Critical<br/>Issue?"}
    DECIDE_QA -->|YES| REJECT["❌ Reject Refactoring<br/>Return to Phase 3"]
    DECIDE_QA -->|NO| QA_FIX["🔧 Apply Fix<br/>Address feedback"]
    QA_FIX --> REVALIDATE["✅ Revalidate<br/>with QA"]
    REVALIDATE --> RESOLVE
    
    CLASSIFY -->|Deployment Failure| DEPLOY_ERR["🔴 Deploy Failed<br/>├─ Capture error log<br/>├─ Automatic rollback<br/>└─ Alert on-call"]
    DEPLOY_ERR --> INVESTIGATE["🔍 Investigation<br/>├─ Root cause<br/>├─ Post-mortem<br/>└─ Fix plan"]
    INVESTIGATE --> RESOLVE
    
    RESOLVE["✅ Resolved<br/>Ready to Proceed"]
    
    style ERROR fill:#FF6B6B
    style BUILD_ERR fill:#FF6B6B
    style TEST_ERR fill:#FF6B6B
    style QA_ERR fill:#FF6B6B
    style DEPLOY_ERR fill:#FF6B6B
    style MANUAL_BUILD fill:#FF6B6B
    style REJECT fill:#FF6B6B
    style RESOLVE fill:#90EE90
    style TASK_RETRY fill:#FFD700
    style QA_FIX fill:#FFD700
    style REVALIDATE fill:#FFD700
```

---

## 9. METRICS TRACKING & REPORTING

```mermaid
graph LR
    subgraph COLLECTION["Metrics Collection"]
        M1["Assessment Phase<br/>├─ Initial Issues Count<br/>├─ Code Quality Score<br/>├─ Technical Debt Hours<br/>└─ Code Coverage %"]
        
        M2["Refactoring Phase<br/>├─ Tasks Completed<br/>├─ Files Modified<br/>├─ Lines Changed<br/>└─ Commits Count"]
        
        M3["Validation Phase<br/>├─ Test Results<br/>├─ Coverage % (After)<br/>├─ Issues Remaining<br/>└─ No Regressions"]
        
        M4["Deployment Phase<br/>├─ Deploy Time<br/>├─ Health Status<br/>├─ Performance Impact<br/>└─ Error Rates"]
    end
    
    subgraph ANALYSIS["Analysis & Comparison"]
        COMPARE["Compare Before/After<br/>├─ Issues Reduced: X%<br/>├─ Debt Reduced: Y%<br/>├─ Coverage Improved: Z%<br/>└─ Quality Score: Δ"]
        
        TREND["Trend Analysis<br/>├─ Quality trajectory<br/>├─ Efficiency metrics<br/>├─ Team velocity<br/>└─ Cost savings"]
    end
    
    subgraph OUTPUT["Reports & Dashboards"]
        EXEC_REPORT["Executive Report<br/>✓ Metrics achieved<br/>✓ Quality improved<br/>✓ Cost/benefit<br/>✓ Lessons learned"]
        
        TECH_REPORT["Technical Report<br/>✓ Changes applied<br/>✓ Patterns used<br/>✓ Risks mitigated<br/>✓ Knowledge captured"]
        
        DASHBOARD["Live Dashboard<br/>✓ Real-time status<br/>✓ Progress tracking<br/>✓ Quality metrics<br/>✓ Team performance"]
    end
    
    M1 --> COMPARE
    M2 --> COMPARE
    M3 --> COMPARE
    M4 --> COMPARE
    
    COMPARE --> TREND
    TREND --> EXEC_REPORT
    TREND --> TECH_REPORT
    TREND --> DASHBOARD
    
    EXEC_REPORT -->|Share| STAKEHOLDERS["Stakeholders<br/>Leadership<br/>Product Team<br/>Finance"]
    
    TECH_REPORT -->|Share| DEVTEAM["Development Team<br/>Architects<br/>QA<br/>DevOps"]
    
    DASHBOARD -->|Monitor| ONGOING["Ongoing Monitoring<br/>Continuous improvement<br/>Next iterations<br/>Scaling"]
    
    style COLLECTION fill:#E3F2FD
    style ANALYSIS fill:#FFF3E0
    style OUTPUT fill:#F3E5F5
    style STAKEHOLDERS fill:#E8F5E9
    style DEVTEAM fill:#E8F5E9
    style ONGOING fill:#E8F5E9
```

---

## 10. SUCCESS CRITERIA VALIDATION MATRIX

```mermaid
graph TB
    CRITERIA["Success Criteria Check"]
    
    CRITERIA --> C1{"1. Code Areas<br/>Identified?"}
    C1 -->|✓| C1_PASS["✅ PASS<br/>SonarQube analysis<br/>complete"]
    C1 -->|✗| C1_FAIL["❌ FAIL<br/>Retry assessment"]
    
    CRITERIA --> C2{"2. Quality<br/>Improved?"}
    C2 -->|Score ≥75| C2_PASS["✅ PASS<br/>Quality score<br/>improved"]
    C2 -->|Score <75| C2_FAIL["❌ FAIL<br/>Needs more work"]
    
    CRITERIA --> C3{"3. No<br/>Regression?"}
    C3 -->|All tests pass| C3_PASS["✅ PASS<br/>100% test<br/>success rate"]
    C3 -->|Any fail| C3_FAIL["❌ FAIL<br/>Return to fix"]
    
    CRITERIA --> C4{"4. Duplication<br/>Reduced?"}
    C4 -->|Reduced >30%| C4_PASS["✅ PASS<br/>Code duplication<br/>eliminated"]
    C4 -->|<30% reduction| C4_FAIL["❌ FAIL<br/>Continue optimization"]
    
    CRITERIA --> C5{"5. Tests<br/>Added/Updated?"}
    C5 -->|Coverage ≥70%| C5_PASS["✅ PASS<br/>Test coverage<br/>acceptable"]
    C5 -->|<70%| C5_FAIL["❌ FAIL<br/>Add more tests"]
    
    CRITERIA --> C6{"6. Code<br/>Reviewed?"}
    C6 -->|QA approved| C6_PASS["✅ PASS<br/>Code review<br/>complete"]
    C6 -->|QA rejected| C6_FAIL["❌ FAIL<br/>Address feedback"]
    
    CRITERIA --> C7{"7. Changes<br/>Documented?"}
    C7 -->|Doc complete| C7_PASS["✅ PASS<br/>Changes<br/>documented"]
    C7 -->|Doc missing| C7_FAIL["❌ FAIL<br/>Complete docs"]
    
    CRITERIA --> C8{"8. Deployed<br/>Successfully?"}
    C8 -->|Deploy OK| C8_PASS["✅ PASS<br/>Production<br/>ready"]
    C8 -->|Deploy fail| C8_FAIL["❌ FAIL<br/>Troubleshoot"]
    
    C1_PASS --> SUMMARY["📊 Summary"]
    C2_PASS --> SUMMARY
    C3_PASS --> SUMMARY
    C4_PASS --> SUMMARY
    C5_PASS --> SUMMARY
    C6_PASS --> SUMMARY
    C7_PASS --> SUMMARY
    C8_PASS --> SUMMARY
    
    SUMMARY --> ALL_PASS{"All<br/>Criteria<br/>Met?"}
    ALL_PASS -->|YES| SUCCESS["🎉 SUCCESS<br/>Project Approved<br/>Ready for Production"]
    ALL_PASS -->|NO| FAIL_LIST["⚠️ Failures:<br/>Return to Phase 3"]
    FAIL_LIST --> CRITERIA
    
    style SUCCESS fill:#90EE90,stroke:#000,stroke-width:3px
    style FAIL_LIST fill:#FF6B6B
    style C1_PASS fill:#90EE90
    style C2_PASS fill:#90EE90
    style C3_PASS fill:#90EE90
    style C4_PASS fill:#90EE90
    style C5_PASS fill:#90EE90
    style C6_PASS fill:#90EE90
    style C7_PASS fill:#90EE90
    style C8_PASS fill:#90EE90
    style C1_FAIL fill:#FF6B6B
    style C2_FAIL fill:#FF6B6B
    style C3_FAIL fill:#FF6B6B
    style C4_FAIL fill:#FF6B6B
    style C5_FAIL fill:#FF6B6B
    style C6_FAIL fill:#FF6B6B
    style C7_FAIL fill:#FF6B6B
    style C8_FAIL fill:#FF6B6B
```

---

## Quick Reference: Agent Responsibilities Summary

| Agent | Phase | Input | Primary Action | Output | Next Agent |
|-------|-------|-------|-----------------|--------|------------|
| **Orchestrator** | All | User request | Coordinate flow, enforce gates | Project status | All |
| **Assessment** | 1 | App path + SonarQube key | Analyze code quality | Assessment report | Planning |
| **Planning** | 2 | Assessment report | Design strategy, sequence tasks | Migration plan | Refactoring |
| **Refactoring** | 3 | Migration plan | Apply Copilot suggestions | Refactored code | Validation |
| **Validation** | 4 | Refactored code | Code review + testing | QA report | Orchestrator |
| **Orchestrator** | 5 | QA approval | Trigger deployment | Deployment status | Users |

---

**Document Version**: 1.0  
**Last Updated**: May 21, 2026  
**Status**: Ready for Review
