using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using PharmacyNetwork.Web.Models;

namespace PharmacyNetwork.Web.Services
{
    public interface ICartService
    {
        Task ReserveItemsAsync(IList<CartItem> cartItems, string telephone, CancellationToken cancellationToken = default);
    }
}
