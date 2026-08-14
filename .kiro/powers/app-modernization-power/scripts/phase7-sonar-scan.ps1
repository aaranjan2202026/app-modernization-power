<#
.SYNOPSIS
    Phase 7 mandatory SonarQube scan — prompts for URL and token, runs mvn sonar:sonar automatically.

.DESCRIPTION
    The sonar-maven-plugin (v4.0.0.4121) is pre-configured in Hospital_Servlet1/pom.xml.
    The pom.xml properties resolve from environment variables:
      - sonar.projectKey  → ${env.SONAR_PROJECT_KEY}
      - sonar.host.url    → ${env.SONAR_HOST_URL}
      - sonar.token       → ${env.SONAR_TOKEN}

    This script:
    1. Prompts for SonarQube server URL
    2. Prompts for SonarQube token (masked, never persisted)
    3. Prompts for project key (with default)
    4. Commits and pushes the current branch (scanner needs pushed code)
    5. Sets env vars and runs mvn sonar:sonar — plugin picks them up automatically
    6. Reports the result

    The token is NEVER stored, exported beyond this process, or echoed.

.PARAMETER ProjectKey
    SonarQube project key. Default: Refactoring-legacy-Hospital-uc2

.PARAMETER Module
    Maven module pom.xml path. Default: Hospital_Servlet1/pom.xml

.EXAMPLE
    .\phase7-sonar-scan.ps1
    # Prompts for URL, token, and project key interactively
#>
param(
    [string]$ProjectKey,
    [string]$Module = 'Hospital_Servlet1/pom.xml',
    [string]$MavenPath = '.\.tools\apache-maven-3.9.9\bin\mvn.cmd'
)

$ErrorActionPreference = 'Stop'

# --- Prompt for SonarQube URL ---
Write-Host ""
Write-Host "=== MANDATORY SonarQube Scan ===" -ForegroundColor Cyan
Write-Host "SonarQube scanning is REQUIRED. The sonar-maven-plugin is pre-configured in pom.xml." -ForegroundColor Yellow
Write-Host ""

$sonarUrl = Read-Host -Prompt 'SonarQube Server URL (e.g., https://sonarqube-hub.azurewebsites.net)'
if ([string]::IsNullOrWhiteSpace($sonarUrl)) { throw 'No URL entered. SonarQube scan is mandatory. Aborting.' }

# --- Prompt for token (masked) ---
Write-Host "Token input is masked and is NOT saved, logged, or exported." -ForegroundColor DarkGray
$secure = Read-Host -Prompt 'SonarQube Token' -AsSecureString
if ($secure.Length -eq 0) { throw 'No token entered. SonarQube scan is mandatory. Aborting.' }

$bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
try   { $token = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr) }
finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr) }

# --- Prompt for project key (with default) ---
if (-not $ProjectKey) {
    $ProjectKey = Read-Host -Prompt 'SonarQube Project Key [default: Refactoring-legacy-Hospital-uc2]'
    if ([string]::IsNullOrWhiteSpace($ProjectKey)) { $ProjectKey = 'Refactoring-legacy-Hospital-uc2' }
}

try {
    # --- Step 1: Commit and push ---
    Write-Host ""
    Write-Host "== Step 1: Commit and push migration code ==" -ForegroundColor Cyan
    $branch = (git rev-parse --abbrev-ref HEAD 2>$null)
    if (-not $branch) { throw 'Not in a git repository or no branch found.' }
    Write-Host "Current branch: $branch"

    git add -A
    $status = git status --porcelain
    if ($status) {
        git commit -m "migration: Phase 1-6 complete - Java 21 modernization"
        Write-Host "Committed changes." -ForegroundColor Green
    } else {
        Write-Host "Working tree clean, no commit needed." -ForegroundColor Green
    }

    git push -u origin $branch
    Write-Host "Pushed to origin/$branch" -ForegroundColor Green

    # --- Step 2: Run SonarQube scanner via env vars (pom.xml reads them automatically) ---
    Write-Host ""
    Write-Host "== Step 2: Running SonarQube scanner ==" -ForegroundColor Cyan
    Write-Host "Project key: $ProjectKey"
    Write-Host "Server: $sonarUrl"
    Write-Host "Module: $Module"
    Write-Host "Plugin: sonar-maven-plugin 4.0.0.4121 (pre-configured in pom.xml)"
    Write-Host ""

    # Set env vars that pom.xml properties resolve from
    $env:SONAR_PROJECT_KEY = $ProjectKey
    $env:SONAR_HOST_URL = $sonarUrl
    $env:SONAR_TOKEN = $token

    Write-Host "Executing: mvn sonar:sonar -f $Module (credentials via env vars)" -ForegroundColor Cyan
    $output = cmd /c "$MavenPath sonar:sonar -f $Module 2>&1"
    $output | Write-Host

    if ($output -match 'BUILD SUCCESS') {
        Write-Host ""
        Write-Host "== SonarQube scan COMPLETED SUCCESSFULLY ==" -ForegroundColor Green
        Write-Host "Check quality gate status using MCP tools or SonarQube dashboard at: $sonarUrl" -ForegroundColor Green
    } else {
        Write-Host ""
        Write-Host "== SonarQube scan FAILED ==" -ForegroundColor Red
        Write-Host "Review the output above for errors."
        exit 1
    }
}
finally {
    # Scrub secrets from memory and environment
    $token = $null
    $env:SONAR_TOKEN = $null
    $env:SONAR_HOST_URL = $null
    $env:SONAR_PROJECT_KEY = $null
    [GC]::Collect()
    Write-Host "Credentials scrubbed from environment." -ForegroundColor DarkGray
}
