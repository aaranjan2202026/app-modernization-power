# Toolchain Bootstrap

## What a Power can and cannot do

A Kiro Power is markdown, JSON hooks, and MCP server configs. It has no install-script mechanism, so **a Power cannot install system software**. What it can do is carry the bootstrap procedure and have the agent run it.

That is what this file is: the preflight check plus a no-admin install path for each required tool.

---

## Preflight Check (run before Phase 1)

Every phase gate from 2 onward depends on a build succeeding. Verify the toolchain first — a migration that edits 40 files with no way to compile is worse than one that never starts.

```powershell
java -version          # need 21+ (25 is fine, targets 21 via --release)
mvn -v                 # Java track
dotnet --version       # .NET track
```

Decision:

| Result | Action |
|---|---|
| All present | Proceed to Phase 1 |
| Maven missing, Java track requested | Bootstrap Maven (below), then proceed |
| .NET SDK missing, .NET track requested | Bootstrap .NET SDK (below), then proceed |
| JDK missing | Stop — report; JDK cannot be reliably bootstrapped without admin |

Never start Phase 2 with a missing build tool. Phase 1 (assessment) is read-only and may run regardless.

---

## Bootstrap Maven — no admin required

Try a package manager first:

```powershell
winget install --id Apache.Maven --accept-package-agreements
choco  install maven -y
scoop  install maven
```

If none of those exist, install into a workspace-local `.tools/` directory:

```powershell
$ErrorActionPreference = 'Stop'
$version = '3.9.9'
$url = "https://archive.apache.org/dist/maven/maven-3/$version/binaries/apache-maven-$version-bin.zip"
$tools = Join-Path $PWD '.tools'
$zip = Join-Path $tools 'maven.zip'

New-Item -ItemType Directory -Force -Path $tools | Out-Null
$ProgressPreference = 'SilentlyContinue'
Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing
Expand-Archive -Path $zip -DestinationPath $tools -Force
Remove-Item $zip
```

Resulting binary: `.tools\apache-maven-3.9.9\bin\mvn.cmd`

Pin the version rather than tracking "latest" so a rerun produces the same toolchain.

### Using the local Maven

Define `MVN` once and use it for every build command in the run:

```
MVN = .\.tools\apache-maven-3.9.9\bin\mvn.cmd     (if bootstrapped locally)
MVN = mvn                                          (if on PATH)
```

Then:

```
BUILD  = <MVN> clean compile -f <module>/pom.xml
VERIFY = <MVN> clean verify  -f <module>/pom.xml
TEST   = <MVN> test          -f <module>/pom.xml
```

### PowerShell stderr caveat

On JDK 24+, Maven emits a native-access warning to stderr. PowerShell treats any stderr output as a command failure, so `mvn` appears to fail with exit code 1 even on success. Wrap invocations to read stdout only:

```powershell
cmd /c ".\.tools\apache-maven-3.9.9\bin\mvn.cmd clean compile -f Hospital_Servlet1/pom.xml 2>NUL"
```

Judge success by the `BUILD SUCCESS` / `BUILD FAILURE` line, not by the PowerShell exit code.

### Maven Wrapper alternative

If the project has `mvnw.cmd`, prefer it — it self-bootstraps the correct Maven version per project:

```powershell
.\mvnw.cmd clean compile
```

Once a real Maven is available, generate the wrapper so future runs need no bootstrap:

```powershell
<MVN> -f <module>/pom.xml wrapper:wrapper -Dmaven=3.9.9
```

---

## Bootstrap .NET SDK

```powershell
winget install Microsoft.DotNet.SDK.8
```

No-admin fallback using Microsoft's official install script:

```powershell
$ErrorActionPreference = 'Stop'
$script = Join-Path $env:TEMP 'dotnet-install.ps1'
Invoke-WebRequest -Uri 'https://dot.net/v1/dotnet-install.ps1' -OutFile $script -UseBasicParsing
& $script -Channel 8.0 -InstallDir (Join-Path $PWD '.tools\dotnet')
```

Resulting binary: `.tools\dotnet\dotnet.exe`

---

## Keep .tools out of version control

Add to `.gitignore`:

```
.tools/
```

These are several hundred MB of binaries and must never be committed.

---

## Toolchain vs target version

The JDK running Maven does not have to equal the migration target. JDK 25 compiling with `--release 21` produces correct Java 21 bytecode and rejects any API newer than 21, which is exactly the guarantee needed.

Install JDK 21 and point `JAVA_HOME` at it only if you need the runtime to match the target for testing purposes.
