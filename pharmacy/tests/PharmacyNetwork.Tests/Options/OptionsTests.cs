using PharmacyNetwork.Web.Options;
using Xunit;

namespace PharmacyNetwork.Tests.Options
{
    /// <summary>
    /// TASK-008: Unit tests for SessionSettings binding.
    /// </summary>
    public class SessionSettingsTests
    {
        [Fact]
        public void SessionSettings_DefaultValues_AreCorrect()
        {
            var settings = new SessionSettings();

            Assert.Equal(".PharmNet.Session", settings.CookieName);
            Assert.Equal(15, settings.IdleTimeoutMinutes);
            Assert.True(settings.CookieHttpOnly);
            Assert.True(settings.CookieIsEssential);
        }

        [Fact]
        public void SessionSettings_CustomValues_AreApplied()
        {
            var settings = new SessionSettings
            {
                CookieName = ".Custom.Session",
                IdleTimeoutMinutes = 30,
                CookieHttpOnly = false,
                CookieIsEssential = false
            };

            Assert.Equal(".Custom.Session", settings.CookieName);
            Assert.Equal(30, settings.IdleTimeoutMinutes);
            Assert.False(settings.CookieHttpOnly);
            Assert.False(settings.CookieIsEssential);
        }

        [Fact]
        public void SessionSettings_CanBeWrappedInIOptions()
        {
            var settings = new SessionSettings { IdleTimeoutMinutes = 60 };
            var options = Microsoft.Extensions.Options.Options.Create(settings);

            Assert.Equal(60, options.Value.IdleTimeoutMinutes);
        }
    }

    /// <summary>
    /// TASK-010: Unit tests for DatabaseRetryOptions binding.
    /// </summary>
    public class DatabaseRetryOptionsTests
    {
        [Fact]
        public void DatabaseRetryOptions_DefaultValues_AreCorrect()
        {
            var options = new DatabaseRetryOptions();

            Assert.Equal(5, options.MaxRetryCount);
            Assert.Equal(30, options.MaxRetryDelaySeconds);
        }

        [Fact]
        public void DatabaseRetryOptions_CustomValues_AreApplied()
        {
            var options = new DatabaseRetryOptions
            {
                MaxRetryCount = 3,
                MaxRetryDelaySeconds = 15
            };

            Assert.Equal(3, options.MaxRetryCount);
            Assert.Equal(15, options.MaxRetryDelaySeconds);
        }

        [Fact]
        public void DatabaseRetryOptions_CanBeWrappedInIOptions()
        {
            var dbOptions = new DatabaseRetryOptions { MaxRetryCount = 10 };
            var wrappedOptions = Microsoft.Extensions.Options.Options.Create(dbOptions);

            Assert.Equal(10, wrappedOptions.Value.MaxRetryCount);
        }
    }
}
