<#
.SYNOPSIS
    Phase 7 quality gate check against a SonarQube MCP server.

.DESCRIPTION
    Bundled with app-modernization-power. Use when Kiro's native MCP tools are
    unavailable in-session; this talks to the same endpoint over raw JSON-RPC.

    The token is PROMPTED FOR at runtime. It is never read from an environment
    variable, never written to disk, and never echoed to the terminal. It lives
    only in process memory as a SecureString for the duration of the run.

    Steps:
      1. Prompt for the token, then handshake
      2. Verify the project key EXISTS - a wrong key silently returns another
         project's data with no error, which the server itself warns about
      3. Report last analysis date and branch  <-- staleness check
      4. Fetch quality gate status and measures

.PARAMETER ProjectKey
    SonarQube project key, e.g. Refactoring-legacy-Hospital-uc2

.EXAMPLE
    .\phase7-quality-gate.ps1 -ProjectKey Refactoring-legacy-Hospital-uc2
    # prompts: SonarQube token: ****
#>
param(
    [Parameter(Mandatory = $true)][string]$ProjectKey,
    [string]$Url = 'https://sonarqube-mcp-server.azurewebsites.net/mcp'
)

$ErrorActionPreference = 'Stop'

# --- prompt for the token (masked, never persisted) ---
Write-Host "SonarQube token required for project '$ProjectKey'."
Write-Host "Input is masked and is not saved, logged, or exported." -ForegroundColor DarkGray
$secure = Read-Host -Prompt 'SonarQube token' -AsSecureString
if ($secure.Length -eq 0) { throw 'No token entered. Aborting.' }

$bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
try   { $token = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr) }
finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr) }

$headers = @{
    'Accept'          = 'application/json, text/event-stream'
    'Content-Type'    = 'application/json'
    'SONARQUBE_TOKEN' = $token
}

function Send-Mcp([string]$Body) {
    try {
        return @{ ok = $true; body = (Invoke-WebRequest -Uri $Url -Method POST -Headers $headers -Body $Body -UseBasicParsing).Content }
    } catch {
        $r = $_.Exception.Response; $t = ''
        if ($r) { $t = (New-Object System.IO.StreamReader($r.GetResponseStream())).ReadToEnd() }
        return @{ ok = $false; status = [int]$r.StatusCode; body = $t }
    }
}

function Parse-Sse($raw) {
    $j = ($raw -split "`n" | Where-Object { $_ -like 'data:*' } | ForEach-Object { $_.Substring(5).Trim() }) -join ''
    if (-not $j) { $j = $raw }
    try { return $j | ConvertFrom-Json } catch { return $null }
}

function Invoke-Tool([string]$Name, $Arguments, [int]$Id) {
    $body = @{ jsonrpc = '2.0'; id = $Id; method = 'tools/call'
               params = @{ name = $Name; arguments = $Arguments } } | ConvertTo-Json -Depth 12 -Compress
    $r = Send-Mcp $body
    if (-not $r.ok) { throw "HTTP $($r.status) calling $Name : $($r.body)" }
    $p = Parse-Sse $r.body
    if ($p.error) { throw "JSON-RPC error calling $Name : $($p.error.message)" }
    return (($p.result.content | ForEach-Object { $_.text }) -join "`n")
}

try {
    # --- handshake ---
    Send-Mcp (@{ jsonrpc='2.0'; id=1; method='initialize'
                 params=@{ protocolVersion='2025-06-18'; capabilities=@{}
                           clientInfo=@{ name='app-modernization-power'; version='1.0' } } } |
              ConvertTo-Json -Depth 10 -Compress) | Out-Null
    Send-Mcp (@{ jsonrpc='2.0'; method='notifications/initialized' } | ConvertTo-Json -Compress) | Out-Null

    # --- 1. verify the key exists ---
    Write-Host ""
    Write-Host "== Verifying project key =="
    $projects = Invoke-Tool 'search_my_sonarqube_projects' @{ page = 1 } 10
    if ($projects -notmatch [regex]::Escape($ProjectKey)) {
        Write-Host "FAIL: '$ProjectKey' not found on this server." -ForegroundColor Red
        Write-Host "A wrong key returns another project's data with NO error. Available:"
        Write-Host $projects
        exit 1
    }
    Write-Host "OK: '$ProjectKey' exists." -ForegroundColor Green

    # --- 2. staleness check ---
    Write-Host ""
    Write-Host "== Last analysis (staleness check) =="
    Write-Host (Invoke-Tool 'list_branches' @{ projectKey = $ProjectKey } 11)
    Write-Host "Compare analysisDate and branch against your working tree." -ForegroundColor Yellow
    Write-Host "If your changes are uncommitted or on an unanalyzed branch, the gate" -ForegroundColor Yellow
    Write-Host "below describes DIFFERENT CODE. Record Phase 7 as indeterminate."     -ForegroundColor Yellow

    # --- 3. gate + measures ---
    Write-Host ""
    Write-Host "== Quality gate =="
    Write-Host (Invoke-Tool 'get_project_quality_gate_status' @{ projectKey = $ProjectKey } 12)

    Write-Host ""
    Write-Host "== Measures =="
    Write-Host (Invoke-Tool 'get_component_measures' @{
        projectKey = $ProjectKey
        metricKeys = @('ncloc','bugs','vulnerabilities','code_smells','coverage','duplicated_lines_density')
    } 13)
}
finally {
    # scrub the plaintext copy from memory
    $token = $null
    [GC]::Collect()
}
