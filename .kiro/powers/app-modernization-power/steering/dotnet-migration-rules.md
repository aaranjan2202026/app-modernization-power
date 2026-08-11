# .NET Migration Rules — Framework → .NET 8

## API Replacements

| Legacy (.NET Framework) | Modern (.NET 8+) |
|------------------------|-------------------|
| `System.Web.HttpContext.Current` | Inject `IHttpContextAccessor` |
| `System.Web.Mvc.Controller` | `Microsoft.AspNetCore.Mvc.Controller` |
| `System.Web.Http.ApiController` | `ControllerBase` or Minimal APIs |
| `ConfigurationManager.AppSettings["key"]` | `IConfiguration` / `IOptions<T>` |
| `ConfigurationManager.ConnectionStrings` | `builder.Configuration.GetConnectionString()` |
| `Server.MapPath("~/")` | `IWebHostEnvironment.WebRootPath` |
| `FormsAuthentication` | ASP.NET Core Authentication middleware |
| `JavaScriptSerializer` | `System.Text.Json.JsonSerializer` |
| `new HttpClient()` | `IHttpClientFactory.CreateClient()` |
| `HttpContext.Current.Session` | `IHttpContextAccessor` + `.HttpContext.Session` |
| `System.Data.Entity` (EF6) | `Microsoft.EntityFrameworkCore` |
| `.ToList()` (sync EF) | `.ToListAsync()` |
| `.Find(id)` | `.FindAsync(id)` |
| `.SaveChanges()` | `.SaveChangesAsync()` |
| `WebMvcConfigurerAdapter` | `implements WebMvcConfigurer` |

---

## Project File Migration

### From (old-style .csproj)
```xml
<?xml version="1.0" encoding="utf-8"?>
<Project ToolsVersion="15.0" DefaultTargets="Build">
  <PropertyGroup>
    <TargetFrameworkVersion>v4.7.2</TargetFrameworkVersion>
  </PropertyGroup>
  <ItemGroup>
    <Reference Include="System.Web" />
    <Reference Include="System.Web.Mvc, Version=5.2.7.0" />
  </ItemGroup>
</Project>
```

### To (SDK-style .csproj)
```xml
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net8.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
  </PropertyGroup>
  <ItemGroup>
    <PackageReference Include="Microsoft.EntityFrameworkCore.SqlServer" Version="8.0.*" />
    <PackageReference Include="Microsoft.AspNetCore.Diagnostics.EntityFrameworkCore" Version="8.0.*" />
  </ItemGroup>
</Project>
```

---

## .NET 8 / C# 12 Features to Apply

### Records
```csharp
public record ProductDTO(int Id, string Name, decimal Price, string Category);
public record CreateRequest(string Name, decimal Price, int CategoryId);
```

### Primary Constructors (C# 12)
```csharp
public class ProductService(IProductRepository repository, ILogger<ProductService> logger)
    : IProductService
{
    public async Task<IEnumerable<Product>> GetAllAsync()
        => await repository.GetAllAsync();
}
```

### Collection Expressions (C# 12)
```csharp
List<string> categories = ["Medicine", "Equipment", "Supplements"];
int[] ids = [1, 2, 3, 4, 5];
```

### Raw String Literals (C# 11)
```csharp
var sql = """
    SELECT p.Id, p.Name, p.Price
    FROM Products p
    WHERE p.CategoryId = @categoryId
    """;
```

### Pattern Matching
```csharp
var message = statusCode switch
{
    >= 200 and < 300 => "Success",
    >= 400 and < 500 => "Client Error",
    >= 500 => "Server Error",
    _ => "Unknown"
};
```

### Nullable Reference Types
```csharp
// Enable in .csproj: <Nullable>enable</Nullable>
public string? MiddleName { get; set; }  // explicitly nullable
public string FirstName { get; set; } = "";  // non-nullable with default
```

### IHttpClientFactory
```csharp
// Program.cs
builder.Services.AddHttpClient();

// Service
public class ApiClient(IHttpClientFactory clientFactory)
{
    public async Task<string> GetAsync(string url)
    {
        var client = clientFactory.CreateClient();
        return await client.GetStringAsync(url);
    }
}
```

### Options Pattern
```csharp
public record AppSettings
{
    public string ApiUrl { get; init; } = "";
    public int MaxRetries { get; init; } = 3;
}

// Program.cs
builder.Services.Configure<AppSettings>(builder.Configuration.GetSection("AppSettings"));

// Usage via injection:
public class MyService(IOptions<AppSettings> options) { }
```

---

## Program.cs Template (.NET 8 Minimal Hosting)

```csharp
var builder = WebApplication.CreateBuilder(args);

// Services
builder.Services.AddControllersWithViews();
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("Default")));
builder.Services.AddScoped<IProductService, ProductService>();
builder.Services.AddScoped<IProductRepository, ProductRepository>();
builder.Services.AddHttpClient();
builder.Services.AddHealthChecks();

var app = builder.Build();

// Middleware (correct order)
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    app.UseHsts();
}
app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();
app.MapControllerRoute(name: "default", pattern: "{controller=Home}/{action=Index}/{id?}");
app.MapHealthChecks("/health");

app.Run();
```

---

## EF Core Migration

### DbContext (EF Core style)
```csharp
public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }
    
    public DbSet<Product> Products => Set<Product>();
    public DbSet<Category> Categories => Set<Category>();
}
```

### Repository Pattern (Async)
```csharp
public interface IProductRepository
{
    Task<IEnumerable<Product>> GetAllAsync();
    Task<Product?> GetByIdAsync(int id);
    Task<Product> AddAsync(Product product);
    Task UpdateAsync(Product product);
    Task DeleteAsync(int id);
}

public class ProductRepository(AppDbContext context) : IProductRepository
{
    public async Task<IEnumerable<Product>> GetAllAsync()
        => await context.Products.ToListAsync();
        
    public async Task<Product?> GetByIdAsync(int id)
        => await context.Products.FindAsync(id);
}
```

---

## Common .NET Migration Pitfalls

1. **Global.asax** — Remove entirely, move logic to Program.cs middleware
2. **BundleConfig** — Replace with modern bundler (Vite, webpack) or `<link>`/`<script>` tags
3. **FilterConfig** — Move to ASP.NET Core filters in Program.cs
4. **RouteConfig** — Use `MapControllerRoute` in Program.cs
5. **Session state** — Must explicitly add `builder.Services.AddSession()`
6. **ViewState** — Remove (doesn't exist in ASP.NET Core)
7. **Web.config** — Remove (use appsettings.json + environment variables)
