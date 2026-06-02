---
applyTo: '**/*.cs, **/*.csproj, **/*.json, **/Program.cs, **/Startup.cs, **/appsettings*.json'
---

# .NET Framework to .NET 6+ Modernization Instructions

## Purpose
These instructions guide the modernization of .NET Framework applications to .NET 6+. Follow these guidelines when executing modernization tasks from the migration plan (`Migration/01-Migration_Plan.md`).

## Strict Scope Enforcement

**🔴 CRITICAL**: You MUST ONLY perform these four modernizations:

### ✅ 1. Synchronous → Async/Await Modernization
### ✅ 2. Configuration Externalization  
### ✅ 3. Business Logic Modularization
### ✅ 4. Deprecated API Replacement

**❌ DO NOT perform any other refactoring or enhancements**

---

## Target Framework
- **Framework**: .NET 6+ (LTS)
- **Project Type**: ASP.NET Core MVC, ASP.NET Core Web API, or Class Library
- **C# Version**: 10 or higher

---

## 1. Async/Await Modernization

### Objective
Replace all blocking synchronous calls with non-blocking async/await patterns to improve scalability and responsiveness.

### Detection Patterns

Identify these anti-patterns:
```csharp
// ❌ BLOCKING PATTERNS TO REPLACE:

// 1. .Result property
var data = _service.GetDataAsync().Result;

// 2. .Wait() method
_service.SaveDataAsync().Wait();

// 3. Synchronous I/O
var content = File.ReadAllText(path);
var response = httpClient.GetAsync(url).Result;

// 4. Synchronous database access
var customers = _context.Customers.ToList();
var customer = _context.Customers.Find(id);

// 5. Synchronous LINQ materialization
var items = query.ToList();
var first = query.FirstOrDefault();
var count = query.Count();
```

### Replacement Patterns

```csharp
// ✅ ASYNC REPLACEMENTS:

// 1. Replace .Result with await
var data = await _service.GetDataAsync();

// 2. Replace .Wait() with await
await _service.SaveDataAsync();

// 3. Replace synchronous I/O with async I/O
var content = await File.ReadAllTextAsync(path);
var response = await httpClient.GetAsync(url);

// 4. Replace synchronous database access with async
var customers = await _context.Customers.ToListAsync();
var customer = await _context.Customers.FindAsync(id);

// 5. Replace synchronous LINQ with async LINQ
var items = await query.ToListAsync();
var first = await query.FirstOrDefaultAsync();
var count = await query.CountAsync();
```

### Method Signature Transformations

```csharp
// ❌ BEFORE (Synchronous):
public ActionResult Index()
{
    var data = _service.GetData();
    return View(data);
}

public bool SaveCustomer(Customer customer)
{
    _repository.Add(customer);
    return true;
}

public List<Customer> GetCustomers()
{
    return _context.Customers.ToList();
}

// ✅ AFTER (Async/Await):
public async Task<IActionResult> Index()
{
    var data = await _service.GetDataAsync();
    return View(data);
}

public async Task<bool> SaveCustomerAsync(Customer customer)
{
    await _repository.AddAsync(customer);
    return true;
}

public async Task<List<Customer>> GetCustomersAsync()
{
    return await _context.Customers.ToListAsync();
}
```

### Repository Layer Async Patterns

```csharp
// IRepository<T> interface
public interface IRepository<T> where T : class
{
    Task<T> GetByIdAsync(int id);
    Task<IEnumerable<T>> GetAllAsync();
    Task<T> AddAsync(T entity);
    Task UpdateAsync(T entity);
    Task DeleteAsync(int id);
    Task<bool> ExistsAsync(int id);
}

// Repository<T> implementation
public class Repository<T> : IRepository<T> where T : class
{
    protected readonly DbContext _context;
    protected readonly DbSet<T> _dbSet;
    
    public Repository(DbContext context)
    {
        _context = context;
        _dbSet = context.Set<T>();
    }
    
    public virtual async Task<T> GetByIdAsync(int id)
    {
        return await _dbSet.FindAsync(id);
    }
    
    public virtual async Task<IEnumerable<T>> GetAllAsync()
    {
        return await _dbSet.ToListAsync();
    }
    
    public virtual async Task<T> AddAsync(T entity)
    {
        await _dbSet.AddAsync(entity);
        await _context.SaveChangesAsync();
        return entity;
    }
    
    public virtual async Task UpdateAsync(T entity)
    {
        _dbSet.Update(entity);
        await _context.SaveChangesAsync();
    }
    
    public virtual async Task DeleteAsync(int id)
    {
        var entity = await GetByIdAsync(id);
        if (entity != null)
        {
            _dbSet.Remove(entity);
            await _context.SaveChangesAsync();
        }
    }
    
    public virtual async Task<bool> ExistsAsync(int id)
    {
        return await _dbSet.FindAsync(id) != null;
    }
}
```

### Service Layer Async Patterns

```csharp
// IService interface
public interface ICustomerService
{
    Task<CustomerViewModel> GetByIdAsync(int id);
    Task<IEnumerable<CustomerViewModel>> GetAllAsync();
    Task<CustomerViewModel> CreateAsync(CustomerViewModel model);
    Task UpdateAsync(CustomerViewModel model);
    Task DeleteAsync(int id);
}

// Service implementation
public class CustomerService : ICustomerService
{
    private readonly IRepository<Customer> _repository;
    private readonly ILogger<CustomerService> _logger;
    
    public CustomerService(
        IRepository<Customer> repository,
        ILogger<CustomerService> logger)
    {
        _repository = repository;
        _logger = logger;
    }
    
    public async Task<CustomerViewModel> GetByIdAsync(int id)
    {
        var customer = await _repository.GetByIdAsync(id);
        if (customer == null)
            throw new NotFoundException($"Customer {id} not found");
            
        return MapToViewModel(customer);
    }
    
    public async Task<IEnumerable<CustomerViewModel>> GetAllAsync()
    {
        var customers = await _repository.GetAllAsync();
        return customers.Select(MapToViewModel);
    }
    
    public async Task<CustomerViewModel> CreateAsync(CustomerViewModel model)
    {
        var customer = MapToEntity(model);
        var created = await _repository.AddAsync(customer);
        return MapToViewModel(created);
    }
}
```

### Controller Layer Async Patterns

```csharp
// ❌ BEFORE (Synchronous controller):
public class CustomerController : Controller
{
    private readonly ICustomerService _service;
    
    public CustomerController(ICustomerService service)
    {
        _service = service;
    }
    
    public ActionResult Index()
    {
        var customers = _service.GetAll();
        return View(customers);
    }
    
    [HttpPost]
    public ActionResult Create(CustomerViewModel model)
    {
        if (!ModelState.IsValid)
            return View(model);
            
        _service.Create(model);
        return RedirectToAction("Index");
    }
}

// ✅ AFTER (Async controller):
public class CustomerController : Controller
{
    private readonly ICustomerService _service;
    
    public CustomerController(ICustomerService service)
    {
        _service = service;
    }
    
    public async Task<IActionResult> Index()
    {
        var customers = await _service.GetAllAsync();
        return View(customers);
    }
    
    [HttpPost]
    public async Task<IActionResult> Create(CustomerViewModel model)
    {
        if (!ModelState.IsValid)
            return View(model);
            
        await _service.CreateAsync(model);
        return RedirectToAction("Index");
    }
}
```

### API Controller Async Patterns

```csharp
// ✅ Async API controller
[ApiController]
[Route("api/[controller]")]
public class CustomersController : ControllerBase
{
    private readonly ICustomerService _service;
    
    public CustomersController(ICustomerService service)
    {
        _service = service;
    }
    
    [HttpGet]
    public async Task<ActionResult<IEnumerable<CustomerViewModel>>> GetAll()
    {
        var customers = await _service.GetAllAsync();
        return Ok(customers);
    }
    
    [HttpGet("{id}")]
    public async Task<ActionResult<CustomerViewModel>> GetById(int id)
    {
        try
        {
            var customer = await _service.GetByIdAsync(id);
            return Ok(customer);
        }
        catch (NotFoundException)
        {
            return NotFound();
        }
    }
    
    [HttpPost]
    public async Task<ActionResult<CustomerViewModel>> Create(CustomerViewModel model)
    {
        if (!ModelState.IsValid)
            return BadRequest(ModelState);
            
        var created = await _service.CreateAsync(model);
        return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);
    }
}
```

### Async Best Practices

1. **Always propagate async**: If you use `await`, the method must be `async`
2. **Avoid async void**: Only use `async void` for event handlers
3. **Use Async suffix**: Name methods with `Async` suffix (e.g., `GetCustomersAsync`)
4. **ConfigureAwait**: Not needed in ASP.NET Core (no synchronization context)
5. **Don't block on async code**: Never use `.Result` or `.Wait()`

---

## 2. Configuration Externalization

### Objective
Move all configuration from web.config, app.config, and hardcoded values to modern configuration patterns (appsettings.json, IConfiguration, Options pattern).

### Detection Patterns

Identify these patterns:
```csharp
// ❌ PATTERNS TO REPLACE:

// 1. web.config/app.config access
var connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
var setting = ConfigurationManager.AppSettings["TaxRate"];

// 2. Hardcoded values
var taxRate = 0.13;
var maxRetries = 3;
var apiUrl = "https://api.example.com";

// 3. Magic strings
if (user.Role == "Admin") { }
var path = @"C:\Data\Files";
```

### Replacement Patterns

#### Step 1: Create appsettings.json

```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=(localdb)\\mssqllocaldb;Database=MyApp;Trusted_Connection=true"
  },
  "AppSettings": {
    "TaxRate": 0.13,
    "MaxRetries": 3,
    "ApiUrl": "https://api.example.com",
    "AdminRole": "Admin",
    "DataPath": "C:\\Data\\Files"
  },
  "EmailSettings": {
    "SmtpServer": "smtp.example.com",
    "SmtpPort": 587,
    "FromAddress": "noreply@example.com",
    "EnableSsl": true
  },
  "Logging": {
    "LogLevel": {
      "Default": "Information",
      "Microsoft.AspNetCore": "Warning"
    }
  }
}
```

#### Step 2: Create Strongly-Typed Options Classes

```csharp
// Options/AppSettings.cs
public class AppSettings
{
    public const string SectionName = "AppSettings";
    
    public double TaxRate { get; set; }
    public int MaxRetries { get; set; }
    public string ApiUrl { get; set; }
    public string AdminRole { get; set; }
    public string DataPath { get; set; }
}

// Options/EmailSettings.cs
public class EmailSettings
{
    public const string SectionName = "EmailSettings";
    
    public string SmtpServer { get; set; }
    public int SmtpPort { get; set; }
    public string FromAddress { get; set; }
    public bool EnableSsl { get; set; }
}
```

#### Step 3: Register Options in Program.cs

```csharp
// Program.cs
var builder = WebApplication.CreateBuilder(args);

// Register strongly-typed options
builder.Services.Configure<AppSettings>(
    builder.Configuration.GetSection(AppSettings.SectionName));

builder.Services.Configure<EmailSettings>(
    builder.Configuration.GetSection(EmailSettings.SectionName));

// Rest of configuration...
```

#### Step 4: Inject and Use Options

```csharp
// ❌ BEFORE (Hardcoded or ConfigurationManager):
public class OrderService : IOrderService
{
    public decimal CalculateTax(decimal amount)
    {
        var taxRate = 0.13; // Hardcoded
        return amount * taxRate;
    }
}

// ✅ AFTER (Options pattern):
public class OrderService : IOrderService
{
    private readonly AppSettings _appSettings;
    
    public OrderService(IOptions<AppSettings> appSettings)
    {
        _appSettings = appSettings.Value;
    }
    
    public decimal CalculateTax(decimal amount)
    {
        return amount * (decimal)_appSettings.TaxRate;
    }
}
```

### Connection String Access

```csharp
// ❌ BEFORE (.NET Framework):
var connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;

// ✅ AFTER (.NET 6+):
// In Program.cs:
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));
```

### IConfiguration Direct Access

For simple scenarios or startup configuration:
```csharp
public class Startup
{
    private readonly IConfiguration _configuration;
    
    public Startup(IConfiguration configuration)
    {
        _configuration = configuration;
    }
    
    public void ConfigureServices(IServiceCollection services)
    {
        var apiUrl = _configuration["AppSettings:ApiUrl"];
        var taxRate = _configuration.GetValue<double>("AppSettings:TaxRate");
    }
}
```

### Environment-Specific Configuration

Create multiple appsettings files:
- `appsettings.json` - Default settings
- `appsettings.Development.json` - Development overrides
- `appsettings.Production.json` - Production overrides

```json
// appsettings.Development.json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Database=MyApp_Dev;Trusted_Connection=true"
  },
  "AppSettings": {
    "ApiUrl": "https://dev-api.example.com"
  }
}
```

### Configuration Best Practices

1. **Never hardcode**: All configuration values in appsettings.json
2. **Use Options pattern**: Strongly-typed options classes for groups of settings
3. **Environment-specific**: Use appsettings.{Environment}.json for overrides
4. **Secret management**: Use User Secrets (dev) and Azure Key Vault (prod) for sensitive data
5. **Validate options**: Add validation to options classes using data annotations

---

## 3. Business Logic Modularization

### Objective
Separate business logic from controllers/UI and data access from business logic using a layered architecture with dependency injection.

### Detection Patterns

Identify these anti-patterns:
```csharp
// ❌ ANTI-PATTERNS TO REPLACE:

// 1. Business logic in controllers
public class CustomerController : Controller
{
    private readonly ApplicationDbContext _context;
    
    [HttpPost]
    public IActionResult Create(CustomerViewModel model)
    {
        // Validation logic in controller
        if (string.IsNullOrWhiteSpace(model.Email))
        {
            ModelState.AddModelError("Email", "Email required");
            return View(model);
        }
        
        // Business logic in controller
        if (_context.Customers.Any(c => c.Email == model.Email))
        {
            ModelState.AddModelError("Email", "Email already exists");
            return View(model);
        }
        
        // Data access in controller
        var customer = new Customer
        {
            Name = model.Name,
            Email = model.Email
        };
        _context.Customers.Add(customer);
        _context.SaveChanges();
        
        return RedirectToAction("Index");
    }
}

// 2. Data access mixed with business logic
public class OrderProcessor
{
    private readonly ApplicationDbContext _context;
    
    public void ProcessOrder(Order order)
    {
        // Business logic mixed with data access
        var customer = _context.Customers.Find(order.CustomerId);
        if (customer.CreditLimit < order.Total)
            throw new Exception("Credit limit exceeded");
            
        _context.Orders.Add(order);
        _context.SaveChanges();
    }
}
```

### Replacement Pattern: Layered Architecture

```
┌─────────────────────────────────────┐
│     Presentation Layer              │
│  (Controllers, Views, ViewModels)   │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│      Business Logic Layer           │
│    (Services, Interfaces)           │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│      Data Access Layer              │
│  (Repositories, DbContext)          │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│       Database                      │
└─────────────────────────────────────┘
```

### Modularization Process

#### Step 1: Extract Repository Layer

```csharp
// Repositories/Interfaces/ICustomerRepository.cs
public interface ICustomerRepository
{
    Task<Customer> GetByIdAsync(int id);
    Task<IEnumerable<Customer>> GetAllAsync();
    Task<Customer> GetByEmailAsync(string email);
    Task<bool> EmailExistsAsync(string email);
    Task<Customer> AddAsync(Customer customer);
    Task UpdateAsync(Customer customer);
    Task DeleteAsync(int id);
}

// Repositories/CustomerRepository.cs
public class CustomerRepository : ICustomerRepository
{
    private readonly ApplicationDbContext _context;
    
    public CustomerRepository(ApplicationDbContext context)
    {
        _context = context;
    }
    
    public async Task<Customer> GetByIdAsync(int id)
    {
        return await _context.Customers.FindAsync(id);
    }
    
    public async Task<IEnumerable<Customer>> GetAllAsync()
    {
        return await _context.Customers.ToListAsync();
    }
    
    public async Task<Customer> GetByEmailAsync(string email)
    {
        return await _context.Customers
            .FirstOrDefaultAsync(c => c.Email == email);
    }
    
    public async Task<bool> EmailExistsAsync(string email)
    {
        return await _context.Customers
            .AnyAsync(c => c.Email == email);
    }
    
    public async Task<Customer> AddAsync(Customer customer)
    {
        await _context.Customers.AddAsync(customer);
        await _context.SaveChangesAsync();
        return customer;
    }
    
    public async Task UpdateAsync(Customer customer)
    {
        _context.Customers.Update(customer);
        await _context.SaveChangesAsync();
    }
    
    public async Task DeleteAsync(int id)
    {
        var customer = await GetByIdAsync(id);
        if (customer != null)
        {
            _context.Customers.Remove(customer);
            await _context.SaveChangesAsync();
        }
    }
}
```

#### Step 2: Extract Service Layer

```csharp
// Services/Interfaces/ICustomerService.cs
public interface ICustomerService
{
    Task<CustomerViewModel> GetByIdAsync(int id);
    Task<IEnumerable<CustomerViewModel>> GetAllAsync();
    Task<CustomerViewModel> CreateAsync(CustomerViewModel model);
    Task UpdateAsync(CustomerViewModel model);
    Task DeleteAsync(int id);
}

// Services/CustomerService.cs
public class CustomerService : ICustomerService
{
    private readonly ICustomerRepository _repository;
    private readonly ILogger<CustomerService> _logger;
    
    public CustomerService(
        ICustomerRepository repository,
        ILogger<CustomerService> logger)
    {
        _repository = repository;
        _logger = logger;
    }
    
    public async Task<CustomerViewModel> GetByIdAsync(int id)
    {
        var customer = await _repository.GetByIdAsync(id);
        if (customer == null)
            throw new NotFoundException($"Customer {id} not found");
            
        return MapToViewModel(customer);
    }
    
    public async Task<IEnumerable<CustomerViewModel>> GetAllAsync()
    {
        var customers = await _repository.GetAllAsync();
        return customers.Select(MapToViewModel);
    }
    
    public async Task<CustomerViewModel> CreateAsync(CustomerViewModel model)
    {
        // Business validation
        if (await _repository.EmailExistsAsync(model.Email))
            throw new ValidationException("Email already exists");
            
        // Map and save
        var customer = new Customer
        {
            Name = model.Name,
            Email = model.Email
        };
        
        var created = await _repository.AddAsync(customer);
        
        _logger.LogInformation("Created customer {Id}", created.Id);
        
        return MapToViewModel(created);
    }
    
    public async Task UpdateAsync(CustomerViewModel model)
    {
        var customer = await _repository.GetByIdAsync(model.Id);
        if (customer == null)
            throw new NotFoundException($"Customer {model.Id} not found");
            
        // Update properties
        customer.Name = model.Name;
        customer.Email = model.Email;
        
        await _repository.UpdateAsync(customer);
        
        _logger.LogInformation("Updated customer {Id}", customer.Id);
    }
    
    public async Task DeleteAsync(int id)
    {
        await _repository.DeleteAsync(id);
        _logger.LogInformation("Deleted customer {Id}", id);
    }
    
    private CustomerViewModel MapToViewModel(Customer customer)
    {
        return new CustomerViewModel
        {
            Id = customer.Id,
            Name = customer.Name,
            Email = customer.Email
        };
    }
}
```

#### Step 3: Refactor Controller to Use Service

```csharp
// ✅ AFTER (Modularized controller):
public class CustomerController : Controller
{
    private readonly ICustomerService _service;
    private readonly ILogger<CustomerController> _logger;
    
    public CustomerController(
        ICustomerService service,
        ILogger<CustomerController> logger)
    {
        _service = service;
        _logger = logger;
    }
    
    public async Task<IActionResult> Index()
    {
        try
        {
            var customers = await _service.GetAllAsync();
            return View(customers);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error loading customers");
            return View("Error");
        }
    }
    
    public IActionResult Create()
    {
        return View(new CustomerViewModel());
    }
    
    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Create(CustomerViewModel model)
    {
        if (!ModelState.IsValid)
            return View(model);
            
        try
        {
            await _service.CreateAsync(model);
            TempData["Success"] = "Customer created successfully";
            return RedirectToAction(nameof(Index));
        }
        catch (ValidationException ex)
        {
            ModelState.AddModelError("", ex.Message);
            return View(model);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error creating customer");
            ModelState.AddModelError("", "An error occurred while creating the customer");
            return View(model);
        }
    }
}
```

#### Step 4: Register Dependencies in Program.cs

```csharp
// Program.cs
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

// Register logging
builder.Services.AddLogging();

var app = builder.Build();
```

### Modularization Best Practices

1. **Single Responsibility**: Each class has one clear purpose
2. **Dependency Injection**: Constructor injection only
3. **Interface Segregation**: Small, focused interfaces
4. **Layer Separation**: Controllers → Services → Repositories → Database
5. **No direct DbContext access**: Controllers/Services use repositories only

---

## 4. Deprecated API Replacement

### Objective
Replace .NET Framework-specific APIs with .NET 6+ equivalents to ensure compatibility and leverage modern capabilities.

### System.Web Replacement

```csharp
// ❌ BEFORE (.NET Framework - System.Web):
using System.Web;
using System.Web.Mvc;
using System.Web.Http;

var context = HttpContext.Current;
var request = HttpContext.Current.Request;
var response = HttpContext.Current.Response;
var server = HttpContext.Current.Server;
var session = HttpContext.Current.Session;

var queryString = Request.QueryString["id"];
var formValue = Request.Form["name"];
var cookie = Request.Cookies["token"];

Response.Write("Hello");
Response.Redirect("/home");
Server.MapPath("~/uploads");

// ✅ AFTER (.NET 6+ - ASP.NET Core):
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http;

// In controller:
var queryString = Request.Query["id"];
var formValue = Request.Form["name"];
var cookie = Request.Cookies["token"];

// Response operations:
return Content("Hello");
return Redirect("/home");
var path = _env.WebRootPath + "/uploads";

// HttpContext access in controller:
var user = HttpContext.User;
var session = HttpContext.Session;

// HttpContext access outside controller (via IHttpContextAccessor):
public class MyService
{
    private readonly IHttpContextAccessor _contextAccessor;
    
    public MyService(IHttpContextAccessor contextAccessor)
    {
        _contextAccessor = contextAccessor;
    }
    
    public void DoSomething()
    {
        var httpContext = _contextAccessor.HttpContext;
        var user = httpContext.User;
    }
}

// Register IHttpContextAccessor in Program.cs:
builder.Services.AddHttpContextAccessor();
```

### HttpClient Replacement

```csharp
// ❌ BEFORE (Direct instantiation):
public class ApiClient
{
    public async Task<string> GetDataAsync(string url)
    {
        using (var httpClient = new HttpClient()) // DON'T DO THIS
        {
            return await httpClient.GetStringAsync(url);
        }
    }
}

// ✅ AFTER (HttpClientFactory):
public class ApiClient
{
    private readonly IHttpClientFactory _clientFactory;
    
    public ApiClient(IHttpClientFactory clientFactory)
    {
        _clientFactory = clientFactory;
    }
    
    public async Task<string> GetDataAsync(string url)
    {
        var httpClient = _clientFactory.CreateClient();
        return await httpClient.GetStringAsync(url);
    }
}

// Register in Program.cs:
builder.Services.AddHttpClient();
builder.Services.AddScoped<ApiClient>();

// Named client configuration:
builder.Services.AddHttpClient("MyApi", client =>
{
    client.BaseAddress = new Uri("https://api.example.com");
    client.DefaultRequestHeaders.Add("Accept", "application/json");
});

// Use named client:
var client = _clientFactory.CreateClient("MyApi");
```

### JSON Serialization Replacement

```csharp
// ❌ BEFORE (JavaScriptSerializer):
using System.Web.Script.Serialization;

var serializer = new JavaScriptSerializer();
var json = serializer.Serialize(obj);
var obj = serializer.Deserialize<MyClass>(json);

// ❌ BEFORE (Newtonsoft.Json):
using Newtonsoft.Json;

var json = JsonConvert.SerializeObject(obj);
var obj = JsonConvert.DeserializeObject<MyClass>(json);

// ✅ AFTER (System.Text.Json):
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

// In controllers (automatic serialization):
[ApiController]
public class ApiController : ControllerBase
{
    [HttpGet]
    public ActionResult<MyClass> Get()
    {
        return Ok(new MyClass()); // Automatically serialized
    }
}
```

### Entity Framework to EF Core

```csharp
// ❌ BEFORE (Entity Framework 6):
using System.Data.Entity;

public class ApplicationDbContext : DbContext
{
    public ApplicationDbContext() : base("DefaultConnection") { }
    
    public DbSet<Customer> Customers { get; set; }
}

var customers = context.Customers.ToList(); // Synchronous

// ✅ AFTER (Entity Framework Core):
using Microsoft.EntityFrameworkCore;

public class ApplicationDbContext : DbContext
{
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
        : base(options) { }
    
    public DbSet<Customer> Customers { get; set; }
}

// Register in Program.cs:
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

// Use async methods:
var customers = await context.Customers.ToListAsync();
```

### Configuration Access Replacement

```csharp
// ❌ BEFORE (ConfigurationManager):
using System.Configuration;

var connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
var setting = ConfigurationManager.AppSettings["TaxRate"];

// ✅ AFTER (IConfiguration):
public class MyService
{
    private readonly IConfiguration _configuration;
    
    public MyService(IConfiguration configuration)
    {
        _configuration = configuration;
    }
    
    public void DoSomething()
    {
        var connectionString = _configuration.GetConnectionString("DefaultConnection");
        var setting = _configuration["AppSettings:TaxRate"];
    }
}
```

### File Path Replacement

```csharp
// ❌ BEFORE (Server.MapPath):
var path = Server.MapPath("~/uploads");

// ✅ AFTER (IWebHostEnvironment):
public class FileService
{
    private readonly IWebHostEnvironment _env;
    
    public FileService(IWebHostEnvironment env)
    {
        _env = env;
    }
    
    public string GetUploadPath()
    {
        return Path.Combine(_env.WebRootPath, "uploads");
    }
}

// Register in Program.cs:
// IWebHostEnvironment is automatically registered
```

### Authentication/Authorization Replacement

```csharp
// ❌ BEFORE (FormsAuthentication):
using System.Web.Security;

FormsAuthentication.SetAuthCookie(username, createPersistentCookie);
FormsAuthentication.SignOut();

// ✅ AFTER (ASP.NET Core Identity/Authentication):
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using System.Security.Claims;

public class AccountController : Controller
{
    public async Task<IActionResult> Login(LoginViewModel model)
    {
        // Validate user...
        
        var claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, model.Username),
            new Claim(ClaimTypes.Role, "User")
        };
        
        var claimsIdentity = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
        var authProperties = new AuthenticationProperties
        {
            IsPersistent = model.RememberMe
        };
        
        await HttpContext.SignInAsync(
            CookieAuthenticationDefaults.AuthenticationScheme,
            new ClaimsPrincipal(claimsIdentity),
            authProperties);
        
        return RedirectToAction("Index", "Home");
    }
    
    public async Task<IActionResult> Logout()
    {
        await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
        return RedirectToAction("Index", "Home");
    }
}

// Register in Program.cs:
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(options =>
    {
        options.LoginPath = "/Account/Login";
        options.LogoutPath = "/Account/Logout";
    });

// In middleware pipeline:
app.UseAuthentication();
app.UseAuthorization();
```

### Common API Mapping Table

| .NET Framework API | .NET 6+ Equivalent | Notes |
|--------------------|-------------------|-------|
| `System.Web.HttpContext` | `Microsoft.AspNetCore.Http.HttpContext` | Use IHttpContextAccessor |
| `HttpContext.Current` | Inject `IHttpContextAccessor` | No static access |
| `Server.MapPath` | `IWebHostEnvironment.WebRootPath` | Inject IWebHostEnvironment |
| `ConfigurationManager` | `IConfiguration` | Inject IConfiguration |
| `HttpClient` (direct) | `IHttpClientFactory` | Inject IHttpClientFactory |
| `JavaScriptSerializer` | `System.Text.Json.JsonSerializer` | Built-in |
| `Newtonsoft.Json` | `System.Text.Json` | Preferred for .NET 6+ |
| `FormsAuthentication` | `HttpContext.SignInAsync` | ASP.NET Core Authentication |
| `Session["key"]` | `HttpContext.Session.SetString` | Enable session in Program.cs |
| `Response.Write` | `return Content()` | In controllers |

---

## Project File Modernization

### .csproj Transformation

```xml
<!-- ❌ BEFORE (.NET Framework): -->
<Project ToolsVersion="15.0" xmlns="http://schemas.microsoft.com/developer/msbuild/2003">
  <PropertyGroup>
    <TargetFrameworkVersion>v4.7.2</TargetFrameworkVersion>
    <Configuration Condition=" '$(Configuration)' == '' ">Debug</Configuration>
  </PropertyGroup>
  <ItemGroup>
    <Reference Include="System" />
    <Reference Include="System.Web" />
    <Reference Include="System.Web.Mvc, Version=5.2.7.0" />
  </ItemGroup>
  <ItemGroup>
    <Compile Include="Controllers\HomeController.cs" />
  </ItemGroup>
</Project>

<!-- ✅ AFTER (.NET 6+): -->
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net6.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="Microsoft.EntityFrameworkCore.SqlServer" Version="6.0.0" />
    <PackageReference Include="Microsoft.EntityFrameworkCore.Tools" Version="6.0.0" />
  </ItemGroup>
</Project>
```

---

## Testing Guidelines

### Unit Test Structure

```csharp
// Tests/Services/CustomerServiceTests.cs
using Xunit;
using Moq;

public class CustomerServiceTests
{
    private readonly Mock<ICustomerRepository> _mockRepo;
    private readonly Mock<ILogger<CustomerService>> _mockLogger;
    private readonly CustomerService _service;
    
    public CustomerServiceTests()
    {
        _mockRepo = new Mock<ICustomerRepository>();
        _mockLogger = new Mock<ILogger<CustomerService>>();
        _service = new CustomerService(_mockRepo.Object, _mockLogger.Object);
    }
    
    [Fact]
    public async Task CreateAsync_ValidModel_ReturnsCustomer()
    {
        // Arrange
        var model = new CustomerViewModel
        {
            Name = "Test Customer",
            Email = "test@example.com"
        };
        
        _mockRepo.Setup(r => r.EmailExistsAsync(model.Email))
            .ReturnsAsync(false);
        _mockRepo.Setup(r => r.AddAsync(It.IsAny<Customer>()))
            .ReturnsAsync(new Customer { Id = 1, Name = model.Name, Email = model.Email });
        
        // Act
        var result = await _service.CreateAsync(model);
        
        // Assert
        Assert.NotNull(result);
        Assert.Equal(1, result.Id);
        Assert.Equal(model.Name, result.Name);
    }
    
    [Fact]
    public async Task CreateAsync_DuplicateEmail_ThrowsValidationException()
    {
        // Arrange
        var model = new CustomerViewModel { Email = "exists@example.com" };
        _mockRepo.Setup(r => r.EmailExistsAsync(model.Email))
            .ReturnsAsync(true);
        
        // Act & Assert
        await Assert.ThrowsAsync<ValidationException>(() => 
            _service.CreateAsync(model));
    }
}
```

### Test Coverage Requirements
- Services: 100% method coverage
- Repositories: 90%+ coverage
- Controllers: 80%+ coverage
- Overall: 90%+ coverage

---

## Validation Checklist

For each modernization task:

### Async/Await Checklist
- [ ] No `.Result` or `.Wait()` calls remaining
- [ ] All I/O operations use async methods
- [ ] Method signatures updated to `async Task<T>`
- [ ] Async propagates correctly through call stack
- [ ] Tests updated to use async test methods

### Configuration Checklist
- [ ] No `ConfigurationManager` usage
- [ ] All settings in appsettings.json
- [ ] Strongly-typed options classes created
- [ ] Options registered in Program.cs
- [ ] No hardcoded configuration values

### Modularization Checklist
- [ ] Business logic extracted to services
- [ ] Data access extracted to repositories
- [ ] Controllers are thin (orchestration only)
- [ ] Interfaces created for all services/repositories
- [ ] Dependencies registered in Program.cs
- [ ] No direct DbContext access in controllers

### API Replacement Checklist
- [ ] No `System.Web` references
- [ ] HttpClient uses IHttpClientFactory
- [ ] JSON serialization uses System.Text.Json
- [ ] EF Core used instead of EF6 (if applicable)
- [ ] Project targets .NET 6+
- [ ] Application builds with 0 errors

---

## Best Practices

1. **Make incremental changes**: One file/method at a time
2. **Test after each change**: Ensure no regressions
3. **Preserve behavior**: Functionality must remain identical
4. **Document TODO items**: If unsure, leave comments for review
5. **Use TODO comments**: Mark areas needing manual verification
6. **Commit frequently**: After each completed task
7. **Follow naming conventions**: Async methods end with "Async"
8. **Use interfaces**: Enable testability and flexibility
9. **Inject dependencies**: Constructor injection only
10. **Log appropriately**: Use ILogger for errors and important operations

---

## End of Instructions

Follow these guidelines strictly when modernizing .NET Framework applications to .NET 6+. Always refer back to the migration plan for task-specific details.
