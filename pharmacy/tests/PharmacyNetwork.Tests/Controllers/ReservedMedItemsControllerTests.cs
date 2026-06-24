using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Moq;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Web.Controllers;
using Xunit;

namespace PharmacyNetwork.Tests.Controllers
{
    /// <summary>
    /// TASK-023+037: Unit tests for ReservedMedItemsController — CSRF fix on Remove + CancellationToken.
    /// </summary>
    public class ReservedMedItemsControllerTests
    {
        private readonly Mock<IAsyncRepository<ReservedMedItem>> _mockRepo;
        private readonly Mock<MediatR.IMediator> _mockMediator;

        public ReservedMedItemsControllerTests()
        {
            _mockRepo = new Mock<IAsyncRepository<ReservedMedItem>>();
            _mockMediator = new Mock<MediatR.IMediator>();
        }

        private ReservedMedItemsController CreateController()
        {
            var controller = new ReservedMedItemsController(_mockRepo.Object, _mockMediator.Object);
            controller.ControllerContext = new ControllerContext
            {
                HttpContext = new DefaultHttpContext()
            };
            return controller;
        }

        [Fact]
        public async Task Remove_WithValidId_DeletesAndRedirects()
        {
            var item = new ReservedMedItem { ReservedId = 1, Telephone = "555-0000" };
            _mockRepo.Setup(r => r.GetByIdAsync(1, It.IsAny<CancellationToken>())).ReturnsAsync(item);
            _mockRepo.Setup(r => r.DeleteAsync(item, It.IsAny<CancellationToken>())).Returns(Task.CompletedTask);
            var controller = CreateController();

            var result = await controller.Remove(1);

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
            _mockRepo.Verify(r => r.DeleteAsync(item, It.IsAny<CancellationToken>()), Times.Once);
        }

        [Fact]
        public async Task Remove_WithNullId_ReturnsNotFound()
        {
            var controller = CreateController();

            var result = await controller.Remove(null);

            Assert.IsType<NotFoundResult>(result);
        }

        [Fact]
        public async Task Remove_WhenItemNotFound_ReturnsNotFound()
        {
            _mockRepo.Setup(r => r.GetByIdAsync(999, It.IsAny<CancellationToken>()))
                .ReturnsAsync((ReservedMedItem)null);
            var controller = CreateController();

            var result = await controller.Remove(999);

            Assert.IsType<NotFoundResult>(result);
        }

        [Fact]
        public async Task Details_WithValidId_ReturnsView()
        {
            var item = new ReservedMedItem { ReservedId = 1 };
            _mockRepo.Setup(r => r.GetByIdAsync(1, It.IsAny<CancellationToken>())).ReturnsAsync(item);
            var controller = CreateController();

            var result = await controller.Details(1);

            var viewResult = Assert.IsType<ViewResult>(result);
            Assert.Equal(item, viewResult.Model);
        }

        [Fact]
        public async Task Details_WithNullId_ReturnsNotFound()
        {
            var controller = CreateController();

            var result = await controller.Details(null);

            Assert.IsType<NotFoundResult>(result);
        }
    }
}
