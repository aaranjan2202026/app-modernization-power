# Autopilot Mode — Full Autonomous Execution

## Overview

When autopilot is active, the migration agent executes all 7 phases without any user interaction. This document defines the rules for fully autonomous operation.

## Autopilot Rules

### NEVER Do These During Migration
- ❌ Ask "Would you like me to continue?"
- ❌ Ask "Which approach should I use?"
- ❌ Offer manual alternatives
- ❌ Stop between phases
- ❌ Create progress summaries mid-execution (only at 100%)
- ❌ Wait for user confirmation
- ❌ Say "Ready to proceed when you are"
- ❌ Create branches, commit, or push (unless explicitly requested)

### ALWAYS Do These
- ✅ Continue automatically through all 7 phases
- ✅ Fix build errors immediately and retry
- ✅ Fix test failures caused by migration
- ✅ Document pre-existing issues and continue
- ✅ Generate reports at completion only
- ✅ Make autonomous decisions based on steering rules
- ✅ Verify build passes after each phase before proceeding

## Decision Framework

```
Q: Build failed after phase?
   → Fix errors immediately, rebuild, continue

Q: Tests fail?
   → If caused by migration: fix and continue
   → If pre-existing: document and continue

Q: SonarQube unreachable?
   → Document as "pending", continue with local validation

Q: Unsure which pattern to use?
   → Follow the migration-rules steering file, pick the standard pattern

Q: File doesn't exist for deletion?
   → Skip, document, continue

Q: Dependency conflict?
   → Update to latest compatible version, rebuild
```

## Phase Transition Rules

```
Phase 1 (Assessment) COMPLETE
  → Immediately start Phase 2 (no pause)

Phase 2 (Build Config) COMPLETE + build passes
  → Immediately start Phase 3 (no pause)

Phase 3 (Legacy Removal) COMPLETE + build passes
  → Immediately start Phase 4 (no pause)

Phase 4 (Modernization) COMPLETE + build passes
  → Immediately start Phase 5 (no pause)

Phase 5 (Configuration) COMPLETE + app starts
  → Immediately start Phase 6 (no pause)

Phase 6 (Testing) COMPLETE + all tests pass
  → Immediately start Phase 7 (no pause)

Phase 7 (Quality Gate) COMPLETE
  → Generate final summary report
  → STOP (migration complete)
```

## Error Recovery (Autonomous)

| Situation | Action |
|-----------|--------|
| Compilation error | Read error, fix source, recompile |
| Missing dependency | Add to pom.xml/csproj, rebuild |
| Test failure (new) | Fix the code that caused failure |
| Test failure (pre-existing) | Document, do not count against migration |
| File not found | Skip operation, log warning, continue |
| MCP timeout | Retry once, then skip SonarQube phase |
| Circular dependency | Refactor injection, use interface |

## Completion Signal

The migration is ONLY complete when:
1. All 7 phases executed
2. Build passes with 0 errors
3. All tests pass (0 new failures)
4. Final report generated at `Migration/[Java21|DotNet8]-Migration-Summary.md`

At that point, output: "✅ Migration complete. See Migration/[summary file] for details."
