# Generic Document Templates - Implementation Summary

**Created**: 2024-01-XX
**Scope**: 5 new generic document templates with comprehensive evidence-based estimation requirements

---

## Overview

Created 5 new document templates based on samples folder content, applying all evidence-based estimation rules to ensure no ungrounded estimates appear in generated documentation.

---

## Templates Created

### 1. EXECUTIVE-SUMMARY.template.md

**Location**: `.doctemplates/EXECUTIVE-SUMMARY.template.md`
**Lines**: 500+
**Purpose**: Master template for executive-level consolidated summary documents

**Key Features**:
- **Mandatory Disclaimer Section**: Appears at document start, explains all estimate limitations
- **Evidence-Based Guidelines**: Dedicated section with requirements and examples
- **WHY/WHAT/WHERE Requirements**: Throughout document for all missing data scenarios
- **Inline Warnings**: ⚠️ markers before every section that might contain estimates
- **Template Usage Notes**: 7 specific requirements for compiler agent
- **Bad vs Good Examples**: Throughout showing prohibited vs acceptable patterns

**Critical Sections**:
- Current Situation (evidence-required)
- Business Case (cost calculation requirements)
- Investment Summary (effort/timeline calculation or "TBD" requirements)
- Risk Management (probability source requirements)
- Success Metrics (baseline and target with basis)

---

### 2. GLOSSARY.template.md

**Location**: `.doctemplates/GLOSSARY.template.md`
**Lines**: 200+
**Purpose**: Technical glossary template for all domain/technical terms

**Key Features**:
- **Evidence-Based Definitions**: Every term grounded in actual codebase usage
- **Code References**: Technical terms cite specific files/classes
- **Version Specificity**: All technology versions from actual analysis (not "latest")
- **Cross-Referencing**: Links to detailed documentation
- **Organized by Category**: General, Architecture, Technologies, Frameworks, Cloud, Security, Business Domain, Localization, Acronyms

**Quality Requirements**:
- No generic definitions without solution context
- Current usage must be documented
- All versions match dependency analysis
- Cross-references must work (no broken links)

---

### 3. QUICK-START-EXECUTIVE.template.md

**Location**: `.doctemplates/QUICK-START-EXECUTIVE.template.md`
**Lines**: 600+
**Purpose**: Fast-track executive understanding with strategic overview and decision points

**Key Features**:
- **CRITICAL DISCLAIMER**: Comprehensive disclaimer at document start covering all estimate types
- **Evidence Requirements Per Section**:
  - Investment Summary: ALL resource/effort with calculation or "TBD"
  - Cloud Costs: Show calculation (quantity × price), pricing source, date
  - TCO Comparison: Complete calculation with assumptions
  - Risk Quantification: Source citation or qualitative assessment
  - Timeline/Milestones: Logical sequencing or evidence-based durations
  - Benefits/ROI: Calculation method or qualitative description
- **Required Alternatives**: When data missing, explain WHY/WHAT/WHERE
- **Decision Points**: Go/No-Go criteria with objective measures

**Prohibited Content Examples**:
- ❌ "$X,XXX" without calculation
- ❌ "X months" without task breakdown
- ❌ "Y% probability" without source
- ❌ "Similar projects cost..." without citation

---

### 4. QUICK-START-ARCHITECT.template.md

**Location**: `.doctemplates/QUICK-START-ARCHITECT.template.md`
**Lines**: 700+
**Purpose**: Technical deep-dive for architects with ADRs, patterns, implementation approach

**Key Features**:
- **ADR (Architecture Decision Record) Template**: Complete structure with alternatives, decision matrix, validation
- **Evidence Requirements**:
  - All decisions: Document alternatives with pros/cons
  - Cost comparisons: Show calculation for each option OR state estimation requirements
  - Migration effort: Basis (complexity metrics, similar data) OR "TBD - requires spike"
  - Performance metrics: Current from monitoring data OR "baseline establishment needed"
  - DORA metrics: Current state from analysis, targets from benchmark source
- **Risk Assessment**: Qualitative (HIGH/MEDIUM/LOW) with rationale OR quantitative with data source
- **Roadmap Focus**: Logical dependencies and sequencing (acceptable) vs firm dates (requires evidence)

**Quality Criteria**:
- Every architecture decision has documented rationale
- Every estimate shows calculation or states why unknown
- Every metric has measurement approach defined
- Code references are specific (file/class level)

---

### 5. QUICK-START-DEVELOPER.template.md

**Location**: `.doctemplates/QUICK-START-DEVELOPER.template.md`
**Lines**: 600+
**Purpose**: Hands-on developer onboarding guide from setup to productive contributor

**Key Features**:
- **Setup Instructions**: Step-by-step with OS-specific variations
- **Evidence Requirements**:
  - Setup times: From actual onboarding data OR "varies by environment"
  - Task efforts: From sprint tracking OR "complexity varies"
  - Technology versions: Exact versions from dependency analysis
  - File paths: Actual repository structure
  - Test execution times: From measurement OR "varies by scope"
- **Real Troubleshooting**: Actual team issues (when available), not generic placeholders
- **Code Examples**: Current → Target pattern migrations
- **No Ungrounded Promises**: Every time estimate cites basis or acknowledges variability

**Required Pattern**:
- Every estimate either cites basis OR acknowledges variability
- Every path/version is from actual analysis
- Commands verified or clearly marked as examples

---

## Instructions File Updated

### documentation.generic.instructions.md

**Location**: `.github/instructions/documentation.generic.instructions.md`
**Updates**: Added comprehensive guidance for all 5 new templates

**Key Additions**:
1. **Global Evidence-Based Requirements Section**:
   - 3 core principles (no ungrounded estimates, WHY/WHAT/WHERE when unavailable, acceptable alternatives)
   - Comprehensive BAD vs GOOD examples
   - Applies to ALL generic documents

2. **Template-Specific Requirements**:
   - Each template has dedicated section with specific evidence requirements
   - Prohibited content patterns per template type
   - Quality criteria per template

3. **applyTo Directive Updated**:
   - Now covers all 6 generic documents: README, EXECUTIVE-SUMMARY, GLOSSARY, QUICK-START-EXECUTIVE, QUICK-START-ARCHITECT, QUICK-START-DEVELOPER

---

## Evidence-Based Patterns Applied

### Across All Templates

**Pattern 1: Cost Estimates**
- ❌ Prohibited: "$5,000/month infrastructure cost"
- ✅ Required: "$4,200-5,800/month (Azure pricing calculator, 2024-01-15). Based on: Container Apps Standard tier (4-8 instances @ $0.12/hr), Azure SQL S2 ($150/mo), Storage ($200/mo). See calculation: [link]"

**Pattern 2: Timeline Estimates**
- ❌ Prohibited: "Migration will take 3-6 months"
- ✅ Required: "3-6 months based on: 47 components × 2-4 days/component ÷ 3 FTE team = 94-188 days. Assumes velocity from [source]. Confidence: Medium"
- ✅ Alternative: "Timeline TBD because: task decomposition incomplete. Requires: 2-week architectural spike. Sequencing: Phase A before Phase B"

**Pattern 3: Risk Probabilities**
- ❌ Prohibited: "30% probability of this risk"
- ✅ Required (Qualitative): "HIGH probability based on: similar migration in Project X encountered this in 3/4 cases"
- ✅ Required (Quantitative): "30% probability (source: historical data - 3 incidents in 10 similar projects [source])"

**Pattern 4: Performance Metrics**
- ❌ Prohibited: "Current latency: 200ms p95"
- ✅ Required: "Current latency: 200ms p95 (source: APM data 2024-01-01 to 2024-01-30)"
- ✅ Alternative: "Current latency unknown - no APM. Baseline establishment requires: Application Insights deployment, 30-day collection"

**Pattern 5: Missing Data Explanation**
- ❌ Prohibited: "Data not available", "Cannot estimate at this time", "Timeline TBD"
- ✅ Required: "Timeline cannot be estimated because: (WHY) 23 components have unknown refactoring scope. (WHAT) Need: component complexity analysis, dependency mapping. (WHERE) Requires: 2-week architectural spike"

---

## Template Relationships

```
EXECUTIVE-SUMMARY.md (Comprehensive)
├── Consolidates from all CURRENT-STATE and MODERNIZATION docs
├── Referenced by: QUICK-START-EXECUTIVE.md (condensed version)
└── Uses: GLOSSARY.md for term definitions

QUICK-START-EXECUTIVE.md (Strategic)
├── Distills: EXECUTIVE-SUMMARY.md
├── Links to: Detailed strategy and architecture docs
└── Uses: GLOSSARY.md for acronyms

QUICK-START-ARCHITECT.md (Technical)
├── Sources from: System Architecture, Implementation Plan, Cloud Recommendations
├── Links to: ADRs, Component details, Security architecture
└── Uses: GLOSSARY.md for technical terms

QUICK-START-DEVELOPER.md (Hands-on)
├── Sources from: Component Inventory, Dependency Analysis
├── References: Code structure, development standards
└── Uses: GLOSSARY.md for framework terms

GLOSSARY.md (Reference)
├── Extracts from: ALL documentation
├── Defines: Terms, acronyms, technologies, domain concepts
└── Referenced by: All other documents

README.md (Index)
├── Links to: ALL documents
└── Provides: Navigation and overview
```

---

## Compliance Summary

All 5 templates now enforce:

✅ **No Ungrounded Estimates**: Every cost, timeline, effort, or risk probability requires:
- Derivable calculation with formula shown
- Data source with date
- Methodology explanation
- Explicit assumptions and caveats

✅ **WHY/WHAT/WHERE Explanations**: When data unavailable:
- WHY it cannot be provided (missing analysis, unmeasured state)
- WHAT information is needed (specific data points, analysis types)
- WHERE to get it (data source, validation approach, measurement strategy)

✅ **Acceptable Alternatives**: 
- Qualitative assessments with clear rationale
- Logical sequencing without dates
- Order-of-magnitude ranges with confidence levels
- "TBD pending [specific analysis]" with explanation

✅ **Comprehensive Examples**: Every template includes:
- Bad vs Good patterns
- Inline warnings before estimate sections
- Template usage notes for compiler agent
- Prohibited content lists
- Quality criteria checklists

---

## Usage by Compiler Agent

When generating generic documents, the compiler agent will:

1. **Read Template**: Load appropriate template from `.doctemplates/`
2. **Apply Instructions**: Follow `documentation.generic.instructions.md` requirements
3. **Enforce Evidence Rules**: All estimates must meet evidence-based criteria
4. **Explain Unavailability**: Use WHY/WHAT/WHERE pattern when data missing
5. **Validate Quality**: Check against template-specific quality criteria

**Prohibited Agent Behaviors**:
- ❌ Include ANY numeric estimate without showing derivation
- ❌ Use vague "data not available" without explanation
- ❌ Reference "industry benchmarks" without specific source and date
- ❌ Make assumptions about timelines/costs without stating them explicitly
- ❌ Skip disclaimer sections

**Required Agent Behaviors**:
- ✅ Show calculation for every cost estimate
- ✅ Explain basis for every timeline estimate OR state "TBD" with reasoning
- ✅ Cite source for every external reference
- ✅ Use actual data from analysis (versions, paths, metrics)
- ✅ Include comprehensive disclaimers before estimate sections

---

## Next Steps

Templates are ready for use. When compiler agent generates these documents:

1. **First Generation**: Create all 6 generic documents from templates
2. **Validation**: Review for compliance with evidence-based requirements
3. **Iteration**: Update based on actual analysis data availability
4. **Maintenance**: Keep templates updated as standards evolve

**Related Compliance Files**:
- `.github/agents/documentation-compiler.agent.md` - References generic instructions
- `EVIDENCE-BASED-ESTIMATES-COMPLIANCE.md` - Overall compliance report
- All other template and instruction files follow same principles

---

**Status**: ✅ COMPLETE - All 5 templates created with comprehensive evidence-based requirements applied
