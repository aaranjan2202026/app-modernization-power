---
name: SonarQube Quality Check
---

# SonarQube Quality Check Skill

Activate this skill to run a SonarQube quality gate check against the current project.

## What it does
- Connects to the SonarQube MCP server
- Verifies the project key exists
- Reports the last analysis date and branch (staleness check)
- Shows quality gate status (pass/fail/indeterminate)
- Lists all open issues by severity
- Reports security hotspots

## How to use
Say: "run sonarqube check" or "check quality gate"

## Configuration
The SonarQube MCP server must be configured in `.kiro/settings/mcp.json` with a valid token.
