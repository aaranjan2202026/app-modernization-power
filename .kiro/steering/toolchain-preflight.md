---
inclusion: auto
---

# Toolchain Preflight

Run this before Phase 1 of any migration. Phase gates 2–7 all depend on a build succeeding, so a missing build tool must be resolved first.

## Resolved Toolchain (this workspace)

| Tool | Status | Invocation |
|---|---|---|
| JDK | OpenJDK 25.0.1 Temurin | `java` |
| Maven | 3.9.9, workspace-local | `.\.tools\apache-maven-3.9.9\bin\mvn.cmd` |
| .NET SDK | **not installed** | — |

Maven is not on `PATH`. It lives in `.tools/` and must be invoked by path.

## Build Commands — Java track

Because Maven is workspace-local and JDK 25 writes a native-access warning to stderr (which PowerShell misreads as failure), wrap Maven calls through `cmd /c` and suppress stderr:

```powershell
# compile
cmd /c ".\.tools\apache-maven-3.9.9\bin\mvn.cmd clean compile -f Hospital_Servlet1/pom.xml 2>NUL"

# test
cmd /c ".\.tools\apache-maven-3.9.9\bin\mvn.cmd test -f Hospital_Servlet1/pom.xml 2>NUL"

# full verify
cmd /c ".\.tools\apache-maven-3.9.9\bin\mvn.cmd clean verify -f Hospital_Servlet1/pom.xml 2>NUL"
```

**Judge gate pass/fail by the `BUILD SUCCESS` or `BUILD FAILURE` line in stdout — not by the shell exit code.** JDK 25 warnings make the exit code unreliable here.

## .NET track — blocked

`dotnet` is not installed. Do not start Phase 2 of the .NET migration. Phase 1 assessment is read-only and may still run.

To unblock:

```powershell
winget install Microsoft.DotNet.SDK.8
```

No-admin fallback:

```powershell
$s = Join-Path $env:TEMP 'dotnet-install.ps1'
Invoke-WebRequest -Uri 'https://dot.net/v1/dotnet-install.ps1' -OutFile $s -UseBasicParsing
& $s -Channel 8.0 -InstallDir (Join-Path $PWD '.tools\dotnet')
```

Then invoke as `.\.tools\dotnet\dotnet.exe`.

## Target version note

The Java project's `pom.xml` currently declares `<java.version>17</java.version>`, not 7. Imports are already `jakarta.*`. Treat this as a **17 → 21** migration: the javax→jakarta sweep is already complete, so the remaining work is Java 21 language features, removing leftover `servlet/` and `*Dao` classes, and adding the service layer.

JDK 25 compiling with `--release 21` is correct and enforces the Java 21 API boundary.

## Rule

Never begin a phase whose gate you cannot verify. If the build tool for a track is missing, either bootstrap it (see the power's `toolchain-bootstrap.md`) or run Phase 1 only and report the blocker.
