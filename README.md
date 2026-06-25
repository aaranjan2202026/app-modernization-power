# Java & .NET Modernization Refactoring Demo

This project demonstrates automated Java and .NET application modernization using GitHub Copilot custom agents and SonarQube integration. The project transforms legacy Java 8 applications to modern Java 17+ with Spring Boot 3.x, and legacy .NET applications to .NET 8+.

## Objectives

- Modernize legacy Java 8 code to Java 17+ with async patterns (CompletableFuture/reactive streams)
- Externalize configuration to Spring application.yml files
- Modularize business logic into service layers
- Replace deprecated Java APIs with modern equivalents
- Integrate SonarQube for continuous code quality assessment

## Getting Started

### Prerequisites

#### Java Project
1. **Java 17+ SDK** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK
2. **Maven 3.8+** or **Gradle 7.5+** - Build tools

#### .NET Project
3. **.NET 8 SDK** - Download from [Microsoft](https://dotnet.microsoft.com/en-us/download/dotnet/8.0)
4. **ASP.NET Core runtime** - Included with .NET 8 SDK

#### Common
5. **VS Code** with GitHub Copilot extension
6. **Git** for version control
7. **SonarQube** access (optional for quality gates)

### Installation Process



1. Configure SonarQube in `.github/copilot-instructions.md`:

   **Java Project**:
   - `sonarqube project name = "Refactoring-legacy-Hospital-uc2"`
   - `sonarqube server = "https://sonarqube-hub.azurewebsites.net"`

   **.NET Project**:
   - `sonarqube dotnet project name = "Refactoring-legacy-DotNet-uc2"`
   - `sonarqube server = "https://sonarqube-hub.azurewebsites.net"`

2. Enable Copilot Agent Mode:
      Open repo in VS Code
      Open Copilot Chat
      Select Agent mode

Agent-Based Workflow Execution
    This project follows a 6-phase modernization workflow defined in copilot-instructions:

## FULL AUTOMATED FLOW (Recommended)
 Run one of the following commands:
 ```bash
 # Java project:
 @modernization-orchestrator Start the full Java modernization workflow

 # .NET project:
 @modernization-orchestrator Start the full .NET modernization workflow
```

What it does:

- Automatically executes all phases (Phase 0 → Phase 6)
- Auto-detects project type (Java or .NET) and selects the correct branch
- Java branch: `feature/java-modernization` | .NET branch: `feature/dotnet-modernization`
- Triggers baseline SonarQube scan, runs refactoring end-to-end
- Commits & pushes changes with traceable task IDs
- Enforces quality gates (G1–G5) before proceeding to each phase

## Automated Modernization Workflow
This project uses custom GitHub Copilot agents to automate the modernization process. All agents **auto-detect** the project type (Java or .NET) and adjust commands, branches, and patterns accordingly.

### Phase 0: Baseline Scan
- Configure SonarQube project in `.github/copilot-instructions.md`
- **Java**: Trigger `.github/workflows/sonarqube.yml`
  ```bash
  gh workflow run sonarqube.yml --ref feature/java-modernization
  ```
- **.NET**: Trigger `.github/workflows/sonarqube-dotnet.yml`
  ```bash
  gh workflow run sonarqube-dotnet.yml --ref feature/dotnet-modernization
  ```

### Phase 1: Assessment
```bash
@SonarQubeGenie Fix all SonarQube issues
```

### Phase 2: Planning
```bash
@modernization-plan Create modernization plan
```

### Phase 3: Refactoring
```bash
@modernization-developer Execute all modernization tasks
```

### Phase 4: Validation
```bash
@modernization-validator Run full validation
```

### Phase 5: Deploy
Orchestrator triggers Azure DevOps pipeline automatically.

### Phase 6: Final Scan
`@modernization-validator` re-triggers the appropriate SonarQube workflow after all tests pass and generates a final quality report comparing baseline vs. post-refactoring metrics.

### Or run the full workflow:
```bash
# Java project:
@modernization-orchestrator Start the full Java modernization workflow

# .NET project:
@modernization-orchestrator Start the full .NET modernization workflow
```

## Build and Test

### Build
```bash
# Maven
mvn clean package

# Gradle
gradle build
```

### Run Tests
```bash
# Maven
mvn test

# Gradle
gradle test
```

### Check Coverage
```bash
# Maven with JaCoCo
mvn jacoco:report

# Gradle with JaCoCo
gradle jacocoTestReport
```

## Project Structure

```
├── Hospital_Servlet1/         # Java legacy application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/          # Java source files
│   │   │   └── resources/
│   │   │       ├── application.yml           # Main config
│   │   │       ├── application-dev.yml       # Dev config
│   │   │       └── application-prod.yml      # Prod config
│   │   └── test/
│   │       └── java/          # Unit and integration tests
│   └── pom.xml                # Maven build file
├── pharmacy/                  # .NET legacy application
│   ├── src/
│   │   ├── ApplicationCore/   # Domain/business logic
│   │   ├── Infrastructure/    # Data & identity
│   │   └── Web/               # ASP.NET Core web app
│   └── PharmacyNetwork.sln
├── .github/
│   ├── agents/                # Custom Copilot agents
│   ├── instructions/          # Modernization instructions
│   └── workflows/             # CI/CD pipelines (sonarqube.yml, sonarqube-dotnet.yml)
├── Migration/                 # Migration plans and reports
├── Orchestration/             # Workflow state, SonarQube fix summaries, validation reports
└── README.md
```

## Contribute

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Quality Standards

- Minimum 80% test coverage
- All SonarQube issues resolved
- Follow Spring Boot and Java best practices
- Use Java 17+ features appropriately
- All agents operate in fully autonomous mode — quality gates (G1–G5) must pass before proceeding
- Never modify: new features, UI, domain redesign (out of scope)

## References

- [Spring Boot 3.x Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [.NET 8 Documentation](https://learn.microsoft.com/en-us/dotnet/core/whats-new/dotnet-8/overview)
- [GitHub Copilot Documentation](https://docs.github.com/en/copilot)
- [SonarQube Documentation](https://docs.sonarqube.org/latest/)
