---
inclusion: auto
---

# .NET Legacy → .NET 8+ Migration — Full Automated Workflow

## When This Applies

This workflow executes automatically when the user says:
- "migrate .net"
- "modernize .net"
- "upgrade .net"
- ".net migration"
- "migrate dotnet"
- ".net framework to .net 8"

## Project Context

- **Application**: Pharmacy Network (`pharmacy/PharmacyNetwork.sln`)
- **Source**: .NET Framework / Legacy ASP.NET (MVC, Web API, Entity Framework 6)
- **Target**: .NET 8+ with ASP.NET Core Minimal APIs, EF Core, async/await
- **Build**: `dotnet build`
- **Test**: `dotnet test`
- **SonarQube Project**: `Refactoring-legacy-DotNet-uc2`
- **SonarQube Server**: `https://sonarqube-hub.azurewebsites.net`

---

## EXECUTION MODE: FULLY AUTONOMOUS — NO STOPPING

Execute ALL 7 phases continuously. NEVER ask "Would you like me to continue?" between phases.

---

## PHASE 1: Assessment & Baseline

**Agent Role: Assessor**

1. Scan workspace: find `*.sln`, `*.csproj` files
2. Identify current target framework (`<TargetFramework>` or `<TargetFrameworkVersion>`)
3. Inventory all source files — count controllers, services, repositories, models
4. Identify legacy patterns:
   - `System.Web` references
   - `ConfigurationManager` usage
   - Entity Framework 6 (not EF Core)
   - Synchronous database calls (`.ToList()`, `.Find()`, `.SaveChanges()`)
   - `HttpContext.Current` (static access)
   - `FormsAuthentication`
   - `JavaScriptSerializer`
   - Manual `new HttpClient()` instantiation
   - Business logic in controllers
   - Hardcoded connection strings
5. Connect to SonarQube MCP → fetch issues for `Refactoring-legacy-DotNet-uc2`
6. Capture baseline metrics

**Output**: `Migration/00-DotNet-Assessment-Report.md`

**Gate**: Assessment complete → proceed immediately.

---

## PHASE 2: Upgrade Project Files

**Agent Role: Build Migrator**

1. Convert to SDK-style `.csproj` format:
   ```xml
   <Project Sdk="Microsoft.NET.Sdk.Web">
     <PropertyGroup>
       <TargetFramework>net8.0</TargetFramework>
       <Nullable>enable</Nullable>
       <ImplicitUsings>enable</ImplicitUsings>
     </PropertyGroup>
   </Project>
   ```
2. Replace .NET Framework `<Reference>` elements with `<PackageReference>`
3. Remove `System.Web`, `System.Configuration` package references
4. Add .NET 8 packages:
   - `Microsoft.EntityFrameworkCore.SqlServer`
   - `Microsoft.AspNetCore.Diagnostics.EntityFrameworkCore`
   - `Microsoft.Extensions.Logging`
5. Remove incompatible packages (EF6, System.Web.Mvc, etc.)
6. Run: `dotnet build`
7. Fix ALL build errors

**Gate**: `dotnet build` succeeds with 0 errors → proceed.

---

## PHASE 3: Architecture Modernization

**Agent Role: Refactoring Developer**

### 3.1 Replace Startup with Program.cs (Minimal API style)
```csharp
var builder = WebApplication.CreateBuilder(args);

// Register services
builder.Services.AddControllersWithViews();
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));
builder.Services.AddScoped<IPharmacyService, PharmacyService>();

var app = builder.Build();

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();
app.MapControllerRoute(name: "default", pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
```

### 3.2 Replace System.Web with ASP.NET Core
| Legacy | Modern Replacement |
|--------|-------------------|
| `System.Web.HttpContext.Current` | Inject `IHttpContextAccessor` |
| `Server.MapPath("~/")` | Inject `IWebHostEnvironment`, use `.WebRootPath` |
| `ConfigurationManager.AppSettings` | `IConfiguration` / `IOptions<T>` |
| `FormsAuthentication` | ASP.NET Core Authentication middleware |
| `System.Web.Mvc.Controller` | `Microsoft.AspNetCore.Mvc.Controller` |

### 3.3 Add Service Layer
- Extract business logic from controllers into service interfaces + implementations
- Register with DI: `builder.Services.AddScoped<IService, ServiceImpl>()`
- Controllers become thin — delegate to services

### 3.4 Replace Entity Framework 6 with EF Core
```csharp
// EF Core DbContext
public class ApplicationDbContext : DbContext
{
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
        : base(options) { }

    public DbSet<Pharmacy> Pharmacies { get; set; }
    public DbSet<Product> Products { get; set; }
}
```

### 3.5 Convert to Async/Await
Replace ALL synchronous data access:
```csharp
// BEFORE: var items = _context.Products.ToList();
// AFTER:  var items = await _context.Products.ToListAsync();

// BEFORE: _context.SaveChanges();
// AFTER:  await _context.SaveChangesAsync();

// BEFORE: var item = _context.Products.Find(id);
// AFTER:  var item = await _context.Products.FindAsync(id);
```

Controller actions become:
```csharp
public async Task<IActionResult> Index()
{
    var products = await _productService.GetAllAsync();
    return View(products);
}
```

**Gate**: `dotnet build` passes → proceed.

---

## PHASE 4: Apply Modern .NET 8 Features

**Agent Role: .NET 8 Modernizer**

### 4.1 Records for DTOs
```csharp
public record ProductDTO(int Id, string Name, decimal Price, string Category);
public record CreateProductRequest(string Name, decimal Price, int CategoryId);
```

### 4.2 Pattern Matching
```csharp
var result = statusCode switch
{
    >= 200 and < 300 => "Success",
    >= 400 and < 500 => "Client Error",
    >= 500 => "Server Error",
    _ => "Unknown"
};
```

### 4.3 Nullable Reference Types
- Enable `<Nullable>enable</Nullable>` in .csproj
- Add `?` to nullable properties
- Add null checks where needed

### 4.4 Collection Expressions (C# 12)
```csharp
List<string> categories = ["Medicine", "Equipment", "Supplements"];
```

### 4.5 Primary Constructors (C# 12)
```csharp
public class PharmacyService(IPharmacyRepository repository, ILogger<PharmacyService> logger)
    : IPharmacyService
{
    public async Task<IEnumerable<Pharmacy>> GetAllAsync()
        => await repository.GetAllAsync();
}
```

### 4.6 Raw String Literals
```csharp
var sql = """
    SELECT p.Id, p.Name, p.Price
    FROM Products p
    WHERE p.CategoryId = @categoryId
    ORDER BY p.Name
    """;
```

### 4.7 IHttpClientFactory
```csharp
// Register: builder.Services.AddHttpClient();
// Inject:
public class ExternalApiService(IHttpClientFactory clientFactory)
{
    public async Task<string> GetDataAsync(string url)
    {
        var client = clientFactory.CreateClient();
        return await client.GetStringAsync(url);
    }
}
```

### 4.8 Options Pattern for Configuration
```csharp
public record PharmacySettings
{
    public string ConnectionString { get; init; } = "";
    public int MaxItemsPerPage { get; init; } = 50;
}

// Register: builder.Services.Configure<PharmacySettings>(builder.Configuration.GetSection("Pharmacy"));
// Inject: IOptions<PharmacySettings> settings
```

**Gate**: `dotnet build` passes → proceed.

---

## PHASE 5: Configuration Modernization

**Agent Role: Config Migrator**

### 5.1 Create appsettings.json
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Database=PharmacyNetwork;Trusted_Connection=true"
  },
  "Pharmacy": {
    "MaxItemsPerPage": 50,
    "EnableAuditLog": true
  },
  "Logging": {
    "LogLevel": {
      "Default": "Information",
      "Microsoft.AspNetCore": "Warning"
    }
  }
}
```

### 5.2 Create appsettings.Development.json
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Database=PharmacyNetwork_Dev;Trusted_Connection=true"
  },
  "Logging": {
    "LogLevel": {
      "Default": "Debug"
    }
  }
}
```

### 5.3 Externalize secrets
Use environment variables for production:
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "${DB_CONNECTION_STRING}"
  }
}
```

**Gate**: Application starts: `dotnet run`

---

## PHASE 6: Testing & Validation

**Agent Role: Validator**

1. Update/create test project targeting .NET 8
2. Add unit tests for service layer (xUnit + Moq)
3. Run: `dotnet test`
4. Run: `dotnet build --configuration Release`
5. Verify: 0 failures, 0 errors

**Gate**: Build + tests pass → proceed.

---

## PHASE 7: SonarQube Scan & Final Report

**Agent Role: Quality Gate**

1. Connect to SonarQube MCP → fetch issues for `Refactoring-legacy-DotNet-uc2`
2. Fix any NEW issues introduced during migration
3. Verify quality gate passes
4. Generate final report

**Output**: `Migration/DotNet8-Migration-Summary.md`

---

## Completion Criteria (ALL must be TRUE)

- [ ] `.csproj` targets `net8.0`
- [ ] `dotnet build` succeeds with 0 errors
- [ ] All tests pass (`dotnet test` — 0 failures)
- [ ] No `System.Web` references remain
- [ ] No `ConfigurationManager` usage
- [ ] Entity Framework Core replaces EF6
- [ ] All data access is async (`ToListAsync`, `SaveChangesAsync`)
- [ ] Service layer exists between controllers and repositories
- [ ] Records used for DTOs
- [ ] `appsettings.json` with environment-specific overrides
- [ ] Options pattern for configuration
- [ ] `IHttpClientFactory` replaces `new HttpClient()`
- [ ] Nullable reference types enabled
- [ ] SonarQube quality gate passes (or documented as pending)
- [ ] `Migration/DotNet8-Migration-Summary.md` generated

---

## Error Recovery

- Build fails → fix errors immediately, re-run, continue
- Tests fail → fix migration-caused failures, document pre-existing ones
- SonarQube unreachable → skip Phase 7, document as pending
- Never stop for non-critical issues — document and continue
