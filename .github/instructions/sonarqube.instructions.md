# SonarQube MCP Integration Instructions

**CRITICAL**: When the user mentions SonarQube, SonarCloud, code quality reports, quality gates, or asks about issues/metrics, ALWAYS use the SonarQube MCP tools first. This applies to ALL applications and ALL SonarQube/SonarCloud server instances.

## Automatic MCP Tool Usage

- **DO NOT** search for local files (`.sonarqube`, `.scannerwork`, `sonar-project.properties`) when user asks about SonarQube reports
- **ALWAYS** query the SonarQube server using MCP tools:
  - `mcp_sonarqubemcp_search_my_sonarqube_projects` - to discover and list all available projects
  - `mcp_sonarqubemcp_search_sonar_issues_in_projects` - to get issues for any project
  - `mcp_sonarqubemcp_get_project_quality_gate_status` - for quality gate status of any project
  - `mcp_sonarqubemcp_get_component_measures` - for metrics (coverage, complexity, bugs, vulnerabilities, etc.)
  - `mcp_sonarqubemcp_show_rule` - to get detailed information about specific rules
  - `mcp_sonarqubemcp_get_system_health` - to check SonarQube server health
  - `mcp_sonarqubemcp_list_quality_gates` - to list all quality gates
  - `mcp_sonarqubemcp_analyze_code_snippet` - to analyze code snippets

## Trigger Keywords

When user mentions any of these, use MCP tools immediately:
- "SonarQube report" / "SonarCloud report"
- "code quality" / "code analysis"
- "quality gate" / "quality profile"
- "sonar issues" / "sonar bugs"
- "technical debt"
- "code coverage from sonar"
- "security vulnerabilities in sonar"
- "code smells"
- "sonar metrics"
- "sonar analysis"
- "sonar findings"

## Dynamic Project Discovery

**ALWAYS** start by discovering available projects:
1. Use `mcp_sonarqubemcp_search_my_sonarqube_projects` to list all projects in the connected SonarQube instance
2. Present the available projects to the user if needed
3. Use the discovered project keys for subsequent queries
4. Handle multiple projects, branches, and pull requests dynamically

## Cross-Application Support

This instruction file applies to:
- Any programming language (Java, C#, Python, JavaScript, TypeScript, etc.)
- Any application type (web, desktop, mobile, microservices, etc.)
- Any SonarQube or SonarCloud instance
- Any project structure or size
- Multiple projects in the same workspace

## Best Practices

1. **Discovery First**: Always discover projects before querying specific data
2. **Dynamic Keys**: Never hardcode project keys; always use discovered keys
3. **Comprehensive Analysis**: When analyzing issues, include:
   - Issue severity and type
   - Affected files and line numbers
   - Rule descriptions
   - Remediation guidance
4. **Multiple Metrics**: Query multiple metrics together when possible:
   - `ncloc` (lines of code)
   - `bugs`, `vulnerabilities`, `code_smells`
   - `coverage`, `duplicated_lines_density`
   - `complexity`, `cognitive_complexity`
5. **Context Awareness**: Consider branches and pull requests when available
