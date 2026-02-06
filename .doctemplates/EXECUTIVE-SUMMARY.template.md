---
title: [Solution Name] - Executive Documentation Summary
description: Consolidated summary of architecture analysis, security assessment, and modernization recommendations
version: 1.0
date: [YYYY-MM-DD]
author: Software Architecture Analyzer
status: Draft
audience: Technical leadership, architects, stakeholders
templateVersion: 1.0
---

# [Solution Name] - Executive Summary

> **⚠️ CRITICAL - Evidence-Based Estimates Only**:
> - All timeframes, costs, effort estimates, and risk probabilities in this document must be **immediately derivable** from concrete evidence
> - **MUST** document calculation methodology and data sources
> - **If data unavailable**: Clearly explain WHY estimation cannot be provided and WHAT specific data is needed
> - **DO NOT** include placeholder estimates like "3 months", "$X,XXX", "Week 1-2" without documented evidence
> - See [Evidence-Based Estimation Guidelines](#evidence-based-estimation-guidelines) section below

> **Quick Reference**: This document consolidates key findings from comprehensive architecture analysis across [N] analysis artifacts covering architecture, security, operations, quality, and modernization readiness.

---

## Evidence-Based Estimation Guidelines

### When Including Estimates

**REQUIRED for ALL estimates (timeframe, cost, effort, risk)**:

1. **Data Source**: Cite specific evidence
   - ✅ Example: "Current database size: 500GB (from production metrics query)"
   - ❌ Example: "Database approximately 500GB"

2. **Calculation Method**: Show derivation
   - ✅ Example: "Monthly cost: 500GB × $0.90/GB = $450/month (Azure SQL pricing calculator)"
   - ❌ Example: "Estimated cost: $450/month"

3. **Assumptions**: Document all assumptions explicitly
   - ✅ Example: "Assumes 80% read, 20% write workload (from application logs analysis)"
   - ❌ Example: "Standard read/write pattern"

4. **Validation Caveat**: State limitations
   - ✅ Example: "Estimate requires validation with 30-day production workload monitoring"

### When Data is Unavailable

**DO NOT** use vague statements. **MUST** provide specific explanation:

**❌ Bad Examples**:
- "Cost estimation not available"
- "Timeline TBD"
- "Requires more data"

**✅ Good Example**:
> **Cost Estimation**: Cannot be calculated from static code analysis because:
> 1. **Request Volume Unknown**: Production logs not accessible; need APM data showing requests/day over 30-day period
> 2. **Database Size Uncertain**: Current size requires runtime query; static analysis shows schema only
> 3. **Peak Load Patterns Missing**: Traffic patterns need monitoring data; code analysis shows potential concurrency but not actual usage
>
> **Required Data Sources**: 
> - Application Performance Monitoring logs (30 days minimum)
> - Database size query results (SELECT SUM(size) FROM sys.database_files)
> - Peak concurrent user metrics from session logs or analytics

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Critical Security Findings](#critical-security-findings)
- [Operations & Infrastructure](#operations--infrastructure)
- [Quality & Technical Debt](#quality--technical-debt)
- [Modernization Priorities](#modernization-priorities)
- [Recommended Action Plan](#recommended-action-plan)

---

## Architecture Overview

### Application Profile

| Aspect | Details |
|--------|---------|
| **Application** | <!-- e.g., Application Name (Japanese: 業務システム) --> |
| **Technology** | <!-- e.g., Java 8, Spring 4.0.3, Apache Tomcat 8.0, SQL Server --> |
| **Framework** | <!-- e.g., Framework Name X.X.X (if proprietary, note vendor) --> |
| **Architecture** | <!-- e.g., 4-tier layered (Presentation, Application, Business Logic, Data) --> |
| **Deployment** | <!-- e.g., Multi-tier on-premises (6 environments) --> |
| **Codebase Size** | <!-- Cite from analysis: X LOC, Y files, Z dependencies --> |
| **Module Count** | <!-- e.g., N Maven/Gradle modules --> |

**Evidence Source**: <!-- Reference analysis files, e.g., project-inventory.json, codebase-metrics.json -->

### Architectural Strengths

<!-- List 5-8 architectural strengths discovered from analysis
✅ **Strength description** with supporting evidence
✅ Use evidence from analysis (patterns.json, architecture-layers.json, etc.)
✅ Each strength should cite specific code/config evidence
-->

---

## Critical Security Findings

### OWASP Top 10 Risk Assessment

| Risk Category | Status | Severity | Priority |
|---------------|--------|----------|----------|
| <!-- e.g., **A02: Cryptographic Failures** --> | <!-- ⛔ VULNERABLE / ⚠️ GAPS / ✅ PROTECTED --> | <!-- CRITICAL/HIGH/MEDIUM/LOW --> | <!-- 🔴 Immediate / 🟡 Short-term / 🟢 Monitor --> |
| <!-- Repeat for all applicable OWASP categories --> | | | |

**Evidence Source**: <!-- Reference security-assessment.json, vulnerabilities.json -->

### Critical Vulnerabilities (Immediate Action Required)

<!-- For EACH critical vulnerability, include:

#### N. **Vulnerability Title** 🔴 CRITICAL/🟡 HIGH

**Vulnerability**: Brief description

**Evidence**:
```[language]
// Actual code snippet from repository showing vulnerability
// File path: src/path/to/file.ext
// Lines: X-Y
```

**Impact**: Business/technical impact statement

**CVSS Score**: X.X (Severity) - ONLY if CVE exists with published CVSS
**CWE**: CWE-XXX (Weakness Type) - ONLY if applicable

**Remediation**: Specific fix description

**Effort Estimation**:
⚠️ ONLY include if derivable from evidence:
- ✅ Example: "Low effort (1-2 days): Single file change, 50 LOC replacement"
- ✅ Example: "Medium effort: Affects 12 files (from grep search), requires code review + testing"
- ❌ Example: "Medium (1-2 weeks)" without evidence

OR if not derivable:
- ⚠️ "Effort estimation not possible because: (1) Full impact analysis requires dependency graph not available from static analysis, (2) Test coverage unknown, (3) Deployment complexity unclear. Required: Dependency analysis tool output, test execution report, deployment runbook review."
-->

### Security Strengths

<!-- List security mechanisms that ARE implemented correctly
✅ **Security feature** with evidence (code snippets, config files)
-->

### Compliance Status

<!-- For regulatory compliance (GDPR, PIPL, HIPAA, etc.)

**Compliance Framework**: [e.g., Japanese PIPL, GDPR, etc.]

**Compliance Status**: ✅ Implemented / ⚠️ Partial / ❌ Missing

**Protection Mechanisms**:
- **Control Name**: Description with evidence reference
- Cite actual code/config implementing controls

**Gaps**:
- ⚠️ **Gap description** with evidence of what's missing
-->

---

## Operations & Infrastructure

### Deployment Environments

| Environment | Code | Purpose | Server Count | Deployment Path |
|-------------|------|---------|--------------|-----------------|
| <!-- e.g., **Development** --> | <!-- DEV --> | <!-- Developer workstations --> | <!-- N per developer --> | <!-- Path or method --> |
| <!-- Repeat for all environments --> | | | | |

**Evidence Source**: <!-- Reference deployment-topology.json, infrastructure-inventory.json -->

### Infrastructure Stack

<!-- List infrastructure components with versions

**Component Type**: Version details (with evidence)

Example:
**Application Servers**: 3x Apache Tomcat 8.0 (from deployment-topology.json)
**Database**: Microsoft SQL Server [version] (from database-schema.json)
-->

### Scalability Assessment

| Tier | Horizontal Scaling | Status | Limitations |
|------|-------------------|--------|-------------|
| **Web** | <!-- ✅ Supported / ⚠️ Limited / ❌ Not supported --> | <!-- Current state --> | <!-- Technical limitations --> |
| **Application** | | | |
| **Database** | | | |
| **Cache** | | | |

**Current Capacity**:
⚠️ **ONLY include if measurable from evidence**:
- ✅ Example: "~100 concurrent users (from session monitoring data)"
- ✅ Example: "~50 requests/second (from application logs analysis)"
- ❌ Example: "~100 concurrent users" without data source

OR if not measurable:
> **Capacity Estimation**: Cannot be determined from static code analysis because:
> 1. **Concurrent User Metrics Missing**: Requires session monitoring or APM data
> 2. **Request Throughput Unknown**: Application logs not accessible for analysis
> 3. **Resource Utilization Unclear**: Server monitoring data (CPU, memory) not available
>
> **Required Data**: 30-day session analytics, application server access logs, infrastructure monitoring metrics (CPU/memory/network)

**Scalability Recommendations**:
<!-- Technical recommendations for improving scalability - NO time estimates unless derived from evidence -->

### High Availability

**Current HA Status**: <!-- ⚠️ Partial / ✅ Full / ❌ None -->

<!-- List HA status per tier with evidence
✅/⚠️/❌ **Tier**: Current configuration (cite from deployment topology)
-->

**SLA Estimation**:
⚠️ **ONLY include if calculable**:
- ✅ Example: "Estimated 99.5% uptime (based on single-point-of-failure analysis: 43.8 hours downtime/year)"
- ❌ Example: "99.5% uptime" without calculation

**RTO/RPO**:
⚠️ **ONLY include if based on evidence**:
- ✅ Example: "RTO: 4 hours (from disaster recovery runbook)"
- ✅ Example: "RPO: 1 hour (from backup schedule configuration)"
- ❌ Example: "RTO: 4 hours" without documented procedure

### Monitoring & Observability

**Logging Framework**: <!-- e.g., Logback 1.1.3 + SLF4J 1.7.12 (from dependencies.json) -->

**Log Types**:
<!-- List log types with evidence from code/config
- **Log Type**: Description (file path or config reference)
-->

**Monitoring Capabilities**:
<!-- List what IS monitored vs NOT monitored
- ✅ **Capability**: Description with evidence
- ⚠️ **Partial Capability**: What's missing
- ❌ **Missing Capability**: Not implemented
-->

---

## Quality & Technical Debt

### Technical Debt Inventory

#### 1. **Obsolete Technology Stack** Priority Level

| Technology | Current Version | Status | EOL Date | Recommended |
|------------|----------------|--------|----------|-------------|
| <!-- e.g., **Java** --> | <!-- 8 --> | <!-- ⚠️ Extended support / ⛔ Unsupported --> | <!-- 2030 / N/A --> | <!-- Java 17/21 LTS --> |
| <!-- Repeat for all EOL components --> | | | | |

**Evidence Source**: <!-- Reference dependencies.json, technology-inventory.json -->

#### 2. **Architectural Debt** Priority Level

<!-- Describe architectural debt with evidence

**Issue Description**:
- Specific technical problem
- Evidence from codebase (e.g., "Single deployable unit: X.war, Y MB")
- Impact on scalability/maintainability
-->

#### 3. **Code Quality Metrics**

**Positive Indicators**:
<!-- List quality strengths with evidence
- ✅ **Indicator**: Evidence source (e.g., "from static analysis tool output")
-->

**Quality Concerns**:
<!-- List quality issues with evidence
- ⚠️ **Concern**: Evidence (e.g., "No test coverage data available")
-->

### Dependency Vulnerabilities

**Total External Dependencies**: <!-- N (from dependencies.json) -->

**High-Risk Dependencies**:
<!-- List dependencies with known vulnerabilities
1. **Dependency Name Version** - Specific CVEs OR security advisories (with CVE numbers if available)
-->

**Recommendation**: <!-- Tools/processes for ongoing vulnerability management -->

---

## Modernization Priorities

### Modernization Readiness Score: **X/100** (Assessment)

<!-- Base score on 12-Factor compliance, cloud readiness assessment, technology age analysis -->

**Evidence Source**: <!-- Reference cloud-readiness.json, 12-factor-assessment.json -->

### Phase-Based Modernization Approach

⚠️ **Timeline Requirements**: 
- **ONLY include phase durations if based on**:
  - Documented team capacity (N FTE confirmed)
  - Task breakdown with complexity estimates from similar projects
  - Dependency analysis showing critical path
- **If timeline cannot be estimated**: Explain why and what data is needed

#### Phase N: Phase Name (Timeline if derivable) Priority

**Priority N - Task Group**:
<!-- List technical tasks (WHAT needs to be done)
1. Task description
2. Task description

NO duration estimates unless:
✅ Based on: "Similar task in Project X took Y days with Z-person team (reference: project retrospective doc)"
❌ Generic: "1-2 weeks" without basis
-->

**Expected Outcomes**: 
<!-- Technical outcomes, improvements
- DO NOT include time-based outcomes unless evidence-based
- Focus on measurable technical improvements
-->

**Evidence for Phase Definition**:
<!-- Cite analysis artifacts that justify this phase grouping -->

---

## Recommended Action Plan

### Immediate Actions (Next 30 Days)

⚠️ **Timeline Disclaimer**: 
The "Next 30 Days" framing is an example planning horizon. Actual timeline must be based on:
- Confirmed team availability and capacity
- Organizational change approval processes
- Technical validation through proof-of-concept

**Week 1-2: Task Group** (Timeline example only)
<!-- List specific technical actions
1. ✅ Action item (technical task, NO effort estimate unless derived)

IF you include week-based grouping:
> **Note**: "Week 1-2" is an illustrative grouping. Actual schedule depends on: [list specific factors like team size, skill level, approval processes]
-->

**Week 3-4: Task Group** (Timeline example only)
<!-- Same pattern -->

### Short-Term Actions (90 Days)

⚠️ **Timeline Disclaimer**: 
90-day planning horizon is illustrative. Adjust based on actual team velocity and organizational constraints.

<!-- Month-based grouping with same disclaimer approach -->

### Medium-Term Roadmap (6-12 Months)

⚠️ **Roadmap Disclaimer**:
This roadmap provides logical sequencing and dependencies, NOT firm commitments. Actual timeline requires:
- Detailed sprint planning with assigned team
- Validation through proof-of-concept work
- Adjustment based on discovered complexity

<!-- Quarter-based planning with focus on logical sequence, dependencies -->

### Long-Term Vision (12-24 Months)

⚠️ **Vision Disclaimer**:
Long-term timeline is highly uncertain. Use for strategic planning only. Refine through iterative planning.

<!-- Year-based vision with strategic direction, dependencies -->

---

## Success Metrics

### Security Metrics

| Metric | Current | Target (3M) | Target (12M) |
|--------|---------|-------------|--------------|
| **Critical Vulnerabilities** | <!-- N (from analysis) --> | <!-- Target --> | <!-- Target --> |
| <!-- Additional metrics with CURRENT values from evidence --> | | | |

**Evidence for Current Values**: <!-- Reference security scans, analysis artifacts -->

**Target Rationale**: 
⚠️ Targets should be based on:
- Industry benchmarks (cite source)
- Regulatory requirements (cite standard)
- NOT arbitrary goals

### Operational Metrics

| Metric | Current | Target (6M) | Target (12M) |
|--------|---------|-------------|--------------|
| **Deployment Time** | <!-- e.g., Manual (hours) - from deployment runbook --> | <!-- Target --> | <!-- Target --> |
| **Uptime SLA** | <!-- Calculate from current HA setup --> | | |

**Current Values Source**: <!-- Reference operational data or explain if not available -->

### Technical Metrics

| Metric | Current | Target (6M) | Target (12M) |
|--------|---------|-------------|--------------|
| **Test Coverage** | <!-- From test execution report OR "Unknown - tests disabled" --> | | |
| **Code Quality Score** | <!-- From static analysis OR "Unknown" --> | | |

**Measurement Approach**: <!-- How metrics will be collected going forward -->

---

## Conclusion

<!-- 2-3 paragraph summary

Paragraph 1: Current state assessment (use evidence)
Paragraph 2: Key recommendations (logical priorities, NO timelines unless evidence-based)
Paragraph 3: Strategic direction and business value
-->

**Key Recommendations**:

1. **Immediate**: <!-- Technical priority with evidence-based rationale -->
2. **Short-term**: <!-- Priority with dependency on #1 -->
3. **Medium-term**: <!-- Priority with dependencies -->
4. **Long-term**: <!-- Strategic direction -->

**Investment Required**: 
⚠️ **ONLY include if calculable**:
- ✅ Example: "Estimated X-Y person-months (based on: [list basis - similar project, task breakdown, etc.])"
- ❌ Example: "12-18 person-months" without basis

OR if not calculable:
> **Investment Estimation**: Cannot be calculated without:
> 1. Detailed task breakdown (requires technical spike for each phase)
> 2. Team capacity and skill level assessment
> 3. Organizational change management complexity analysis
>
> **Recommended Approach**: Start with Phase 1 detailed planning (2-week sprint 0) to establish baseline velocity.

**Business Value**: <!-- Focus on measurable outcomes, risk mitigation -->

---

**Document Generated**: <!-- [YYYY-MM-DD] -->
**Analysis Source**: <!-- [N] JSON analysis artifacts from `.solutiondocs/analysis/` -->
**Comprehensive Documentation**: <!-- [N] documents available in `.solutiondocs/[folders]/` -->

**Next Steps**: <!-- Recommended actions for stakeholders -->

---

## Template Usage Notes

**For Documentation Compiler Agent**:

1. **Evidence Requirement**: Every estimate, metric, or numeric value MUST have a documented source
2. **Unknown Values**: If data is not in analysis files, state explicitly what's missing and why
3. **Calculation Method**: Show formulas for all derived values (costs, scores, metrics)
4. **Avoid Placeholders**: Do NOT include "$X,XXX", "N months", "TBD" without full explanation
5. **Risk Probabilities**: ONLY include if based on historical data, industry benchmarks, or formal risk analysis
6. **Cost Estimates**: Include ONLY if:
   - Based on actual resource usage (from monitoring/logs)
   - Include pricing source (e.g., "Azure Pricing Calculator 2025-XX-XX")
   - Show calculation: quantity × unit price = total
7. **Timeline Estimates**: Include ONLY if:
   - Based on actual team capacity (confirmed, not assumed)
   - Include task breakdown showing derivation
   - Document all assumptions (skill levels, dependencies, etc.)

**Acceptable Alternatives When Data Missing**:
- Logical sequencing without timeframes
- Risk categories without numeric probabilities
- Recommendation priorities without cost estimates
- Strategic direction without ROI calculations
