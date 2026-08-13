---
name: .NET Migration
---

# .NET Migration Skill

Activate this skill to run the .NET modernization workflow on the Pharmacy project.

## What it does
- Scans the .NET solution for legacy patterns and build issues
- Fixes broken solution references
- Consolidates Startup.cs into minimal hosting Program.cs
- Removes vulnerable unused dependencies
- Adds ModelState validation to controller actions
- Creates unit tests for specification logic
- Checks SonarQube quality gate

## How to use
Say: "migrate .net"

The workflow detects whether the project is already modern and switches to a remediation track if so.
