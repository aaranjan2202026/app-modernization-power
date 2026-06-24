namespace PharmacyNetwork.Web.Options
{
    public class DatabaseRetryOptions
    {
        public int MaxRetryCount { get; set; } = 5;
        public int MaxRetryDelaySeconds { get; set; } = 30;
    }
}
