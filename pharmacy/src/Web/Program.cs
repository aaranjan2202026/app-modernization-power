using System;
using System.Linq;
using System.Threading.Tasks;
using MediatR;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Infrastructure.Data;
using PharmacyNetwork.Infrastructure.Identity;
using PharmacyNetwork.Infrastructure.Logging;

namespace PharmacyNetwork.Web
{
    // Minimal hosting (.NET 6+). Replaces the previous
    // Host.CreateDefaultBuilder(...).UseStartup<Startup>() pattern.
    public class Program
    {
        public static async Task Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // ---------- Services ----------
            var services = builder.Services;
            var configuration = builder.Configuration;

            AddIdentityIfNotRegistered(services);

            services.AddScoped(typeof(IAsyncRepository<>), typeof(EfRepository<>));
            services.AddScoped(typeof(IAppLogger<>), typeof(LoggerAdapter<>));

            services.AddDbContext<AppIdentityDbContext>(options =>
                options.UseSqlServer(configuration.GetConnectionString("IdentityConnection")));

            services.AddDbContext<PharmacyNetworkContext>(options =>
                options.UseSqlServer(
                    configuration.GetConnectionString("PharmacyNetworkConnection"),
                    sqlOption => sqlOption.EnableRetryOnFailure(
                        maxRetryCount: 5,
                        maxRetryDelay: TimeSpan.FromSeconds(30),
                        errorNumbersToAdd: null)));

            // Replaces the obsolete app.UseDatabaseErrorPage() middleware (CS0618).
            services.AddDatabaseDeveloperPageExceptionFilter();

            services.AddMediatR(typeof(Program));

            services.AddControllersWithViews();
            services.AddMvc();
            services.AddRazorPages();

            services.AddResponseCaching();
            services.AddHttpContextAccessor();
            services.AddDistributedMemoryCache();
            services.AddMemoryCache();

            services.ConfigureApplicationCookie(options =>
            {
                options.LoginPath = "/Identity/Account/Login";
                options.LogoutPath = "/Identity/Account/Logout";
                options.AccessDeniedPath = "/Identity/Account/AccessDenied";
            });

            services.AddSession(options =>
            {
                options.Cookie.Name = ".PharmNet.Session";
                options.IdleTimeout = TimeSpan.FromMinutes(15);
                options.Cookie.HttpOnly = true;
                options.Cookie.IsEssential = true;
            });

            services.AddSwaggerGen();

            var app = builder.Build();

            // ---------- Seed identity data ----------
            using (var scope = app.Services.CreateScope())
            {
                var sp = scope.ServiceProvider;
                try
                {
                    var userManager = sp.GetRequiredService<UserManager<ApplicationUser>>();
                    var roleManager = sp.GetRequiredService<RoleManager<IdentityRole>>();
                    await AppIdentityDbContextSeed.SeedAsync(userManager, roleManager);
                }
                catch (Exception ex)
                {
                    sp.GetRequiredService<ILoggerFactory>()
                      .CreateLogger<Program>()
                      .LogError(ex, "An error occurred seeding the DB.");
                }
            }

            // ---------- Middleware ----------
            // Order matters: exception handling -> HSTS/HTTPS -> static files ->
            // routing -> session -> authn -> authz -> endpoints.
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

            app.UseSession();

            app.UseAuthentication();
            app.UseAuthorization();

            app.UseSwagger();
            app.UseSwaggerUI();

            app.MapControllerRoute(
                name: "default",
                pattern: "{controller=Home}/{action=Index}/{id?}");
            app.MapRazorPages();

            await app.RunAsync();
        }

        /// <summary>
        /// Registers ASP.NET Core Identity unless it is already present.
        /// Inspects the service collection directly rather than calling
        /// services.BuildServiceProvider(), which built a throwaway container
        /// and duplicated singletons (ASP0000).
        /// </summary>
        private static void AddIdentityIfNotRegistered(IServiceCollection services)
        {
            var alreadyRegistered = services.Any(s => s.ServiceType == typeof(UserManager<ApplicationUser>));
            if (alreadyRegistered)
            {
                return;
            }

            services.AddIdentity<ApplicationUser, IdentityRole>()
                .AddDefaultUI()
                .AddEntityFrameworkStores<AppIdentityDbContext>()
                .AddDefaultTokenProviders();
        }
    }
}
