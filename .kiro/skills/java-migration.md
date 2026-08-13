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
- Checks SonarQube quality gate

## How to use
Say: "migrate java from 7 to java 21"

The workflow runs all 7 phases autonomously without stopping.
