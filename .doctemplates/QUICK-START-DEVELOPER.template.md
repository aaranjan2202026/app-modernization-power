# Developer Quick Start - [Solution Name] Modernization

> **Hands-on guide for developers joining the [Solution Name] modernization project - from zero to productive in [timeframe if known, else "minimal time"]**

**Target Audience**: Developers, QA Engineers, DevOps Engineers joining the modernization team
**Prerequisites**: [Language] development experience, basic cloud concepts, git proficiency
**Est. Setup Time**: <!-- ⚠️ IF evidence-based: X-Y hours (basis: [documented setup process, team onboarding data])
                          ELSE: "Varies by environment and experience level" -->

---

## 📋 Table of Contents

- [Quick Start Overview](#quick-start-overview)
- [Development Environment Setup](#development-environment-setup)
- [Codebase Overview](#codebase-overview)
- [Local Development Workflow](#local-development-workflow)
- [Code Modernization Patterns](#code-modernization-patterns)
- [Testing Guidelines](#testing-guidelines)
- [Common Tasks](#common-tasks)
- [Troubleshooting](#troubleshooting)
- [Contributing Guidelines](#contributing-guidelines)
- [Resources & References](#resources--references)

---

## 🚀 Quick Start Overview

### What You'll Be Working On

**[Solution Name]** is a **[type of application]** currently undergoing modernization from **[current stack]** to **[target stack]**.

**Current Architecture**: <!-- Brief description with key technologies -->
**Target Architecture**: <!-- Brief description with key technologies -->
**Your Role**: <!-- Example: "Migrate [component X] from [tech A] to [tech B]" -->

**Key Documents for Developers**:
1. **[System Architecture](../CURRENT-STATE/1-architecture/1.1-system-architecture.md)** (15 min) - Understand current system
2. **[Implementation Plan](../MODERNIZATION/3-execution/3.1-detailed-implementation-plan.md)** (20 min) - See what's being built
3. **[Modernization Strategy](../MODERNIZATION/2-strategy/2.3-modernization-strategy.md)** (10 min) - Understand why

---

## 💻 Development Environment Setup

### Prerequisites Checklist

Install the following tools before proceeding:

- [ ] **[Language/Runtime]**: Version [X.Y.Z or later]
  - Download: [URL]
  - Verify: `[command] --version`
  
- [ ] **[IDE/Editor]**: [Recommended IDE] (or compatible)
  - Download: [URL]
  - Extensions: [List essential extensions]

- [ ] **[Package Manager]**: Version [X.Y.Z or later]
  - Download: [URL]
  - Verify: `[command] --version`

- [ ] **Docker Desktop**: Version [X.Y or later]
  - Download: [URL]
  - Required for: [local database, containerized services, etc.]

- [ ] **Git**: Version [X.Y or later]
  - Download: [URL]
  - Configure: See [Authentication Setup](#git-authentication)

- [ ] **[Cloud CLI]**: (if applicable)
  - Download: [URL]
  - Purpose: [Local development, deployment, etc.]

- [ ] **[Other Required Tools]**:
  - Purpose and installation instructions

### Installation Guides by OS

<details>
<summary><b>Windows Setup</b></summary>

```powershell
# Example installation commands for Windows
# Using winget or Chocolatey

# Install [Tool 1]
winget install [tool]

# Install [Tool 2]
choco install [tool]

# Verify installations
[command] --version
```

**Windows-Specific Configuration**:
- <!-- Any Windows-specific setup steps -->

</details>

<details>
<summary><b>macOS Setup</b></summary>

```bash
# Example installation commands for macOS
# Using Homebrew

# Install [Tool 1]
brew install [tool]

# Install [Tool 2]
brew install [tool]

# Verify installations
[command] --version
```

**macOS-Specific Configuration**:
- <!-- Any macOS-specific setup steps -->

</details>

<details>
<summary><b>Linux Setup</b></summary>

```bash
# Example installation commands for Linux
# Using apt (Debian/Ubuntu)

# Install [Tool 1]
sudo apt install [tool]

# Using snap
sudo snap install [tool]

# Verify installations
[command] --version
```

**Linux-Specific Configuration**:
- <!-- Any Linux-specific setup steps -->

</details>

### Repository Setup

#### 1. Clone Repository

```bash
# Clone the main repository
git clone [repository-url]
cd [solution-name]

# Check repository structure
ls -la
```

**Expected Structure**:
```
[solution-name]/
├── src/                    # Source code
│   ├── [project 1]/
│   ├── [project 2]/
├── tests/                  # Test projects
├── docs/                   # Documentation
├── .github/                # CI/CD workflows
├── [build-file]            # Build configuration
└── README.md
```

#### 2. Install Dependencies

⚠️ **Installation Time Disclaimer**:
- **Estimated time**: <!-- IF known from team data: X-Y minutes on [connection speed] ELSE: "Varies by network speed and machine specs" -->
- **First-time setup**: <!-- Includes downloads, cache building, etc. -->
- **Subsequent setups**: <!-- Faster due to caching -->

```bash
# Install project dependencies
[package-manager-command]

# Example: npm install, dotnet restore, pip install -r requirements.txt
```

**Troubleshooting Dependencies**:
- If installation fails: See [Dependency Issues](#dependency-installation-issues)
- For private package authentication: See [Package Registry Setup](#package-registry-authentication)

#### 3. Configure Local Environment

```bash
# Copy environment template
cp .env.example .env

# Edit .env with your local configuration
[editor] .env
```

**Required Configuration**:
<!-- List required environment variables with descriptions

- `DATABASE_CONNECTION_STRING`: Local database connection
  - Example: `Server=localhost;Database=...`
  - See: [Database Setup](#local-database-setup)
  
- `API_KEY`: [Service] API key
  - Obtain from: [Where/how to get it]
  - Purpose: [What it's used for]
-->

**Secrets Management**:
- ⚠️ **NEVER commit `.env` to git** (already in `.gitignore`)
- Store production secrets in: [Secret management service]
- For local development: Use `.env` with dummy/dev values

### Local Database Setup

#### Option 1: Docker (Recommended)

```bash
# Start local database container
docker-compose up -d database

# Verify database is running
docker ps | grep database

# View logs if issues
docker logs [container-name]
```

**Database Access**:
- **Host**: localhost
- **Port**: [port]
- **Database**: [database-name]
- **User**: [username]
- **Password**: [password] (from .env)

#### Option 2: Local Installation

<!-- If Docker not used, provide local installation instructions
1. Download [Database] from [URL]
2. Install with these settings: [...]
3. Create database: [command]
4. Run migrations: [command]
-->

#### Database Initialization

```bash
# Run database migrations
[migration-command]

# Seed development data (optional)
[seed-command]

# Verify setup
[verification-command]
```

⚠️ **Initialization Time**:
- **Estimated duration**: <!-- IF known: X-Y minutes for [N] migrations/seeds ELSE: "Depends on data volume" -->
- **First run**: <!-- May be slower due to initial setup -->

### Verify Setup

Run the verification script to ensure everything is configured correctly:

```bash
# Run setup verification
[verification-script-command]

# Example output:
# ✅ [Runtime] version: X.Y.Z
# ✅ Database connection: OK
# ✅ Dependencies installed: OK
# ✅ Configuration valid: OK
```

**If verification fails**: See [Troubleshooting](#troubleshooting) section

---

## 📂 Codebase Overview

### Repository Structure

```
[solution-name]/
│
├── src/
│   ├── [Core Project]/           # Core business logic
│   │   ├── Domain/               # Domain models
│   │   ├── Services/             # Business services
│   │   └── Infrastructure/       # Data access, external integrations
│   │
│   ├── [Web Project]/            # Web application/API
│   │   ├── Controllers/          # API controllers
│   │   ├── Models/               # ViewModels, DTOs
│   │   └── [Frontend]/           # Frontend code (if applicable)
│   │
│   └── [Other Projects]/
│
├── tests/
│   ├── [Unit Tests]/             # Unit test projects
│   ├── [Integration Tests]/      # Integration test projects
│   └── [E2E Tests]/              # End-to-end test projects
│
├── docs/
│   └── .solutiondocs/            # Architecture and modernization docs
│
├── scripts/                      # Build and utility scripts
├── .github/                      # CI/CD workflows, templates
└── [Build/Config Files]/         # Solution-level configuration
```

**Key Directories** (from analysis):
<!-- List actual key directories with purposes
- **`src/[Project]/[Directory]`**: Purpose (based on code analysis)
  - Key files: [List important files]
  - Responsibilities: [What this code does]
-->

### Technology Stack

#### Current Stack

| Layer | Technology | Version | Notes |
|-------|------------|---------|-------|
| **Backend** | <!-- Framework --> | <!-- Version from analysis --> | <!-- Current state --> |
| **Frontend** | <!-- Framework --> | <!-- Version --> | <!-- Current state --> |
| **Database** | <!-- Database --> | <!-- Version --> | <!-- Current state --> |
| **Infrastructure** | <!-- Platform --> | <!-- Version --> | <!-- Current state --> |

#### Target Stack (Migration In Progress)

| Layer | Technology | Version | Status |
|-------|------------|---------|--------|
| **Backend** | <!-- Target framework --> | <!-- Target version --> | <!-- Migration status --> |
| **Frontend** | <!-- Target framework --> | <!-- Target version --> | <!-- Migration status --> |
| **Database** | <!-- Target database --> | <!-- Target version --> | <!-- Migration status --> |
| **Infrastructure** | <!-- Target platform --> | <!-- Target config --> | <!-- Migration status --> |

**Migration Progress**: <!-- [X]% complete (basis: [component count, LoC, feature count]) OR "See migration dashboard" -->

### Key Components

#### Component: [Component Name]

**Location**: `src/[path]`
**Purpose**: <!-- What this component does -->
**Status**: <!-- ✅ Modernized | 🔄 In Progress | ⏳ Not Started -->

**Key Classes/Files**:
- **`[ClassName]`** (`[file-path]`): <!-- Purpose -->
- **`[ClassName]`** (`[file-path]`): <!-- Purpose -->

**Dependencies**:
- Depends on: <!-- [Other components] -->
- Used by: <!-- [Other components] -->

**Modernization Notes**:
- **Current**: <!-- Current implementation approach -->
- **Target**: <!-- What it will become -->
- **Migration Task**: <!-- Link to tracking item if applicable -->

<!-- Repeat for 5-10 key components -->

---

## 🔧 Local Development Workflow

### Running the Application Locally

#### 1. Start Dependencies (if using Docker)

```bash
# Start all infrastructure services (database, cache, etc.)
docker-compose up -d

# Check service health
docker-compose ps
```

#### 2. Run Application

```bash
# Build application
[build-command]

# Run application
[run-command]

# Application should be available at:
# [URL] - Main application
# [URL] - API/Swagger (if applicable)
```

⚠️ **Startup Time**:
- **First run**: <!-- IF known: X-Y seconds (includes [compilation, dependency resolution, warmup]) ELSE: "Varies by machine specs" -->
- **Subsequent runs**: <!-- Faster due to caching -->
- **With debugging**: <!-- May be slower -->

#### 3. Verify Application Running

```bash
# Test health endpoint
curl http://localhost:[port]/health

# Expected response: 200 OK with health status
```

**Application URLs**:
- **Web UI**: http://localhost:[port]
- **API Docs**: http://localhost:[port]/[swagger-path]
- **Database Admin**: http://localhost:[port] (if applicable)

### Making Code Changes

#### Development Loop

1. **Create Feature Branch**
   ```bash
   # Pull latest main
   git checkout main
   git pull origin main
   
   # Create feature branch
   git checkout -b feature/[your-feature-name]
   ```

2. **Make Changes**
   - Edit code in your IDE
   - Follow [coding standards](#coding-standards)
   - Add/update tests

3. **Test Locally**
   ```bash
   # Run unit tests
   [test-command]
   
   # Run specific test
   [test-specific-command]
   
   # Check code coverage (if configured)
   [coverage-command]
   ```

4. **Commit Changes**
   ```bash
   # Stage changes
   git add [files]
   
   # Commit with descriptive message
   git commit -m "[type]: [description]"
   
   # Commit message format: [conventional commits, other standard]
   # Examples:
   # feat: add user authentication
   # fix: resolve database connection issue
   # refactor: modernize payment service
   ```

5. **Push and Create PR**
   ```bash
   # Push to remote
   git push origin feature/[your-feature-name]
   
   # Create Pull Request via GitHub/GitLab UI
   # Use PR template (auto-populated)
   ```

### Hot Reload / Watch Mode

```bash
# Run in watch mode (auto-reload on file changes)
[watch-command]

# Hot reload is available for:
# ✅ [Frontend changes - immediate reload]
# ✅ [Backend changes - requires restart]
# ❌ [Configuration changes - manual restart required]
```

---

## 🔄 Code Modernization Patterns

### Modernization Guidelines

When migrating code from current to target architecture:

#### Pattern 1: [Migration Pattern Name]

**Use Case**: When to apply this pattern
**Current Code Pattern**:
```[language]
// Example of current (legacy) code pattern
// Location: [Typical file location]
[Current code example]
```

**Modernized Code Pattern**:
```[language]
// Modernized equivalent
// New location: [Target file location]
[Modernized code example]
```

**Key Changes**:
- ✅ Change: Explanation
- ✅ Change: Explanation
- ⚠️ **Breaking Change**: What breaks and how to handle

**Testing Considerations**:
- Tests needed: [Unit test approach]
- Validation: [How to verify correctness]

<!-- Repeat for common modernization patterns:
- Dependency injection migration
- Async/await pattern adoption
- Configuration management changes
- Logging framework migration
- Authentication/authorization updates
- API client modernization
-->

### Coding Standards

#### [Language] Style Guide

Follow **[Style Guide Name]** with these project-specific conventions:

**Naming Conventions**:
- Classes: `[Convention]`
- Methods: `[Convention]`
- Variables: `[Convention]`
- Constants: `[Convention]`

**Code Organization**:
- File structure: [Pattern]
- Import/using order: [Pattern]
- Comment style: [Pattern]

**Linting**:
```bash
# Run linter
[lint-command]

# Auto-fix issues
[lint-fix-command]
```

**Code Formatter**:
```bash
# Format code
[format-command]

# IDE integration: [How to configure auto-format]
```

### Anti-Patterns to Avoid

❌ **Don't**:
- [Anti-pattern]: Why it's problematic
- [Anti-pattern]: What to do instead

✅ **Do**:
- [Best practice]: Why it's better
- [Best practice]: How to implement

---

## 🧪 Testing Guidelines

### Test Structure

```
tests/
├── [Unit Tests Project]/
│   ├── [Component]Tests/
│   │   ├── [Class]Tests.cs
│   │   └── [Class]Tests.cs
│   └── TestHelpers/
│
├── [Integration Tests Project]/
│   ├── [Feature]Tests/
│   └── TestFixtures/
│
└── [E2E Tests Project]/
    └── Scenarios/
```

### Writing Unit Tests

**Template**:
```[language]
// Unit test example for [Component]
[Test code with comments explaining pattern]
```

**Guidelines**:
- Test naming: `[MethodName]_[Scenario]_[ExpectedBehavior]`
- AAA pattern: Arrange, Act, Assert
- One assert per test (ideally)
- Use test fixtures for shared setup

### Running Tests

```bash
# Run all tests
[test-all-command]

# Run tests for specific project
[test-project-command]

# Run tests with code coverage
[test-coverage-command]

# Run tests in watch mode
[test-watch-command]
```

⚠️ **Test Execution Time**:
- **Unit tests**: <!-- IF measured: ~X seconds for N tests ELSE: "Should be fast (<1 min typically)" -->
- **Integration tests**: <!-- IF measured: ~Y seconds for M tests ELSE: "Slower due to DB/service setup" -->
- **E2E tests**: <!-- IF measured: ~Z minutes for K scenarios ELSE: "Longest - full application stack" -->

**Test Coverage Targets**:
- **Unit test coverage**: <!-- Target % with rationale OR "See team standards" -->
- **Critical paths**: <!-- 100% or specific requirement -->
- **New code**: <!-- Coverage requirement for new code -->

⚠️ **Coverage Disclaimer**:
- Coverage targets based on: <!-- [team standards, industry benchmarks, OR "aspirational goals"] -->
- Measured using: [Coverage tool]
- Reported in: [CI/CD pipeline, coverage report location]

### Integration Testing

**Setup**:
```bash
# Start test dependencies
docker-compose -f docker-compose.test.yml up -d

# Run integration tests
[integration-test-command]

# Teardown
docker-compose -f docker-compose.test.yml down
```

**Test Database**:
- Separate database for integration tests
- Reset between test runs
- Seed test data via: [approach]

---

## 📝 Common Tasks

### Task: Add New API Endpoint

**Estimated Effort**: <!-- IF typical task tracked: X-Y hours (basis: [similar feature history]) ELSE: "Varies by complexity" -->

**Steps**:
1. Define API contract (request/response models)
2. Implement controller/handler
3. Implement business logic (service layer)
4. Add data access (if needed)
5. Write tests (unit + integration)
6. Update API documentation
7. Create PR with test evidence

**Example** (for [Framework]):
```[language]
// Code example showing new endpoint implementation
[Code snippet with inline comments]
```

### Task: Add Database Migration

**Steps**:
1. Create migration file
   ```bash
   [migration-create-command]
   ```

2. Edit migration (add schema changes)
   ```[language]
   // Example migration code
   [Code snippet]
   ```

3. Test migration locally
   ```bash
   # Apply migration
   [migration-up-command]
   
   # Verify schema
   [schema-verify-command]
   
   # Test rollback
   [migration-down-command]
   ```

4. Commit migration with application code

### Task: Update Frontend Component

**Steps**:
1. Locate component: `src/[path]/[Component]`
2. Make changes following [component pattern]
3. Update unit tests
4. Test in browser with hot reload
5. Check browser console for errors
6. Verify responsive design (if applicable)

**Example**:
```[language]
// Example component modification
[Code snippet]
```

### Task: Debug Production Issue

**Steps**:
1. Reproduce locally (if possible)
   - Get production logs: [How to access]
   - Get production data: [How to access safely]
   
2. Use logging to trace issue
   ```[language]
   // Add debug logging
   [Logging pattern]
   ```

3. Attach debugger (if local reproduction)
   - IDE: Set breakpoints in [locations]
   - Run in debug mode: [command]

4. Identify root cause
5. Write test that reproduces bug
6. Fix and verify test passes
7. Deploy fix following [deployment process]

---

## 🛠️ Troubleshooting

### Dependency Installation Issues

**Problem**: `[Common error message]`

**Solution**:
```bash
# Clear package cache
[clear-cache-command]

# Reinstall dependencies
[reinstall-command]
```

**Still failing?**: [Alternative troubleshooting steps]

### Database Connection Failures

**Problem**: Cannot connect to local database

**Checklist**:
- [ ] Database container running: `docker ps`
- [ ] Connection string correct in `.env`
- [ ] Port not blocked by firewall
- [ ] Database created: [verification query]

**Solution**:
```bash
# Restart database container
docker-compose restart database

# Check logs for errors
docker logs [container-name]
```

### Build Failures

**Problem**: Build fails with `[error]`

**Common Causes**:
- Missing dependencies: Run `[install-command]`
- Version mismatch: Check [file] for required versions
- Cache corruption: Clean build with `[clean-command]`

### Test Failures

**Problem**: Tests passing locally but failing in CI

**Common Causes**:
- Environment differences: Check CI environment variables
- Test isolation issues: Tests may depend on order
- Timing issues: Async tests may need longer timeouts

**Debug**:
```bash
# Run tests in CI mode locally
[ci-test-command]
```

### Port Already in Use

**Problem**: `Port [X] already in use`

**Solution**:
```bash
# Find process using port (Windows)
netstat -ano | findstr :[port]

# Kill process (Windows)
taskkill /PID [PID] /F

# Find process using port (Mac/Linux)
lsof -ti:[port]

# Kill process (Mac/Linux)
kill -9 [PID]
```

### Performance Issues

**Problem**: Application slow locally

**Checklist**:
- [ ] Docker resources: Ensure sufficient CPU/RAM allocated
- [ ] Database size: Large dev database? Consider smaller dataset
- [ ] Debug mode overhead: Release mode faster
- [ ] Anti-virus interference: Exclude project folder

---

## 🤝 Contributing Guidelines

### Branch Strategy

**Main Branches**:
- `main`: Production-ready code
- `develop`: Integration branch for features (if applicable)

**Feature Branches**:
- `feature/[name]`: New features
- `fix/[name]`: Bug fixes
- `refactor/[name]`: Code modernization
- `docs/[name]`: Documentation updates

### Pull Request Process

1. **Before Creating PR**:
   - [ ] Code follows [coding standards](#coding-standards)
   - [ ] All tests pass locally
   - [ ] New tests added for new functionality
   - [ ] Code coverage maintained or improved
   - [ ] No linting errors
   - [ ] Documentation updated (if needed)

2. **Create PR**:
   - Fill out PR template completely
   - Link related issues/tasks
   - Add reviewers (suggested: [team members])
   - Add labels (feature, bug, refactor, etc.)

3. **PR Review**:
   - Address review comments
   - Push updates to feature branch
   - Request re-review when ready
   - Don't force-push after review started (unless requested)

4. **Merge**:
   - Requires [N] approvals
   - CI must pass
   - Merge strategy: [Squash / Rebase / Merge commit]
   - Delete branch after merge

### Code Review Checklist (for Reviewers)

- [ ] Code follows modernization patterns
- [ ] Tests cover new functionality
- [ ] No security vulnerabilities introduced
- [ ] Performance considerations addressed
- [ ] Error handling appropriate
- [ ] Logging added for key operations
- [ ] Documentation updated
- [ ] No unnecessary dependencies added

---

## 📚 Resources & References

### Essential Documents

**Read First** (for all developers):
1. **[System Architecture](../CURRENT-STATE/1-architecture/1.1-system-architecture.md)** - Understand current system
2. **[Modernization Strategy](../MODERNIZATION/2-strategy/2.3-modernization-strategy.md)** - Understand migration approach
3. **[Code Standards](link-if-exists)** - Coding conventions

**Reference as Needed**:
- **[API Documentation](link)** - API reference
- **[Database Schema](link)** - Data model
- **[Security Guidelines](../CURRENT-STATE/4-operations/4.2-security-architecture.md)** - Security practices

### External Resources

**[Technology/Framework] Documentation**:
- Official docs: [URL]
- Migration guide: [URL]
- Best practices: [URL]

**Cloud Platform Documentation**:
- [Service] docs: [URL]
- Quickstarts: [URL]
- Samples: [URL]

### Team Resources

**Communication**:
- Team chat: [Platform/channel]
- Daily standup: [Time/location]
- Sprint planning: [Schedule]

**Issue Tracking**:
- Board: [URL]
- Sprint: [Current sprint]
- Backlog: [URL]

**CI/CD**:
- Pipeline: [URL]
- Build status: [URL]
- Deployment status: [URL]

### Getting Help

**Questions About**:
- Architecture/design: @[architect-handle]
- Cloud/infrastructure: @[devops-handle]
- Frontend: @[frontend-lead-handle]
- Backend: @[backend-lead-handle]
- Database: @[db-expert-handle]

**Onboarding Buddy**: [Assigned team member]

---

## 🎯 Developer Checklist

**First Day**:
- [ ] Development environment setup complete
- [ ] Repository cloned and dependencies installed
- [ ] Application runs locally
- [ ] Tests run successfully
- [ ] Read essential documentation
- [ ] Introduced to team
- [ ] Onboarding buddy assigned

**First Week**:
- [ ] Completed first small task/bug fix
- [ ] Created first PR
- [ ] Participated in code review
- [ ] Understand project structure
- [ ] Familiar with CI/CD process

**First Month**:
- [ ] Contributed to [X] features/fixes
- [ ] Understand modernization strategy
- [ ] Comfortable with codebase navigation
- [ ] Contributing to technical discussions

---

## Template Usage Notes

**For Documentation Compiler Agent**:

1. **Evidence-Based Setup Times**: 
   - Installation/build times: Provide IF documented from team onboarding data. ELSE: "Varies by machine/network"
   - Task effort: Provide IF tracked in team velocity data. ELSE: "Varies by complexity"
2. **Actual Versions**: All technology versions from dependency analysis (not "latest" or "current")
3. **Real File Paths**: Use actual repository structure from codebase analysis
4. **Verified Commands**: All commands should be tested/verified (or marked as examples requiring customization)
5. **No Ungrounded Promises**: 
   - ❌ "Setup takes 15 minutes" without data
   - ✅ "Setup typically takes 15-30 minutes based on team onboarding history" OR "Setup duration varies"
   - ❌ "This task takes 2-4 hours" without tracking data
   - ✅ "Similar tasks have taken 2-4 hours" OR "Task complexity varies"
6. **Troubleshooting Realism**: Include actual common errors from team experience, not generic issues

**Quality Criteria**:
- ✅ All commands are copy-paste ready (or clearly marked as placeholders)
- ✅ All paths reference actual repository structure
- ✅ All versions match dependency analysis
- ✅ All estimates cite basis or acknowledge variability
- ✅ Troubleshooting section includes real issues encountered
- ✅ Links to other documentation work (no broken references)
