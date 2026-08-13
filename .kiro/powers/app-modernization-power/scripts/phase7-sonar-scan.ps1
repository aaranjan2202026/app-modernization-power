<#
.SYNOPSIS
    Phase 7 mandatory SonarQube scan — prompts for URL and token, runs Maven sonar:sonar.

.DESCRIPTION
    Bundled with app-modernization-power. This script:
    1. Prompts for SonarQube server URL (masked input not needed for URL)
    2. Prompts for SonarQube token (masked, never persisted)
    3. Commits and pushes the current branch (scanner needs pushed code)
    4. Runs mvn sonar:sonar against the project
    5. Reports the result

    The token is NEVER stored, exported, or echoed.

.PARAMETER ProjectKey
    SonarQube project key, e.g. Refactoring-legacy-Hospital-uc2

.PARAMETER Module
    Maven module path, e.g. Hospital_Servlet1/pom.xml

.EXAMPLE
    .\phase7-sonar-scan.ps1 -ProjectKey Refactoring-legacy-Hospital-uc2 -Module Hospital_Servlet1/pom.xml
#>
param(
    [Parameter(Mandatory = $true)][string]$ProjectKey,
    [string]$Module = 'Hospital_Servlet1/pom.xml',
    [string]$MavenPath = '.\.tools\apache-maven-3.9.9\bin\mvn.cmd'
)

$ErrorActionPreference = 'Stop'

# --- Prompt for SonarQube URL ---
Write-Host ""
Write-Host "=== MANDATORY SonarQube Scan ===" -ForegroundColor Cyan
Write-Host "SonarQube scanning is REQUIRED. You must provide credentials." -ForegroundColor Yellow
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
        git commit -m "migration: Phase 1-6 complete - code modernization"
        Write-Host "Committed changes." -ForegroundColor Green
    } else {
        Write-Host "Working tree clean, no commit needed." -ForegroundColor Green
    }

    git push -u origin $branch
    Write-Host "Pushed to origin/$branch" -ForegroundColor Green

    # --- Step 2: Run SonarQube scanner ---
    Write-Host ""
    Write-Host "== Step 2: Running SonarQube scanner ==" -ForegroundColor Cyan
    Write-Host "Project key: $ProjectKey"
    Write-Host "Server: $sonarUrl"
    Write-Host "Module: $Module"
    Write-Host ""

    $scanCmd = "$MavenPath sonar:sonar " +
               "-Dsonar.projectKey=$ProjectKey " +
               "-Dsonar.host.url=$sonarUrl " +
               "-Dsonar.token=$token " +
               "-f $Module"

    Write-Host "Executing: mvn sonar:sonar -Dsonar.projectKey=$ProjectKey -Dsonar.host.url=$sonarUrl -Dsonar.token=*** -f $Module"
    cmd /c "$scanCmd 2>&1"

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "== SonarQube scan COMPLETED SUCCESSFULLY ==" -ForegroundColor Green
        Write-Host "Check quality gate status using the MCP tools or SonarQube dashboard."
    } else {
        Write-Host ""
        Write-Host "== SonarQube scan FAILED (exit code: $LASTEXITCODE) ==" -ForegroundColor Red
        Write-Host "Review the output above for errors."
        exit 1
    }
}
finally {
    # Scrub token from memory
    $token = $null
    [GC]::Collect()
}
