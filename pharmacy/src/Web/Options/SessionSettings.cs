namespace PharmacyNetwork.Web.Options
{
    public class SessionSettings
    {
        public string CookieName { get; set; } = ".PharmNet.Session";
        public int IdleTimeoutMinutes { get; set; } = 15;
        public bool CookieHttpOnly { get; set; } = true;
        public bool CookieIsEssential { get; set; } = true;
    }
}
