---
applyTo: '**/.solutiondocs/CURRENT-STATE/1-architecture/**/*.md, **/.solutiondocs/CURRENT-STATE/3-implementation/**/*.md, **/.solutiondocs/CURRENT-STATE/4-operations/**/*.md, **/.solutiondocs/CURRENT-STATE/5-quality/**/*.md, **/.solutiondocs/MODERNIZATION/1-assessment/**/*.md, **/.solutiondocs/MODERNIZATION/3-execution/**/*.md, **/.solutiondocs/MODERNIZATION/4-improvement/**/*.md'
---

# Architecture Documentation Guidelines

This file contains instructions for generating architecture-focused documentation that captures system design, technical structure, dependencies, and operational aspects.

Documents must be created in their respective folders following the template structure:
- Current State Architecture: `.solutiondocs/CURRENT-STATE/1-architecture/`
- Current State Implementation: `.solutiondocs/CURRENT-STATE/3-implementation/`
- Current State Operations: `.solutiondocs/CURRENT-STATE/4-operationīs/`
- Current State Quality: `.solutiondocs/CURRENT-STATE/5-quality/`
- Modernization Assessment: `.solutiondocs/MODERNIZATION/1-assessment/`
- Modernization Strategy: `.solutiondīocs/MODERNIZATION/2-strategy/`
- Modernization Execution: `.solutiondocs/MODERNIZATION/3-execution/`
- Modernization Improvement: `.solutiondocs/MODERNIZATION/4-improvement/`

Ensure:
- All sections from the template are included in the same order.
- Replace placeholder text with relevant content.
- Maintain Markdown formatting exactly as in the template.
- Do not add extra sections unless specified.

---

## CRITICAL: Prohibited Content Without Evidence

**The following content types are STRICTLY PROHIBITED in architecture documentation unless supported by concrete data and detailed calculations:**

### Absolutely Prohibited Without Evidence:

1. **Timeline Estimates**:
   - ❌ Phase durations (weeks, months, quarters)
   - ❌ Migration timelines
   - ❌ Implementation schedules
   - ❌ Deployment timelines
   - ✅ ONLY IF: Based on actual task breakdown × team velocity data with documented calculation

2. **Effort Calculations**:
   - ❌ Development effort (person-months, person-days, FTE)
   - ❌ Team size estimates
   - ❌ Resource requirements
   - ❌ Story points or velocity estimates
   - ✅ ONLY IF: Derived from itemized task list × historical velocity × team composition with full calculation shown

3. **Cost Estimates**:
   - ❌ Infrastructure costs (monthly/annual)
   - ❌ Cloud service costs
   - ❌ Reserved Instance (RI) calculations
   - ❌ Total Cost of Ownership (TCO)
   - ❌ Development/implementation costs
   - ❌ Licensing costs
   - ❌ ROI (Return on Investment) calculations
   - ✅ ONLY IF: Based on actual pricing data (with date) × measured/calculated usage with itemized calculation methodology

4. **Risk Quantification**:
   - ❌ Probability percentages ("30% chance")
   - ❌ Risk cost estimates
   - ❌ Impact dollar amounts
   - ✅ ONLY IF: Based on historical incident data or industry studies (with citation)
   - ✅ ACCEPTABLE: Qualitative risk levels (HIGH/MEDIUM/LOW) with clear rationale

5. **Performance Metrics Without Measurement**:
   - ❌ "Will improve performance by X%"
   - ❌ "Reduces latency by Y ms"
   - ❌ "Increases throughput by Z requests/sec"
   - ✅ ONLY IF: Based on actual benchmarking or load testing results with data

### Required Approach When Data Unavailable:

**MUST explain WHY estimation cannot be provided**:
```markdown
## Timeline Estimation

**Cannot be estimated at this stage because**:
- WHY: No detailed task breakdown exists for infrastructure migration
- WHAT: Requires sprint planning with story point estimation for 47 identified migration tasks
- WHERE: Task breakdown should come from infrastructure team assessment with complexity ratings
- NEXT STEPS: Conduct 2-day estimation workshop with infrastructure team to create detailed task list
```

**MUST state data requirements explicitly**:
```markdown
## Infrastructure Cost Analysis

**Cost estimation not possible because**:
- Missing production usage metrics (CPU, memory, network, storage patterns)
- No historical request volume data
- Database size and growth rate unknown

**Required data for estimation**:
1. 30-day production metrics from monitoring system
2. Database size query: `SELECT SUM(size)/1024/1024 as SizeInMB FROM sys.database_files`
3. Request logs for traffic pattern analysis
4. Current infrastructure costs (for TCO comparison)

**How to gather**:
- Production metrics: Export from [monitoring tool] for period [dates]
- Database metrics: Run query against production DB during maintenance window
- Request logs: Extract from [log aggregation system]
```

### Validation Checklist:

Before including ANY of the above prohibited content, verify:
- [ ] Do I have concrete source data? (not assumptions)
- [ ] Is my calculation methodology documented?
- [ ] Are all assumptions explicitly stated?
- [ ] Is the data source cited with date?
- [ ] Can another person reproduce this calculation?

**If answer to ANY question is NO → Do NOT include the estimate**

---

## Architecture Documentation Deliverables

### Current State Architecture Documentation

**Output Folder**: `.solutiondocs/CURRENT-STATE/1-architecture/`

#### Document 1.1: `1.1-system-architecture.md`

**Purpose**: Document the complete architectural design using C4 model standards.

**Function**: System context, container diagrams, component diagrams, deployment architecture, architectural patterns, cross-cutting concerns, and architectural decision records (ADRs).

**Template**: Use `.doctemplates/CURRENT-STATE/1-architecture/1.1-system-architecture.template.md` as the starting point for this document.

---

#### Document 1.2: `1.2-data-architecture.md`

**Purpose**: Document all data storage, flow, and management patterns.

**Function**: Database schemas, data models, data flow diagrams, data storage technologies, data access patterns, data governance, and data migration considerations.

**Template**: Use `.doctemplates/CURRENT-STATE/1-architecture/1.2-data-architecture.template.md` as the starting point for this document.

---

#### Document 1.3: `1.3-api-integration-catalog.md`

**Purpose**: Document all APIs and integration points.

**Function**: API inventory, endpoint documentation, authentication/authorization mechanisms, API contracts, rate limiting, versioning strategies, and API governance.

**Template**: Use `.doctemplates/CURRENT-STATE/1-architecture/1.3-api-integration-catalog.template.md` as the starting point for this document.

---

#### Document 1.4: `1.4-project-inventory.md`

**Purpose**: Catalog all projects and their relationships.

**Function**: Solution structure, project dependencies, build configurations, shared libraries, framework versions, and inter-project relationships.

**Template**: Use `.doctemplates/CURRENT-STATE/1-architecture/1.4-project-inventory.template.md` as the starting point for this document.

---

#### Document 1.5: `1.5-infrastructure-architecture.md`

**Purpose**: Document current hosting, deployment, and infrastructure setup.

**Function**: Hosting environments, network architecture, deployment infrastructure, scalability configuration, backup and recovery procedures, and infrastructure tooling.

**Template**: Use `.doctemplates/CURRENT-STATE/1-architecture/1.5-infrastructure-architecture.template.md` as the starting point for this document.

---

#### Document 1.6: `1.6-integration-architecture.md`

**Purpose**: Catalog all current external system integrations and interfaces.

**Function**: Integration inventory, integration patterns, protocols, authentication methods, data exchange formats, integration health, and dependency mapping.

**Template**: Use `.doctemplates/CURRENT-STATE/1-architecture/1.6-integration-architecture.template.md` as the starting point for this document.

---

### Current State Implementation Documentation

**Output Folder**: `.solutiondocs/CURRENT-STATE/3-implementation/`

#### Document 3.1: `3.1-developer-guide.md`

**Purpose**: Enable new developers to contribute effectively.

**Function**: Development environment setup, coding standards, build procedures, debugging guides, contribution workflows, and onboarding resources.

**Template**: Use `.doctemplates/CURRENT-STATE/3-implementation/3.1-developer-guide.template.md` as the starting point for this document.

---

#### Document 3.2: `3.2-coding-standards.md`

**Purpose**: Document coding standards and conventions used in the codebase.

**Function**: Language-specific standards, naming conventions, code organization, design patterns, code review guidelines, and best practices.

**Template**: Use `.doctemplates/CURRENT-STATE/3-implementation/3.2-coding-standards.template.md` as the starting point for this document.

---

#### Document 3.3: `3.3-test-strategy.md`

**Purpose**: Document testing approach and quality assurance practices.

**Function**: Test pyramid, unit testing, integration testing, end-to-end testing, test coverage, testing tools, and quality gates.

**Template**: Use `.doctemplates/CURRENT-STATE/3-implementation/3.3-test-strategy.template.md` as the starting point for this document.

---

### Current State Operations Documentation

**Output Folder**: `.solutiondocs/CURRENT-STATE/4-operations/`

#### Document 4.1: `4.1-deployment-operations.md`

**Purpose**: Document deployment and operational procedures.

**Function**: Deployment pipelines, CI/CD configuration, environment management, release processes, rollback procedures, and operational runbooks.

**Template**: Use `.doctemplates/CURRENT-STATE/4-operations/4.1-deployment-operations.template.md` as the starting point for this document.

---

#### Document 4.2: `4.2-security-architecture.md`

**Purpose**: Document security implementation and posture.

**Function**: Authentication/authorization, data protection, security controls, vulnerability management, security testing, compliance frameworks, and threat model.

**Template**: Use `.doctemplates/CURRENT-STATE/4-operations/4.2-security-architecture.template.md` as the starting point for this document.

---

#### Document 4.3: `4.3-monitoring-observability.md`

**Purpose**: Document monitoring, logging, and observability practices.

**Function**: Monitoring tools, metrics collection, log aggregation, alerting rules, dashboards, tracing, and observability strategy.

**Template**: Use `.doctemplates/CURRENT-STATE/4-operations/4.3-monitoring-observability.template.md` as the starting point for this document.

---

#### Document 4.4: `4.4-disaster-recovery.md`

**Purpose**: Document disaster recovery and business continuity plans.

**Function**: Recovery time objectives (RTO), recovery point objectives (RPO), backup strategies, failover procedures, disaster recovery testing, and business continuity planning.

**Template**: Use `.doctemplates/CURRENT-STATE/4-operations/4.4-disaster-recovery.template.md` as the starting point for this document.

---

#### Document 4.5: `4.5-scalability-capacity.md`

**Purpose**: Document scalability patterns and capacity planning.

**Function**: Scalability patterns, load balancing, caching strategies, resource utilization, capacity planning, performance baselines, and growth projections.

**Template**: Use `.doctemplates/CURRENT-STATE/4-operations/4.5-scalability-capacity.template.md` as the starting point for this document.

---

#### Document 4.6: `4.6-incident-management.md`

**Purpose**: Document incident response and management procedures.

**Function**: Incident classification, escalation procedures, incident response playbooks, post-mortem processes, SLA/SLO definitions, and on-call procedures.

**Template**: Use `.doctemplates/CURRENT-STATE/4-operations/4.6-incident-management.template.md` as the starting point for this document.

---

### Current State Quality Documentation

**Output Folder**: `.solutiondocs/CURRENT-STATE/5-quality/`

#### Document 5.1: `5.1-technical-debt-assessment.md`

**Purpose**: Identify technical debt and quality concerns.

**Function**: Technical debt inventory, code quality metrics, architectural violations, deprecated dependencies, anti-patterns, refactoring opportunities, and remediation roadmap.

**Template**: Use `.doctemplates/CURRENT-STATE/5-quality/5.1-technical-debt-assessment.template.md` as the starting point for this document.

---

#### Document 5.2: `5.2-performance-assessment.md`

**Purpose**: Document performance characteristics and optimization opportunities.

**Function**: Performance baselines, bottleneck analysis, load testing results, optimization recommendations, performance metrics, and SLA compliance.

**Template**: Use `.doctemplates/CURRENT-STATE/5-quality/5.2-performance-assessment.template.md` as the starting point for this document.

---

#### Document 5.3: `5.3-risk-assessment.md`

**Purpose**: Identify and assess technical and operational risks.

**Function**: Risk inventory, risk categorization, impact analysis, likelihood assessment, mitigation strategies, and risk monitoring.

**Template**: Use `.doctemplates/CURRENT-STATE/5-quality/5.3-risk-assessment.template.md` as the starting point for this document.

---

#### Document 5.4: `5.4-dependency-management.md`

**Purpose**: Document dependency management practices and third-party components.

**Function**: Dependency inventory, version management, vulnerability scanning, license compliance, upgrade strategies, and dependency health.

**Template**: Use `.doctemplates/CURRENT-STATE/5-quality/5.4-dependency-management.template.md` as the starting point for this document.

---

#### Document 5.5: `5.5-accessibility-compliance.md`

**Purpose**: Document accessibility features and compliance status.

**Function**: WCAG compliance level, accessibility testing results, assistive technology support, remediation plan, and accessibility standards.

**Template**: Use `.doctemplates/CURRENT-STATE/5-quality/5.5-accessibility-compliance.template.md` as the starting point for this document.

---

### Modernization Assessment Documentation

**Output Folder**: `.solutiondocs/MODERNIZATION/1-assessment/`

#### Document 1.1: `1.1-legacy-assessment.md`

**Purpose**: Analyze gaps between current state and modern practices for modernization planning.

**Function**: Technology obsolescence analysis, architecture modernization gaps, development practice gaps, operational maturity gaps, security/compliance gaps, integration/API gaps, data management gaps, and modernization readiness score.

**Template**: Use `.doctemplates/MODERNIZATION/1-assessment/1.1-legacy-assessment.template.md` as the starting point for this document.

---

#### Document 1.2: `1.2-cloud-readiness-assessment.md`

**Purpose**: Assess application readiness for cloud migration.

**Function**: Cloud compatibility analysis, migration blockers, cloud-native capabilities assessment, and cloud migration strategy recommendations.

**Cost Analysis Note**: Only include cost analysis if actual usage metrics (transactions, storage, compute) are available with documented calculation methodology. Otherwise, state requirements for cost estimation.

**Template**: Use `.doctemplates/MODERNIZATION/1-assessment/1.2-cloud-readiness-assessment.template.md` as the starting point for this document.

---

#### Document 1.3: `1.3-technology-evolution-roadmap.md`

**Purpose**: Define technology evolution and upgrade path.

**Function**: Current technology stack analysis, target technology stack, upgrade paths, migration strategies, compatibility assessment, and logical sequencing.

**Timeline Planning Note**: Provide logical dependencies and sequencing. Only include specific timelines if based on evidence (team capacity, task estimates, complexity analysis). Otherwise, note that timeline planning requires project-specific estimation.

**Template**: Use `.doctemplates/MODERNIZATION/1-assessment/1.3-technology-evolution-roadmap.template.md` as the starting point for this document.

---

### Modernization Strategy Documentation

**Output Folder**: `.solutiondocs/MODERNIZATION/2-strategy/`

#### Document 2.3: `2.3-modernization-strategy.md`

**Purpose**: Define comprehensive modernization strategy and approach with detailed technical analysis.

**Function**: Technology stack assessment with EOL dates, current architecture overview, technical debt analysis, modernization options evaluation, migration patterns, architectural evolution, technology choices, risk mitigation strategies, phased implementation plans, and success metrics.

**Template**: Use `.doctemplates/MODERNIZATION/2-strategy/2.3-modernization-strategy.template.md` as the starting point for this document.

---

#### Document 2.4: `2.4-azure-service-recommendations.md`

**Purpose**: Provide Azure-specific service recommendations based on analyzed codebase and architecture patterns.

**Function**: Map discovered technologies, patterns, and requirements to recommended Azure services including compute (App Service, Container Apps, AKS, Functions), data storage (SQL Database, Cosmos DB, Storage Accounts), messaging (Service Bus, Event Grid), caching (Redis Cache), security (Key Vault, Entra ID), monitoring (Application Insights, Log Analytics), integration (API Management, Logic Apps), and infrastructure services. Include service tier recommendations, configuration guidance, migration patterns, and architectural best practices for Azure.

**Cost and Timeline Requirements**:
- **Cost Estimates**: Only include if immediately derivable from actual usage data (e.g., database size, request volumes) with full calculation methodology
- **If Cost Data Unavailable**: Explain WHY estimation cannot be done (e.g., "production metrics not accessible from static analysis") and WHAT data is needed (e.g., "requires 30-day CloudWatch logs")
- **Migration Timeline**: Provide logical phase sequencing and dependencies; only include duration estimates if based on evidence (team capacity, task breakdown, complexity analysis)
- **If Timeline Data Unavailable**: Explain WHY (e.g., "team composition not finalized") and WHAT is needed (e.g., "requires team roster with skill assessments")
- **Follow Template Warnings**: Template includes explicit guidance on evidence-based estimates - adhere strictly to these requirements

**Template**: Use `.doctemplates/MODERNIZATION/2-strategy/2.4-azure-service-recommendations.template.md` as the starting point for this document.

---

#### Document 2.5: `2.5-aws-service-recommendations.md`

**Purpose**: Provide AWS-specific service recommendations based on analyzed codebase and architecture patterns.

**Function**: Map discovered technologies, patterns, and requirements to recommended AWS services including compute (EC2, ECS, EKS, Lambda, Elastic Beanstalk), data storage (RDS, DynamoDB, S3, EFS), messaging (SQS, SNS, EventBridge), caching (ElastiCache), security (Secrets Manager, IAM, Cognito), monitoring (CloudWatch, X-Ray), integration (API Gateway, Step Functions), and infrastructure services. Include service tier recommendations, configuration guidance, migration patterns, and architectural best practices for AWS.

**Cost and Timeline Requirements**:
- **Cost Estimates**: Only include if immediately derivable from actual usage data with full calculation methodology
- **Migration Timeline**: Provide logical phase sequencing and dependencies; only include duration estimates if based on evidence
- **Follow Template Warnings**: Template includes explicit guidance on evidence-based estimates - adhere strictly to these requirements

**Template**: Use `.doctemplates/MODERNIZATION/2-strategy/2.5-aws-service-recommendations.template.md` as the starting point for this document.

---

#### Document 2.6: `2.6-gcp-service-recommendations.md`

**Purpose**: Provide GCP-specific service recommendations based on analyzed codebase and architecture patterns.

**Function**: Map discovered technologies, patterns, and requirements to recommended GCP services including compute (Compute Engine, Cloud Run, GKE, Cloud Functions, App Engine), data storage (Cloud SQL, Firestore, Cloud Storage, Filestore), messaging (Pub/Sub, Eventarc), caching (Memorystore), security (Secret Manager, IAM, Identity Platform), monitoring (Cloud Monitoring, Cloud Trace), integration (API Gateway, Cloud Workflows), and infrastructure services. Include service tier recommendations, configuration guidance, migration patterns, and architectural best practices for GCP.

**Cost and Timeline Requirements**:
- **Cost Estimates**: Only include if immediately derivable from actual usage data with full calculation methodology
- **Migration Timeline**: Provide logical phase sequencing and dependencies; only include duration estimates if based on evidence
- **Follow Template Warnings**: Template includes explicit guidance on evidence-based estimates - adhere strictly to these requirements

**Template**: Use `.doctemplates/MODERNIZATION/2-strategy/2.6-gcp-service-recommendations.template.md` as the starting point for this document.

---

### Modernization Execution Documentation

**Output Folder**: `.solutiondocs/MODERNIZATION/3-execution/`

#### Document 3.1: `3.1-migration-plan.md`

**Purpose**: Define detailed migration execution plan.

**Function**: Migration phases, task breakdown, resource allocation, timeline, dependencies, rollback procedures, validation criteria, and go-live checklist.

**Template**: Use `.doctemplates/MODERNIZATION/3-execution/3.1-migration-plan.template.md` as the starting point for this document.

---

#### Document 3.2: `3.2-refactoring-patterns.md`

**Purpose**: Document refactoring patterns and code transformation strategies.

**Function**: Refactoring catalog, code transformation patterns, architectural refactoring, design pattern implementation, code modernization techniques, and refactoring guidelines.

**Template**: Use `.doctemplates/MODERNIZATION/3-execution/3.2-refactoring-patterns.template.md` as the starting point for this document.

---

#### Document 3.3: `3.3-knowledge-transfer.md`

**Purpose**: Document knowledge transfer plan for modernized system.

**Function**: Training materials, documentation updates, knowledge transfer sessions, skill gap analysis, onboarding guides, and support transition.

**Template**: Use `.doctemplates/MODERNIZATION/3-execution/3.3-knowledge-transfer.template.md` as the starting point for this document.

---

### Modernization Improvement Documentation

**Output Folder**: `.solutiondocs/MODERNIZATION/4-improvement/`

**Purpose**: Document post-modernization continuous improvement strategy, monitoring, optimization, and ongoing technical excellence.

**Content Focus**: Performance monitoring, quality metrics dashboard, feedback mechanisms, iterative optimization cycles, technical debt prevention, knowledge management, team capability development, and innovation pipeline based on DevOps (DORA metrics) and SRE principles.

**Key Deliverables**:

#### Document 4.1: `4.1-continuous-improvement.md`

**Purpose**: Establish continuous improvement framework for post-modernization optimization.

**Function**: PDCA cycle implementation, performance monitoring strategy, quality metrics dashboard (DORA metrics), feedback collection mechanisms, iterative optimization cycles, technical debt prevention, knowledge management, team capability development, innovation pipeline, and success KPIs.

**Template**: Use `.doctemplates/MODERNIZATION/4-improvement/4.1-continuous-improvement.template.md` as the starting point for this document.

---