# .NET Modernization Summary — Pharmacy

**Solution:** `pharmacy/PharmacyNetwork.sln` · **Branch:** `feature/hospital-java-modernization` · **SDK:** 8.0.423

---

## Correction to the stated scope

The request was to migrate .NET. **All three projects already targeted `net8.0`.**

| Project | Target |
|---|---|
| `ApplicationCore.csproj` | `net8.0` |
| `Infrastructure.csproj` | `net8.0` |
| `Web.csproj` | `net8.0` |

I scanned all 96 `.cs` files for every legacy pattern the plan targets:

| Pattern | Occurrences |
|---|---|
| `System.Web` | 0 |
| `ConfigurationManager` | 0 |
| `System.Data.Entity` (EF6) | 0 |
| `JavaScriptSerializer` | 0 |
| `new HttpClient()` | 0 |

Already EF Core, async repositories, DI, `appsettings.json`, Options pattern. A Framework→8 migration had nothing to migrate, so Phases 2–4 were redirected at defects the assessment actually found.

The `.ToList()`/`.Find()`/`.Any()` matches were in-memory LINQ on already-materialized collections — converting those to async would have been wrong.

## Outcome

| | Before | After |
|---|---|---|
| Solution build | **FAILED** (MSB4025) | **SUCCESS**, 0 errors |
| Warnings | 6 | 2 |
| Known vulnerabilities | **1 high** | **0** |
| Tests | **none** | **7 passing** |
| Test project | missing, sln referenced it | created |
| Hosting | `Startup.cs` + `UseStartup<>` | minimal hosting `Program.cs` |

## Phase Results

| Phase | Gate | Result |
|---|---|---|
| 1 Assessment | report | pass |
| 2 Build config | solution builds | pass |
| 3 Hosting modernization | build | pass |
| 4 Language features / warnings | build | pass |
| 5 Configuration | — | skipped, already modern |
| 6 Testing | 0 failures | pass |
| 7 Quality gate | vulnerabilities | pass (local) |

## Changes

### Phase 2 — the build was broken before I touched it

```
pharmacy\tests\UnitTests\UnitTests.csproj : error MSB4025:
The project file could not be loaded.
```

`PharmacyNetwork.sln` referenced a test project that did not exist; `pharmacy/tests/` was absent entirely. The three real projects built fine individually, so only the solution was broken.

Rather than delete the reference, I **created the project at the path and GUID the solution already expected**. That fixed the build without editing the solution file, and gave the missing test suite a home.

### Phase 3 — hosting consolidated

`Program.cs` used the pre-.NET 6 `Host.CreateDefaultBuilder(...).UseStartup<Startup>()` pattern. Consolidated into minimal hosting `WebApplication.CreateBuilder`, preserving every service registration, the middleware order, and the identity seeding. `Startup.cs` deleted.

Also removed a `services.BuildServiceProvider()` call inside the identity guard. That built a throwaway container and duplicated singletons (ASP0000). Replaced with a direct service-collection inspection, preserving the guard's semantics.

### Phase 4 — warnings cleared

| Warning | Fix |
|---|---|
| `CS0618` — `UseDatabaseErrorPage` obsolete | Replaced with `AddDatabaseDeveloperPageExceptionFilter()` |
| `CS0168` — unused `ex` in `IncomesController` | Injected `ILogger<T>` and actually logged it |

The `CS0168` site was worse than a style warning: the catch block **swallowed the exception silently**, hiding the cause of failed income creation. It now logs the error.

### Phase 7 — vulnerability removed, not upgraded

`AutoMapper` 9.0.0 carried a known high-severity advisory ([GHSA-rvv3-g6hj-g44x](https://github.com/advisories/GHSA-rvv3-g6hj-g44x)).

An upgrade looked risky — AutoMapper has breaking API changes after v9, and there was no test coverage to catch regressions. So I checked usage first:

- `AutoMapping.cs` was an **empty `Profile`** with an empty constructor and no `CreateMap` calls
- **Zero** `IMapper` injections anywhere in 96 files
- **Zero** `CreateMap` or static `Mapper.` usage

AutoMapper was a dead dependency. Removed both packages, the registration, and the empty profile. The vulnerability is gone with no API risk — a far better outcome than an upgrade.

### Phase 6 — tests from zero

Created `pharmacy/tests/UnitTests` (xUnit 2.9.2, net8.0) with 7 tests covering `MedicalItemsPaginatedSpecification`: paging state, and the filter expression across no-filter, category-only, firm-only, combined, and no-match cases. The `Criteria` expression is compiled and evaluated in memory, so no database is required.

## Outstanding

**`CS8981` ×2 — deliberately not fixed.** EF-generated migration classes named `sample` (lowercase names may become reserved). Renaming a migration class risks breaking EF's `__EFMigrationsHistory` tracking. Rename only alongside a migration-history plan.

**Coverage is narrow.** 7 tests on `ApplicationCore` specifications. Controllers, MediatR handlers, and repositories remain untested — the specifications were chosen because they are pure logic and testable without infrastructure.

**Old but not vulnerable:** Swashbuckle 5.4.0, MediatR 8.0.1. No advisories; upgrading is optional and carries API churn.

**SonarQube not run** against this branch. Project key `Refactoring-legacy-DotNet-uc2` exists on the server, but the last analysis predates this work. Same indeterminate state as the Java track.

**Uncommitted.** All changes are in the working tree.

## Verification

```powershell
cmd /c ".\.tools\dotnet\dotnet.exe build pharmacy\PharmacyNetwork.sln --nologo 2>&1"
cmd /c ".\.tools\dotnet\dotnet.exe test  pharmacy\PharmacyNetwork.sln --nologo 2>&1"
```

Result: 0 errors, 2 warnings (both `CS8981`), 7/7 tests passing.
