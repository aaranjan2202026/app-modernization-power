---
inclusion: auto
---

# .NET Migration Rules

## Phase 1 first: is there anything to migrate?

Do **not** assume a project is on .NET Framework because someone said "migrate .NET". Verify before planning.

```powershell
# 1. target framework of every project
Get-ChildItem -Recurse -Filter *.csproj |
  ForEach-Object { "$($_.Name): " + (Select-String $_.FullName -Pattern '<TargetFramework[^>]*>([^<]+)<').Matches.Groups[1].Value }

# 2. legacy pattern census - all must be 0 for "already modern"
System.Web · ConfigurationManager · System.Data.Entity · JavaScriptSerializer · new HttpClient(
```

### Decision

| Finding | Track |
|---|---|
| `TargetFrameworkVersion` / `v4.x` present | **Framework migration** — apply the mapping table below |
| Already `net6.0`+ **and** zero legacy patterns | **Already modern** — switch to the remediation track below |
| Mixed | Migrate the Framework projects; remediate the rest |

Running a Framework→8 migration on an already-modern codebase is theatre. It produces churn, risk, and no benefit. Say so and redirect.

**Verified example:** a "migrate .NET" request on a solution where all three projects were already `net8.0` with zero legacy patterns across 96 files. The real defects were a broken solution file, a vulnerable unused package, and a silently swallowed exception — none of which a Framework migration would have touched.

---

## Already-modern remediation track

When the framework is current, the valuable work is different. Run these in order; each is build-gated.

### 1. Does the solution actually build?

Build the **solution**, not just the projects. They fail differently.

```
error MSB4025: The project file could not be loaded. Could not find a part of the path
```

A `.sln` referencing a deleted project fails the solution build while every real project still builds individually. Easy to miss and it blocks CI.

Two fixes — prefer the second when the project *should* exist:

| Fix | When |
|---|---|
| Remove the `Project(...)`/`EndProject` block, its `ProjectConfigurationPlatforms` lines, and its `NestedProjects` entry | The project is genuinely gone |
| **Create the project at the referenced path** | A test project is missing and coverage is absent anyway |

The second requires no `.sln` edit — the path and GUID already match — and fixes two problems at once.

### 2. Vulnerable dependencies — check usage before upgrading

`NU1903` / `NU1901`-`NU1904` flag known advisories.

**Check whether the package is used at all before planning an upgrade.** Upgrading across major versions of a mapping or serialization library is high-risk, especially with no tests. Removal is risk-free by comparison.

```powershell
# for a package like AutoMapper, look for real usage
IMapper · CreateMap · Mapper\. · : Profile
```

| Usage found | Action |
|---|---|
| None | **Remove** the package, its registration, and any empty marker classes |
| Some | Upgrade deliberately; check the major-version breaking changes first |

**Verified example:** AutoMapper 9.0.0 carried a high-severity advisory. Its only trace was an empty `Profile` subclass with no `CreateMap` calls and zero `IMapper` injections. Deleting two `PackageReference` lines, one `AddAutoMapper` call, and the empty profile cleared the advisory with zero API risk. An upgrade would have been strictly worse.

### 3. Treat warnings as defect leads, not noise

Some warnings mark real bugs.

| Warning | Often means |
|---|---|
| `CS0168` unused exception variable | **A catch block silently swallowing the exception** |
| `CS0618` obsolete API | Removal in a future version |
| `ASP0000` `BuildServiceProvider` in `ConfigureServices` | Throwaway container, duplicated singletons |
| `CS8981` lowercase type name | May become a reserved word |

`CS0168` deserves particular attention. `catch (Exception ex) { return Redirect(...); }` discards the cause of a failure entirely. The fix is to inject a logger and log it — not to delete the variable, which hides the bug and keeps the behaviour.

**Do not rename EF-generated migration classes** to clear `CS8981`. Migration names are tracked in `__EFMigrationsHistory`; renaming breaks migration state. Leave them and document.

### 4. No test project? Create one.

Zero coverage means no safety net for any subsequent change. Start with pure logic that needs no database — specifications, validators, calculators, mappers.

Specification classes are ideal: compile the `Criteria` expression and evaluate it against in-memory objects.

```csharp
var spec = new MedicalItemsPaginatedSpecification(0, 10, categId: 100, firmId: null);
var predicate = spec.Criteria.Compile();
var matched = Sample.Where(predicate).Select(i => i.MedItemId).ToArray();
Assert.Equal([1, 2], matched);
```

Avoid `Assert.Empty(collection.Where(p))` — the xUnit analyzer flags it (`xUnit2029`). Use `Assert.DoesNotContain(collection, p)`.

### 5. Hosting pattern

`Host.CreateDefaultBuilder(...).UseStartup<Startup>()` predates .NET 6. Consolidating into `WebApplication.CreateBuilder` is a genuine .NET 8 idiom update. See the template below.

Preserve exactly: every service registration, **middleware order**, and any startup seeding. Removing `Startup.cs` breaks `typeof(Startup)` assembly-marker arguments — repoint them at `typeof(Program)`.

Carry the `using` directives across. `Startup.cs` typically has `Microsoft.Extensions.Configuration` (for `GetConnectionString`) plus one per DI-extension package (`AutoMapper`, `MediatR`); omitting them yields `CS1501`/`CS1061` that look like version problems but are missing imports.

---

## Framework → .NET 8 mapping

Only relevant when actual Framework code is present.

| Legacy | Modern |
|---|---|
| `System.Web.HttpContext.Current` | Inject `IHttpContextAccessor` |
| `System.Web.Mvc.Controller` | `Microsoft.AspNetCore.Mvc.Controller` |
| `System.Web.Http.ApiController` | `ControllerBase` or Minimal APIs |
| `ConfigurationManager.AppSettings` | `IConfiguration` / `IOptions<T>` |
| `ConfigurationManager.ConnectionStrings` | `Configuration.GetConnectionString()` |
| `Server.MapPath("~/")` | `IWebHostEnvironment.WebRootPath` |
| `FormsAuthentication` | ASP.NET Core Authentication middleware |
| `JavaScriptSerializer` | `System.Text.Json.JsonSerializer` |
| `new HttpClient()` | `IHttpClientFactory.CreateClient()` |
| `System.Data.Entity` (EF6) | `Microsoft.EntityFrameworkCore` |
| `Global.asax` | Middleware in `Program.cs` |
| `Web.config` | `appsettings.json` + environment variables |
| `BundleConfig` | Modern bundler or plain tags |
| `RouteConfig` | `MapControllerRoute` |
| ViewState | no equivalent — redesign required |

### Project file

```xml
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net8.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
  </PropertyGroup>
</Project>
```

### Synchronous → async

```csharp
.ToList()        -> await .ToListAsync()
.Find(id)        -> await .FindAsync(id)
.FirstOrDefault()-> await .FirstOrDefaultAsync()
.SaveChanges()   -> await .SaveChangesAsync()
```

**Only for database queries.** `.ToList()`, `.Find()`, and `.Any()` on an
already-materialized in-memory collection are not database calls and must be
left alone:

```csharp
var all = await _repo.GetAllAsync();        // DB call - already async
var one = all.Find(x => x.Id == id);        // in-memory - correct as-is
```

Converting the second is wrong. Check what the receiver is before rewriting.

---

## Program.cs template (minimal hosting)

```csharp
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllersWithViews();
builder.Services.AddRazorPages();
builder.Services.AddDbContext<AppDbContext>(o =>
    o.UseSqlServer(builder.Configuration.GetConnectionString("Default")));
builder.Services.AddDatabaseDeveloperPageExceptionFilter();  // not UseDatabaseErrorPage
builder.Services.AddScoped<IThingService, ThingService>();
builder.Services.AddHttpClient();

var app = builder.Build();

// seeding goes here, after Build, before the pipeline

if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();
}
else
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseSession();          // only if AddSession was called
app.UseAuthentication();
app.UseAuthorization();
app.MapControllerRoute(name: "default", pattern: "{controller=Home}/{action=Index}/{id?}");
app.MapRazorPages();

await app.RunAsync();
```

Middleware order is not stylistic. Routing before auth, auth before endpoints. Getting it wrong produces 401s or silently unauthenticated requests.

### Replacing `BuildServiceProvider` guards

```csharp
// WRONG - builds a throwaway container, duplicates singletons (ASP0000)
var sp = services.BuildServiceProvider();
if (sp.GetService<UserManager<AppUser>>() == null) { ... }

// RIGHT - inspect the collection
if (!services.Any(s => s.ServiceType == typeof(UserManager<AppUser>))) { ... }
```

---

## Modern C# to apply

| Feature | Since |
|---|---|
| Records, target-typed `new` | C# 9 |
| Global usings, file-scoped namespaces | C# 10 |
| Raw string literals, required members | C# 11 |
| Primary constructors, collection expressions | C# 12 |

Nullable reference types: enable per project and fix warnings incrementally. Turning it on across a large solution at once produces hundreds of warnings and tempts blanket `!` suppression, which is worse than leaving it off.

---

## Build command note

`dotnet` writes progress to stderr, and PowerShell treats any stderr output as
failure — a successful build can report exit code 1. Judge by the
`Build succeeded` / `Build FAILED` line, not the exit code:

```powershell
cmd /c ".\.tools\dotnet\dotnet.exe build MySolution.sln --nologo 2>&1"
```

Use `--no-incremental` for a true warning count; incremental builds omit
warnings from projects they skip.
