---
applyTo: '**/*.cs, **/*.csproj, **/*.json, **/Program.cs, **/Startup.cs, **/appsettings*.json'
---

# .NET Framework to .NET 6+ Modernization Review Instructions

## Purpose
Review the modernized .NET 6+ code to ensure it correctly transforms .NET Framework applications following the migration plan and technical guidelines.

## Review Scope
Validate modernization against:
- Migration Plan (`Migration/01-Migration_Plan.md`)
- Technical Instructions (`dotnet-modernization.instructions.md`)
- .NET 6+ best practices
- Functional equivalence with .NET Framework source

## Review Process
1. Read the migration plan to understand expected modernizations and task list
2. Review each modernized component systematically using the checklist below
3. Run build and tests to verify compilation and test coverage
4. Test manually to confirm functional equivalence
5. Document findings in structured categories:
   - **CRITICAL**: Blocks modernization, must fix immediately (build errors, broken functionality, async deadlocks)
   - **MAJOR**: Violates best practices, should fix before completion (missing tests, improper DI, config not externalized)
   - **MINOR**: Suggestions for improvement, optional (naming conventions, comments, code style)
   - **COMMENDATIONS**: Well-executed areas to acknowledge

---

# Modernization Review Checklist

## 1. Migration Plan Alignment
**Validates**: Modernization follows the documented plan

### 1.1 Task Completion
- [ ] All tasks from Task List (Section 4 of migration plan) are addressed
- [ ] Each task ID has corresponding code changes
- [ ] Dependencies between tasks are respected
- [ ] Only the 4 authorized modernizations were performed

**Validation Method**: Compare completed work to task list table
**Critical Issues**:
- Task marked complete but no code exists
- Task dependencies not met (e.g., Test created before implementation)
- Out-of-scope refactoring performed

### 1.2 Scope Compliance
- [ ] Only async/await modernization performed (no other async patterns)
- [ ] Only configuration externalization performed (no other config changes)
- [ ] Only modularization performed (no architectural redesign)
- [ ] Only deprecated API replacement performed (no unnecessary upgrades)

**Validation Method**: Check all code changes map to one of the 4 authorized modernizations
**Critical Issues**:
- Performance optimizations beyond async/await
- UI/UX redesigns
- Feature additions
- Security enhancements beyond modernization
- Analyzer agent modifications

## 2. Async/Await Modernization
**Validates**: Synchronous code correctly converted to async/await

### 2.1 Blocking Call Elimination
- [ ] No `.Result` usage remaining (except in justified Main/startup scenarios)
- [ ] No `.Wait()` usage remaining
- [ ] No synchronous I/O operations (`File.ReadAllText`, `StreamReader.ReadToEnd`)
- [ ] No synchronous database calls (`ToList()`, `Find()`, `SaveChanges()`)

**Validation Method**: 
```bash
# Search for blocking patterns
grep -r "\.Result" --include="*.cs"
grep -r "\.Wait\(\)" --include="*.cs"
grep -r "\.ToList\(\)" --include="*.cs"
grep -r "\.Find\(" --include="*.cs"
grep -r "SaveChanges\(\)" --include="*.cs"
```

**Critical Issues**:
- `.Result` or `.Wait()` in controller actions
- Synchronous database calls in repositories
- Blocking calls that could cause deadlocks

**Major Issues**:
- Inconsistent async propagation (some methods async, others sync)
- Missing async suffix on method names

### 2.2 Method Signature Conversion
- [ ] Controller actions return `Task<IActionResult>` or `Task<ActionResult<T>>`
- [ ] Service methods return `Task<T>` or `Task`
- [ ] Repository methods return `Task<T>` or `Task`
- [ ] All async methods have `Async` suffix

**Validation Method**: Review method signatures in controllers, services, repositories
**Critical Issues**:
- Controller actions not async but calling async methods
- Methods using `await` but not marked `async`

**Major Issues**:
- Async methods missing `Async` suffix
- Inconsistent return types (some Task, some not)

### 2.3 Async Flow Propagation
- [ ] `await` used consistently (no forgotten awaits)
- [ ] No `async void` methods (except event handlers)
- [ ] Async propagates from controller → service → repository

**Validation Method**: 
```bash
# Check for async void
grep -r "async void" --include="*.cs"

# Check for missing awaits
dotnet build /p:TreatWarningsAsErrors=true
```

**Critical Issues**:
- `async void` methods (exceptions can't be caught)
- Missing `await` keywords causing fire-and-forget scenarios
- Async methods not awaited in calling code

### 2.4 EF Core Async Usage
- [ ] `ToListAsync()` instead of `ToList()`
- [ ] `FirstOrDefaultAsync()` instead of `FirstOrDefault()`
- [ ] `FindAsync()` instead of `Find()`
- [ ] `SaveChangesAsync()` instead of `SaveChanges()`
- [ ] `AnyAsync()` instead of `Any()`
- [ ] `CountAsync()` instead of `Count()`

**Validation Method**: Review all EF Core query operations
**Critical Issues**:
- Synchronous EF Core methods in async methods
- Database queries blocking threads

## 3. Configuration Externalization
**Validates**: Configuration moved to appsettings.json and Options pattern

### 3.1 ConfigurationManager Elimination
- [ ] No `ConfigurationManager.AppSettings` usage
- [ ] No `ConfigurationManager.ConnectionStrings` usage
- [ ] No `System.Configuration` namespace imports
- [ ] No references to `System.Configuration.dll`

**Validation Method**:
```bash
# Search for ConfigurationManager
grep -r "ConfigurationManager" --include="*.cs"

# Check project references
grep -r "System.Configuration" --include="*.csproj"
```

**Critical Issues**:
- `ConfigurationManager` still used in code
- Configuration values not accessible in .NET 6+ runtime

### 3.2 Hardcoded Value Elimination
- [ ] No hardcoded connection strings in code
- [ ] No hardcoded API URLs
- [ ] No hardcoded file paths
- [ ] No hardcoded credentials
- [ ] No magic strings for roles, statuses, etc.

**Validation Method**: Code review for literal values that should be configurable
**Critical Issues**:
- Hardcoded connection strings
- Hardcoded credentials or secrets

**Major Issues**:
- Hardcoded API URLs
- Hardcoded file paths
- Magic strings for business logic values

### 3.3 appsettings.json Structure
- [ ] `appsettings.json` exists in project root
- [ ] `appsettings.Development.json` exists for dev overrides
- [ ] Connection strings in `ConnectionStrings` section
- [ ] Application settings in logical sections (e.g., `AppSettings`, `EmailSettings`)
- [ ] No sensitive data in appsettings.json (should be in User Secrets or Key Vault)

**Validation Method**: Review appsettings.json files
**Critical Issues**:
- Missing appsettings.json file
- Sensitive data (passwords, keys) in appsettings.json

**Major Issues**:
- No environment-specific overrides
- Flat structure instead of nested sections

### 3.4 Options Pattern Implementation
- [ ] Strongly-typed options classes created for each config section
- [ ] Options classes have `SectionName` constant
- [ ] Options registered in Program.cs using `Configure<T>()`
- [ ] Options injected using `IOptions<T>` in services/controllers
- [ ] No direct `IConfiguration` usage in business logic (use Options instead)

**Validation Method**: Review options classes and Program.cs registration
**Critical Issues**:
- Configuration accessed via `IConfiguration` in business logic
- Options not registered in DI container

**Major Issues**:
- Missing strongly-typed options classes
- Inconsistent options pattern usage

### 3.5 Connection String Access
- [ ] Connection strings accessed via `IConfiguration.GetConnectionString()`
- [ ] DbContext registered with connection string from configuration
- [ ] No connection strings in code

**Validation Method**: Review Program.cs DbContext registration
**Critical Issues**:
- Connection strings not in configuration
- DbContext not using configuration

## 4. Business Logic Modularization
**Validates**: Separation of concerns using layered architecture

### 4.1 Service Layer Existence
- [ ] Service interfaces created (`ICustomerService`, etc.)
- [ ] Service implementations created (`CustomerService`, etc.)
- [ ] Services contain business logic (validation, orchestration)
- [ ] Services do not contain data access code (use repositories)
- [ ] Services do not contain presentation logic (ViewModels OK)

**Validation Method**: Review Services/ folder structure and content
**Critical Issues**:
- Business logic still in controllers
- No service layer created

**Major Issues**:
- Services directly accessing DbContext (bypassing repositories)
- Inconsistent service layer implementation

### 4.2 Repository Layer Existence
- [ ] Repository interfaces created (`ICustomerRepository`, etc.)
- [ ] Repository implementations created (`CustomerRepository`, etc.)
- [ ] Repositories contain data access logic only
- [ ] Repositories use DbContext for data access
- [ ] Repositories return entities (not ViewModels)

**Validation Method**: Review Repositories/ folder structure and content
**Critical Issues**:
- No repository layer created
- Controllers directly accessing DbContext

**Major Issues**:
- Business logic in repositories
- Repositories returning ViewModels

### 4.3 Controller Refactoring
- [ ] Controllers are thin (orchestration only)
- [ ] Controllers inject services (not repositories or DbContext)
- [ ] Controllers handle HTTP concerns (request/response, routing, model binding)
- [ ] Controllers do not contain business logic
- [ ] Controllers do not contain data access logic

**Validation Method**: Review controller implementations
**Critical Issues**:
- Business logic in controller actions
- DbContext injected into controllers
- Repositories injected into controllers (should use services)

**Major Issues**:
- Fat controllers with complex logic
- Validation logic in controllers (should be in ViewModels or services)

### 4.4 Dependency Injection Configuration
- [ ] All services registered in Program.cs
- [ ] All repositories registered in Program.cs
- [ ] Correct lifetimes used (Scoped for DbContext-dependent services)
- [ ] Interfaces registered with implementations
- [ ] No direct instantiation of services/repositories (use DI)

**Validation Method**: Review Program.cs service registration
**Critical Issues**:
- Services/repositories not registered in DI
- Direct instantiation using `new` in controllers/services
- Incorrect lifetimes (Singleton for DbContext-dependent services)

**Major Issues**:
- Inconsistent registration pattern
- Missing interface registrations

### 4.5 Layer Separation Validation
- [ ] Controllers → Services → Repositories → Database
- [ ] No layer bypassing (e.g., Controller → Repository directly)
- [ ] No circular dependencies
- [ ] Clear separation of concerns

**Validation Method**: Review dependency flow and injection
**Critical Issues**:
- Controllers accessing repositories directly
- Controllers accessing DbContext directly
- Circular dependencies between layers

## 5. Deprecated API Replacement
**Validates**: .NET Framework APIs replaced with .NET 6+ equivalents

### 5.1 System.Web Elimination
- [ ] No `using System.Web;` statements
- [ ] No `using System.Web.Mvc;` statements (should be `Microsoft.AspNetCore.Mvc`)
- [ ] No `using System.Web.Http;` statements
- [ ] No `HttpContext.Current` usage
- [ ] No `Server.MapPath` usage
- [ ] No references to `System.Web.dll`

**Validation Method**:
```bash
# Search for System.Web usage
grep -r "using System.Web" --include="*.cs"
grep -r "HttpContext.Current" --include="*.cs"
grep -r "Server.MapPath" --include="*.cs"

# Check project references
grep -r "System.Web" --include="*.csproj"
```

**Critical Issues**:
- `System.Web` references present
- `HttpContext.Current` usage (not available in .NET 6+)
- Code won't compile on .NET 6+

### 5.2 HttpContext Replacement
- [ ] `HttpContext` accessed via controller property (not `HttpContext.Current`)
- [ ] `IHttpContextAccessor` injected in services needing HttpContext
- [ ] `IHttpContextAccessor` registered in Program.cs

**Validation Method**: Review HttpContext access patterns
**Critical Issues**:
- Static `HttpContext.Current` usage
- Services accessing HttpContext without IHttpContextAccessor

**Major Issues**:
- IHttpContextAccessor not registered

### 5.3 HttpClient Replacement
- [ ] No direct `new HttpClient()` instantiation
- [ ] `IHttpClientFactory` injected where HttpClient needed
- [ ] `IHttpClientFactory` registered in Program.cs (`AddHttpClient()`)
- [ ] Named clients configured if needed

**Validation Method**:
```bash
# Search for direct HttpClient instantiation
grep -r "new HttpClient" --include="*.cs"
```

**Critical Issues**:
- Direct `new HttpClient()` usage (socket exhaustion risk)
- HttpClient usage without IHttpClientFactory

**Major Issues**:
- IHttpClientFactory not registered

### 5.4 JSON Serialization Replacement
- [ ] No `JavaScriptSerializer` usage
- [ ] No `using System.Web.Script.Serialization;`
- [ ] `System.Text.Json.JsonSerializer` used for serialization
- [ ] Newtonsoft.Json removed if no longer needed

**Validation Method**:
```bash
# Search for deprecated serializers
grep -r "JavaScriptSerializer" --include="*.cs"
grep -r "System.Web.Script.Serialization" --include="*.cs"
```

**Critical Issues**:
- `JavaScriptSerializer` usage (not available in .NET 6+)

**Major Issues**:
- Newtonsoft.Json used when System.Text.Json would suffice

### 5.5 Entity Framework Core Migration
- [ ] `using Microsoft.EntityFrameworkCore;` instead of `using System.Data.Entity;`
- [ ] DbContext constructor accepts `DbContextOptions<T>`
- [ ] Async methods used (`ToListAsync`, `FindAsync`, etc.)
- [ ] No `Database.SetInitializer` usage
- [ ] Connection string from configuration

**Validation Method**: Review DbContext implementation
**Critical Issues**:
- Entity Framework 6 references still present
- Synchronous EF methods used

**Major Issues**:
- DbContext not using options pattern
- Missing async methods

### 5.6 File Path Replacement
- [ ] No `Server.MapPath` usage
- [ ] `IWebHostEnvironment` injected where file paths needed
- [ ] `IWebHostEnvironment.WebRootPath` or `IWebHostEnvironment.ContentRootPath` used

**Validation Method**:
```bash
# Search for Server.MapPath
grep -r "Server.MapPath" --include="*.cs"
```

**Critical Issues**:
- `Server.MapPath` usage (not available in .NET 6+)

**Major Issues**:
- Hardcoded file paths instead of using IWebHostEnvironment

### 5.7 Authentication Replacement
- [ ] No `FormsAuthentication` usage
- [ ] ASP.NET Core Authentication middleware configured
- [ ] `HttpContext.SignInAsync` used for login
- [ ] `HttpContext.SignOutAsync` used for logout
- [ ] Authentication scheme registered in Program.cs

**Validation Method**: Review authentication implementation
**Critical Issues**:
- `FormsAuthentication` usage (not available in .NET 6+)

**Major Issues**:
- No ASP.NET Core authentication configured
- Authentication not using middleware

## 6. Project Structure & Configuration
**Validates**: Project correctly configured for .NET 6+

### 6.1 Project File (.csproj)
- [ ] SDK-style project format (`<Project Sdk="Microsoft.NET.Sdk.Web">`)
- [ ] Target framework is `net6.0` or higher
- [ ] No legacy `<Reference>` elements (should use `<PackageReference>`)
- [ ] No `<Compile Include>` elements (implicit compilation)
- [ ] Nullable reference types enabled (`<Nullable>enable</Nullable>`)

**Validation Method**: Review .csproj file
**Critical Issues**:
- Old-style project format
- Target framework not .NET 6+
- Won't compile on .NET 6+

**Major Issues**:
- Unnecessary explicit file inclusions
- Nullable not enabled

### 6.2 NuGet Packages
- [ ] All packages compatible with .NET 6+
- [ ] No .NET Framework-specific packages
- [ ] Entity Framework Core packages (not EF6)
- [ ] ASP.NET Core packages (not System.Web)
- [ ] Package versions compatible with each other

**Validation Method**: Review package references in .csproj
**Critical Issues**:
- .NET Framework-only packages
- Incompatible package versions

### 6.3 Program.cs Structure
- [ ] Top-level statements used (or explicit Program class)
- [ ] `WebApplication.CreateBuilder` used
- [ ] Services registered before `builder.Build()`
- [ ] Middleware configured after `app` created
- [ ] Middleware in correct order (exception handling, HTTPS, static files, routing, auth, endpoints)

**Validation Method**: Review Program.cs
**Critical Issues**:
- Incorrect middleware order
- Services not registered
- Application won't run correctly

### 6.4 Startup.cs (if exists)
- [ ] If Startup.cs exists, it follows .NET 6+ conventions
- [ ] `ConfigureServices` method signature correct
- [ ] `Configure` method signature correct
- [ ] Consider migrating to Program.cs top-level statements

**Validation Method**: Review Startup.cs
**Major Issues**:
- Old .NET Core 3.1 patterns used
- Should consolidate into Program.cs

## 7. Testing Validation
**Validates**: Tests updated and passing

### 7.1 Test Project Configuration
- [ ] Test project targets .NET 6+
- [ ] xUnit, NUnit, or MSTest framework used
- [ ] Moq or NSubstitute for mocking
- [ ] All tests compile

**Validation Method**: Review test project .csproj and build output
**Critical Issues**:
- Tests don't compile
- Test framework not compatible with .NET 6+

### 7.2 Async Test Methods
- [ ] Test methods for async code are async
- [ ] Tests use `await` when calling async methods
- [ ] No `.Result` or `.Wait()` in tests

**Validation Method**: Review test method signatures and implementations
**Critical Issues**:
- Async methods called synchronously in tests (can cause deadlocks)

**Major Issues**:
- Inconsistent async test patterns

### 7.3 Test Coverage
- [ ] Service layer: 100% method coverage
- [ ] Repository layer: 90%+ coverage
- [ ] Controller layer: 80%+ coverage
- [ ] Overall: 90%+ coverage

**Validation Method**: Run test coverage tool
```bash
dotnet test /p:CollectCoverage=true /p:CoverletOutputFormat=cobertura
```

**Critical Issues**:
- No tests for critical business logic
- Coverage below 70%

**Major Issues**:
- Coverage below 90%
- Key scenarios not tested

### 7.4 Test Execution
- [ ] All tests pass: `dotnet test`
- [ ] No flaky tests (tests pass consistently)
- [ ] Tests run in reasonable time (<5 min for small projects)

**Validation Method**: Run tests multiple times
**Critical Issues**:
- Any tests failing
- Tests won't run

**Major Issues**:
- Flaky tests
- Very slow tests

## 8. Build & Runtime Validation
**Validates**: Application builds and runs on .NET 6+

### 8.1 Build Success
- [ ] `dotnet build` succeeds with 0 errors
- [ ] No warnings related to modernization issues
- [ ] Release build succeeds: `dotnet build -c Release`

**Validation Method**:
```bash
dotnet clean
dotnet build
dotnet build -c Release
```

**Critical Issues**:
- Build fails
- Errors related to deprecated APIs

**Major Issues**:
- Warnings about obsolete APIs
- Warnings about nullability

### 8.2 Runtime Execution
- [ ] Application starts without errors
- [ ] No runtime exceptions during startup
- [ ] Dependency injection resolves all dependencies
- [ ] Database connection successful (if applicable)

**Validation Method**: Run application and check startup logs
**Critical Issues**:
- Application crashes on startup
- DI configuration errors
- Database connection failures

### 8.3 Functional Equivalence
- [ ] All features from .NET Framework version work identically
- [ ] No behavior changes observed
- [ ] Performance is equivalent or better
- [ ] Manual testing confirms functionality

**Validation Method**: Manual testing of key scenarios
**Critical Issues**:
- Features broken or behaving differently
- Data loss or corruption
- Critical functionality not working

## 9. Documentation & Tracking
**Validates**: Changes documented and tracked

### 9.1 Migration Plan Updates
- [ ] All tasks marked "Completed" in migration plan
- [ ] Task completion aligns with actual code changes
- [ ] No tasks skipped without documentation

**Validation Method**: Review Migration/01-Migration_Plan.md
**Critical Issues**:
- Tasks marked complete but not actually done
- Plan not updated

**Major Issues**:
- Incomplete task status tracking

### 9.2 Migration Summary
- [ ] Migration summary document created (`Migration/02-Migration_Summary.md`)
- [ ] All modernizations documented with file counts
- [ ] Manual follow-ups documented
- [ ] Quality metrics included (build status, test coverage)

**Validation Method**: Review Migration/02-Migration_Summary.md
**Major Issues**:
- Missing migration summary
- Incomplete documentation

### 9.3 Code Comments
- [ ] TODO comments for areas needing manual review
- [ ] Complex modernizations explained with comments
- [ ] No debug comments or commented-out code

**Validation Method**: Search for TODO, FIXME, HACK comments
**Minor Issues**:
- Excessive comments
- Debug code left in

## 10. Security & Best Practices
**Validates**: Security and coding standards maintained

### 10.1 Security Considerations
- [ ] No secrets in appsettings.json
- [ ] Sensitive data in User Secrets (dev) or Key Vault (prod)
- [ ] HTTPS enforced
- [ ] Anti-forgery tokens used in forms
- [ ] Input validation present

**Validation Method**: Security-focused code review
**Critical Issues**:
- Secrets in source code or config
- No HTTPS enforcement

**Major Issues**:
- Missing input validation
- Missing anti-forgery tokens

### 10.2 Error Handling
- [ ] Try-catch blocks in appropriate places
- [ ] Exceptions logged with ILogger
- [ ] User-friendly error messages
- [ ] Global exception handler configured

**Validation Method**: Review error handling patterns
**Major Issues**:
- No error handling
- Exceptions not logged

### 10.3 Logging
- [ ] ILogger injected and used
- [ ] Appropriate log levels (Information, Warning, Error)
- [ ] Sensitive data not logged
- [ ] Logging configured in Program.cs

**Validation Method**: Review logging usage
**Major Issues**:
- No logging
- Sensitive data logged

## Review Summary Template

After completing the review, document findings:

```markdown
# Modernization Review Summary

## Overall Assessment
- Build Status: ✅ Pass / ❌ Fail
- Tests Status: ✅ All Passing / ❌ Failures
- Coverage: [X]%
- Scope Compliance: ✅ Compliant / ❌ Out of Scope Work Found

## Critical Issues (Must Fix)
1. [Description] - [Location]
2. ...

## Major Issues (Should Fix)
1. [Description] - [Location]
2. ...

## Minor Issues (Optional)
1. [Description] - [Location]
2. ...

## Commendations
1. [Well-executed aspect]
2. ...

## Recommendations
1. [Improvement suggestion]
2. ...
```

---

## Best Practices for Reviewers

1. **Use automation**: Run build, tests, and search commands first
2. **Follow checklist systematically**: Don't skip sections
3. **Prioritize issues**: Critical > Major > Minor
4. **Provide examples**: Show how to fix issues
5. **Be constructive**: Focus on solutions, not just problems
6. **Verify scope**: Ensure only authorized modernizations performed
7. **Test manually**: Don't rely only on automated tests
8. **Document thoroughly**: Clear, actionable feedback

---

## End of Review Instructions

Use this checklist to thoroughly review .NET Framework to .NET 6+ modernizations before marking them complete.
