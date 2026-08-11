# Hooks Reference — App Modernization Power

## Available Hooks

Copy these to `.kiro/hooks/` in your workspace to enable automation.

---

### 1. Migration Prompt Trigger
**File**: `migration-prompt-trigger.json`
**Trigger**: `promptSubmit`
**Purpose**: Detects migration intent in user messages and ensures the full workflow executes.

```json
{
  "name": "Migration Workflow Trigger",
  "version": "1.0.0",
  "description": "Detects migration keywords and ensures full autonomous workflow execution",
  "when": {
    "type": "promptSubmit"
  },
  "then": {
    "type": "askAgent",
    "prompt": "Check if the user's message contains migration keywords (java 21, migrate java, migrate .net, dotnet migration, upgrade java, modernize). If YES: Follow the full automated workflow from steering files. Execute ALL phases (1-7) continuously without stopping. NEVER ask 'Would you like me to continue?' If NO: proceed normally."
  }
}
```

---

### 2. Build on Save (Java)
**File**: `build-on-java-save.json`
**Trigger**: `fileEdited` (*.java, pom.xml)
**Purpose**: Catches compilation errors immediately during migration.

```json
{
  "name": "Compile Check on Java Edit",
  "version": "1.0.0",
  "description": "Runs Maven compilation when Java files are saved",
  "when": {
    "type": "fileEdited",
    "patterns": ["*.java", "pom.xml"]
  },
  "then": {
    "type": "runCommand",
    "command": "mvn compile -q -f Hospital_Servlet1/pom.xml"
  }
}
```

---

### 3. Build on Save (.NET)
**File**: `build-on-dotnet-save.json`
**Trigger**: `fileEdited` (*.cs, *.csproj)
**Purpose**: Catches compilation errors during .NET migration.

```json
{
  "name": "Compile Check on .NET Edit",
  "version": "1.0.0",
  "description": "Runs dotnet build when C# files are saved",
  "when": {
    "type": "fileEdited",
    "patterns": ["*.cs", "*.csproj"]
  },
  "then": {
    "type": "runCommand",
    "command": "dotnet build --nologo -q"
  }
}
```

---

### 4. Namespace Guard (Java — jakarta)
**File**: `jakarta-namespace-guard.json`
**Trigger**: `preToolUse` (write)
**Purpose**: Blocks introduction of legacy javax.* EE imports.

```json
{
  "name": "Jakarta Namespace Guard",
  "version": "1.0.0",
  "description": "Ensures no legacy javax.* EE imports in Java files targeting Java 17+",
  "when": {
    "type": "preToolUse",
    "toolTypes": ["write"]
  },
  "then": {
    "type": "askAgent",
    "prompt": "If writing a Java file, verify NO javax.servlet, javax.persistence, javax.validation, javax.annotation, javax.inject, javax.ws.rs imports are used. Must be jakarta.* equivalents. Exceptions: javax.sql, javax.crypto, javax.net (Java SE). If .NET file, verify NO System.Web, System.Configuration, or System.Data.Entity imports are used."
  }
}
```

---

### 5. Scope Enforcement
**File**: `scope-enforcement.json`
**Trigger**: `preToolUse` (write)
**Purpose**: Prevents out-of-scope changes during migration.

```json
{
  "name": "Migration Scope Enforcement",
  "version": "1.0.0",
  "description": "Blocks changes outside the authorized migration scope",
  "when": {
    "type": "preToolUse",
    "toolTypes": ["write"]
  },
  "then": {
    "type": "askAgent",
    "prompt": "Confirm this change is within migration scope. Authorized: (1) Language version upgrade, (2) Legacy code removal, (3) Service layer addition, (4) Modern language features, (5) Configuration externalization, (6) Deprecated API replacement. NOT authorized: new features, UI redesign, database schema changes, unrelated refactoring. Proceed if in scope, skip if not."
  }
}
```

---

### 6. SonarQube Post-Task Check
**File**: `sonarqube-post-task.json`
**Trigger**: `postTaskExecution`
**Purpose**: Validates quality after each migration task.

```json
{
  "name": "SonarQube Post-Task Check",
  "version": "1.0.0",
  "description": "Verifies SonarQube compliance after completing a migration task",
  "when": {
    "type": "postTaskExecution"
  },
  "then": {
    "type": "askAgent",
    "prompt": "A migration task just completed. Verify: (1) No hardcoded credentials, (2) Resources closed with try-with-resources, (3) No e.printStackTrace() / Console.WriteLine for errors, (4) No duplicated string literals >3 times, (5) Build still passes. If issues found, fix immediately."
  }
}
```

---

### 7. Git Safety Guard
**File**: `git-safety-guard.json`
**Trigger**: `preToolUse` (shell)
**Purpose**: Prevents accidental git operations during migration.

```json
{
  "name": "Git Safety Guard",
  "version": "1.0.0",
  "description": "Blocks git checkout/commit/push unless user explicitly requested",
  "when": {
    "type": "preToolUse",
    "toolTypes": ["shell"]
  },
  "then": {
    "type": "askAgent",
    "prompt": "WORKSPACE POLICY: If this shell command contains git checkout, git commit, git push, git branch, or any branch-switching/pushing operation, it is BLOCKED unless the user explicitly requested it. All changes must stay on the current local branch. Safe commands (build, test, compile) may proceed."
  }
}
```

---

## Installation

To install all hooks, copy the JSON files to your workspace:
```
.kiro/hooks/
├── migration-prompt-trigger.json
├── build-on-java-save.json
├── build-on-dotnet-save.json
├── jakarta-namespace-guard.json
├── scope-enforcement.json
├── sonarqube-post-task.json
└── git-safety-guard.json
```

## Hook Execution Flow

```
User Types "migrate java to 21"
  → [promptSubmit] migration-prompt-trigger → activates workflow

Agent Writes Java File
  → [preToolUse:write] jakarta-namespace-guard → validates imports
  → [preToolUse:write] scope-enforcement → validates scope
  → File is written
  → [fileEdited] build-on-java-save → runs mvn compile

Agent Runs Shell Command
  → [preToolUse:shell] git-safety-guard → blocks unsafe git ops

Spec Task Completes
  → [postTaskExecution] sonarqube-post-task → validates quality
```
