# App Modernization Power for Kiro

Automated end-to-end application modernization for **Java (7/8 → 21)** and **.NET Framework (→ .NET 8+)**.

## Installation

### From GitHub (once published)
1. Open Kiro → Command Palette → "Powers: Install"
2. Enter repository URL: `https://github.com/YOUR_ORG/app-modernization-power`

### Local Installation
1. Copy the `app-modernization-power/` folder to `~/.kiro/powers/` or your workspace `.kiro/powers/`
2. Restart Kiro

## Usage

### Java Migration
Simply type in Kiro chat:
```
migrate java from 7 to java 21
```

### .NET Migration
```
migrate .net
```

The power will automatically:
1. Detect your project type (Maven/Gradle for Java, .sln/.csproj for .NET)
2. Assess current state and capture SonarQube baseline
3. Upgrade build configuration to target version
4. Remove legacy code patterns
5. Apply modern language features (records, pattern matching, etc.)
6. Externalize configuration
7. Validate with build + tests + SonarQube quality gate
8. Generate a migration summary report

## Configuration

### SonarQube (Optional)
Set your SonarQube token as an environment variable:
```bash
export SONARQUBE_TOKEN=your_token_here
```

Or configure in `.kiro/settings/mcp.json`:
```json
{
  "mcpServers": {
    "sonarqubemcp": {
      "url": "https://your-sonarqube-server.com",
      "type": "http",
      "headers": {
        "SONARQUBE_TOKEN": "your_token"
      }
    }
  }
}
```

## Power Structure

```
app-modernization-power/
├── POWER.md                          # Power manifest and description
├── README.md                         # This file
├── mcp.json                          # MCP server configuration
├── steering/
│   ├── getting-started.md            # Complete workflow guide
│   ├── java-migration-rules.md       # Java 7→21 patterns & rules
│   ├── dotnet-migration-rules.md     # .NET Framework→8 patterns
│   ├── sonarqube-validation.md       # Quality gate workflow
│   ├── hooks-reference.md            # All hooks documentation
│   └── autopilot.md                  # Autonomous execution rules
└── hooks/
    ├── migration-prompt-trigger.json # Detects "migrate" intent
    ├── build-on-java-save.json       # Auto-compile Java
    ├── build-on-dotnet-save.json     # Auto-compile .NET
    ├── namespace-guard.json          # Blocks legacy imports
    ├── scope-enforcement.json        # Blocks out-of-scope changes
    ├── sonarqube-post-task.json      # Quality check after tasks
    └── git-safety-guard.json         # Prevents accidental git ops
```

## Supported Migrations

| Source | Target | Trigger Phrase |
|--------|--------|---------------|
| Java 7/8 | Java 21 + Spring Boot 3.4 | "migrate java to 21" |
| Java 11/17 | Java 21 | "upgrade to java 21" |
| .NET Framework 4.x | .NET 8 + ASP.NET Core | "migrate .net" |
| .NET Core 3.1/5/6 | .NET 8 | "upgrade dotnet" |

## License

MIT
