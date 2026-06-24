# Bootstrap 4 → 5 Upgrade Plan

## Overview
This document records the planned Bootstrap 4.3.1 → 5.x upgrade for the PharmacyNetwork Web application. This task was deferred from the main modernization effort (TASK-044) due to the inherent risk of UI regression across all Razor views.

---

## Current State
- **Version**: Bootstrap 4.3.1 (via `wwwroot/lib/bootstrap/`)
- **SonarQube**: ~106 accepted issues in Bootstrap source files (all third-party, cannot be modified)
- **Risk**: Visual regressions across all 20+ Razor views

---

## Key Breaking Changes (Bootstrap 4 → 5)

| Area | Bootstrap 4 | Bootstrap 5 Equivalent |
|------|------------|------------------------|
| jQuery dependency | Required | **Removed** — pure JavaScript |
| Grid system | `.col-*` unchanged | `.col-*` unchanged |
| Flexbox utilities | `d-flex`, `flex-*` | Unchanged |
| Spacing utilities | `mr-*`, `ml-*`, `pr-*`, `pl-*` | `me-*`, `ms-*`, `pe-*`, `ps-*` |
| Text utilities | `text-left`, `text-right` | `text-start`, `text-end` |
| Form controls | `.form-group` | Removed — use margin utilities |
| Badges | `<span class="badge badge-*">` | `<span class="badge bg-*">` |
| Jumbotron | `.jumbotron` | Removed — use bg utilities |
| Media object | `.media` | Removed — use Flexbox |
| Card | `.card-deck` | Removed — use grid row |
| Navbar | `.navbar-toggler-right` | `.ms-auto` |
| Dropdowns | `data-toggle` | `data-bs-toggle` |
| Modals | `data-dismiss` | `data-bs-dismiss` |
| Tooltips | `data-toggle="tooltip"` | `data-bs-toggle="tooltip"` |

---

## Migration Steps

### Step 1: Update libman.json
```json
{
  "libraries": [
    {
      "library": "twitter-bootstrap@5.3.3",
      "destination": "wwwroot/lib/bootstrap/"
    }
  ]
}
```

### Step 2: Remove jQuery dependency
- Bootstrap 5 is jQuery-free.
- Audit all scripts for direct `$()` usage in views.
- Migrate to vanilla JS or keep jQuery separately if other components require it.

### Step 3: Global find-and-replace patterns
Run across all `.cshtml` files:

| Find | Replace |
|------|---------|
| `data-toggle=` | `data-bs-toggle=` |
| `data-dismiss=` | `data-bs-dismiss=` |
| `data-target=` | `data-bs-target=` |
| `data-ride=` | `data-bs-ride=` |
| `mr-` (spacing) | `me-` |
| `ml-` (spacing) | `ms-` |
| `pl-` (padding) | `ps-` |
| `pr-` (padding) | `pe-` |
| `text-left` | `text-start` |
| `text-right` | `text-end` |
| `badge-primary` | `badge bg-primary` |
| `badge-secondary` | `badge bg-secondary` |

### Step 4: Visual regression testing
- Manually review all 20+ Razor views across browsers
- Test on mobile viewport sizes (Bootstrap 5 responsive breakpoints unchanged)
- Test all modals, dropdowns, tooltips

### Step 5: Remove SonarQube accepted issues
After upgrading, re-scan and review if Bootstrap 5 source files have fewer issues.

---

## Estimated Effort
- **Development**: 4-6 hours
- **Testing**: 3-4 hours (UI regression testing)
- **Total**: 1-2 developer days

---

## Dependencies
- Must be done after all other modernization tasks are complete
- Requires staging environment for visual validation
- Consider using automated screenshot diffing (e.g., BackstopJS) for regression safety

---

## Decision
**Status**: Deferred — follow-on task  
**Rationale**: UI regression risk warrants dedicated sprint; all application functionality is independent of Bootstrap version  
**Tracking**: Add to backlog as a separate user story
