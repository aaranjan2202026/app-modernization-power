---
name: Java 21 Migration
---

# Java 21 Migration Skill

Activate this skill to run the full Java 7→21 migration workflow on the Hospital_Servlet1 project.

## What it does
- Scans the Java project for legacy patterns
- Upgrades pom.xml to Java 21
- Removes legacy servlets, DAOs, and ConnectionHelper
- Applies Java 21 language features (records, text blocks, pattern matching, virtual threads)
- Externalizes configuration to YAML with profiles
- Runs build + tests to verify zero regressions
- Runs SonarQube scan (MANDATORY — prompts for URL + token)
- Generates audit report and pushes to GitHub

## How to use
Say: "migrate java from 7 to java 21"

The workflow runs all 8 phases autonomously without stopping.

## Phases
1. Assessment — legacy inventory, SonarQube baseline
2. Build Config — target Java 21
3. Legacy Removal — delete servlets, DAOs, add service layer
4. Language Features — records, text blocks, sealed classes, virtual threads
5. Configuration — YAML with profiles, env var secrets
6. Testing — verify 0 failures
7. SonarQube Quality Gate (MANDATORY — prompts for credentials)
8. Audit Report + GitHub Push
