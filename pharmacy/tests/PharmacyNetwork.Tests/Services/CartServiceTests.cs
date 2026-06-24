using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Moq;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Web.Models;
using PharmacyNetwork.Web.Services;
using Xunit;

namespace PharmacyNetwork.Tests.Services
{
    /// <summary>
    /// TASK-015: Unit tests for CartService.ReserveItemsAsync.
    /// </summary>
    public class CartServiceTests
    {
        private readonly Mock<IAsyncRepository<ReservedMedItem>> _mockReservedRepo;
        private readonly CartService _cartService;

        public CartServiceTests()
        {
            _mockReservedRepo = new Mock<IAsyncRepository<ReservedMedItem>>();
            _cartService = new CartService(_mockReservedRepo.Object);
        }

        [Fact]
        public async Task ReserveItemsAsync_WithValidItems_CreatesReservationsForEachItem()
        {
            var cartItems = new List<CartItem>
            {
                new CartItem { MedicalItemId = 1, PharmacyId = 10, Count = 2, MedItemPrice = 5.99m },
                new CartItem { MedicalItemId = 2, PharmacyId = 10, Count = 1, MedItemPrice = 3.50m }
            };
            _mockReservedRepo
                .Setup(r => r.AddAsync(It.IsAny<ReservedMedItem>(), It.IsAny<CancellationToken>()))
                .ReturnsAsync((ReservedMedItem r, CancellationToken ct) => r);

            await _cartService.ReserveItemsAsync(cartItems, "555-1234", CancellationToken.None);

            _mockReservedRepo.Verify(
                r => r.AddAsync(It.IsAny<ReservedMedItem>(), It.IsAny<CancellationToken>()),
                Times.Exactly(2));
        }

        [Fact]
        public async Task ReserveItemsAsync_WithEmptyCart_DoesNotCallRepository()
        {
            var emptyCart = new List<CartItem>();

            await _cartService.ReserveItemsAsync(emptyCart, "555-1234", CancellationToken.None);

            _mockReservedRepo.Verify(
                r => r.AddAsync(It.IsAny<ReservedMedItem>(), It.IsAny<CancellationToken>()),
                Times.Never);
        }

        [Fact]
        public async Task ReserveItemsAsync_WithNullCart_DoesNotCallRepository()
        {
            await _cartService.ReserveItemsAsync(null, "555-1234", CancellationToken.None);

            _mockReservedRepo.Verify(
                r => r.AddAsync(It.IsAny<ReservedMedItem>(), It.IsAny<CancellationToken>()),
                Times.Never);
        }

        [Fact]
        public async Task ReserveItemsAsync_SetsCorrectReservationFields()
        {
            var cartItems = new List<CartItem>
            {
                new CartItem { MedicalItemId = 5, PharmacyId = 3, Count = 2, MedItemPrice = 10m }
            };
            ReservedMedItem capturedReservation = null;
            _mockReservedRepo
                .Setup(r => r.AddAsync(It.IsAny<ReservedMedItem>(), It.IsAny<CancellationToken>()))
                .Callback<ReservedMedItem, CancellationToken>((item, ct) => capturedReservation = item)
                .ReturnsAsync((ReservedMedItem r, CancellationToken ct) => r);

            var beforeCall = DateTime.Now;
            await _cartService.ReserveItemsAsync(cartItems, "555-9999", CancellationToken.None);

            Assert.NotNull(capturedReservation);
            Assert.Equal(5, capturedReservation.MedItemId);
            Assert.Equal(3, capturedReservation.PharmId);
            Assert.Equal(2, capturedReservation.Count);
            Assert.Equal("555-9999", capturedReservation.Telephone);
            Assert.True(capturedReservation.DateStart >= beforeCall);
            Assert.Equal(capturedReservation.DateStart.AddHours(24), capturedReservation.DateFinish);
        }

        [Fact]
        public async Task ReserveItemsAsync_PassesCancellationTokenToRepository()
        {
            var cts = new CancellationTokenSource();
            var cartItems = new List<CartItem>
            {
                new CartItem { MedicalItemId = 1, PharmacyId = 1, Count = 1, MedItemPrice = 1m }
            };
            _mockReservedRepo
                .Setup(r => r.AddAsync(It.IsAny<ReservedMedItem>(), cts.Token))
                .ReturnsAsync((ReservedMedItem r, CancellationToken ct) => r);

            await _cartService.ReserveItemsAsync(cartItems, "555-0000", cts.Token);

            _mockReservedRepo.Verify(
                r => r.AddAsync(It.IsAny<ReservedMedItem>(), cts.Token),
                Times.Once);
        }
    }
}
