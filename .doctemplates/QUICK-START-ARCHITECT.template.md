# Architect Quick Start - [Solution Name] Modernization

> **Technical deep-dive for architects, lead developers, and technical decision-makers on [Solution Name] modernization architecture, patterns, and implementation roadmap**

**Target Audience**: Solution Architects, Technical Leads, Senior Developers, Platform Engineers
**Est. Reading Time**: 30-45 minutes
**Prerequisites**: Understanding of cloud architecture patterns, containerization, and microservices

---

## 📋 Table of Contents

- [Architecture Overview](#architecture-overview)
- [Current State Architecture](#current-state-architecture)
- [Target State Architecture](#target-state-architecture)
- [Architecture Decision Records (ADRs)](#architecture-decision-records-adrs)
- [Migration Patterns & Strategies](#migration-patterns--strategies)
- [Component Breakdown](#component-breakdown)
- [Data Architecture & Migration](#data-architecture--migration)
- [Security Architecture](#security-architecture)
- [Operational Excellence](#operational-excellence)
- [Technical Risks & Mitigations](#technical-risks--mitigations)
- [Performance & Scalability](#performance--scalability)
- [Implementation Roadmap](#implementation-roadmap)
- [Reference Implementation](#reference-implementation)

---

## 🏗️ Architecture Overview

### Current vs Target State Summary

**Current Architecture Pattern**: <!-- e.g., Monolithic 3-tier web application -->
**Target Architecture Pattern**: <!-- e.g., Cloud-native containerized microservices -->

| Aspect | Current State | Target State | Improvement |
|--------|---------------|--------------|-------------|
| **Deployment** | <!-- e.g., Manual IIS deployment --> | <!-- e.g., Automated container deployment --> | <!-- Benefit with evidence basis --> |
| **Scalability** | <!-- e.g., Vertical only (limit: 32GB RAM) --> | <!-- e.g., Horizontal auto-scaling --> | <!-- Capability improvement --> |
| **Availability** | <!-- e.g., Single-instance (SLA: 95%) --> | <!-- e.g., Multi-region (target SLA: 99.9%) --> | <!-- ⚠️ Current SLA from monitoring data, Target SLA with basis --> |
| **Recovery** | <!-- e.g., Daily backups, 24hr RPO/RTO --> | <!-- e.g., Point-in-time restore, <1hr RPO/RTO --> | <!-- ⚠️ Current from DR plan, Target from cloud provider capabilities --> |

⚠️ **Metrics Disclaimer**:
- **Current metrics**: Based on <!-- [actual monitoring data, OR estimates from system analysis] -->
- **Target metrics**: Based on <!-- [cloud provider SLA commitments, industry benchmarks, OR "capability goals requiring validation"] -->
- **"Improvement" claims**: Require validation through <!-- [load testing, PoC deployment, production monitoring] -->

**Key Architecture Documents**:
- **Current State**: [System Architecture](../CURRENT-STATE/1-architecture/1.1-system-architecture.md)
- **Target State**: [Modernization Strategy](../MODERNIZATION/2-strategy/2.3-modernization-strategy.md)
- **Technical Details**: [Implementation Plan](../MODERNIZATION/3-execution/3.1-detailed-implementation-plan.md)

---

## 🔍 Current State Architecture

### System Context (C4 Level 1)

```
[Diagram placeholder: System context showing external actors and systems]
```

**Key Components**:
- <!-- List with evidence from analysis -->

**Technology Stack** (from analysis):
<!-- List actual versions found in dependency analysis
- Backend: [Framework] version [X.Y.Z]
- Database: [DB] version [X.Y]
- Frontend: [Framework] version [X.Y]
- Infrastructure: [Platform] version [X.Y]
-->

**Constraints**:
<!-- List technical constraints with evidence
- [Constraint]: Description (Source: [analysis finding, infrastructure limitation])
-->

### Current Architecture Diagram (C4 Level 2)

```
[Diagram placeholder: Container diagram showing current runtime components]
```

**Component Interactions**:
<!-- Describe with evidence from code analysis
- [Component A] → [Component B]: [protocol/pattern]
  - Source: [Code reference: file/class]
  - Data flow: [What is exchanged]
-->

### Current State Challenges

| Challenge | Technical Impact | Business Impact | Evidence |
|-----------|------------------|-----------------|----------|
| **[Challenge]** | <!-- Technical problem --> | <!-- Business cost --> | <!-- Reference: analysis artifact --> |

---

## ☁️ Target State Architecture

### Target System Context (C4 Level 1)

```
[Diagram placeholder: Modernized system context]
```

**Changes from Current**:
- ✅ <!-- Change with rationale -->

### Target Architecture Diagram (C4 Level 2)

```
[Diagram placeholder: Target container architecture]
```

**New Components**:
<!-- List with purpose and rationale
- **[Component]**: Purpose
  - Replaces: [Current component/approach]
  - Rationale: [Why this component - technical/business justification]
  - Service: [Specific cloud service, SKU if determined]
-->

### Architecture Patterns Applied

| Pattern | Application | Benefit | Reference |
|---------|-------------|---------|-----------|
| **[Pattern Name]** | <!-- How it's used --> | <!-- What it solves --> | <!-- ADR or doc reference --> |

---

## 📝 Architecture Decision Records (ADRs)

### ADR-001: [Decision Title]

**Status**: <!-- PROPOSED / ACCEPTED / DEPRECATED / SUPERSEDED -->
**Date**: <!-- YYYY-MM-DD -->
**Deciders**: <!-- Who made this decision -->

**Context**:
<!-- What is the issue motivating this decision? Include technical and business context.
- Current problem: [Description with evidence from analysis]
- Constraints: [Technical, organizational, budgetary]
- Requirements: [What the solution must achieve]
-->

**Decision**:
<!-- What is the change we're proposing/making? -->

**Alternatives Considered**:

1. **Option A**: [Description]
   - ✅ Pros: <!-- Advantages with evidence basis -->
   - ❌ Cons: <!-- Disadvantages with evidence -->
   - ⚠️ **Cost**: <!-- IF derivable: calculation. ELSE: "Estimation requires: [data needed]" -->
   - ⚠️ **Complexity**: <!-- Evidence-based assessment -->
   
2. **Option B**: [Description]
   - ✅ Pros:
   - ❌ Cons:
   - ⚠️ **Cost**:
   - ⚠️ **Complexity**:

**Decision Criteria**:
<!-- How did we evaluate options? What were the priorities?
- [Criterion 1]: Weight/importance
- [Criterion 2]: Weight/importance
-->

**Decision Matrix**:

| Criterion | Weight | Option A | Option B | Selected Option |
|-----------|--------|----------|----------|-----------------|
| **[Criterion]** | <!-- Importance --> | <!-- Score × Weight --> | <!-- Score × Weight --> | <!-- Score × Weight --> |
| **Total** | | <!-- Sum --> | <!-- Sum --> | <!-- Sum --> |

**Rationale**:
<!-- Why did we choose the selected option based on decision matrix? -->

**Consequences**:
- ✅ **Positive**: <!-- Benefits with evidence -->
- ❌ **Negative**: <!-- Tradeoffs and costs -->
- ⚠️ **Risks**: <!-- What could go wrong -->

**Validation**:
<!-- How will we validate this decision was correct?
- Success metrics: [Measurable criteria]
- Validation approach: [PoC, pilot, monitoring]
- Review timeline: [When to reassess]
-->

**References**:
- [Related Document](path)
- [External Reference](URL) - <!-- Brief description -->

---

<!-- Repeat ADR template for each major architecture decision
Typical ADRs for modernization:
- Cloud platform selection
- Compute service selection (VM vs Container vs Serverless)
- Database migration strategy
- Authentication/authorization approach
- API gateway pattern
- Monitoring/observability solution
- CI/CD pipeline technology
- Container orchestration platform
- Network architecture
- Secrets management
-->

---

## 🔄 Migration Patterns & Strategies

### Migration Strategy Overview

**Primary Pattern**: **[e.g., Strangler Fig Pattern]**

**Pattern Description**:
<!-- Explain the pattern with references
- What it is: [Definition with citation if external pattern]
- How it works: [Mechanics]
- Why chosen: [Rationale with evidence]
-->

**Implementation Approach**:
<!-- Describe concrete implementation
1. Step/Phase: What happens
2. Step/Phase: Dependencies and prerequisites
-->

### Migration Phases

#### Phase 1: [Phase Name]

**Objective**: <!-- What this phase achieves -->
**Scope**: <!-- What components are migrated -->

**Technical Approach**:
<!-- Describe specific migration steps
1. [Step]: Technical action
   - Input: What's needed
   - Process: How it's done
   - Output: What's produced
   - Validation: How success is verified
-->

**Dependencies**:
- ✅ **Technical**: <!-- Required before phase can start -->
- ✅ **Organizational**: <!-- Approvals, resources needed -->

⚠️ **Duration Estimate** (if evidence-based):
- **Estimated Duration**: <!-- X-Y weeks/months -->
- **Basis**: 
  - <!-- Task breakdown: [N] stories/tasks -->
  - <!-- Team capacity: [M] FTE available -->
  - <!-- Velocity assumption: [story points or tasks per sprint] from [source: similar project, industry benchmark, OR "TBD - establish baseline"] -->
  - <!-- Risk buffer: [%] for [specific risks] -->
- **Confidence**: <!-- HIGH/MEDIUM/LOW based on [rationale] -->

⚠️ **Duration Unknown** (if not evidence-based):
- **Cannot estimate because**: <!-- [missing task breakdown, unknown team size, uncertain complexity] -->
- **To establish timeline**: <!-- [required analysis/planning activities] -->
- **Sequencing**: Must complete after <!-- [dependencies] -->

**Success Criteria**:
<!-- Measurable, objective criteria
- ✅ [Criterion]: How measured
-->

**Rollback Plan**:
- **Trigger**: <!-- When to rollback -->
- **Process**: <!-- How to rollback -->
- **Data Safety**: <!-- How data is protected -->
- **Rollback Time**: <!-- Estimated duration with basis -->

<!-- Repeat for each migration phase -->

---

## 🧩 Component Breakdown

### Component: [Component Name]

**Current Implementation**:
- **Technology**: <!-- Framework/platform with version -->
- **Location**: <!-- File path or project reference -->
- **Dependencies**: <!-- What it depends on -->
- **Complexity**: <!-- Lines of code, function/class count from analysis -->

**Target Implementation**:
- **Technology**: <!-- Target framework/platform with version -->
- **Cloud Service**: <!-- Specific service (if applicable) with SKU -->
- **Rationale**: <!-- Why this service/technology -->

**Migration Approach**:
<!-- Specific migration strategy for this component
- Pattern: [Rehost / Replatform / Refactor / Rebuild / Replace]
- Steps: [Concrete technical steps]
- Challenges: [Specific technical challenges with this component]
- Mitigations: [How challenges are addressed]
-->

⚠️ **Effort Estimate** (if evidence-based):
- **Estimated Effort**: <!-- X-Y person-days/weeks -->
- **Basis**: 
  - <!-- Complexity metrics: [LOC, file count, dependency count] from analysis -->
  - <!-- Comparison: [similar component migration data] -->
  - <!-- Assumptions: [skill level, tooling available, reusability] -->
- **Confidence**: <!-- HIGH/MEDIUM/LOW -->

⚠️ **Effort Unknown** (if not evidence-based):
- **Cannot estimate because**: <!-- [unknown refactoring scope, unclear cloud service mapping, dependency complexity] -->
- **To establish effort**: <!-- [code analysis, PoC migration, architectural spike] -->

**Testing Strategy**:
- **Unit Tests**: <!-- Approach -->
- **Integration Tests**: <!-- Approach -->
- **Acceptance Criteria**: <!-- Measurable criteria -->

<!-- Repeat for major components or component categories -->

---

## 💾 Data Architecture & Migration

### Current Data Architecture

**Databases**:
<!-- List with evidence from analysis
- **[Database Name]**: [Type] version [X.Y]
  - Size: [GB/TB] (source: [database stats, OR "estimated from table/row counts"])
  - Tables: [N] (from schema analysis)
  - Key entities: [List]
  - Complexity: [relationships, constraints, stored procedures count]
-->

**Data Challenges**:
<!-- List with evidence
- [Challenge]: Description
  - Impact: [Technical impact]
  - Evidence: [Analysis finding reference]
-->

### Target Data Architecture

**Target Databases**:
<!-- List with rationale
- **[Database Service]**: [Purpose]
  - SKU: [Specific tier with justification]
  - Migration from: [Source database]
  - Changes required: [Schema changes, if any]
-->

### Data Migration Strategy

**Migration Pattern**: <!-- e.g., Dual-write, Snapshot-migrate, CDC-based -->

**Migration Steps**:
<!-- Detailed technical approach
1. **[Phase]**: 
   - Process: [What happens]
   - Data validation: [How data integrity is verified]
   - Rollback: [How to revert]
-->

⚠️ **Migration Duration Estimate** (if evidence-based):
- **Estimated Duration**: <!-- X hours/days -->
- **Basis**:
  - <!-- Data volume: [X GB] from analysis -->
  - <!-- Transfer rate: [MB/s] from [network test, cloud provider specs, OR assumption] -->
  - <!-- Calculation: volume ÷ rate × safety factor -->
  - <!-- Validation time: [estimated based on row count and validation approach] -->
- **Downtime Required**: <!-- IF required: duration with basis. ELSE: "Zero-downtime via [pattern]" -->

⚠️ **Migration Duration Unknown**:
- **Cannot estimate because**: <!-- [unknown data volume, network throughput untested, validation complexity unclear] -->
- **To establish duration**: <!-- [database statistics query, network performance test, migration tool PoC] -->

**Data Validation**:
<!-- How to ensure data integrity
- Row count verification
- Checksum validation
- Sample data comparison
- Business logic validation
-->

---

## 🔒 Security Architecture

### Current Security Posture

**Authentication**: <!-- Current mechanism with evidence -->
**Authorization**: <!-- Current approach -->
**Data Protection**: <!-- Encryption, masking, etc. -->

**Security Findings** (from analysis):
<!-- List vulnerabilities with evidence
- **[OWASP Category]**: [Specific issue]
  - Severity: [CRITICAL/HIGH/MEDIUM/LOW]
  - Location: [Code reference]
  - Evidence: [Analysis finding]
-->

### Target Security Architecture

**Authentication**: 
- **Mechanism**: <!-- e.g., OAuth 2.0 + OIDC -->
- **Provider**: <!-- e.g., Azure AD, Auth0 -->
- **Implementation**: <!-- Technical approach -->

**Authorization**:
- **Model**: <!-- e.g., RBAC, ABAC -->
- **Implementation**: <!-- Specific approach -->

**Data Protection**:
- **Encryption at Rest**: <!-- Service, key management -->
- **Encryption in Transit**: <!-- TLS version, cert management -->
- **Key Management**: <!-- Service/approach -->

**Security Controls**:
<!-- List security enhancements
- [Control]: Implementation
  - Addresses: [Current vulnerability/gap]
  - Standard: [Compliance framework if applicable]
-->

**Secrets Management**:
- **Current**: <!-- How secrets are managed (from analysis) -->
- **Target**: <!-- e.g., Azure Key Vault, AWS Secrets Manager -->
- **Migration**: <!-- How secrets will be migrated -->

---

## 🎯 Operational Excellence

### Observability Strategy

**Monitoring**:
- **Service**: <!-- e.g., Azure Monitor, Datadog -->
- **Metrics Collected**: <!-- List key metrics -->
- **Alerting**: <!-- Alert strategy -->

**Logging**:
- **Service**: <!-- e.g., Azure Log Analytics -->
- **Log Levels**: <!-- Strategy -->
- **Retention**: <!-- Duration with compliance basis -->

**Distributed Tracing**:
- **Implementation**: <!-- e.g., Application Insights, OpenTelemetry -->
- **Span collection**: <!-- What's traced -->

### Service Level Objectives (SLOs)

⚠️ **SLO Disclaimer**:
- **Current baselines**: Based on <!-- [actual monitoring data for X months, OR "not currently measured"] -->
- **Target SLOs**: Based on <!-- [business requirements, cloud provider capabilities, industry benchmarks] -->
- **Achievability**: Requires <!-- [specific architectural patterns, redundancy, monitoring] -->

| Service | Metric | Current Baseline | Target SLO | Rationale |
|---------|--------|------------------|------------|-----------|
| **[Service]** | <!-- e.g., Availability --> | <!-- X% (source: [monitoring data OR "unknown"]) --> | <!-- Y% --> | <!-- Why this target: [business requirement, cloud SLA, benchmark] --> |
| **[Service]** | <!-- e.g., Latency (p95) --> | <!-- Xms (source: [APM data OR "unknown"]) --> | <!-- Yms --> | <!-- Rationale --> |

**Baseline Establishment** (if unknown):
- **Current metrics unknown because**: <!-- [no monitoring, incomplete coverage, not retained] -->
- **To establish baseline**: <!-- [implement monitoring, 30-day collection period, analysis] -->
- **Interim approach**: <!-- [industry benchmarks as proxy, conservative targets] -->

**Error Budget**:
- **Calculation**: <!-- 100% - SLO = error budget -->
- **Policy**: <!-- How error budget is used for deployment decisions -->

### DORA Metrics Targets

⚠️ **DORA Metrics Disclaimer**:
- **Current state**: Based on <!-- [git/deployment analysis, OR "manual process - not measured"] -->
- **Target benchmarks**: From <!-- [DORA State of DevOps Report YYYY, specific industry/company size category] -->
- **Achievement timeline**: <!-- Phased approach OR "TBD based on CI/CD implementation progress" -->

| Metric | Current | Target ([Timeframe]) | Benchmark Source |
|--------|---------|----------------------|------------------|
| **Deployment Frequency** | <!-- e.g., Monthly (from deployment logs) --> | <!-- e.g., Daily --> | <!-- DORA Elite/High/Medium/Low --> |
| **Lead Time for Changes** | <!-- e.g., X days (from git analysis) --> | <!-- e.g., < 1 day --> | <!-- Benchmark category --> |
| **Mean Time to Recovery** | <!-- e.g., X hours (from incident logs OR "unknown") --> | <!-- e.g., < 1 hour --> | <!-- Benchmark category --> |
| **Change Failure Rate** | <!-- e.g., X% (from deployment/incident correlation) --> | <!-- e.g., < 15% --> | <!-- Benchmark category --> |

**Measurement Approach**:
<!-- For each metric:
- How collected: [CI/CD tool, monitoring service]
- Validation: [How accuracy is ensured]
- Review cadence: [How often reviewed and acted upon]
-->

---

## ⚠️ Technical Risks & Mitigations

### Risk Assessment

| Risk | Probability | Technical Impact | Mitigation | Validation |
|------|-------------|------------------|------------|------------|
| **[Risk]** | <!-- HIGH/MEDIUM/LOW (basis: [similar project data, expert judgment]) --> | <!-- Specific impact --> | <!-- Specific mitigation strategy --> | <!-- How mitigation effectiveness is validated --> |

⚠️ **Risk Probability Disclaimer**:
- Probabilities are <!-- [qualitative assessments based on expert judgment, OR quantitative based on historical data from similar projects] -->
- For quantitative: Based on <!-- [specific data source with sample size and context] -->
- Validation: Risk register reviewed <!-- [frequency] -->, adjusted based on <!-- [actual incidents, new information] -->

**Top Technical Risks**:

#### Risk: [Risk Name]

**Description**: <!-- Detailed description of the risk -->

**Probability**: <!-- HIGH/MEDIUM/LOW -->
- **Basis**: <!-- [Historical incident data, complexity analysis, dependency assessment] -->

**Impact**: <!-- CRITICAL/HIGH/MEDIUM/LOW -->
- **Technical**: <!-- What breaks/degrades -->
- **Business**: <!-- Business consequence -->

**Mitigation Strategy**:
<!-- Specific, actionable mitigations
1. **[Mitigation]**: How it reduces probability or impact
   - Responsibility: [Who implements]
   - Timeline: [When implemented - phase/milestone]
   - Success criteria: [How to know it's effective]
-->

**Contingency Plan**:
<!-- If mitigation fails, what's the backup plan? -->

**Monitoring**:
<!-- How to detect if risk is materializing
- Indicator: [What to watch]
- Trigger: [Threshold for action]
- Response: [What action to take]
-->

<!-- Repeat for top 5-10 technical risks -->

---

## 📈 Performance & Scalability

### Performance Requirements

⚠️ **Performance Requirements Disclaimer**:
- **Current performance**: Based on <!-- [actual load testing data, production monitoring, OR estimates from traffic analysis] -->
- **Target requirements**: Based on <!-- [business requirements, user experience goals, OR assumptions pending validation] -->
- **Scalability targets**: Require validation through <!-- [load testing, pilot deployment, production monitoring] -->

| Scenario | Current Performance | Target Requirement | Gap Analysis |
|----------|--------------------|--------------------|--------------|
| **[Scenario]** | <!-- e.g., 100 req/s, 500ms p95 latency (source: [monitoring/testing]) --> | <!-- e.g., 1000 req/s, 200ms p95 latency --> | <!-- What's needed to achieve target --> |

**Performance Baseline** (if unknown):
- **Current state unknown because**: <!-- [no load testing, production not instrumented, historical data not retained] -->
- **To establish baseline**: <!-- [implement APM, conduct load testing, analyze production logs for X days] -->
- **Assumption for planning**: <!-- [Industry benchmark, vendor example, OR "conservative estimate requiring validation"] -->

### Scalability Architecture

**Horizontal Scaling**:
- **Approach**: <!-- e.g., Auto-scaling groups, Kubernetes HPA -->
- **Trigger**: <!-- e.g., CPU > 70%, Request queue depth > 100 -->
- **Limits**: <!-- Min/max instances with rationale -->

**Vertical Scaling**:
- **Applicability**: <!-- Which components, why -->
- **Limits**: <!-- Maximum size with justification -->

**Data Layer Scalability**:
- **Approach**: <!-- e.g., Read replicas, sharding, caching -->
- **Scaling Limits**: <!-- Technical or cost constraints -->

**Scalability Testing**:
- **Test Scenarios**: <!-- What load patterns will be tested -->
- **Success Criteria**: <!-- Measurable criteria -->
- **Validation Plan**: <!-- How scalability will be proven -->

---

## 🛣️ Implementation Roadmap

⚠️ **ROADMAP DISCLAIMER**:
This roadmap shows **logical sequencing and dependencies**, NOT firm dates or timelines.

- **Durations**: Shown where evidence-based (task breakdown + team capacity). Otherwise marked "TBD"
- **Sequencing**: Based on technical dependencies and risk mitigation strategy
- **Actual timeline**: Requires assigned team, established velocity, and sprint planning
- **Use**: Strategic planning and dependency management, NOT scheduling or commitments

### Roadmap Phases

#### Phase 1: [Phase Name]

**Objective**: <!-- What this phase achieves -->

**Key Deliverables**:
<!-- List with acceptance criteria
- **[Deliverable]**: Description
  - Acceptance: [Measurable criteria]
-->

**Technical Tasks**:
<!-- Detailed task breakdown
1. **[Task Category]**:
   - [ ] [Specific task]
   - [ ] [Specific task]
   - Estimated Effort: <!-- IF derivable: X-Y person-days (basis: [complexity, team experience])
                           ELSE: "TBD - requires [architectural spike, PoC, technical discovery]" -->
-->

**Dependencies**:
- ⬅️ **Depends On**: <!-- What must complete before this phase -->
- ➡️ **Enables**: <!-- What this phase enables -->

**Success Criteria**:
- ✅ <!-- Objective, measurable criterion -->

**Duration** (if evidence-based):
- **Estimated Duration**: <!-- X-Y weeks -->
- **Basis**: 
  - <!-- Total effort: [sum of task estimates] person-days -->
  - <!-- Team size: [N FTE] -->
  - <!-- Calculation: effort ÷ (team size × [work days/week] × efficiency factor) -->
  - <!-- Efficiency factor: [%] accounts for [meetings, context switching, learning curve] -->
  - <!-- Risk buffer: [%] for [specific uncertainties] -->
- **Assumptions**: <!-- [team availability, skill level, no major blockers] -->
- **Confidence**: <!-- HIGH/MEDIUM/LOW based on [rationale] -->

**Duration** (if not evidence-based):
- **Cannot estimate because**: <!-- [tasks not decomposed to sufficient detail, team composition unknown, technical unknowns] -->
- **Sequencing**: Must follow <!-- [previous phase] -->, must precede <!-- [next phase] -->
- **To establish duration**: <!-- [sprint 0 for task breakdown, team assignment, velocity baseline] -->

**Risk Mitigations Active This Phase**:
- <!-- Link to specific risks and mitigations from risk section -->

<!-- Repeat for each phase -->

### Cross-Phase Concerns

**Testing Strategy** (across all phases):
<!-- Describe testing approach
- Unit testing: [Coverage target with basis]
- Integration testing: [Scope and approach]
- Performance testing: [When and how]
- Security testing: [SAST/DAST tools and schedule]
- UAT: [Involvement and criteria]
-->

**Quality Gates**:
<!-- Define quality gates between phases
- **Gate [N]**: Required before proceeding to Phase [N+1]
  - [ ] [Objective criterion]
  - [ ] [Objective criterion]
  - Decision: [Who approves gate]
-->

---

## 🔬 Reference Implementation

### Proof of Concept (PoC) Scope

**Objective**: Validate <!-- [specific technical assumption, migration approach, performance target] -->

**Scope**:
- **Component**: <!-- What part of system -->
- **Technology**: <!-- What technology/service to validate -->
- **Success Criteria**: <!-- Measurable, objective criteria -->

**PoC Plan**:
<!-- Detailed PoC steps
1. **[Step]**: What to implement/test
   - Expected outcome: [What should happen]
   - Validation: [How to measure]
-->

⚠️ **PoC Duration/Effort**:
- **IF known**: <!-- X days/weeks (basis: [task list, team allocation]) -->
- **IF unknown**: "Estimation requires: <!-- [task breakdown, resource assignment] -->"

**Go/No-Go Decision**:
- ✅ **GO**: Proceed if <!-- [specific success criteria met] -->
- ❌ **NO-GO**: Re-evaluate if <!-- [failure conditions] -->
- 🔄 **ITERATE**: Adjust and retry if <!-- [partial success conditions] -->

### Sample Code / Configuration

```[language]
// Example implementation showing [pattern/approach]
// 
// Context: [What this code demonstrates]
// Source: [PoC repository, reference architecture, vendor sample]
//
[Code snippet]
```

**Code Notes**:
- **Purpose**: <!-- What this demonstrates -->
- **Assumptions**: <!-- What's assumed for this sample -->
- **Production Considerations**: <!-- What needs to change for production use -->

---

## 📚 Technical Reference

### Essential Architecture Documents

| Document | Purpose | Key Sections | Est. Reading Time |
|----------|---------|--------------|-------------------|
| **[Document Title](path)** | <!-- What architects will learn --> | <!-- Key sections --> | <!-- X min --> |

### External References

| Resource | Description | Use Case |
|----------|-------------|----------|
| **[Resource Title](URL)** | <!-- What it is --> | <!-- When to reference --> |

### Technology-Specific Guides

<!-- Links to official documentation for key technologies
- **[Technology]**: [Official docs URL] - [What to reference]
-->

---

## 🎯 Architect Decision Checklist

Before proceeding with modernization, validate:

- [ ] **Architecture Alignment**: Target architecture aligns with business requirements and constraints
- [ ] **ADRs Documented**: All major decisions have ADRs with rationale and alternatives
- [ ] **Risk Assessment**: Top risks identified with specific mitigations
- [ ] **Security Reviewed**: Security architecture addresses current findings and compliance requirements
- [ ] **Performance Validated**: Performance requirements are achievable (PoC validated or evidence-based)
- [ ] **Scalability Designed**: Auto-scaling strategy defined and tested
- [ ] **Data Strategy Solid**: Data migration approach proven through testing
- [ ] **Observability Planned**: Monitoring, logging, tracing implementation defined
- [ ] **Rollback Possible**: Every phase has rollback plan
- [ ] **Costs Understood**: Infrastructure costs estimated with evidence (or flagged as requiring PoC)

---

## Template Usage Notes

**For Documentation Compiler Agent**:

1. **Evidence-Based Architecture**: Every architecture decision must reference analysis artifacts or PoC results
2. **ADR Discipline**: Create ADR for EVERY significant decision. Show alternatives and decision criteria explicitly
3. **Metrics Must Be Measurable**: ALL SLOs, performance targets, DORA metrics must be:
   - Measurable (specific tools/approach defined)
   - Baselined (current state from data) OR marked as "to be established"
   - Achievable (evidence or validation plan shown)
4. **No Ungrounded Estimates**:
   - Duration/effort WITH evidence: Show calculation (tasks × time, throughput × volume)
   - Duration/effort WITHOUT evidence: State "TBD pending [specific planning/discovery]" and explain WHY unknown
   - Migration times: Must show calculation (data volume ÷ transfer rate) OR mark as requiring testing
5. **Risk Assessment Quality**:
   - Probability: Use qualitative (HIGH/MEDIUM/LOW) with basis, not percentages without data
   - Mitigation: Specific, actionable strategies (not vague "we'll monitor")
   - Validation: Define how you'll know if mitigation worked
6. **Roadmap as Dependency Map**: Focus on logical sequencing and dependencies, not calendar dates (unless evidence-based)

**Quality Criteria**:
- ✅ Every architecture decision has documented rationale
- ✅ Every estimate shows calculation method or states why unknown
- ✅ Every risk has specific mitigation and validation approach
- ✅ Every metric has measurement approach defined
- ✅ Every "TBD" explains what information is needed to determine it
- ✅ Code references are specific (file/class level, not "see codebase")
