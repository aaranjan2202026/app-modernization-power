using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Session;
using Moq;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Web.Controllers;
using PharmacyNetwork.Web.Extensions;
using PharmacyNetwork.Web.Models;
using PharmacyNetwork.Web.Services;
using PharmacyNetwork.Web.ViewModels;
using Xunit;

namespace PharmacyNetwork.Tests.Controllers
{
    /// <summary>
    /// TASK-017: Unit tests for CartController.
    /// </summary>
    public class CartControllerTests
    {
        private static CartController CreateController(
            ICartService cartService = null,
            IAsyncRepository<Purchase> purchaseRepo = null,
            IAsyncRepository<Check> checkRepo = null)
        {
            cartService ??= new Mock<ICartService>().Object;
            purchaseRepo ??= new Mock<IAsyncRepository<Purchase>>().Object;
            checkRepo ??= new Mock<IAsyncRepository<Check>>().Object;

            var controller = new CartController(cartService, purchaseRepo, checkRepo);

            // Set up a minimal HTTP context with session support
            var httpContext = new DefaultHttpContext();
            var session = new Mock<ISession>();
            byte[] cartBytes = null;
            session.Setup(s => s.TryGetValue(It.IsAny<string>(), out cartBytes)).Returns(false);
            session.Setup(s => s.Set(It.IsAny<string>(), It.IsAny<byte[]>()));
            httpContext.Session = session.Object;
            controller.ControllerContext = new ControllerContext { HttpContext = httpContext };
            return controller;
        }

        [Fact]
        public void Index_ReturnsViewResult()
        {
            var controller = CreateController();

            var result = controller.Index();

            Assert.IsType<ViewResult>(result);
        }

        [Fact]
        public async Task Reserve_POST_InvalidModelState_ReturnsView()
        {
            var controller = CreateController();
            controller.ModelState.AddModelError("Telephone", "Required");

            var result = await controller.Reserve(new ReserveMedItemsViewModel());

            Assert.IsType<ViewResult>(result);
        }

        [Fact]
        public async Task Reserve_POST_EmptyCart_RedirectsToIndex()
        {
            var mockCartService = new Mock<ICartService>();
            var controller = CreateController(cartService: mockCartService.Object);

            var result = await controller.Reserve(new ReserveMedItemsViewModel { Telephone = "555-1234" });

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
        }

        [Fact]
        public void Reserve_GET_ReturnsViewWithViewModel()
        {
            var controller = CreateController();

            var result = controller.Reserve();

            Assert.IsType<ViewResult>(result);
        }
    }
}
