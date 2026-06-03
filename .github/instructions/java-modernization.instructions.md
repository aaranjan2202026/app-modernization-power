---
applyTo: '**/*.java, **/pom.xml, **/build.gradle, **/*.xml, **/*.properties, **/*.yml, **/*.yaml'
---

# Java 8 to Java 17+ Modernization Instructions

## Purpose
These instructions guide the modernization of legacy Java 8 applications to Java 17+. Follow these guidelines when executing modernization tasks from the migration plan (`Migration/01-Migration_Plan.md`).

## Strict Scope Enforcement

**🔴 CRITICAL**: You MUST ONLY perform these four modernizations:

### ✅ 1. Synchronous → Async/Reactive Modernization
### ✅ 2. Configuration Externalization  
### ✅ 3. Business Logic Modularization
### ✅ 4. Deprecated API Replacement

**❌ DO NOT perform any other refactoring or enhancements**

---

## Target Framework
- **Framework**: Java 17+ (LTS) with Spring Boot 3.x or Jakarta EE 10+
- **Project Type**: Spring Boot REST API, Spring MVC, or Modular Java Library
- **Java Version**: 17 or higher
- **Build Tool**: Maven 3.8+ or Gradle 7.5+

---

## 1. Async/Reactive Modernization

### Objective
Replace blocking synchronous calls with non-blocking async patterns (CompletableFuture, reactive streams, or virtual threads) to improve scalability and responsiveness.

### Detection Patterns

Identify these anti-patterns:
```java
// ❌ BLOCKING PATTERNS TO REPLACE:

// 1. Blocking service calls
Data data = service.getData();

// 2. Blocking I/O operations
String content = Files.readString(path);
String response = httpClient.send(request, BodyHandlers.ofString()).body();

// 3. Blocking database access
List<Customer> customers = customerRepository.findAll();
Customer customer = customerRepository.findById(id).orElse(null);

// 4. Synchronous stream operations on large datasets
List<Item> items = largeList.stream()
    .filter(predicate)
    .collect(Collectors.toList());

// 5. Thread.sleep() or blocking waits
Thread.sleep(1000);
future.get(); // blocking
```

### Replacement Patterns

```java
// ✅ ASYNC REPLACEMENTS (CompletableFuture):

// 1. Replace blocking calls with CompletableFuture
CompletableFuture<Data> dataFuture = service.getDataAsync();
Data data = dataFuture.join(); // or use thenApply/thenCompose

// 2. Replace blocking I/O with async I/O
CompletableFuture<String> contentFuture = CompletableFuture.supplyAsync(() -> {
    try {
        return Files.readString(path);
    } catch (IOException e) {
        throw new CompletionException(e);
    }
});

// 3. Spring WebFlux reactive approach
Mono<Customer> customerMono = customerRepository.findById(id);
Flux<Customer> customersFlux = customerRepository.findAll();

// 4. Parallel streams for large datasets
List<Item> items = largeList.parallelStream()
    .filter(predicate)
    .collect(Collectors.toList());

// 5. Use ScheduledExecutorService instead of Thread.sleep
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.schedule(() -> doWork(), 1, TimeUnit.SECONDS);
```

### Method Signature Transformations

```java
// ❌ BEFORE (Synchronous):
@GetMapping("/customers")
public ResponseEntity<List<Customer>> getAllCustomers() {
    List<Customer> customers = customerService.getAll();
    return ResponseEntity.ok(customers);
}

public Customer saveCustomer(Customer customer) {
    return customerRepository.save(customer);
}

public List<Customer> getCustomers() {
    return customerRepository.findAll();
}

// ✅ AFTER (Async with CompletableFuture):
@GetMapping("/customers")
public CompletableFuture<ResponseEntity<List<Customer>>> getAllCustomers() {
    return customerService.getAllAsync()
        .thenApply(ResponseEntity::ok);
}

public CompletableFuture<Customer> saveCustomerAsync(Customer customer) {
    return CompletableFuture.supplyAsync(() -> 
        customerRepository.save(customer)
    );
}

public CompletableFuture<List<Customer>> getCustomersAsync() {
    return CompletableFuture.supplyAsync(() -> 
        customerRepository.findAll()
    );
}

// ✅ ALTERNATIVE (Reactive with Spring WebFlux):
@GetMapping("/customers")
public Mono<ResponseEntity<List<Customer>>> getAllCustomers() {
    return customerService.getAllReactive()
        .collectList()
        .map(ResponseEntity::ok);
}

public Mono<Customer> saveCustomerReactive(Customer customer) {
    return customerRepository.save(customer);
}

public Flux<Customer> getCustomersReactive() {
    return customerRepository.findAll();
}
```

### Repository Layer Async Patterns

```java
// Using Spring Data JPA with CompletableFuture
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    @Async
    CompletableFuture<Customer> findByEmail(String email);
    
    @Async
    CompletableFuture<List<Customer>> findByLastName(String lastName);
}

// Or using Spring Data R2DBC for reactive
public interface ReactiveCustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    
    Mono<Customer> findByEmail(String email);
    
    Flux<Customer> findByLastName(String lastName);
}

// Custom async repository implementation
@Repository
public class AsyncCustomerRepositoryImpl {
    
    @Autowired
    private EntityManager entityManager;
    
    @Async
    public CompletableFuture<Customer> findByIdAsync(Long id) {
        return CompletableFuture.supplyAsync(() -> 
            entityManager.find(Customer.class, id)
        );
    }
    
    @Async
    public CompletableFuture<List<Customer>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> 
            entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList()
        );
    }
    
    @Async
    @Transactional
    public CompletableFuture<Customer> saveAsync(Customer customer) {
        return CompletableFuture.supplyAsync(() -> {
            if (customer.getId() == null) {
                entityManager.persist(customer);
                return customer;
            } else {
                return entityManager.merge(customer);
            }
        });
    }
}
```

### Service Layer Async Patterns

```java
// Service interface with async methods
public interface CustomerService {
    CompletableFuture<Customer> findByIdAsync(Long id);
    CompletableFuture<List<Customer>> findAllAsync();
    CompletableFuture<Customer> createAsync(Customer customer);
    CompletableFuture<Customer> updateAsync(Long id, Customer customer);
    CompletableFuture<Void> deleteAsync(Long id);
}

// Service implementation
@Service
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    @Async
    public CompletableFuture<Customer> findByIdAsync(Long id) {
        return CompletableFuture.supplyAsync(() -> 
            customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"))
        );
    }
    
    @Override
    @Async
    public CompletableFuture<List<Customer>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> 
            customerRepository.findAll()
        );
    }
    
    @Override
    @Async
    @Transactional
    public CompletableFuture<Customer> createAsync(Customer customer) {
        return CompletableFuture.supplyAsync(() -> 
            customerRepository.save(customer)
        );
    }
    
    @Override
    @Async
    @Transactional
    public CompletableFuture<Customer> updateAsync(Long id, Customer customer) {
        return findByIdAsync(id).thenCompose(existing -> {
            // Update fields
            existing.setName(customer.getName());
            existing.setEmail(customer.getEmail());
            return CompletableFuture.supplyAsync(() -> 
                customerRepository.save(existing)
            );
        });
    }
    
    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> deleteAsync(Long id) {
        return findByIdAsync(id).thenAccept(customer -> 
            customerRepository.delete(customer)
        );
    }
}
```

### Controller Layer Async Patterns

```java
// REST Controller with async endpoints
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    public CompletableFuture<ResponseEntity<List<Customer>>> getAllCustomers() {
        return customerService.findAllAsync()
            .thenApply(ResponseEntity::ok);
    }
    
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerById(@PathVariable Long id) {
        return customerService.findByIdAsync(id)
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public CompletableFuture<ResponseEntity<Customer>> createCustomer(@RequestBody Customer customer) {
        return customerService.createAsync(customer)
            .thenApply(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }
    
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Customer>> updateCustomer(
            @PathVariable Long id, 
            @RequestBody Customer customer) {
        return customerService.updateAsync(id, customer)
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteAsync(id)
            .thenApply(v -> ResponseEntity.noContent().<Void>build())
            .exceptionally(ex -> ResponseEntity.notFound().build());
    }
}
```

### Enable Async Support

```java
// Configuration class to enable async processing
@Configuration
@EnableAsync
public class AsyncConfiguration {
    
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}
```

---

## 2. Configuration Externalization

### Objective
Move hardcoded configuration values from code to external configuration files (application.properties, application.yml, environment variables) for environment-specific configuration.

### Detection Patterns

```java
// ❌ HARDCODED CONFIGURATION:

// 1. Database credentials in code
String dbUrl = "jdbc:mysql://localhost:3306/mydb";
String dbUser = "root";
String dbPassword = "password123";

// 2. API endpoints hardcoded
String apiUrl = "https://api.example.com/v1";

// 3. Feature flags hardcoded
boolean enableFeatureX = true;

// 4. Business constants hardcoded
int maxRetries = 3;
long timeout = 5000L;

// 5. File paths hardcoded
String uploadDir = "C:/uploads";
```

### Replacement Patterns

```java
// ✅ EXTERNALIZED CONFIGURATION:

// 1. Use @Value annotation
@Component
public class DatabaseConfig {
    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String dbUser;
    
    @Value("${spring.datasource.password}")
    private String dbPassword;
}

// 2. Use @ConfigurationProperties for grouped config
@ConfigurationProperties(prefix = "api")
@Component
public class ApiConfig {
    private String baseUrl;
    private int timeout;
    private int maxRetries;
    
    // Getters and setters
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
}

// 3. Environment-specific application.yml
/*
application.yml:
---
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}

---
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:mysql://localhost:3306/mydb_dev
    username: devuser
    password: devpass
api:
  base-url: https://dev-api.example.com
  timeout: 5000
  max-retries: 3
  
---
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
api:
  base-url: ${API_BASE_URL}
  timeout: ${API_TIMEOUT:10000}
  max-retries: ${API_MAX_RETRIES:5}
*/
```

### Configuration File Structure

```
src/main/resources/
├── application.yml (or application.properties)
├── application-dev.yml
├── application-test.yml
├── application-prod.yml
└── application-local.yml
```

### Environment Variables

```bash
# Use environment variables for sensitive data
export DB_URL=jdbc:mysql://prod-server:3306/mydb
export DB_USER=produser
export DB_PASSWORD=secretpassword
export API_BASE_URL=https://api.example.com
```

---

## 3. Business Logic Modularization

### Objective
Separate business logic from controllers and move it into dedicated service classes with clear single responsibilities.

### Detection Patterns

```java
// ❌ BUSINESS LOGIC IN CONTROLLER:
@RestController
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Validation logic in controller
        if (order.getTotalAmount() < 0) {
            return ResponseEntity.badRequest().build();
        }
        
        // Business calculations in controller
        double tax = order.getTotalAmount() * 0.08;
        order.setTax(tax);
        order.setGrandTotal(order.getTotalAmount() + tax);
        
        // Inventory check in controller
        if (!checkInventory(order.getItems())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // Direct repository call from controller
        Order saved = orderRepository.save(order);
        
        // Email notification logic in controller
        sendOrderConfirmationEmail(saved);
        
        return ResponseEntity.ok(saved);
    }
    
    // Helper methods polluting controller
    private boolean checkInventory(List<OrderItem> items) { ... }
    private void sendOrderConfirmationEmail(Order order) { ... }
}
```

### Replacement Patterns

```java
// ✅ BUSINESS LOGIC IN SERVICE LAYER:

// Controller - thin, delegates to service
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderDTO order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}

// Service - contains business logic
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private TaxCalculator taxCalculator;
    
    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        // Validation
        validateOrder(request);
        
        // Check inventory
        inventoryService.reserveItems(request.getItems());
        
        // Create order entity
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setItems(request.getItems());
        order.setTotalAmount(calculateTotal(request.getItems()));
        
        // Calculate tax
        double tax = taxCalculator.calculateTax(order.getTotalAmount());
        order.setTax(tax);
        order.setGrandTotal(order.getTotalAmount() + tax);
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Send notification (async)
        notificationService.sendOrderConfirmationAsync(savedOrder);
        
        return mapToDTO(savedOrder);
    }
    
    private void validateOrder(CreateOrderRequest request) {
        if (request.getTotalAmount() < 0) {
            throw new InvalidOrderException("Total amount cannot be negative");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new InvalidOrderException("Order must contain at least one item");
        }
    }
    
    private double calculateTotal(List<OrderItem> items) {
        return items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
    
    private OrderDTO mapToDTO(Order order) {
        // Mapping logic
        return new OrderDTO(order);
    }
}

// Separate specialized services
@Service
public class InventoryService {
    public void reserveItems(List<OrderItem> items) {
        // Inventory reservation logic
    }
}

@Service
public class NotificationService {
    @Async
    public void sendOrderConfirmationAsync(Order order) {
        // Email sending logic
    }
}

@Component
public class TaxCalculator {
    @Value("${tax.rate}")
    private double taxRate;
    
    public double calculateTax(double amount) {
        return amount * taxRate;
    }
}
```

### Service Layer Principles

1. **Single Responsibility**: Each service class has one clear responsibility
2. **Dependency Injection**: Use constructor or field injection for dependencies
3. **Transactional Boundaries**: Use `@Transactional` for database operations
4. **Error Handling**: Throw business exceptions, handle in controller advice
5. **DTOs**: Use Data Transfer Objects to decouple API from entities

---

## 4. Deprecated API Replacement

### Objective
Replace deprecated Java APIs, Spring Framework methods, and third-party libraries with modern equivalents.

### Common Deprecated API Replacements

```java
// ❌ DEPRECATED PATTERNS:

// 1. Date and Calendar API (deprecated in Java 8+)
Date date = new Date();
Calendar calendar = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

// 2. Stack (deprecated, use Deque)
Stack<String> stack = new Stack<>();

// 3. Vector and Hashtable (deprecated, use ArrayList and HashMap)
Vector<String> vector = new Vector<>();
Hashtable<String, String> hashtable = new Hashtable<>();

// 4. StringBuffer in single-threaded context
StringBuffer sb = new StringBuffer();

// 5. Thread.stop(), Thread.suspend() (deprecated)
thread.stop();

// 6. finalize() method (deprecated in Java 9)
@Override
protected void finalize() throws Throwable {
    // cleanup
}

// ✅ MODERN REPLACEMENTS:

// 1. Use java.time API (Java 8+)
LocalDate date = LocalDate.now();
LocalDateTime dateTime = LocalDateTime.now();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// 2. Use ArrayDeque
Deque<String> stack = new ArrayDeque<>();

// 3. Use ArrayList and HashMap
List<String> list = new ArrayList<>();
Map<String, String> map = new HashMap<>();

// 4. Use StringBuilder
StringBuilder sb = new StringBuilder();

// 5. Use interrupt() and proper thread management
thread.interrupt();
ExecutorService executor = Executors.newFixedThreadPool(10);

// 6. Use try-with-resources or Cleaner
try (Resource resource = new Resource()) {
    // use resource
} // automatically closed
```

### Spring Framework Deprecated API Replacements

```java
// ❌ SPRING DEPRECATED:

// 1. WebMvcConfigurerAdapter (deprecated in Spring 5)
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // config
    }
}

// 2. RestTemplate (not deprecated but WebClient preferred)
RestTemplate restTemplate = new RestTemplate();
String result = restTemplate.getForObject(url, String.class);

// ✅ SPRING MODERN:

// 1. Implement WebMvcConfigurer directly
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}

// 2. Use WebClient (reactive)
WebClient webClient = WebClient.create();
Mono<String> result = webClient.get()
    .uri(url)
    .retrieve()
    .bodyToMono(String.class);
```

### Hibernate/JPA Deprecated API Replacements

```java
// ❌ HIBERNATE DEPRECATED:

// 1. Criteria API (deprecated in Hibernate 5.2)
Criteria criteria = session.createCriteria(Customer.class);
criteria.add(Restrictions.eq("lastName", "Smith"));
List<Customer> customers = criteria.list();

// ✅ HIBERNATE MODERN:

// 1. Use CriteriaBuilder API
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
Root<Customer> root = query.from(Customer.class);
query.select(root).where(cb.equal(root.get("lastName"), "Smith"));
List<Customer> customers = entityManager.createQuery(query).getResultList();

// Or use Spring Data JPA
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}
```

---

## Testing Requirements

### Unit Test Coverage
- Minimum 80% code coverage for service layer
- All business logic methods must have unit tests
- Use JUnit 5 and Mockito for testing

```java
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private CustomerServiceImpl customerService;
    
    @Test
    void testFindByIdAsync_Success() throws ExecutionException, InterruptedException {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer(customerId, "John", "Doe");
        when(customerRepository.findById(customerId))
            .thenReturn(Optional.of(customer));
        
        // Act
        CompletableFuture<Customer> result = customerService.findByIdAsync(customerId);
        Customer actualCustomer = result.get();
        
        // Assert
        assertNotNull(actualCustomer);
        assertEquals("John", actualCustomer.getFirstName());
        verify(customerRepository).findById(customerId);
    }
}
```

### Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetAllCustomers_ReturnsCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(greaterThan(0)));
    }
}
```

---

## Build Configuration

### Maven pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>pharmacy-network</artifactId>
    <version>2.0.0</version>
    <name>Pharmacy Network</name>
    <description>Modernized Pharmacy Network Application</description>
    
    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Validation Checklist

After completing modernization tasks, validate:

- [ ] All code compiles without errors on Java 17+
- [ ] All unit tests pass
- [ ] No blocking calls in async methods
- [ ] Configuration externalized to application.yml
- [ ] Business logic separated into service classes
- [ ] No usage of deprecated APIs
- [ ] Code coverage ≥ 80% for service layer
- [ ] Integration tests pass
- [ ] Application starts successfully
- [ ] API endpoints respond correctly

---

## Common Pitfalls to Avoid

1. **Mixing Blocking and Non-Blocking Code**: Don't mix synchronous database calls with CompletableFuture
2. **Thread Pool Exhaustion**: Configure proper thread pool sizes for async execution
3. **Improper Exception Handling**: Use `exceptionally()` or `handle()` for CompletableFuture errors
4. **Circular Dependencies**: Avoid circular service dependencies
5. **Transaction Boundaries**: Ensure `@Transactional` is on service methods, not controllers
6. **Hardcoded Values**: Always externalize configuration
7. **God Services**: Keep services focused on single responsibilities

---

## References

- [Spring Boot 3.x Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Java 17 API Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [CompletableFuture Guide](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
