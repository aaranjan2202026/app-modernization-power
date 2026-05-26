namespace PharmacyNetwork.ApplicationCore.Configuration
{
    /// <summary>
    /// Configuration settings for database connections.
    /// Used with IOptions pattern for externalized configuration.
    /// </summary>
    public class DatabaseSettings
    {
        /// <summary>
        /// Connection string for ASP.NET Core Identity database
        /// </summary>
        public string IdentityConnection { get; set; }

        /// <summary>
        /// Connection string for PharmacyNetwork application database
        /// </summary>
        public string PharmacyNetworkConnection { get; set; }
    }
}
