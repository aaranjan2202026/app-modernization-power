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
    /// TASK-025: Unit tests for FirmsController async cancellation.
    /// </summary>
    public class FirmsControllerTests
    {
        private readonly Mock<IAsyncRepository<Firm>> _mockRepo;

        public FirmsControllerTests()
        {
            _mockRepo = new Mock<IAsyncRepository<Firm>>();
        }

        private FirmsController CreateController()
        {
            var controller = new FirmsController(_mockRepo.Object);
            controller.ControllerContext = new ControllerContext
            {
                HttpContext = new DefaultHttpContext()
            };
            return controller;
        }

        [Fact]
        public async Task Index_ReturnsViewWithFirms()
        {
            var firms = new List<Firm> { new Firm { FirmId = 1, FirmName = "Test Firm" } };
            _mockRepo.Setup(r => r.GetAllAsync(It.IsAny<CancellationToken>())).ReturnsAsync(firms);
            var controller = CreateController();

            var result = await controller.Index();

            var viewResult = Assert.IsType<ViewResult>(result);
            Assert.Equal(firms, viewResult.Model);
            _mockRepo.Verify(r => r.GetAllAsync(It.IsAny<CancellationToken>()), Times.Once);
        }

        [Fact]
        public async Task Details_WithValidId_ReturnsView()
        {
            var firm = new Firm { FirmId = 1, FirmName = "Firm A" };
            _mockRepo.Setup(r => r.GetByIdAsync(1, It.IsAny<CancellationToken>())).ReturnsAsync(firm);
            var controller = CreateController();

            var result = await controller.Details(1);

            var viewResult = Assert.IsType<ViewResult>(result);
            Assert.Equal(firm, viewResult.Model);
        }

        [Fact]
        public async Task Details_WithNullId_ReturnsNotFound()
        {
            var controller = CreateController();

            var result = await controller.Details(null);

            Assert.IsType<NotFoundResult>(result);
        }

        [Fact]
        public async Task Create_POST_ValidFirm_AddsAndRedirects()
        {
            var firm = new Firm { FirmName = "New Firm", FirmAddress = "123 Main St", FirmContact = "555-0001", FirmMarkup = 0.1m };
            _mockRepo.Setup(r => r.AddAsync(firm, It.IsAny<CancellationToken>())).ReturnsAsync(firm);
            var controller = CreateController();

            var result = await controller.Create(firm);

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
            _mockRepo.Verify(r => r.AddAsync(firm, It.IsAny<CancellationToken>()), Times.Once);
        }

        [Fact]
        public async Task Create_POST_InvalidModel_ReturnsView()
        {
            var controller = CreateController();
            controller.ModelState.AddModelError("FirmName", "Required");

            var result = await controller.Create(new Firm());

            Assert.IsType<ViewResult>(result);
        }

        [Fact]
        public async Task Edit_GET_WithValidId_ReturnsView()
        {
            var firm = new Firm { FirmId = 1, FirmName = "Edit Firm" };
            _mockRepo.Setup(r => r.GetByIdAsync(1, It.IsAny<CancellationToken>())).ReturnsAsync(firm);
            var controller = CreateController();

            var result = await controller.Edit(1);

            var viewResult = Assert.IsType<ViewResult>(result);
            Assert.Equal(firm, viewResult.Model);
        }
    }
}
