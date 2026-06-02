---
applyTo: '**/.solutiondocs/README.md, **/.solutiondocs/EXECUTIVE-SUMMARY.md, **/.solutiondocs/GLOSSARY.md, **/.solutiondocs/QUICK-START-EXECUTIVE.md, **/.solutiondocs/QUICK-START-ARCHITECT.md, **/.solutiondocs/QUICK-START-DEVELOPER.md'
---
## Generic Documentation Deliverables

Generate the following documents in the `.solutiondocs` root folder. Each document must follow the specified structure and include all required sections.

Ensure:
- All sections from the template are included in the same order.
- Replace placeholder text with relevant content.
- Maintain Markdown formatting exactly as in the template.
- Do not add extra sections unless specified.

---

## CRITICAL: Prohibited Content in Generic Documents

**Generic documents (README, EXECUTIVE-SUMMARY, GLOSSARY, QUICK-START guides) serve ALL audiences and must avoid speculative metrics.**

### STRICTLY PROHIBITED Unless Evidence-Based:

1. **All Timeline References**:
   - ❌ "Migration takes 3-6 months"
   - ❌ "Phase 1: Weeks 1-4"
   - ❌ "Expected go-live: Q2 2025"
   - ❌ "2-week sprints"
   - ✅ ONLY IF: Reference actual project plan with documented basis
   - ✅ ACCEPTABLE: "See detailed timeline in [Project Plan] based on [methodology]"

2. **All Cost References**:
   - ❌ "Infrastructure costs: $5,000/month"
   - ❌ "Migration budget: $500K"
   - ❌ "ROI in 18 months"
   - ❌ "TCO savings: $200K/year"
   - ❌ "Reserved Instance savings"
   - ✅ ONLY IF: Reference actual cost analysis with calculation details
   - ✅ ACCEPTABLE: "See cost analysis in [Cost Document] based on [data sources]"

3. **All Effort References**:
   - ❌ "Requires 5-7 developers"
   - ❌ "Estimated 40 person-months"
   - ❌ "3 FTE for 6 months"
   - ❌ "200 story points"
   - ✅ ONLY IF: Reference actual resource plan with documented basis

4. **All Risk Quantification**:
   - ❌ "30% chance of data breach"
   - ❌ "High risk ($2M potential loss)"
   - ❌ "Downtime costs $10K/hour"
   - ✅ ACCEPTABLE: "HIGH risk - see risk analysis in [Risk Document]"

5. **All Performance Claims**:
   - ❌ "50% faster response times"
   - ❌ "Scales to 10,000 requests/sec"
   - ❌ "99.99% uptime"
   - ✅ ONLY IF: Based on actual benchmarks or SLA documentation

### Generic Documents Must Reference, Not Estimate:

**DO THIS** (referencing detailed docs):
```markdown
## Project Overview

This modernization initiative addresses technical debt and scalability challenges identified in the architecture analysis.

**For timeline details**: See [MODERNIZATION/3-execution/3.1-migration-plan.md]
**For cost analysis**: See [MODERNIZATION/2-strategy/2.4-azure-service-recommendations.md#cost-estimation]
**For resource requirements**: See [MODERNIZATION/2-strategy/2.2-stakeholder-matrix.md#resource-planning]
```

**NOT THIS** (speculating in generic doc):
```markdown
## Project Overview

This 6-month, $500K modernization initiative will migrate to Azure using a team of 5-7 developers.
Expected ROI: 18 months. Monthly cloud costs: $4,000-6,000.
```

### When Templates Include Estimate Sections:

Some templates (e.g., QUICK-START-EXECUTIVE) include sections for estimates with evidence requirements.

**If data unavailable, state WHY and WHERE**:
```markdown
## Resource Requirements

**Resource estimation not yet complete**:
- Detailed task breakdown required (scheduled for Sprint 0)
- Team skill assessment pending
- Vendor capacity confirmation needed

**Estimation approach** (once data available):
1. Break down work into sized tasks
2. Map tasks to required skills
3. Assess internal capacity vs external needs
4. Calculate FTE requirements per phase

**For current best estimate**, see architecture team's preliminary assessment in [link].
```

### Validation for Generic Documents:

Before finalizing any generic document:
- [ ] Does it contain ANY timeline, cost, effort, or risk numbers?
- [ ] If YES: Is each number derived from a detailed analysis document?
- [ ] Are all references to detailed documents included?
- [ ] Would an executive understand this is NOT making new estimates, but summarizing existing analysis?

**If any number lacks a detailed source document → REMOVE IT**

---

**CRITICAL - Evidence-Based Estimation Requirements**:

ALL generic documents MUST comply with evidence-based estimation principles:

1. **No Ungrounded Estimates**: NEVER include timelines, durations, costs, effort levels, or risk probabilities without:
   - Derivable calculation from actual data (show formula)
   - Documented data source with date
   - Explicit methodology explanation
   - Clear assumptions and caveats

2. **When Data is Unavailable**: If estimates cannot be derived, you MUST explain:
   - **WHY** the estimate cannot be provided (what data/analysis is missing)
   - **WHAT** specific information is needed to create the estimate
   - **WHERE** that information should come from (data source, analysis type, validation approach)

3. **Acceptable Alternatives**:
   - Qualitative assessments (HIGH/MEDIUM/LOW) with clear rationale
   - Logical sequencing without firm dates ("Phase A before Phase B")
   - Order-of-magnitude ranges with explicit confidence levels and basis
   - "TBD pending [specific analysis/data]" with explanation of what's needed

**Examples**:

❌ **BAD** (Prohibited):
- "Migration will take 3-6 months"
- "Infrastructure costs will be $5,000/month"
- "30% probability of encountering this risk"
- "Team size: 5-7 developers"

✅ **GOOD** (Evidence-based):
- "Migration duration: 3-6 months based on: 47 components × 2-4 days/component ÷ 3 FTE team = 94-188 days. Assumes team velocity from similar project [reference]. Confidence: Medium due to unknowns in components #12-18."
- "Infrastructure costs: $4,200-5,800/month (Azure pricing calculator, 2024-01-15). Based on: Container Apps Standard tier (4-8 instances @ $0.12/hr), Azure SQL S2 ($150/mo), Storage ($200/mo). See calculation: [link]. Excludes bandwidth (requires production usage data)."
- "Risk probability: HIGH (qualitative). Rationale: Similar technology migration in [Project X] encountered this issue in 3/4 cases. Mitigation reduces likelihood to MEDIUM."
- "Team size not yet determined. Estimation requires: task breakdown (pending Sprint 0), skill matrix, availability assessment. Recommended approach: start with 2-3 FTE, adjust based on velocity baseline."

❌ **BAD** (Vague unavailability):
- "Data not available"
- "Cannot estimate at this time"
- "Timeline TBD"

✅ **GOOD** (Proper unavailability explanation):
- "Migration timeline cannot be estimated because: (WHY) task decomposition not complete - 23 components have unknown refactoring scope. (WHAT) Need: component-by-component complexity analysis, dependency mapping. (WHERE) Requires: 2-week architectural spike by senior developer familiar with both current and target frameworks."
- "Infrastructure costs unknown because: (WHY) production usage patterns not measured. (WHAT) Need: Request volumes, data transfer rates, storage growth. (WHERE) Source: 30-day production monitoring with Application Insights, or load testing with realistic scenarios. Interim: Use industry benchmark of $X-Y/month for similar applications [source], validate with pilot."

---

### Document: `README.md` (Documentation Index)

**Purpose**: Provide central navigation and overview for all solution documentation.

**Function**: Documentation suite overview, purpose statement, generation information, navigation guide, maintenance guidelines, document inventory with descriptions, and quick start guides for different audiences (executives, architects, developers, DevOps, business analysts).

**Template**: Use `.doctemplates/README.template.md` as the starting point for this document.

**Audience**: All stakeholders - serves as the entry point to the documentation suite.

**Evidence-Based Requirements**: 
- If README includes any summary metrics (document count, analysis date, etc.), cite actual data source
- If referencing project status/progress, show calculation basis or mark as qualitative assessment
- All links must work (no broken references)

---

### Document: `EXECUTIVE-SUMMARY.md` (Consolidated Executive Summary)

**Purpose**: Provide comprehensive executive-level overview of the entire solution, current state, and modernization initiative.

**Function**: 
- Synthesize key findings from ALL analysis and strategy documents
- Present business case with investment requirements, benefits, risks
- Provide strategic recommendations with evidence-based justification
- Include all critical decision points for executive leadership

**Template**: Use `.doctemplates/EXECUTIVE-SUMMARY.template.md` as the starting point for this document.

**Audience**: C-level executives, VP-level stakeholders, executive sponsors, board members (if applicable).

**Content Sources**:
- Consolidate from: System Architecture, Business Domain Model, Risk Assessment, Cloud Readiness Assessment, Modernization Strategy, Cost Analysis, Implementation Plan
- Cross-reference: All major CURRENT-STATE and MODERNIZATION documents

**Evidence-Based Requirements** (CRITICAL):
- **MANDATORY Disclaimer Section**: Must appear at document start explaining all estimate limitations
- **ALL Cost Estimates**: Show calculation (quantity × price = total) + pricing source + date. If unknown: explain WHY, WHAT data needed, WHERE to get it
- **ALL Timelines**: Show basis (task count, team size, velocity source) OR state "TBD pending [specific planning]"
- **ALL Risk Probabilities**: Cite source (historical data, benchmark, expert judgment) OR use qualitative (HIGH/MEDIUM/LOW) with rationale
- **ALL ROI Claims**: Show complete calculation with assumptions. If not calculable: explain missing data and validation approach
- **Benefit Quantification**: Only quantify if derivable from data. Otherwise use "Risk mitigation" or "Strategic capability" with qualitative explanation
- **NO Generic Benchmarks**: Don't cite "industry average" without specific source, date, and applicability explanation

**Prohibited Content**:
- ❌ ANY cost without calculation method shown
- ❌ ANY timeline without task basis or "TBD" with explanation
- ❌ ANY percentage probability without data source
- ❌ Phrases like "estimated at $X" without showing estimate derivation
- ❌ "Similar projects cost..." without specific citation

---

### Document: `GLOSSARY.md` (Technical Glossary)

**Purpose**: Define all technical terms, acronyms, domain-specific concepts, and technologies referenced across the documentation suite.

**Function**:
- Comprehensive glossary organized by category (general, architecture, technologies, frameworks, cloud services, security, business domain, localization)
- Definitions grounded in actual usage within the solution
- Cross-references to detailed documentation
- Acronym expansion with context

**Template**: Use `.doctemplates/GLOSSARY.template.md` as the starting point for this document.

**Audience**: All audiences - reference document for understanding terminology used throughout documentation.

**Content Sources**:
- Extract terms from: All documentation (scan for domain terms, technical jargon, acronyms)
- Technology versions: From dependency analysis
- Domain terms: From Business Domain Model
- Architecture patterns: From System Architecture and Modernization Strategy
- Cloud services: From Cloud Service Recommendations

**Evidence-Based Requirements**:
- **Actual Usage**: Every term must be actually used in the documentation (not generic dictionary definitions)
- **Version Specificity**: All technology/framework terms include actual version from dependency analysis (not "latest")
- **Code References**: Technical terms cite specific files/classes/configs where they appear
- **Source Attribution**: Definitions from external sources (e.g., OWASP, cloud provider docs) must cite source with URL

**Prohibited Content**:
- ❌ Terms not used in the documentation
- ❌ Generic definitions without solution context
- ❌ Outdated version information
- ❌ "Current" or "latest" version references without specific number

---

### Document: `QUICK-START-EXECUTIVE.md` (Executive Quick Start Guide)

**Purpose**: Fast-track executive understanding of the modernization initiative with strategic overview, business case, and decision points.

**Function**:
- 5-minute executive summary of current situation and recommendation
- Business case with benefits and investment summary
- Key milestones and decision points
- Top risks and mitigation strategies
- Success metrics and KPIs
- Recommended next steps

**Template**: Use `.doctemplates/QUICK-START-EXECUTIVE.template.md` as the starting point for this document.

**Audience**: Executive leadership needing rapid understanding without reading full documentation suite.

**Content Sources**:
- Distill from: EXECUTIVE-SUMMARY.md (condensed version)
- Focus on: Strategic direction, investment requirements, key risks, critical decisions
- Cross-reference: Link to detailed documents for deep-dive

**Evidence-Based Requirements** (CRITICAL - Most Estimate-Heavy Document):
- **MANDATORY Comprehensive Disclaimer**: Must appear at document start before any estimates
- **Investment Summary Section**: 
  - ALL resource/effort estimates: Show calculation (tasks × time, team size × duration) OR state "TBD pending [specific planning]"
  - Team composition: Cite actual staffing plan OR state assumptions explicitly
  - Timeline basis: Must explain derivation (velocity data, task count) OR explain why unknown
- **Cost Summary Section**:
  - EVERY cost line item: Show calculation formula, pricing source with date
  - Cloud costs: Link to pricing calculator or vendor quote, state usage assumptions
  - TCO comparison: Show complete calculation, cite current spend data source
  - If costs unknown: Explain WHY (missing usage data), WHAT needed (specific metrics), WHERE to get (monitoring approach)
- **Risk Section**:
  - Probabilities: Use qualitative (HIGH/MEDIUM/LOW) with rationale, NOT percentages without data
  - Risk quantification: Only if based on actual incident cost data or industry breach cost studies (cite source)
- **Timeline/Milestones**:
  - Show logical sequencing and dependencies (acceptable)
  - If durations included: Must show basis (task breakdown, velocity) OR mark as illustrative only
  - Use "Phase A before Phase B" sequencing over firm dates unless evidence-based
- **Benefits/ROI**:
  - Quantified benefits: Must show calculation method
  - If not quantifiable: Use "Risk mitigation", "Strategic capability", or qualitative improvement description
  - ROI timeline: Show derivation OR mark as "requires validation through pilot"

**Prohibited Content**:
- ❌ "$X,XXX monthly cost" without calculation shown
- ❌ "X-month timeline" without task basis
- ❌ "Y% risk probability" without data source
- ❌ "Z% improvement" without measurement basis
- ❌ ANY unqualified numeric estimate

**Required Pattern**: EVERY estimate section must have inline disclaimer explaining basis and limitations

---

### Document: `QUICK-START-ARCHITECT.md` (Architect Quick Start Guide)

**Purpose**: Technical deep-dive for architects and technical leads on modernization architecture, patterns, and implementation approach.

**Function**:
- Current vs target architecture comparison
- Architecture Decision Records (ADRs) for major decisions
- Migration patterns and strategies per phase
- Component breakdown with migration approach per component
- Data architecture and migration strategy
- Security architecture evolution
- Operational excellence (SLOs, DORA metrics, observability)
- Technical risks and mitigations
- Performance and scalability architecture
- Implementation roadmap with technical dependencies

**Template**: Use `.doctemplates/QUICK-START-ARCHITECT.template.md` as the starting point for this document.

**Audience**: Solution architects, technical leads, senior developers, platform engineers.

**Content Sources**:
- Architecture: From System Architecture, Component Inventory
- Decisions: From Modernization Strategy, Cloud Service Recommendations
- Migration: From Implementation Plan, Migration Plan
- Security: From Security Architecture
- Operations: From Operational Readiness
- Data: From Data Migration Analysis (if exists)

**Evidence-Based Requirements**:
- **ADRs (Architecture Decision Records)**:
  - ALL decisions: Document alternatives considered with pros/cons
  - Decision criteria: Show decision matrix with weights (if quantitative) OR clear rationale
  - Cost comparisons: If costs compared, show calculation for EACH option OR state "rough order of magnitude - detailed costing requires [X]"
  - Complexity assessments: Evidence-based (LoC, dependency count, team experience) OR qualitative with rationale
- **Migration Duration/Effort Estimates**:
  - Per component: Show basis (complexity metrics from analysis, similar migration data) OR state "TBD - requires architectural spike"
  - Per phase: Show calculation (task count × avg time, team size) OR state "logical sequencing only - duration TBD"
  - Data migration: Show calculation (data volume ÷ transfer rate) OR state "requires performance testing to determine"
- **Performance/Scalability Metrics**:
  - Current baselines: From actual monitoring data (cite source, date range) OR state "not currently measured - baseline establishment requires [approach]"
  - Target SLOs: Based on business requirements, cloud provider capabilities, OR industry benchmarks (cite source)
  - If unknown: Explain how baseline will be established, what validation approach will prove targets
- **DORA Metrics**:
  - Current state: From actual git/deployment analysis OR "manual process - not measured"
  - Targets: Cite DORA benchmark category (Elite/High/Medium/Low) from specific report year
  - Achievement approach: Phased improvement plan OR "TBD based on CI/CD implementation"
- **Risk Probabilities**:
  - Use qualitative (HIGH/MEDIUM/LOW) with basis (complexity analysis, dependency assessment, expert judgment)
  - If quantitative: Based on historical data from similar projects (cite source with sample size)
- **Roadmap/Implementation Timeline**:
  - Focus on logical dependencies and sequencing (always acceptable)
  - Durations: Only if evidence-based (task breakdown + team capacity) OR mark as "TBD"
  - Use "Phase A must complete before Phase B" over calendar dates unless derived

**Prohibited Content**:
- ❌ "Component migration: 2-4 weeks" without task breakdown or basis
- ❌ "SLO target: 99.9% availability" without explaining achievability and monitoring approach
- ❌ "Current p95 latency: 200ms" without citing measurement source
- ❌ "DORA target: Daily deployments" without explaining current state and improvement path
- ❌ Generic decision criteria without evidence (e.g., "Option A is simpler" - simpler based on what metric?)

**Required Pattern**: Every technical metric/estimate must cite measurement source or explain estimation method

---

### Document: `QUICK-START-DEVELOPER.md` (Developer Quick Start Guide)

**Purpose**: Hands-on guide for developers joining the modernization project to get productive quickly.

**Function**:
- Development environment setup (step-by-step)
- Codebase overview (structure, key components, technology stack)
- Local development workflow
- Code modernization patterns (current → target code examples)
- Testing guidelines (unit, integration, E2E)
- Common tasks with examples (add endpoint, create migration, debug)
- Troubleshooting guide
- Contributing guidelines (PR process, code review, standards)

**Template**: Use `.doctemplates/QUICK-START-DEVELOPER.template.md` as the starting point for this document.

**Audience**: Developers, QA engineers, DevOps engineers joining the modernization team.

**Content Sources**:
- Structure: From Component Inventory, System Architecture
- Technologies: From Dependency Analysis
- Workflows: From team development practices (if documented)
- Patterns: From code analysis and modernization strategy
- Testing: From existing test suites and quality standards

**Evidence-Based Requirements**:
- **Setup Time Estimates**:
  - IF documented from actual team onboarding: "Setup typically takes X-Y hours based on team onboarding history (last 5 developers, avg X hours)"
  - IF not documented: "Setup duration varies by environment, network speed, and familiarity with tools"
  - Build times: Cite actual measurement OR "varies by machine specs"
- **Task Effort Estimates**:
  - IF tracked in team velocity: "Similar features have taken X-Y hours based on sprint history"
  - IF not tracked: "Task complexity varies - estimate after sprint 0 velocity baseline"
  - Avoid generic claims like "This task takes 2 hours" without basis
- **Technology Versions**:
  - ALL versions: From actual dependency analysis (exact versions, not "latest" or "current")
  - Runtime requirements: Specific version numbers with source
- **File Paths and Commands**:
  - ALL paths: Actual repository structure from codebase analysis
  - ALL commands: Verified or clearly marked as examples requiring customization
- **Test Execution Times**:
  - IF measured: "Unit tests run in ~X seconds (N tests, last measured YYYY-MM-DD)"
  - IF not measured: "Test duration varies by test scope and machine"
- **Coverage Targets**:
  - Cite team standards OR industry practice with rationale
  - If aspirational: "Target 80% coverage - current: X% per latest coverage report"

**Prohibited Content**:
- ❌ "Setup takes 15 minutes" without data from actual onboarding
- ❌ "This feature takes 4-8 hours" without tracking data showing this
- ❌ "Tests run in under 1 minute" without measurement
- ❌ File paths that don't match actual repository structure
- ❌ Version numbers that don't match dependency analysis
- ❌ Commands that haven't been verified (unless clearly marked as examples)

**Required Pattern**: 
- Every time estimate either cites basis OR acknowledges variability
- Every path/version is from actual analysis
- Troubleshooting includes REAL issues (from team experience), not generic placeholders

---