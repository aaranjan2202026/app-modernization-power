# Documentation Templates - Comprehensive Solution Analysis

> **Industry-standard templates for documenting legacy application modernization**

---

## 📚 Overview

This folder contains comprehensive documentation templates organized according to industry standards (Arc42, C4 Model, IEEE 42010, ISO/IEC 42010) to facilitate thorough analysis and documentation of legacy applications for modernization purposes.

### Documentation Philosophy

All templates in this collection emphasize:

✅ **Evidence-Based Analysis** - All findings must be grounded in actual codebase observations
✅ **Source Attribution** - Citations and references for all data points  
✅ **Reasoning Documentation** - Clear explanations of how conclusions were reached
✅ **Traceability** - Links between business requirements, architecture decisions, and implementation  
✅ **Modernization Focus** - Special attention to migration paths, technical debt, and cloud-readiness

### Key Principles

1. **Cite Your Sources**: Every assertion should reference:
   - File paths and line numbers for code observations
   - Configuration files for settings
   - Tool outputs for metrics
   - Interview notes for stakeholder input
   - Industry standards for recommendations

2. **Show Your Reasoning**: Explain:
   - Why a particular technology was chosen
   - How you calculated effort estimates
   - What alternatives were considered
   - Why certain risks are prioritized

3. **Provide Evidence**: Include:
   - Code snippets demonstrating patterns
   - Metrics from analysis tools
   - Architecture diagrams with actual component names
   - Screenshots of actual systems
   - Links to relevant documentation

4. **Enable Verification**: Allow readers to:
   - Retrace your analysis steps
   - Validate your findings
   - Update documentation as system evolves
   - Understand the context of decisions

---

## 🗂️ Template Organization

Templates are organized into **two major groups**: Current State Analysis and Modernization Planning.

### 📊 Organization Philosophy

**CURRENT STATE** templates document the application as it exists today - architecture, code, processes, and issues. These are evidence-based observations of what IS.

**MODERNIZATION** templates plan the future - strategies, migration approaches, and transformation roadmaps. These are forward-looking documents about what COULD BE.

```
.doctemplates/
│
├── 📋 README.md                                    (This file)
├── 🎯 USAGE-GUIDE.md                              (How to use these templates)
│
├── 📊 CURRENT-STATE/                              Current Application Analysis
│   │
│   ├── 1-architecture/                            Technical Architecture (As-Is)
│   │   ├── 1.1-system-architecture.template.md        Current system structure (C4 model)
│   │   ├── 1.2-data-architecture.template.md          Current database design and data flows
│   │   ├── 1.3-api-integration-catalog.template.md    Current APIs and integrations
│   │   ├── 1.4-project-inventory.template.md          Current project catalog
│   │   ├── 1.5-infrastructure-architecture.template.md Current hosting and infrastructure
│   │   └── 1.6-integration-architecture.template.md    Current external integrations
│   │
│   ├── 2-business/                                Business Context (As-Is)
│   │   ├── 2.1-business-domain-model.template.md      Current business processes
│   │   ├── 2.2-requirements-specification.template.md  Current features and requirements
│   │   ├── 2.3-compliance-regulatory.template.md      Current compliance state
│   │   ├── 2.4-user-experience-analysis.template.md    Current UI/UX patterns
│   │   └── 2.5-localization-i18n.template.md          Current language support
│   │
│   ├── 3-implementation/                          Development Practices (As-Is)
│   │   ├── 3.1-developer-guide.template.md            Current development setup
│   │   ├── 3.2-coding-standards.template.md           Current coding patterns observed
│   │   └── 3.3-test-strategy.template.md              Current testing approach
│   │
│   ├── 4-operations/                              Operations & Runtime (As-Is)
│   │   ├── 4.1-deployment-operations.template.md      Current deployment procedures
│   │   ├── 4.2-security-architecture.template.md      Current security controls
│   │   ├── 4.3-monitoring-observability.template.md   Current monitoring setup
│   │   ├── 4.4-disaster-recovery.template.md          Current DR procedures
│   │   ├── 4.5-scalability-capacity.template.md       Current capacity and bottlenecks
│   │   └── 4.6-incident-management.template.md        Current incident response
│   │
│   └── 5-quality/                                 Quality Assessment (As-Is)
│       ├── 5.1-technical-debt-assessment.template.md  Current code quality issues
│       ├── 5.2-performance-assessment.template.md     Current performance metrics
│       ├── 5.3-risk-assessment.template.md            Current risks and vulnerabilities
│       ├── 5.4-dependency-management.template.md      Current dependencies inventory
│       └── 5.5-accessibility-compliance.template.md    Current accessibility state
│
└── 🚀 MODERNIZATION/                              Modernization & Migration Planning
    │
    ├── 1-assessment/                              Modernization Assessment
    │   ├── 1.1-legacy-assessment.template.md          Gap analysis and obsolescence
    │   ├── 1.2-cloud-readiness-assessment.template.md Cloud migration readiness
    │   └── 1.3-technology-evolution-roadmap.template.md Technology upgrade paths
    │
    ├── 2-strategy/                                Modernization Strategy
    │   ├── 2.1-executive-summary.template.md          Modernization recommendations
    │   ├── 2.2-stakeholder-matrix.template.md         Modernization stakeholders
    │   └── 2.3-modernization-strategy.template.md     Approach and roadmap
    │
    ├── 3-execution/                               Migration Execution
    │   ├── 3.1-migration-plan.template.md             Step-by-step migration approach
    │   ├── 3.2-refactoring-patterns.template.md       Refactoring strategies
    │   └── 3.3-knowledge-transfer.template.md         Knowledge preservation
    │
    └── 4-improvement/                             Continuous Improvement
        └── 4.1-continuous-improvement.template.md      Ongoing improvement processes
```

---

## 🎯 Quick Start Guide

### For Different Audiences

#### 👔 **Executives & Business Stakeholders**

**Start here to understand business value and modernization investment needs:**

1. **[Executive Summary](./MODERNIZATION/2-strategy/2.1-executive-summary.template.md)**
   - High-level modernization overview with evidence citations
   - Critical findings from current state analysis
   - Investment recommendations with justification

2. **[Legacy Assessment](./MODERNIZATION/1-assessment/1.1-legacy-assessment.template.md)**
   - Current state baseline analysis
   - Technology obsolescence risks with vendor citations
   - Gap analysis between current and target state

3. **[Cloud Readiness Assessment](./MODERNIZATION/1-assessment/1.2-cloud-readiness-assessment.template.md)**
   - Migration feasibility with evidence
   - Cloud suitability analysis
   - Blockers and enablers identified from codebase

4. **[Modernization Strategy](./MODERNIZATION/2-strategy/2.3-modernization-strategy.template.md)**
   - Evidence-based approach options
   - Strategic roadmap with dependencies
   - Success criteria and measurable metrics

---

#### 🏗️ **Architects & Technical Leaders**

**Start with current state analysis, then review modernization plans:**

**Current State:**

1. **[System Architecture](./CURRENT-STATE/1-architecture/1.1-system-architecture.template.md)**
   - Current C4 architecture diagrams with source annotations
   - Existing technology stack documentation
   - Historical Architectural Decision Records (ADRs)

2. **[Data Architecture](./CURRENT-STATE/1-architecture/1.2-data-architecture.template.md)**
   - Current database design with schema citations
   - Existing data flow analysis
   - Data quality observations

3. **[Technical Debt Assessment](./CURRENT-STATE/5-quality/5.1-technical-debt-assessment.template.md)**
   - Code quality metrics with tool outputs
   - Debt inventory with evidence
   - Impact analysis

**Modernization Planning:**

4. **[Technology Evolution Roadmap](./MODERNIZATION/1-assessment/1.3-technology-evolution-roadmap.template.md)**
   - Technology upgrade paths with dependencies
   - Framework migration strategies
   - Breaking change analysis

5. **[Modernization Strategy](./MODERNIZATION/2-strategy/2.3-modernization-strategy.template.md)**
   - Architecture modernization approach
   - Cloud-native patterns consideration
   - Phased transformation plan

---

#### 👨‍💻 **Developers & Engineers**

**Start with understanding the current system:**

**Current State:**

1. **[Developer Guide](./CURRENT-STATE/3-implementation/3.1-developer-guide.template.md)**
   - Current environment setup with exact versions
   - Existing build process documentation
   - Current contribution workflow

2. **[System Architecture](./CURRENT-STATE/1-architecture/1.1-system-architecture.template.md)**
   - Current system structure overview
   - Existing component relationships with diagrams
   - Design patterns currently in use

3. **[Coding Standards](./CURRENT-STATE/3-implementation/3.2-coding-standards.template.md)**
   - Current coding patterns observed in codebase
   - Existing conventions and styles
   - Common patterns found

**Modernization Work:**

4. **[Knowledge Transfer](./MODERNIZATION/3-execution/3.3-knowledge-transfer.template.md)**
   - Domain knowledge to preserve during migration
   - Legacy system quirks and workarounds
   - Critical business logic documentation

5. **[Refactoring Patterns](./MODERNIZATION/3-execution/3.2-refactoring-patterns.template.md)**
   - Anti-patterns identified and how to fix them
   - Recommended refactoring approaches
   - Code examples with before/after transformations

---

#### ⚙️ **DevOps & Operations**

**Understand current operations, then plan modernization:**

**Current State:**

1. **[Deployment & Operations](./CURRENT-STATE/4-operations/4.1-deployment-operations.template.md)**
   - Current deployment procedures with commands
   - Existing operational runbooks
   - Current environment configurations

2. **[Infrastructure Architecture](./CURRENT-STATE/1-architecture/1.5-infrastructure-architecture.template.md)**
   - Current hosting environment
   - Existing infrastructure as code
   - Current DevOps pipeline

3. **[Monitoring & Observability](./CURRENT-STATE/4-operations/4.3-monitoring-observability.template.md)**
   - Current monitoring setup and tools
   - Existing alerting thresholds
   - Current dashboard inventory

4. **[Scalability & Capacity](./CURRENT-STATE/4-operations/4.5-scalability-capacity.template.md)**
   - Current capacity metrics and limits
   - Existing bottlenecks identified
   - Current scaling constraints

**Modernization Planning:**

5. **[Cloud Readiness Assessment](./MODERNIZATION/1-assessment/1.2-cloud-readiness-assessment.template.md)**
   - Cloud migration readiness for infrastructure
   - IaC transformation requirements
   - DevOps pipeline modernization needs

---

#### 📊 **Product Managers & Business Analysts**

**Understand current business capabilities:**

**Current State:**

1. **[Business Domain Model](./CURRENT-STATE/2-business/2.1-business-domain-model.template.md)**
   - Current business processes mapped to code
   - Existing domain entities and relationships
   - Business rules with implementation citations

2. **[User Experience Analysis](./CURRENT-STATE/2-business/2.4-user-experience-analysis.template.md)**
   - Current user journey mapping
   - UI/UX patterns currently in use
   - Usability findings and pain points

3. **[Requirements Specification](./CURRENT-STATE/2-business/2.2-requirements-specification.template.md)**
   - Current feature inventory with code locations
   - Existing capabilities documentation
   - User stories implemented

**Modernization Planning:**

4. **[Stakeholder Matrix](./MODERNIZATION/2-strategy/2.2-stakeholder-matrix.template.md)**
   - Modernization stakeholder analysis
   - Business value proposition for changes
   - Impact assessment on users and processes

---

## 📖 Template Categories Explained

### 📊 CURRENT STATE Templates

These templates document the application **as it exists today**. Focus on evidence-based observations, not opinions.

---

### 1️⃣ Architecture (Current State)

**Purpose**: Document current technical architecture and structure

**Audience**: Architects, Technical Leads, Senior Developers

**Focus**: How the system is built today - components, data, integrations

**Templates**:
- **1.1 System Architecture**: Current system structure with C4 diagrams
- **1.2 Data Architecture**: Current database design and data flows
- **1.3 API Integration Catalog**: Current APIs and integrations inventory
- **1.4 Project Inventory**: Current projects and modules catalog
- **1.5 Infrastructure Architecture**: Current hosting and deployment setup
- **1.6 Integration Architecture**: Current external system interfaces

**When to use**: Understanding current architecture before planning changes

**Key Value**: Baseline understanding of technical landscape

**Evidence Required**:
- Code citations for all architectural observations
- Configuration file references for settings
- Diagram annotations showing actual component names from codebase
- ADRs with historical context and reasoning

---

### 2️⃣ Business (Current State)

**Purpose**: Document current business context and capabilities

**Audience**: Business Analysts, Product Managers, Domain Experts

**Focus**: Business processes, features, compliance as they exist today

**Templates**:
- **2.1 Business Domain Model**: Current business processes and entities
- **2.2 Requirements Specification**: Current feature inventory
- **2.3 Compliance & Regulatory**: Current compliance state
- **2.4 User Experience Analysis**: Current UI/UX patterns
- **2.5 Localization & i18n**: Current internationalization support

**When to use**: Understanding business capabilities before transformation

**Key Value**: Preservation of business knowledge during technical changes

**Evidence Required**:
- Business process mapped to code locations
- Feature inventory with implementation citations
- Compliance controls with configuration references
- User journey flows with screenshot annotations

---

### 3️⃣ Implementation (Current State)

**Purpose**: Document current development practices and setup

**Audience**: Developers, QA Engineers, Tech Leads

**Focus**: How development is done today - environment, standards, testing

**Templates**:
- **3.1 Developer Guide**: Current development environment and workflow
- **3.2 Coding Standards**: Current coding patterns observed in codebase
- **3.3 Test Strategy**: Current testing approach and coverage

**When to use**: Onboarding developers, understanding current practices

**Key Value**: Baseline for development process improvements

**Evidence Required**:
- Exact dependency versions from package files
- Code examples of current patterns from actual codebase
- Test coverage metrics from tools with version numbers
- Build process documented with actual commands

---

### 4️⃣ Operations (Current State)

**Purpose**: Document current operational procedures and runtime environment

**Audience**: DevOps, SRE, Operations Teams, Security Teams

**Focus**: How the system runs today - deployment, monitoring, security

**Templates**:
- **4.1 Deployment & Operations**: Current deployment procedures
- **4.2 Security Architecture**: Current security controls
- **4.3 Monitoring & Observability**: Current monitoring setup
- **4.4 Disaster Recovery**: Current DR and backup procedures
- **4.5 Scalability & Capacity**: Current capacity and bottlenecks
- **4.6 Incident Management**: Current incident response

**When to use**: Understanding production operations before migration

**Key Value**: Operational continuity during transformation

**Evidence Required**:
- Deployment scripts and commands actually used
- Security scan results with tool versions
- Monitoring dashboard configurations
- Incident postmortems with actual timestamps and impacts
- Capacity metrics from monitoring tools

---

### 5️⃣ Quality (Current State)

**Purpose**: Document current quality state and issues

**Audience**: Engineering Managers, Architects, QA Leads

**Focus**: Current quality metrics, debt, risks, dependencies

**Templates**:
- **5.1 Technical Debt Assessment**: Current code quality and debt inventory
- **5.2 Performance Assessment**: Current performance metrics and bottlenecks
- **5.3 Risk Assessment**: Current risks and vulnerabilities
- **5.4 Dependency Management**: Current third-party dependencies
- **5.5 Accessibility Compliance**: Current accessibility state

**When to use**: Quality baseline before improvement initiatives

**Key Value**: Quantified quality metrics for prioritization

**Evidence Required**:
- Tool-generated metrics (SonarQube, ESLint, etc.) with versions
- Performance test results with load profiles
- Security scan results with CVE numbers
- Dependency versions from lock files
- WCAG audit results with specific violations

---

### 🚀 MODERNIZATION Templates

These templates plan the **future state** and transformation approach. Focus on evidence-based recommendations.

---

### 1️⃣ Assessment (Modernization)

**Purpose**: Evaluate modernization readiness and requirements

**Audience**: Architects, Technical Leaders, Engineering Managers

**Focus**: Gaps, readiness, and upgrade paths for modernization

**Templates**:
- **1.1 Legacy Assessment**: Gap analysis between current and modern practices
- **1.2 Cloud Readiness Assessment**: Cloud migration feasibility analysis
- **1.3 Technology Evolution Roadmap**: Framework and dependency upgrade paths

**When to use**: Before creating modernization strategy

**Key Value**: Evidence-based assessment of modernization feasibility

**Evidence Required**:
- Technology end-of-life dates with vendor citations
- Cloud readiness checklist with codebase evidence for each item
- Dependency compatibility matrix with version requirements
- Breaking change analysis from upgrade documentation

---

### 2️⃣ Strategy (Modernization)

**Purpose**: Define modernization approach and secure buy-in

**Audience**: C-suite, VPs, Directors, Project Sponsors

**Focus**: Business case, approach, stakeholders for modernization

**Templates**:
- **2.1 Executive Summary**: Modernization recommendations and business case
- **2.2 Stakeholder Matrix**: Modernization stakeholder analysis
- **2.3 Modernization Strategy**: Transformation approach and roadmap

**When to use**: Securing funding, aligning stakeholders, strategic planning

**Key Value**: Business justification for modernization investment

**Evidence Required**:
- Findings from current state analysis documents
- Quantified technical debt from quality assessments
- Risk analysis from security and dependency scans
- Comparison of modernization approaches with trade-offs

---

### 3️⃣ Execution (Modernization)

**Purpose**: Execute the modernization transformation

**Audience**: Developers, Architects, Project Managers

**Focus**: Step-by-step migration execution and knowledge preservation

**Templates**:
- **3.1 Migration Plan**: Phased migration approach with dependencies
- **3.2 Refactoring Patterns**: Recommended refactoring strategies
- **3.3 Knowledge Transfer**: Critical knowledge preservation

**When to use**: During active modernization implementation

**Key Value**: Executable transformation plan

**Evidence Required**:
- Dependency graph for migration sequencing
- Code examples of refactoring patterns from actual codebase
- Test coverage requirements to ensure safe refactoring
- Rollback procedures for each migration phase

---

### 4️⃣ Improvement (Modernization)

**Purpose**: Continuous improvement post-modernization

**Audience**: Engineering Managers, DevOps, Quality Teams

**Focus**: Ongoing improvement processes and metrics

**Templates**:
- **4.1 Continuous Improvement**: Post-modernization improvement processes

**When to use**: After initial modernization, ongoing improvements

**Key Value**: Sustained modernization and quality culture

**Evidence Required**:
- Metrics tracking improvement over time
- Retrospective findings with action items
- Quality trend analysis with tool outputs

---

## ⚙️ Template Usage Patterns

### Pattern 1: Complete Assessment (Recommended)

**Use case**: Comprehensive legacy application analysis for modernization

**Documents to create** (logical flow):

1. **Current State Discovery**
   - [Project Inventory](./CURRENT-STATE/1-architecture/1.4-project-inventory.template.md) - Catalog all components
   - [System Architecture](./CURRENT-STATE/1-architecture/1.1-system-architecture.template.md) - Map current architecture
   - [Data Architecture](./CURRENT-STATE/1-architecture/1.2-data-architecture.template.md) - Document database design
   - [Infrastructure Architecture](./CURRENT-STATE/1-architecture/1.5-infrastructure-architecture.template.md) - Document current hosting

2. **Current State Analysis**
   - [Business Domain Model](./CURRENT-STATE/2-business/2.1-business-domain-model.template.md) - Map business processes
   - [Technical Debt Assessment](./CURRENT-STATE/5-quality/5.1-technical-debt-assessment.template.md) - Quantify code quality
   - [Performance Assessment](./CURRENT-STATE/5-quality/5.2-performance-assessment.template.md) - Measure performance
   - [Dependency Management](./CURRENT-STATE/5-quality/5.4-dependency-management.template.md) - Inventory dependencies
   - [Security Architecture](./CURRENT-STATE/4-operations/4.2-security-architecture.template.md) - Document security controls

3. **Modernization Assessment**
   - [Legacy Assessment](./MODERNIZATION/1-assessment/1.1-legacy-assessment.template.md) - Identify gaps and obsolescence
   - [Cloud Readiness Assessment](./MODERNIZATION/1-assessment/1.2-cloud-readiness-assessment.template.md) - Evaluate cloud fit
   - [Technology Evolution Roadmap](./MODERNIZATION/1-assessment/1.3-technology-evolution-roadmap.template.md) - Plan upgrades

4. **Modernization Planning**
   - [Modernization Strategy](./MODERNIZATION/2-strategy/2.3-modernization-strategy.template.md) - Define transformation approach
   - [Migration Plan](./MODERNIZATION/3-execution/3.1-migration-plan.template.md) - Detail execution steps
   - [Knowledge Transfer](./MODERNIZATION/3-execution/3.3-knowledge-transfer.template.md) - Preserve critical knowledge

5. **Communication Phase**
   - [Executive Summary](./MODERNIZATION/2-strategy/2.1-executive-summary.template.md) - Synthesize findings
   - [Stakeholder Matrix](./MODERNIZATION/2-strategy/2.2-stakeholder-matrix.template.md) - Align stakeholders

---

### Pattern 2: Quick Assessment

**Use case**: Rapid initial evaluation for decision-making

**Minimum viable documentation** (prioritized for fast insights):

**Current State (Essential):**
1. [System Architecture](./CURRENT-STATE/1-architecture/1.1-system-architecture.template.md) - High-level C4 diagrams
2. [Technical Debt Assessment](./CURRENT-STATE/5-quality/5.1-technical-debt-assessment.template.md) - Critical issues only
3. [Dependency Management](./CURRENT-STATE/5-quality/5.4-dependency-management.template.md) - Key dependency risks

**Modernization (Essential):**
4. [Legacy Assessment](./MODERNIZATION/1-assessment/1.1-legacy-assessment.template.md) - Gap analysis snapshot
5. [Cloud Readiness Assessment](./MODERNIZATION/1-assessment/1.2-cloud-readiness-assessment.template.md) - Migration feasibility
6. [Executive Summary](./MODERNIZATION/2-strategy/2.1-executive-summary.template.md) - Recommendations with evidence

---

### Pattern 3: Security & Compliance Focus

**Use case**: Security audit or compliance certification

**Key documents** (all current state):

1. [Security Architecture](./CURRENT-STATE/4-operations/4.2-security-architecture.template.md) - Current security controls
2. [Compliance & Regulatory](./CURRENT-STATE/2-business/2.3-compliance-regulatory.template.md) - Current compliance state
3. [Risk Assessment](./CURRENT-STATE/5-quality/5.3-risk-assessment.template.md) - Current vulnerabilities
4. [Disaster Recovery](./CURRENT-STATE/4-operations/4.4-disaster-recovery.template.md) - Current DR procedures
5. [Dependency Management](./CURRENT-STATE/5-quality/5.4-dependency-management.template.md) - Dependency vulnerabilities

---

### Pattern 4: Migration Execution

**Use case**: Actively modernizing an application with implementation team

**Prerequisites (Current State):**
1. [Developer Guide](./CURRENT-STATE/3-implementation/3.1-developer-guide.template.md) - Current development setup
2. [Test Strategy](./CURRENT-STATE/3-implementation/3.3-test-strategy.template.md) - Current testing baseline
3. [Deployment & Operations](./CURRENT-STATE/4-operations/4.1-deployment-operations.template.md) - Current deployment process

**Implementation (Modernization):**
4. [Migration Plan](./MODERNIZATION/3-execution/3.1-migration-plan.template.md) - Detailed execution roadmap
5. [Knowledge Transfer](./MODERNIZATION/3-execution/3.3-knowledge-transfer.template.md) - Preserve domain knowledge
6. [Refactoring Patterns](./MODERNIZATION/3-execution/3.2-refactoring-patterns.template.md) - Standardize refactoring approach
7. [Continuous Improvement](./MODERNIZATION/4-improvement/4.1-continuous-improvement.template.md) - Track and adapt

### Pattern 5: Cloud Migration Focus

**Use case**: Moving legacy application to cloud infrastructure

**Current State Analysis:**
1. [Infrastructure Architecture](./CURRENT-STATE/1-architecture/1.5-infrastructure-architecture.template.md) - Current hosting setup
2. [Scalability & Capacity](./CURRENT-STATE/4-operations/4.5-scalability-capacity.template.md) - Current bottlenecks
3. [Security Architecture](./CURRENT-STATE/4-operations/4.2-security-architecture.template.md) - Current security controls
4. [Performance Assessment](./CURRENT-STATE/5-quality/5.2-performance-assessment.template.md) - Current performance baseline

**Cloud Migration Planning:**
5. [Cloud Readiness Assessment](./MODERNIZATION/1-assessment/1.2-cloud-readiness-assessment.template.md) - Migration readiness
6. [Technology Evolution Roadmap](./MODERNIZATION/1-assessment/1.3-technology-evolution-roadmap.template.md) - Cloud-native frameworks
7. [Modernization Strategy](./MODERNIZATION/2-strategy/2.3-modernization-strategy.template.md) - Cloud migration approach

---

## 🔍 Citation & Evidence Guidelines

> **CRITICAL REQUIREMENT**: Every assertion, finding, metric, and recommendation in documentation MUST include evidence, citations, or explicit acknowledgment of assumptions.

### Why Citations Matter

In modernization projects, decisions often have significant financial and operational impacts. Proper citation:

- **Builds Trust**: Stakeholders can verify findings independently
- **Enables Updates**: Future analysts can retrace analysis steps as system evolves
- **Supports Decisions**: Clear reasoning for technology and architecture choices
- **Reduces Risk**: Distinguishes facts from assumptions explicitly
- **Prevents Errors**: Reduces spread of outdated or incorrect information
- **Ensures Accountability**: Makes analysis auditable and defensible

### Mandatory Evidence Requirements

**Every template includes a dedicated "Evidence Sources" section that MUST contain**:

1. **Source Code Citations**: File paths and line numbers for all code observations
2. **Tool Outputs**: Analysis tool names, versions, and run dates for all metrics
3. **Configuration References**: Config file locations for all settings mentioned
4. **Stakeholder Sources**: Interview dates, attendees for all business requirements
5. **External References**: Links to vendor docs, standards, research papers
6. **Assumption Log**: Explicit list of assumptions made and their justification

**❌ Unacceptable**: "The application has poor performance"

**✅ Acceptable**: "The application shows average response time of 3.2 seconds for search operations (Source: New Relic APM, 7-day average Dec 1-7 2024, Screenshot: monitoring/search-perf.png), which exceeds the industry standard of <1 second (Source: Google PageSpeed Best Practices, https://web.dev/performance-scoring/)."

### Citation Standards

#### 1. Code-Based Observations

**Format**: `[Description] (Source: [File Path]:[Line Numbers])`

**Example**:
> The authentication mechanism uses Forms Authentication with custom membership provider 
> (Source: `Web.config:45-52`, `Custom/MembershipProvider.cs:23-156`)

#### 2. Tool-Generated Metrics

**Format**: `[Metric]: [Value] (Tool: [Tool Name] (Version number), Date: [Analysis Date])`

**Example**:
> Cyclomatic Complexity: Average 15.3, Max 47 
> (Tool: SonarQube 9.9, Date: 2024-12-10)

#### 3. Configuration-Based Findings

**Format**: `[Finding] (Config: [File Path], Setting: [Setting Name])`

**Example**:
> Database connection pooling is disabled 
> (Config: `appsettings.json:23`, Setting: `"Pooling": false`)

#### 4. Stakeholder Input

**Format**: `[Statement] (Source: Interview with [Name/Role], Date: [Date])`

**Example**:
> Critical business process runs during nightly batch window 2AM-4AM EST
> (Source: Interview with Operations Manager John Smith, Date: 2024-12-05)

#### 5. Industry Standards & Best Practices

**Format**: `[Recommendation] (Standard: [Standard Name/Link])`

**Example**:
> Implement token-based authentication for stateless API design
> (Standard: OAuth 2.0 RFC 6749, https://tools.ietf.org/html/rfc6749)

#### 6. Vendor Documentation

**Format**: `[Information] (Vendor Docs: [Product] (Version number), Section: [Section Name])`

**Example**:
> .NET Framework 4.5 reaches end of support April 26, 2022
> (Vendor Docs: Microsoft .NET Framework Support Policy, https://dotnet.microsoft.com/platform/support/policy)

### Evidence Examples by Document Type

#### Architecture Documents
```markdown
### Database Schema

The application uses a normalized relational schema with 47 tables.
(Source: Database script `database/schema.sql`, Lines: 1-1247)

Key tables include:
- `Users` (387 columns) - User accounts and profiles (Lines: 23-45)
- `Orders` (15 columns) - Order transaction data (Lines: 156-178)
- `OrderItems` (8 columns) - Order line items (Lines: 189-201)

**Performance Observation**: 
Orders table has 2.3M records with average query time of 1.8s for filtered searches.
(Tool: SQL Server Management Studio - Execution Plan Analysis, Date: 2024-12-08)
(Query: `queries/order_search.sql:12-34`)
```

#### Technical Debt Documents
```markdown
### Code Duplication

Identified 23% code duplication across business logic layer.
(Tool: ReSharper 2024.2 Code Inspection, Date: 2024-12-09)

Major duplication hotspots:
1. Order validation logic duplicated in 5 files: 
   - `Services/OrderValidator.cs:45-123`
   - `Controllers/OrderController.cs:234-312`
   - `API/OrderAPI.cs:89-167`
   - `Batch/OrderProcessor.cs:156-234`
   - `Reports/OrderReportGenerator.cs:78-156`
   
   **Recommendation**: Extract to shared `OrderValidationService`
   **Effort**: 3-5 days
   **Risk**: Medium (breaking changes to 5 components)
```

#### Requirements Documents
```markdown
### Feature: Multi-Currency Support

**Business Need**: Expand to European markets requires EUR, GBP support
(Source: Interview with VP Product Sarah Johnson, Date: 2024-11-15)

**Current State**: Hard-coded USD throughout application
(Evidence: Search results for "USD": 347 occurrences across 89 files)
(Tool: VS Code global search, Date: 2024-12-10)

**Impact Analysis**:
- 23 database tables with currency columns need schema updates
- 156 code files contain currency formatting logic
- No i18n/l10n framework currently in place
(Analysis: `docs/currency-impact-analysis.xlsx`)
```

### Handling Unquantifiable Data

**NEVER include estimates or assertions that cannot be backed by evidence from codebase analysis:**

❌ **DO NOT Include**:
- Cost estimates without detailed analysis
- Time estimates without historical data
- Resource requirements without capacity planning
- Performance projections without benchmarks
- ROI calculations without measurable baselines

✅ **DO Include Instead**:
- "Unable to estimate without [specific missing data]"
- "Requires further analysis of [specific area]"
- "Baseline metrics needed: [list specific measurements]"
- "Assumptions would be: [explicitly state what you'd assume]"

**Example**:

❌ "Migration will take 6-9 months and cost $500K-750K"

✅ "Migration timeline cannot be accurately estimated without:
- Code complexity analysis (recommend: SonarQube scan)
- Test coverage measurement (recommend: Coverage.py/dotCover)
- Team velocity historical data
- Detailed dependency audit

Based on similar projects documented in literature, typical range is X-Y months, but these projects had [specific characteristics]. Our project differs in [specific ways], requiring actual measurement before estimation."

### Template-Specific Citation Sections

Each template includes dedicated sections for citations:

1. **Evidence Sources** - Listing all primary sources with access information
2. **Tool Outputs** - Analysis tool results with versions, dates, and settings used
3. **Reference Materials** - Standards, documentation, articles with full URLs
4. **Assumptions & Limitations** - Explicitly stated assumptions with justification and known limitations
5. **Version Information** - Exact version/commit of code analyzed, analysis date
6. **Data Gaps** - Explicitly list information that could not be obtained or quantified

---

## 🛠️ Tools & Automation

### Recommended Analysis Tools

**Architecture Analysis**:
- [Structurizr](https://structurizr.com/) - C4 model diagram generation
- [NDepend](https://www.ndepend.com/) / [ArchUnit](https://www.archunit.org/) - Architecture validation
- [Dependency Cruiser](https://github.com/sverweij/dependency-cruiser) - Dependency analysis

**Code Quality**:
- [SonarQube](https://www.sonarqube.org/) - Code quality and security
- [ReSharper](https://www.jetbrains.com/resharper/) - .NET code analysis
- [ESLint](https://eslint.org/) / [Pylint](https://www.pylint.org/) - Language-specific linters

**Security**:
- [OWASP Dependency-Check](https://owasp.org/www-project-dependency-check/) - Vulnerability scanning
- [Snyk](https://snyk.io/) - Dependency vulnerability analysis
- [Retire.js](https://retirejs.github.io/retire.js/) - JavaScript library vulnerability detection

**Performance**:
- [Application Insights](https://docs.microsoft.com/en-us/azure/azure-monitor/app/app-insights-overview) - APM
- [dotMemory](https://www.jetbrains.com/dotmemory/) / [dotTrace](https://www.jetbrains.com/dottrace/) - .NET profiling
- [Lighthouse](https://developers.google.com/web/tools/lighthouse) - Web performance

**Documentation**:
- [Mermaid](https://mermaid-js.github.io/) - Diagram syntax
- [PlantUML](https://plantuml.com/) - UML diagrams
- [draw.io](https://www.diagrams.net/) - General diagramming

### Automation Scripts

Located in `.github/scripts/` folder:

- `analyze-dependencies.ps1` - Extract dependency information
- `generate-metrics.ps1` - Collect code metrics
- `create-architecture-diagram.ps1` - Generate C4 diagrams from code
- `update-documentation-dates.ps1` - Update last-modified dates

---

## 📐 Document Standards

### File Naming

- Use kebab-case: `system-architecture.template.md`
- Include `.template.md` suffix for templates
- Number templates hierarchically: `1.1`, `1.2`, `2.1`, etc.

### Document Structure

Every template includes:

1. **Front Matter** (YAML metadata)
   ```yaml
   ---
   title: [Document Title]
   description: [Brief description]
   version: 1.0
   last_updated: (To be set upon document creation/update)
   author: [Author/Team]
   status: Draft, Review, or Approved
   audience: [Target audience]
   ---
   ```

2. **Table of Contents**
3. **Main Content Sections**
4. **Evidence Sources Section**
5. **Document Metadata**
6. **Version History**

### Diagram Standards

- Use Mermaid for text-based diagrams (preferred)
- Include alt-text for accessibility
- Provide textual description alongside diagrams
- Use consistent color coding:
  - Blue (#e1f5ff): Presentation/UI layer
  - Orange (#fff4e1): Business logic
  - Red (#ffe1e1): Data access
  - Green (#e1ffe1): Infrastructure

### Version Control

- Track document versions in Version History table
- Use semantic versioning: `Major.Minor.Patch`
- Major: Significant restructuring
- Minor: New sections or substantial updates
- Patch: Corrections, clarifications, minor edits

---

## 🔄 Maintenance & Updates

### Review Schedule

**Document Type** | **Review Frequency** | **Trigger Events**
---|---|---
Strategic | Quarterly | Major business changes, technology shifts
Architecture | Monthly | Architecture changes, new integrations
Business | Quarterly | Requirement changes, new compliance needs
Implementation | As needed | Process changes, tool updates
Operations | Monthly | Infrastructure changes, incidents
Quality | Regular cadence | After quality audits, major releases

**Note**: All review cycles should be adjusted based on project phase. Active modernization requires more frequent updates.

### Update Process

1. **Identify Need**: Change in system, new findings, feedback
2. **Create Branch**: Follow git workflow
3. **Update Documentation**: Make necessary changes
4. **Validate**: Review for accuracy, completeness
5. **Peer Review**: Get SME approval
6. **Merge & Publish**: Update version, merge to main
7. **Communicate**: Notify stakeholders of significant changes

### Quality Checklist

Before marking documentation as "Approved":

- [ ] All sections completed (no TODOs or placeholders)
- [ ] Citations provided for all assertions
- [ ] Diagrams current and accurate
- [ ] Code examples tested and working
- [ ] Cross-references valid
- [ ] Spelling and grammar checked
- [ ] Peer reviewed by subject matter expert
- [ ] Version number updated
- [ ] Last updated date current

---

## 📞 Support & Contribution

### Questions?

- **Documentation Issues**: Create an issue in the repository
- **Template Suggestions**: Submit a pull request
- **Usage Questions**: Contact the architecture team

### Contributing

We welcome improvements to these templates!

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add examples demonstrating the improvement
5. Submit a pull request

**See**: `CONTRIBUTING.md` for detailed guidelines

---

## 📚 References & Resources

### Industry Standards

- **Arc42**: https://arc42.org/
- **C4 Model**: https://c4model.com/
- **IEEE 42010**: https://www.iso.org/standard/50508.html
- **ISO/IEC 25010**: Software Quality Model

### Modernization Resources

**Cloud Migration**:
- **Microsoft Cloud Adoption Framework**: https://docs.microsoft.com/en-us/azure/cloud-adoption-framework/
- **AWS Application Modernization**: https://aws.amazon.com/modern-apps/
- **Google Cloud Architecture Framework**: https://cloud.google.com/architecture/framework
- **The Twelve-Factor App**: https://12factor.net/

**Legacy Modernization**:
- **Martin Fowler's Refactoring**: https://refactoring.com/
- **Working Effectively with Legacy Code** (Michael Feathers): https://www.oreilly.com/library/view/working-effectively-with/0131177052/
- **Modernizing Legacy Applications** (NGINX): https://www.nginx.com/resources/library/modernizing-legacy-applications/
- **Strangler Fig Application**: https://martinfowler.com/bliki/StranglerFigApplication.html

**Architecture & Patterns**:
- **Building Evolutionary Architectures**: https://www.oreilly.com/library/view/building-evolutionary-architectures/9781491986356/
- **Microservices Patterns** (Chris Richardson): https://microservices.io/patterns/
- **Domain-Driven Design**: https://www.domainlanguage.com/ddd/
- **Event-Driven Architecture**: https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/

### Documentation Best Practices

- **Diátaxis Framework**: https://diataxis.fr/
- **Write the Docs**: https://www.writethedocs.org/
- **Google Developer Documentation Style Guide**: https://developers.google.com/style

---

## 📄 License

These templates are provided as-is for use in documenting legacy application modernization projects.

---

## Document Metadata

- **Version**: 3.0.0
- **Last Updated**: 2024-12-10
- **Maintained By**: Architecture & Modernization Team
- **Review Schedule**: Quarterly
- **Next Review**: 2025-03-10

---

**Note**: This README is a living document. Please keep it updated as templates are added, modified, or deprecated.
