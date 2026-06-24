using System;
using AutoMapper;
using MediatR;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Infrastructure.Data;
using  PharmacyNetwork.Infrastructure.Identity;
using PharmacyNetwork.Infrastructure.Logging;
using PharmacyNetwork.Web.Options;
using PharmacyNetwork.Web.Services;

namespace PharmacyNetwork.Web
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        #region Commands

        // add-migration InitMigration -Context PharmacyNetworkContext -Project Infrastructure -StartupProject Web
        // update-database -Context AppIdentityDbContext -Project Infrastructure -StartupProject Web

        #endregion

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddIdentity<ApplicationUser, IdentityRole>()
                .AddDefaultUI()
                .AddEntityFrameworkStores<AppIdentityDbContext>()
                .AddDefaultTokenProviders();

            services.AddScoped(typeof(IAsyncRepository<>), typeof(EfRepository<>));
            services.AddScoped(typeof(IAppLogger<>), typeof(LoggerAdapter<>));
            services.AddScoped<ICartService, CartService>();

            // Bind configuration options
            services.Configure<SessionSettings>(Configuration.GetSection("Session"));
            services.Configure<DatabaseRetryOptions>(Configuration.GetSection("DatabaseRetry"));

            // Add Identity DbContext
            services.AddDbContext<AppIdentityDbContext>(options =>
                options.UseSqlServer(Configuration.GetConnectionString("IdentityConnection")));

            //Add PharmacyNetwork DbContext
            services.AddDbContext<PharmacyNetworkContext>((sp, options) =>
                {
                    var retryOptions = sp.GetRequiredService<IOptions<DatabaseRetryOptions>>().Value;
                    options.UseSqlServer(Configuration.GetConnectionString("PharmacyNetworkConnection"),
                    sqlServerOptionsAction: sqlOption =>
                    {
                        sqlOption.EnableRetryOnFailure(
                            maxRetryCount: retryOptions.MaxRetryCount,
                            maxRetryDelay: TimeSpan.FromSeconds(retryOptions.MaxRetryDelaySeconds),
                            errorNumbersToAdd: null);
                    });
                });

            services.AddAutoMapper(cfg => cfg.AddMaps(typeof(Startup).Assembly));

            services.AddControllersWithViews();

            services.AddMvc();
            services.AddRazorPages();

            services.AddMediatR(typeof(Startup));

            services.AddResponseCaching();
            services.AddHttpContextAccessor();

            services.AddDistributedMemoryCache();

            services.ConfigureApplicationCookie(options =>
            {
                options.LoginPath = $"/Identity/Account/Login";
                options.LogoutPath = $"/Identity/Account/Logout";
                options.AccessDeniedPath = $"/Identity/Account/AccessDenied";
            });

            services.AddSession((options) =>
            {
                var sessionSettings = Configuration.GetSection("Session").Get<SessionSettings>() ?? new SessionSettings();
                options.Cookie.Name = sessionSettings.CookieName;
                options.IdleTimeout = TimeSpan.FromMinutes(sessionSettings.IdleTimeoutMinutes);
                options.Cookie.HttpOnly = sessionSettings.CookieHttpOnly;
                options.Cookie.IsEssential = sessionSettings.CookieIsEssential;
            });

            services.AddMemoryCache();

            // Inject an implementation of ISwaggerProvider with defaulted settings applied
            services.AddSwaggerGen();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public static void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseMigrationsEndPoint();
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

            // Enable middleware to serve generated Swagger as a JSON endpoint.
            app.UseSwagger();

            // Enable middleware to serve swagger-ui (HTML, JS, CSS, etc.), 
            // specifying the Swagger JSON endpoint.
            app.UseSwaggerUI();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllerRoute(
                    name: "default",
                    pattern: "{controller=Home}/{action=Index}/{id?}");
                endpoints.MapRazorPages();
            });
        }
    }
}
