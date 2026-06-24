using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Web.Models;

namespace PharmacyNetwork.Web.Services
{
    public class CartService : ICartService
    {
        private readonly IAsyncRepository<ReservedMedItem> _reservedMedItemRepository;

        public CartService(IAsyncRepository<ReservedMedItem> reservedMedItemRepository)
        {
            _reservedMedItemRepository = reservedMedItemRepository;
        }

        public async Task ReserveItemsAsync(IList<CartItem> cartItems, string telephone, CancellationToken cancellationToken = default)
        {
            if (cartItems == null || cartItems.Count == 0)
                return;

            var reservationStart = DateTime.Now;
            var reservationEnd = reservationStart.AddHours(24);

            foreach (var item in cartItems)
            {
                var reservation = new ReservedMedItem
                {
                    DateStart = reservationStart,
                    DateFinish = reservationEnd,
                    MedItemId = item.MedicalItemId,
                    PharmId = item.PharmacyId,
                    Count = item.Count,
                    Telephone = telephone
                };

                await _reservedMedItemRepository.AddAsync(reservation, cancellationToken).ConfigureAwait(false);
            }
        }
    }
}
