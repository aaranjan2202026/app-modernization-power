---
name: .NET Migration
---

# .NET Migration Skill

Activate this skill to run the .NET modernization workflow.

## What it does
- Scans the .NET solution for legacy patterns and build issues
- Fixes broken solution references
- Consolidates Startup.cs into minimal hosting Program.cs
- Removes vulnerable unused dependencies
- Adds ModelState validation to controller actions
- Creates unit tests for specification logic
- Runs SonarQube scan (MANDATORY — prompts for URL + token)
- Generates audit report and pushes to GitHub

## How to use
Say: "migrate .net"

The workflow detects whether the project is already modern and switches to a remediation track if so.

## Phases
1. Assessment — legacy inventory, SonarQube baseline
2. Build Config — target net8.0
3. Legacy Removal — legacy namespace removal, architecture modernization
4. Language Features — records, primary constructors, collection expressions
5. Configuration — appsettings.json, Options pattern, env var secrets
6. Testing — verify 0 failures
7. SonarQube Quality Gate (MANDATORY — prompts for credentials)
8. Audit Report + GitHub Push
