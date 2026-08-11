# Installation — Wiring Up Single-Prompt Autonomy

## Why installation is required

Kiro loads power steering files **on demand** — they are fetched when you ask for them, not injected into every message. That is good for context economy, but it means a power alone cannot guarantee that typing "migrate java to 21" starts a 7-phase run.

Two mechanisms actually deliver autonomous execution, and both live in the **workspace**:

| Mechanism | Location | Why it is needed |
|---|---|---|
| Auto-inclusion steering | `.kiro/steering/*.md` with `inclusion: auto` | Puts the trigger phrases and execution contract in context on every message, so the prompt is recognized without any setup |
| Hooks | `.kiro/hooks/*.json` | Enforce build gates, write guards, and git safety during the run |

So: the power carries the **content**; installation copies the parts that must be always-on into the workspace.

---

## Step 1 — Install the always-on steering

Copy these into `.kiro/steering/` and ensure each has `inclusion: auto` in its front matter:

| From power | To workspace | Front matter |
|---|---|---|
| `steering/orchestrator.md` | `.kiro/steering/migration-orchestrator.md` | `inclusion: auto` |
| `steering/java-migration-rules.md` | `.kiro/steering/java21-migration.md` | `inclusion: auto` |
| `steering/dotnet-migration-rules.md` | `.kiro/steering/dotnet-migration.md` | `inclusion: auto` |

Front matter format:

```markdown
---
inclusion: auto
---
```

Keep these on-demand (no copy needed): `getting-started.md`, `sonarqube-validation.md`, `hooks-reference.md`, `autopilot.md`, `installation.md`.

## Step 2 — Install the hooks

Copy all files from `hooks/` into `.kiro/hooks/`:

```
.kiro/hooks/
├── migration-write-guard.json      # preToolUse:write  — scope + namespace check
├── phase-gate-validation.json      # postTaskExecution — gate + state update
├── build-on-java-save.json         # fileEdited        — mvn compile
├── build-on-dotnet-save.json       # fileEdited        — dotnet build
├── sonarqube-post-task.json        # postTaskExecution — quality spot-check
└── git-safety-guard.json           # preToolUse:shell  — blocks branch/commit/push
```

### Hook conflict warning

Do **not** install more than one `preToolUse` + `write` hook. Every such hook triggers an agent round-trip on **every file write**. A migration touching 200 files with three write hooks costs 600 extra round-trips. `migration-write-guard.json` intentionally merges the scope check and namespace check into one hook for this reason.

Adjust the Maven `-f` path in `build-on-java-save.json` to match your module layout.

## Step 3 — Configure SonarQube (optional)

Two different URLs — do not conflate them:

```json
{
  "mcpServers": {
    "sonarqubemcp": {
      "url": "https://sonarqube-mcp-server.azurewebsites.net/mcp",
      "type": "http",
      "headers": { "SONARQUBE_TOKEN": "${SONARQUBE_TOKEN}" },
      "disabled": false
    }
  }
}
```

- `url` is the **MCP endpoint** (has an `/mcp` path)
- Your SonarQube **server** (e.g. `https://sonarqube-hub.azurewebsites.net`) is a different host, referenced only for context

Skipping this is fine. Phases 1–6 run fully without SonarQube; Phase 7 records itself as `"pending"`.

---

## Step 4 — Verify

Type: `migrate java from 7 to java 21`

Expected behaviour:
1. Creates `Migration/.migration-state.json` with `currentPhase: 1`
2. Writes `Migration/00-Assessment-Report.md`
3. Advances through Phases 2→7, one line of output per transition
4. Build gate runs after each phase
5. Ends with a summary report and `currentPhase: "complete"`

If it stops and asks whether to continue, the orchestrator steering is not auto-included — recheck the front matter in Step 1.

## Resuming after interruption

State lives in `Migration/.migration-state.json`. To resume, say `continue the migration` — the agent reads `currentPhase` and picks up there. To restart clean, delete that file.
