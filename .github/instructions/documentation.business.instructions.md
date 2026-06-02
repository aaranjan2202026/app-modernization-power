---
applyTo: '**/.solutiondocs/CURRENT-STATE/2-business/*.md, **/.solutiondocs/MODERNIZATION/2-strategy/*.md'
---

# Business Domain Documentation Guidelines

This file contains instructions for generating business-focused documentation that captures domain models, business rules, workflows, and stakeholder perspectives.

All Current State business documents must be created in the `.solutiondocs/CURRENT-STATE/2-business/` folder.
All Modernization strategy documents must be created in the `.solutiondocs/MODERNIZATION/2-strategy/` folder.

Ensure:
- All sections from the template are included in the same order.
- Replace placeholder text with relevant content.
- Maintain Markdown formatting exactly as in the template.
- Do not add extra sections unless specified.

---

## CRITICAL: Prohibited Business Metrics Without Evidence

**The following business-facing metrics are STRICTLY PROHIBITED in business documentation unless supported by concrete data and detailed calculations:**

### Absolutely Prohibited Without Evidence:

1. **Financial Estimates**:
   - ❌ ROI (Return on Investment) calculations
   - ❌ Cost savings projections
   - ❌ Revenue impact estimates
   - ❌ Budget requirements
   - ❌ TCO (Total Cost of Ownership) comparisons
   - ❌ Payback period calculations
   - ✅ ONLY IF: Based on actual current costs × projected future costs with itemized calculation

2. **Timeline Estimates**:
   - ❌ Project duration
   - ❌ Phase timelines
   - ❌ Go-live dates
   - ❌ Milestone schedules
   - ✅ ONLY IF: Based on detailed work breakdown × team capacity × historical velocity data
   - ✅ ACCEPTABLE: Logical sequencing without dates ("Phase A must complete before Phase B")

3. **Resource Estimates**:
   - ❌ Team size requirements
   - ❌ FTE (Full-Time Equivalent) counts
   - ❌ Skill mix projections
   - ❌ Contractor/consultant costs
   - ✅ ONLY IF: Based on task analysis × productivity data × market rates (with sources)

4. **Business Impact Quantification**:
   - ❌ "Improves customer satisfaction by X%"
   - ❌ "Reduces operational costs by $Y"
   - ❌ "Increases efficiency by Z%"
   - ❌ "Saves N hours per week"
   - ✅ ONLY IF: Based on actual measurements, user studies, or time-motion analysis with data
   - ✅ ACCEPTABLE: Qualitative benefits with clear rationale ("Expected to improve satisfaction based on [reason]")

5. **Risk Costs**:
   - ❌ "Data breach could cost $X million"
   - ❌ "Downtime costs $Y per hour"
   - ❌ "Compliance violation fine of $Z"
   - ✅ ONLY IF: Based on industry studies (cited), regulatory fine schedules (cited), or historical incident costs
   - ✅ ACCEPTABLE: Risk severity (HIGH/MEDIUM/LOW) with impact description

### Required Approach When Data Unavailable:

**Example - ROI Analysis Not Possible**:
```markdown
## Return on Investment

**ROI cannot be calculated because**:
- WHY: Current operational costs not documented
- WHY: Cloud service usage patterns not measured
- WHAT: Need 12 months of current IT operations costs (infrastructure, licensing, support)
- WHAT: Need production workload metrics to project cloud costs
- WHERE: Finance department for current costs; IT operations for usage data
- CALCULATION: ROI = (Future Annual Savings - Migration Cost) / Migration Cost × 100

**To enable ROI calculation**:
1. Gather current costs: Infrastructure ($X/mo from invoices), licenses ($Y/yr from contracts), labor ($Z/yr for 3 FTE operations)
2. Project future costs: Cloud infrastructure estimate from pricing calculator based on measured usage
3. Calculate migration cost: Development effort + migration services + training
```

**Example - Timeline Not Determinable**:
```markdown
## Implementation Timeline

**Timeline estimation not possible at this stage because**:
- Team composition and availability not finalized
- Detailed requirements not yet defined
- Integration complexity requires technical discovery

**Prerequisites for timeline estimation**:
1. Complete technical discovery (2-week effort)
2. Finalize team roster with skill assessments
3. Break down work into sized stories/tasks
4. Determine parallel vs. sequential work streams

**Logical Sequence** (dates TBD after prerequisites):
1. Foundation Phase: Infrastructure setup
2. Migration Phase: Application migration (depends on Foundation)
3. Integration Phase: External system integration (parallel with Migration)
4. Validation Phase: Testing and validation (after Migration + Integration)
```

### Validation Checklist:

Before including ANY business metric, verify:
- [ ] Is this based on actual data (not assumptions)?
- [ ] Is the data source identified and dated?
- [ ] Is the calculation methodology documented?
- [ ] Can a business stakeholder understand and validate the calculation?
- [ ] Are all assumptions explicitly stated and reasonable?

**If answer to ANY question is NO → Do NOT include the metric**

---

## Business Documentation Deliverables

### Current State Documentation

**Output Folder**: `.solutiondocs/CURRENT-STATE/2-business/`

#### Document 2.1: `2.1-business-domain-model.md`

**Purpose**: Document the business domain model, including core entities, their relationships, business workflows, business rules, and the underlying business logic that drives the system.

**Function**: Defines domain entities, entity relationships, business workflows, business rules catalog, domain invariants, business process patterns, state machines, and domain-driven design elements.

**Template**: Use `.doctemplates/CURRENT-STATE/2-business/2.1-business-domain-model.template.md` as the starting point for this document.

---

#### Document 2.2: `2.2-requirements-specification.md`

**Purpose**: Capture requirements as implemented in the current system, reverse-engineered from code, configuration, user interfaces, and documentation.

**Function**: Documents functional requirements, non-functional requirements, user interface requirements, data requirements, integration requirements, compliance and regulatory requirements, and requirements traceability.

**Template**: Use `.doctemplates/CURRENT-STATE/2-business/2.2-requirements-specification.template.md` as the starting point for this document.

**Standards**: Based on IEEE 29148-2018 (Systems and software engineering - Life cycle processes - Requirements engineering).

---

#### Document 2.3: `2.3-compliance-regulatory.md`

**Purpose**: Document compliance and regulatory requirements identified in the system.

**Function**: Captures regulatory frameworks, compliance requirements, data protection requirements, audit trails, and compliance evidence.

**Template**: Use `.doctemplates/CURRENT-STATE/2-business/2.3-compliance-regulatory.template.md` as the starting point for this document.

---

#### Document 2.4: `2.4-user-experience-analysis.md`

**Purpose**: Analyze user experience aspects of the current system.

**Function**: Documents user journeys, UI/UX patterns, accessibility requirements, and user interaction flows.

**Template**: Use `.doctemplates/CURRENT-STATE/2-business/2.4-user-experience-analysis.template.md` as the starting point for this document.

---

#### Document 2.5: `2.5-localization-i18n.md`

**Purpose**: Document internationalization and localization capabilities.

**Function**: Captures language support, locale-specific formatting, cultural adaptations, and internationalization implementation.

**Template**: Use `.doctemplates/CURRENT-STATE/2-business/2.5-localization-i18n.template.md` as the starting point for this document.

---

### Modernization Strategy Documentation

**Output Folder**: `.solutiondocs/MODERNIZATION/2-strategy/`

#### Document 2.1: `2.1-executive-summary.md`

**Purpose**: Provide executive-level summary of modernization initiative.

**Function**: High-level overview, business case, key recommendations, investment summary, and success metrics.

**Template**: Use `.doctemplates/MODERNIZATION/2-strategy/2.1-executive-summary.template.md` as the starting point for this document.

---

#### Document 2.2: `2.2-stakeholder-matrix.md`

**Purpose**: Map features to business value, stakeholders, risk assessment, and modernization roadmap.

**Function**: Feature prioritization matrix, stakeholder map, business capabilities, risk assessment, modernization roadmap, success criteria & KPIs, governance & decision framework, and communication plan.

**Investment & ROI Analysis Note**: Only include ROI/investment analysis if based on actual current costs and projected costs with documented calculation methodology. If cost data unavailable, describe ROI framework and data requirements instead. Do NOT include placeholder financial estimates.

**Template**: Use `.doctemplates/MODERNIZATION/2-strategy/2.2-stakeholder-matrix.template.md` as the starting point for this document.

---
