# Getting Started — App Modernization Power

## Prerequisites

### Java Projects
- Java project with `pom.xml` or `build.gradle`
- Maven 3.9+ or Gradle 8+ installed
- Target JDK 21 installed
- (Optional) SonarQube access for quality validation

### .NET Projects
- .NET project with `*.sln` or `*.csproj`
- .NET 8 SDK installed
- (Optional) SonarQube access for quality validation

---

## One-Prompt Execution

### Java Migration
Say any of these to trigger the full automated workflow:
```
"migrate java from 7 to java 21"
"scan my Java project for migration to Java 21"
"upgrade to Java 21"
"modernize java"
```

### .NET Migration
Say any of these:
```
"migrate .net"
"modernize .net to .NET 8"
"upgrade dotnet"
".net framework to .net 8"
```

---

## What Happens Automatically

### Java Workflow (7 Phases)

**Phase 1 — Assessment**
- Scans pom.xml for current Java version
- Inventories all .java files by type (servlets, DAOs, controllers, entities)
- Identifies legacy patterns (raw JDBC, manual servlets, pre-diamond generics)
- Connects to SonarQube for baseline metrics
- Outputs: `Migration/00-Assessment-Report.md`

**Phase 2 — Build Configuration**
- Updates `<java.version>21</java.version>` in pom.xml
- Verifies Spring Boot 3.4.x compatibility
- Adds missing dependencies (actuator, validation)
- Removes incompatible dependencies
- Verifies: `mvn clean compile` passes

**Phase 3 — Legacy Removal & Architecture**
- Deletes legacy servlet classes (superseded by @Controller)
- Deletes legacy DAO classes (replaced by JdbcTemplate repositories)
- Deletes static ConnectionHelper bridge
- Creates service layer (interfaces + implementations)
- Refactors controllers to use constructor injection + delegate to services
- Verifies: `mvn clean compile` passes

**Phase 4 — Java 21 Features**
- Records for DTOs and value objects
- Pattern matching (instanceof, switch expressions)
- Text blocks for SQL and multi-line strings
- Sealed classes for type hierarchies
- Virtual threads (Spring Boot 3.2+ config)
- SequencedCollections (getFirst/getLast)
- var for local variables
- Collection factories (List.of, Map.of)
- Verifies: `mvn clean compile` passes

**Phase 5 — Configuration Modernization**
- Converts application.properties → application.yml
- Creates environment-specific profiles (dev, prod)
- Externalizes secrets via environment variables
- Adds @ConfigurationProperties record classes
- Verifies: application starts successfully

**Phase 6 — Testing & Validation**
- Updates tests to use Java 21 patterns
- Adds unit tests for new service layer
- Runs: `mvn clean verify` (build + all tests)
- Verifies: 0 failures, 0 errors

**Phase 7 — SonarQube Quality Gate**
- Connects to SonarQube MCP
- Fixes any new issues introduced
- Verifies quality gate passes
- Generates: `Migration/Java21-Migration-Summary.md`

---

### .NET Workflow (7 Phases)

**Phase 1 — Assessment**
- Scans .sln/.csproj for target framework
- Identifies legacy patterns (System.Web, EF6, ConfigurationManager)
- Connects to SonarQube for baseline
- Outputs: `Migration/00-DotNet-Assessment-Report.md`

**Phase 2 — Project File Upgrade**
- Converts to SDK-style .csproj with `<TargetFramework>net8.0</TargetFramework>`
- Replaces Framework references with NuGet packages
- Adds EF Core, ASP.NET Core packages
- Verifies: `dotnet build` passes

**Phase 3 — Architecture Modernization**
- Replaces Startup.cs with Program.cs (minimal hosting)
- Removes System.Web references → ASP.NET Core equivalents
- Migrates EF6 → EF Core
- Converts all data access to async/await
- Creates service layer
- Verifies: `dotnet build` passes

**Phase 4 — .NET 8 Features**
- Records for DTOs
- Primary constructors (C# 12)
- Collection expressions (C# 12)
- Raw string literals
- Pattern matching
- Nullable reference types
- IHttpClientFactory
- Options pattern
- Verifies: `dotnet build` passes

**Phase 5 — Configuration Modernization**
- Creates appsettings.json with sections
- Creates environment-specific overrides
- Implements Options pattern for typed config
- Externalizes secrets
- Verifies: application starts

**Phase 6 — Testing & Validation**
- Creates/updates test project (xUnit + Moq)
- Adds service layer tests
- Runs: `dotnet test` (0 failures)

**Phase 7 — SonarQube Quality Gate**
- Fixes new issues
- Verifies quality gate
- Generates: `Migration/DotNet8-Migration-Summary.md`

---

## Completion Criteria

The migration is complete when:
- Build passes with 0 errors
- All tests pass (0 failures)
- Legacy code removed
- Modern architecture in place (service layer)
- Target language features applied
- Configuration externalized
- SonarQube quality gate passes (or documented as pending)
- Migration summary report generated

---

## Error Recovery

| Issue | Action |
|-------|--------|
| Build fails after a phase | Fix errors immediately, re-run build, continue |
| Tests fail | Fix migration-caused failures, document pre-existing |
| SonarQube unreachable | Skip Phase 7, document as "pending manual scan" |
| Dependency conflict | Resolve version conflict, update to compatible version |

The workflow never stops for non-critical issues — it documents and continues.
