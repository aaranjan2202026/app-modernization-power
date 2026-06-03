---
applyTo: '**/*.java, **/pom.xml, **/build.gradle, **/*.xml, **/*.properties, **/*.yml, **/*.yaml'
---

# Java 8 to Java 17+ Modernization Review Fix Instructions

## Purpose
Guide the remediation of issues identified during the modernization review process. Use this document when addressing findings from `java-modernization-review.instructions.md`.

## Fix Process

1. **Prioritize Issues**: Fix CRITICAL → MAJOR → MINOR
2. **Fix Systematically**: One issue at a time, test after each fix
3. **Update Status**: Mark issue as fixed in review findings
4. **Re-validate**: Run build and tests after each fix
5. **Document**: Update migration summary with fixes applied

---

## 1. Fixing Async/Reactive Issues

### Issue: Blocking Calls (`.get()`, `.join()`)

**Problem**: Code uses blocking `.get()` or `.join()` calls causing potential thread starvation
```java
// ❌ BLOCKING
Data data = service.getDataAsync().get();
service.saveDataAsync().join();
```

**Fix**: Use non-blocking composition with `thenApply`, `thenCompose`, etc.
```java
// ✅ FIXED
@GetMapping("/data")
public CompletableFuture<ResponseEntity<Data>> getData() {
    return service.getDataAsync()
        .thenCompose(data -> service.saveDataAsync()
            .thenApply(v -> ResponseEntity.ok(data)));
}
```

**Steps**:
1. Replace blocking `.get()` or `.join()` with async composition
2. Use `thenApply()` for transformations
3. Use `thenCompose()` for chaining async operations
4. Return `CompletableFuture<T>` or `Mono<T>`/`Flux<T>`
5. Propagate async to calling methods

### Issue: Synchronous I/O Operations

**Problem**: Using synchronous file/stream operations
```csharp
// ❌ SYNCHRONOUS I/O
var content = File.ReadAllText(path);
var lines = File.ReadAllLines(path);
using (var reader = new StreamReader(stream))
{
    var data = reader.ReadToEnd();
}
```

**Fix**: Use async file/stream operations
```csharp
// ✅ FIXED
var content = await File.ReadAllTextAsync(path);
var lines = await File.ReadAllLinesAsync(path);
using (var reader = new StreamReader(stream))
{
    var data = await reader.ReadToEndAsync();
}
```

### Issue: Synchronous Database Calls

**Problem**: Using synchronous EF Core methods
```csharp
// ❌ SYNCHRONOUS DATABASE
var customers = _context.Customers.ToList();
var customer = _context.Customers.Find(id);
var exists = _context.Customers.Any(c => c.Email == email);
_context.SaveChanges();
```

**Fix**: Use async EF Core methods
```csharp
// ✅ FIXED
var customers = await _context.Customers.ToListAsync();
var customer = await _context.Customers.FindAsync(id);
var exists = await _context.Customers.AnyAsync(c => c.Email == email);
await _context.SaveChangesAsync();
```

**Steps**:
1. Find all synchronous EF methods: `ToList()`, `Find()`, `Any()`, `Count()`, `First()`, `Single()`, etc.
2. Replace with async equivalents: `ToListAsync()`, `FindAsync()`, etc.
3. Add `await` before each call
4. Ensure method is `async`

### Issue: Missing `async` Keyword or `await`

**Problem**: Method uses `await` but not marked `async`, or vice versa
```csharp
// ❌ MISSING async
public Task<IActionResult> Index()
{
    var data = await _service.GetDataAsync(); // ERROR: await without async
    return View(data);
}

// ❌ MISSING await
public async Task<IActionResult> Create()
{
    _service.CreateAsync(model); // WARNING: not awaited
    return RedirectToAction("Index");
}
```

**Fix**: Add `async` and `await` consistently
```csharp
// ✅ FIXED
public async Task<IActionResult> Index()
{
    var data = await _service.GetDataAsync();
    return View(data);
}

public async Task<IActionResult> Create()
{
    await _service.CreateAsync(model);
    return RedirectToAction("Index");
}
```

### Issue: `async void` Methods

**Problem**: Method is `async void` (except event handlers)
```csharp
// ❌ async void
public async void ProcessData()
{
    await _service.ProcessAsync();
}
```

**Fix**: Change to `async Task`
```csharp
// ✅ FIXED
public async Task ProcessDataAsync()
{
    await _service.ProcessAsync();
}
```

**Exception**: Event handlers can be `async void`
```csharp
// ✅ OK for event handlers
private async void Button_Click(object sender, EventArgs e)
{
    await ProcessDataAsync();
}
```

### Issue: Missing `Async` Suffix

**Problem**: Async methods don't have `Async` suffix
```csharp
// ❌ MISSING SUFFIX
public async Task<Customer> GetCustomer(int id)
{
    return await _repository.GetByIdAsync(id);
}
```

**Fix**: Add `Async` suffix to method name
```csharp
// ✅ FIXED
public async Task<Customer> GetCustomerAsync(int id)
{
    return await _repository.GetByIdAsync(id);
}
```

**Steps**:
1. Rename method to include `Async` suffix
2. Update all call sites to use new name
3. Update interface definitions if applicable

---

## 2. Fixing Configuration Issues

### Issue: `ConfigurationManager` Usage

**Problem**: Still using `ConfigurationManager` from .NET Framework
```csharp
// ❌ CONFIGURATIONMANAGER
using System.Configuration;

var connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
var setting = ConfigurationManager.AppSettings["TaxRate"];
```

**Fix**: Use `IConfiguration` and Options pattern
```csharp
// ✅ FIXED - Step 1: Create appsettings.json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=...;Database=..."
  },
  "AppSettings": {
    "TaxRate": 0.13
  }
}

// ✅ FIXED - Step 2: Create options class
public class AppSettings
{
    public const string SectionName = "AppSettings";
    public double TaxRate { get; set; }
}

// ✅ FIXED - Step 3: Register in Program.cs
builder.Services.Configure<AppSettings>(
    builder.Configuration.GetSection(AppSettings.SectionName));

builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

// ✅ FIXED - Step 4: Inject options
public class MyService
{
    private readonly AppSettings _settings;
    
    public MyService(IOptions<AppSettings> settings)
    {
        _settings = settings.Value;
    }
    
    public decimal CalculateTax(decimal amount)
    {
        return amount * (decimal)_settings.TaxRate;
    }
}
```

**Steps**:
1. Remove `using System.Configuration;`
2. Create appsettings.json with configuration values
3. Create strongly-typed options classes
4. Register options in Program.cs
5. Inject `IOptions<T>` in services
6. Remove `System.Configuration` package reference from .csproj

### Issue: Hardcoded Configuration Values

**Problem**: Configuration values hardcoded in code
```csharp
// ❌ HARDCODED
public class EmailService
{
    private const string SmtpServer = "smtp.example.com";
    private const int SmtpPort = 587;
    
    public void SendEmail()
    {
        var client = new SmtpClient(SmtpServer, SmtpPort);
        // ...
    }
}
```

**Fix**: Move to appsettings.json and options
```csharp
// ✅ FIXED - appsettings.json
{
  "EmailSettings": {
    "SmtpServer": "smtp.example.com",
    "SmtpPort": 587,
    "FromAddress": "noreply@example.com"
  }
}

// ✅ FIXED - Options class
public class EmailSettings
{
    public const string SectionName = "EmailSettings";
    
    public string SmtpServer { get; set; }
    public int SmtpPort { get; set; }
    public string FromAddress { get; set; }
}

// ✅ FIXED - Register in Program.cs
builder.Services.Configure<EmailSettings>(
    builder.Configuration.GetSection(EmailSettings.SectionName));

// ✅ FIXED - Use in service
public class EmailService
{
    private readonly EmailSettings _settings;
    
    public EmailService(IOptions<EmailSettings> settings)
    {
        _settings = settings.Value;
    }
    
    public void SendEmail()
    {
        var client = new SmtpClient(_settings.SmtpServer, _settings.SmtpPort);
        // ...
    }
}
```

### Issue: Sensitive Data in appsettings.json

**Problem**: Passwords, API keys, or secrets in appsettings.json
```json
❌ DON'T DO THIS
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=...;User=sa;Password=MyPassword123;"
  },
  "ApiSettings": {
    "ApiKey": "secret-key-12345"
  }
}
```

**Fix**: Use User Secrets (dev) and Key Vault/Environment Variables (prod)
```bash
# For development - use User Secrets
dotnet user-secrets init
dotnet user-secrets set "ConnectionStrings:DefaultConnection" "Server=...;User=sa;Password=MyPassword123;"
dotnet user-secrets set "ApiSettings:ApiKey" "secret-key-12345"
```

```csharp
// Program.cs - User Secrets automatically loaded in Development
// For production, use environment variables or Azure Key Vault

// appsettings.json - NO SECRETS
{
  "ConnectionStrings": {
    "DefaultConnection": "Will be overridden by User Secrets/Environment"
  },
  "ApiSettings": {
    "ApiKey": "Will be overridden by User Secrets/Environment"
  }
}
```

### Issue: No Environment-Specific Configuration

**Problem**: Only appsettings.json exists, no environment overrides
```
Project/
  ├── appsettings.json  ✓
  └── (missing environment files)
```

**Fix**: Create environment-specific files
```
Project/
  ├── appsettings.json              # Default settings
  ├── appsettings.Development.json  # Dev overrides
  └── appsettings.Production.json   # Prod overrides
```

```json
// appsettings.Development.json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Database=MyApp_Dev;Trusted_Connection=true"
  },
  "Logging": {
    "LogLevel": {
      "Default": "Debug"
    }
  }
}

// appsettings.Production.json
{
  "ConnectionStrings": {
    "DefaultConnection": "Will be set via environment variable"
  },
  "Logging": {
    "LogLevel": {
      "Default": "Warning"
    }
  }
}
```

---

## 3. Fixing Modularization Issues

### Issue: Business Logic in Controllers

**Problem**: Controllers contain business logic
```csharp
// ❌ BUSINESS LOGIC IN CONTROLLER
public class CustomerController : Controller
{
    private readonly ApplicationDbContext _context;
    
    [HttpPost]
    public async Task<IActionResult> Create(CustomerViewModel model)
    {
        // Validation logic
        if (await _context.Customers.AnyAsync(c => c.Email == model.Email))
        {
            ModelState.AddModelError("Email", "Email already exists");
            return View(model);
        }
        
        // Business logic
        var customer = new Customer
        {
            Name = model.Name,
            Email = model.Email,
            CreatedDate = DateTime.Now,
            Status = "Active"
        };
        
        // Data access
        _context.Customers.Add(customer);
        await _context.SaveChangesAsync();
        
        return RedirectToAction("Index");
    }
}
```

**Fix**: Extract to service layer
```csharp
// ✅ FIXED - Step 1: Create service interface
public interface ICustomerService
{
    Task<CustomerViewModel> CreateAsync(CustomerViewModel model);
}

// ✅ FIXED - Step 2: Implement service
public class CustomerService : ICustomerService
{
    private readonly ICustomerRepository _repository;
    private readonly ILogger<CustomerService> _logger;
    
    public CustomerService(ICustomerRepository repository, ILogger<CustomerService> logger)
    {
        _repository = repository;
        _logger = logger;
    }
    
    public async Task<CustomerViewModel> CreateAsync(CustomerViewModel model)
    {
        // Business validation
        if (await _repository.EmailExistsAsync(model.Email))
            throw new ValidationException("Email already exists");
        
        // Business logic
        var customer = new Customer
        {
            Name = model.Name,
            Email = model.Email,
            CreatedDate = DateTime.Now,
            Status = "Active"
        };
        
        // Delegate to repository
        var created = await _repository.AddAsync(customer);
        
        _logger.LogInformation("Created customer {Id}", created.Id);
        
        return new CustomerViewModel
        {
            Id = created.Id,
            Name = created.Name,
            Email = created.Email
        };
    }
}

// ✅ FIXED - Step 3: Refactor controller
public class CustomerController : Controller
{
    private readonly ICustomerService _service;
    
    public CustomerController(ICustomerService service)
    {
        _service = service;
    }
    
    [HttpPost]
    public async Task<IActionResult> Create(CustomerViewModel model)
    {
        if (!ModelState.IsValid)
            return View(model);
        
        try
        {
            await _service.CreateAsync(model);
            TempData["Success"] = "Customer created successfully";
            return RedirectToAction("Index");
        }
        catch (ValidationException ex)
        {
            ModelState.AddModelError("", ex.Message);
            return View(model);
        }
    }
}

// ✅ FIXED - Step 4: Register in Program.cs
builder.Services.AddScoped<ICustomerService, CustomerService>();
```

### Issue: Direct DbContext Access in Controllers

**Problem**: Controllers directly inject and use DbContext
```csharp
// ❌ DIRECT DBCONTEXT
public class ProductController : Controller
{
    private readonly ApplicationDbContext _context;
    
    public ProductController(ApplicationDbContext context)
    {
        _context = context;
    }
    
    public async Task<IActionResult> Index()
    {
        var products = await _context.Products.ToListAsync();
        return View(products);
    }
}
```

**Fix**: Create repository layer
```csharp
// ✅ FIXED - Step 1: Create repository interface
public interface IProductRepository
{
    Task<IEnumerable<Product>> GetAllAsync();
    Task<Product> GetByIdAsync(int id);
    Task<Product> AddAsync(Product product);
    Task UpdateAsync(Product product);
    Task DeleteAsync(int id);
}

// ✅ FIXED - Step 2: Implement repository
public class ProductRepository : IProductRepository
{
    private readonly ApplicationDbContext _context;
    
    public ProductRepository(ApplicationDbContext context)
    {
        _context = context;
    }
    
    public async Task<IEnumerable<Product>> GetAllAsync()
    {
        return await _context.Products.ToListAsync();
    }
    
    public async Task<Product> GetByIdAsync(int id)
    {
        return await _context.Products.FindAsync(id);
    }
    
    public async Task<Product> AddAsync(Product product)
    {
        await _context.Products.AddAsync(product);
        await _context.SaveChangesAsync();
        return product;
    }
    
    public async Task UpdateAsync(Product product)
    {
        _context.Products.Update(product);
        await _context.SaveChangesAsync();
    }
    
    public async Task DeleteAsync(int id)
    {
        var product = await GetByIdAsync(id);
        if (product != null)
        {
            _context.Products.Remove(product);
            await _context.SaveChangesAsync();
        }
    }
}

// ✅ FIXED - Step 3: Create service
public interface IProductService
{
    Task<IEnumerable<ProductViewModel>> GetAllAsync();
}

public class ProductService : IProductService
{
    private readonly IProductRepository _repository;
    
    public ProductService(IProductRepository repository)
    {
        _repository = repository;
    }
    
    public async Task<IEnumerable<ProductViewModel>> GetAllAsync()
    {
        var products = await _repository.GetAllAsync();
        return products.Select(p => new ProductViewModel
        {
            Id = p.Id,
            Name = p.Name,
            Price = p.Price
        });
    }
}

// ✅ FIXED - Step 4: Refactor controller
public class ProductController : Controller
{
    private readonly IProductService _service;
    
    public ProductController(IProductService service)
    {
        _service = service;
    }
    
    public async Task<IActionResult> Index()
    {
        var products = await _service.GetAllAsync();
        return View(products);
    }
}

// ✅ FIXED - Step 5: Register in Program.cs
builder.Services.AddScoped<IProductRepository, ProductRepository>();
builder.Services.AddScoped<IProductService, ProductService>();
```

### Issue: Missing Dependency Injection Registration

**Problem**: Services/repositories not registered in Program.cs
```csharp
// ❌ NOT REGISTERED - will cause runtime errors
public class OrderController : Controller
{
    private readonly IOrderService _service;
    
    public OrderController(IOrderService service)
    {
        _service = service; // DI will fail - service not registered
    }
}
```

**Fix**: Register all services and repositories
```csharp
// ✅ FIXED - Program.cs
var builder = WebApplication.CreateBuilder(args);

// Register DbContext
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

// Register Repositories
builder.Services.AddScoped<ICustomerRepository, CustomerRepository>();
builder.Services.AddScoped<IProductRepository, ProductRepository>();
builder.Services.AddScoped<IOrderRepository, OrderRepository>();

// Register Services
builder.Services.AddScoped<ICustomerService, CustomerService>();
builder.Services.AddScoped<IProductService, ProductService>();
builder.Services.AddScoped<IOrderService, OrderService>();

// Register other dependencies
builder.Services.AddHttpContextAccessor();
builder.Services.AddHttpClient();
builder.Services.AddLogging();
```

**Steps**:
1. Identify all service interfaces and implementations
2. Register each with appropriate lifetime:
   - `AddScoped`: Most services and repositories (per-request)
   - `AddSingleton`: Stateless services, expensive-to-create objects
   - `AddTransient`: Lightweight, stateless services
3. Verify registration by running application

---

## 4. Fixing Deprecated API Issues

### Issue: `System.Web` References

**Problem**: Code still uses `System.Web` namespace
```csharp
// ❌ SYSTEM.WEB
using System.Web;
using System.Web.Mvc;

var context = HttpContext.Current;
var path = Server.MapPath("~/uploads");
```

**Fix**: Replace with ASP.NET Core equivalents
```csharp
// ✅ FIXED
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http;

// In controller:
var context = HttpContext;
var path = Path.Combine(_env.WebRootPath, "uploads");

// Inject IWebHostEnvironment
public class FileController : Controller
{
    private readonly IWebHostEnvironment _env;
    
    public FileController(IWebHostEnvironment env)
    {
        _env = env;
    }
    
    public IActionResult Upload()
    {
        var uploadsPath = Path.Combine(_env.WebRootPath, "uploads");
        return View();
    }
}
```

**Steps**:
1. Remove `using System.Web;` statements
2. Replace `HttpContext.Current` with `HttpContext` property (in controllers) or inject `IHttpContextAccessor` (in services)
3. Replace `Server.MapPath` with `IWebHostEnvironment.WebRootPath`
4. Remove `System.Web` package references from .csproj
5. Update using statements to `Microsoft.AspNetCore.*`

### Issue: Direct `HttpClient` Instantiation

**Problem**: Creating `HttpClient` with `new`
```csharp
// ❌ DIRECT INSTANTIATION
public class ApiClient
{
    public async Task<string> GetDataAsync(string url)
    {
        using (var client = new HttpClient())
        {
            return await client.GetStringAsync(url);
        }
    }
}
```

**Fix**: Use `IHttpClientFactory`
```csharp
// ✅ FIXED - Step 1: Register HttpClient in Program.cs
builder.Services.AddHttpClient();

// Named client (optional):
builder.Services.AddHttpClient("MyApi", client =>
{
    client.BaseAddress = new Uri("https://api.example.com");
    client.DefaultRequestHeaders.Add("Accept", "application/json");
});

// ✅ FIXED - Step 2: Inject IHttpClientFactory
public class ApiClient
{
    private readonly IHttpClientFactory _clientFactory;
    
    public ApiClient(IHttpClientFactory clientFactory)
    {
        _clientFactory = clientFactory;
    }
    
    public async Task<string> GetDataAsync(string url)
    {
        var client = _clientFactory.CreateClient();
        return await client.GetStringAsync(url);
    }
    
    // Or use named client:
    public async Task<string> GetFromMyApiAsync(string endpoint)
    {
        var client = _clientFactory.CreateClient("MyApi");
        return await client.GetStringAsync(endpoint);
    }
}
```

### Issue: `JavaScriptSerializer` Usage

**Problem**: Using deprecated `JavaScriptSerializer`
```csharp
// ❌ DEPRECATED SERIALIZER
using System.Web.Script.Serialization;

var serializer = new JavaScriptSerializer();
var json = serializer.Serialize(obj);
var obj = serializer.Deserialize<MyClass>(json);
```

**Fix**: Use `System.Text.Json`
```csharp
// ✅ FIXED
using System.Text.Json;

var json = JsonSerializer.Serialize(obj);
var obj = JsonSerializer.Deserialize<MyClass>(json);

// With options:
var options = new JsonSerializerOptions
{
    PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
    WriteIndented = true
};
var json = JsonSerializer.Serialize(obj, options);
```

**Note**: If using Newtonsoft.Json and it's working, you can keep it, but System.Text.Json is preferred for .NET 6+.

### Issue: Entity Framework 6 References

**Problem**: Still using Entity Framework 6
```csharp
// ❌ ENTITY FRAMEWORK 6
using System.Data.Entity;

public class ApplicationDbContext : DbContext
{
    public ApplicationDbContext() : base("DefaultConnection") { }
    
    public DbSet<Customer> Customers { get; set; }
}

// Usage
var customers = context.Customers.ToList(); // Synchronous
```

**Fix**: Migrate to Entity Framework Core
```csharp
// ✅ FIXED - Step 1: Update using statement
using Microsoft.EntityFrameworkCore;

// ✅ FIXED - Step 2: Update DbContext
public class ApplicationDbContext : DbContext
{
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
        : base(options)
    {
    }
    
    public DbSet<Customer> Customers { get; set; }
}

// ✅ FIXED - Step 3: Register in Program.cs
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

// ✅ FIXED - Step 4: Update usage to async
var customers = await context.Customers.ToListAsync();
```

**Steps**:
1. Remove `EntityFramework` package, add `Microsoft.EntityFrameworkCore.SqlServer`
2. Update DbContext constructor to accept `DbContextOptions<T>`
3. Register DbContext in Program.cs
4. Update all queries to use async methods
5. Run migrations: `dotnet ef migrations add InitialCreate`, `dotnet ef database update`

---

## 5. Fixing Build & Runtime Issues

### Issue: Build Errors - Target Framework

**Problem**: Project still targets .NET Framework
```xml
<!-- ❌ OLD TARGET -->
<TargetFrameworkVersion>v4.7.2</TargetFrameworkVersion>
```

**Fix**: Update to SDK-style project targeting .NET 6+
```xml
<!-- ✅ FIXED -->
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net6.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
  </PropertyGroup>
</Project>
```

### Issue: Dependency Injection Errors

**Problem**: Runtime error: "Unable to resolve service for type..."
```
System.InvalidOperationException: Unable to resolve service for type 'ICustomerService'
```

**Fix**: Register missing service in Program.cs
```csharp
// ✅ FIXED
builder.Services.AddScoped<ICustomerService, CustomerService>();
```

**Debugging Steps**:
1. Identify the type that can't be resolved from error message
2. Find the interface and implementation
3. Add registration in Program.cs with appropriate lifetime
4. Verify all dependencies of that service are also registered

### Issue: Middleware Order Errors

**Problem**: Middleware configured in wrong order
```csharp
// ❌ WRONG ORDER
app.UseRouting();
app.UseStaticFiles();
app.UseAuthentication();
app.UseAuthorization();
```

**Fix**: Correct middleware order
```csharp
// ✅ FIXED - Correct order
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");
```

**Correct Order**:
1. Exception handling (`UseExceptionHandler`, `UseDeveloperExceptionPage`)
2. HSTS (`UseHsts`)
3. HTTPS redirection (`UseHttpsRedirection`)
4. Static files (`UseStaticFiles`)
5. Routing (`UseRouting`)
6. CORS (`UseCors`) - if needed
7. Authentication (`UseAuthentication`)
8. Authorization (`UseAuthorization`)
9. Custom middleware
10. Endpoints (`MapControllers`, `MapControllerRoute`)

---

## 6. Fixing Test Issues

### Issue: Tests Calling Async Methods Synchronously

**Problem**: Tests block on async methods
```csharp
// ❌ BLOCKING IN TESTS
[Fact]
public void CreateCustomer_ValidModel_ReturnsCustomer()
{
    var result = _service.CreateAsync(model).Result; // DON'T DO THIS
    Assert.NotNull(result);
}
```

**Fix**: Make test method async
```csharp
// ✅ FIXED
[Fact]
public async Task CreateCustomer_ValidModel_ReturnsCustomer()
{
    var result = await _service.CreateAsync(model);
    Assert.NotNull(result);
}
```

### Issue: Missing Test Coverage

**Problem**: Coverage below 90%
```
Service Layer: 65%
Repository Layer: 80%
Controller Layer: 50%
```

**Fix**: Add missing tests
```csharp
// ✅ Add tests for uncovered scenarios

[Fact]
public async Task CreateAsync_DuplicateEmail_ThrowsValidationException()
{
    // Arrange
    _mockRepo.Setup(r => r.EmailExistsAsync(It.IsAny<string>()))
        .ReturnsAsync(true);
    
    // Act & Assert
    await Assert.ThrowsAsync<ValidationException>(() => 
        _service.CreateAsync(model));
}

[Fact]
public async Task DeleteAsync_NonExistentId_HandlesGracefully()
{
    // Arrange
    _mockRepo.Setup(r => r.GetByIdAsync(999))
        .ReturnsAsync((Customer)null);
    
    // Act - should not throw
    await _service.DeleteAsync(999);
    
    // Assert
    _mockRepo.Verify(r => r.DeleteAsync(999), Times.Never);
}
```

**Steps to Improve Coverage**:
1. Run coverage report: `dotnet test /p:CollectCoverage=true`
2. Identify uncovered methods and branches
3. Write tests for:
   - Happy path (success scenarios)
   - Error cases (exceptions, validation failures)
   - Edge cases (null values, empty collections, boundary conditions)
   - All conditional branches

---

## 7. Best Practices for Fixes

### Incremental Fixing
1. Fix one issue at a time
2. Run build after each fix
3. Run tests after each fix
4. Commit after each successful fix
5. Move to next issue only if current is resolved

### Validation After Fixes
```bash
# 1. Clean build
dotnet clean
dotnet build

# 2. Run all tests
dotnet test

# 3. Check for remaining issues
grep -r "\.Result" --include="*.cs"
grep -r "\.Wait()" --include="*.cs"
grep -r "ConfigurationManager" --include="*.cs"
grep -r "System.Web" --include="*.cs"

# 4. Run application
dotnet run
```

### Documentation
After fixing issues:
1. Update review findings document (mark as fixed)
2. Update migration summary with fixes applied
3. Add comments in code if fix requires explanation
4. Commit with clear message describing fix

---

## Common Fix Patterns Summary

| Issue | Quick Fix |
|-------|-----------|
| `.Result` / `.Wait()` | Replace with `await`, make method `async Task<T>` |
| Synchronous I/O | Replace with async equivalents (`ReadAllTextAsync`, etc.) |
| Synchronous EF | Replace with async EF methods (`ToListAsync`, `FindAsync`, etc.) |
| `ConfigurationManager` | Create appsettings.json, options classes, register in Program.cs |
| Hardcoded values | Move to appsettings.json, use Options pattern |
| Business logic in controller | Extract to service layer with interface |
| DbContext in controller | Create repository layer, inject repository into service |
| `System.Web.HttpContext` | Use `HttpContext` property or inject `IHttpContextAccessor` |
| `new HttpClient()` | Inject `IHttpClientFactory`, register in Program.cs |
| `JavaScriptSerializer` | Use `System.Text.Json.JsonSerializer` |
| Entity Framework 6 | Migrate to EF Core, update to async methods |
| DI registration missing | Add `builder.Services.Add...` in Program.cs |
| Test not async | Make test method `async Task`, use `await` |

---

## End of Fix Instructions

Use this guide to systematically address all review findings and bring the modernization to completion.
