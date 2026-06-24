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
    /// TASK-027: Unit tests for MedicalItemsController async cancellation.
    /// </summary>
    public class MedicalItemsControllerTests
    {
        private readonly Mock<IAsyncRepository<MedicalItem>> _mockRepo;
        private readonly Mock<MediatR.IMediator> _mockMediator;

        public MedicalItemsControllerTests()
        {
            _mockRepo = new Mock<IAsyncRepository<MedicalItem>>();
            _mockMediator = new Mock<MediatR.IMediator>();
        }

        private MedicalItemsController CreateController()
        {
            var controller = new MedicalItemsController(_mockRepo.Object, _mockMediator.Object);
            controller.ControllerContext = new ControllerContext
            {
                HttpContext = new DefaultHttpContext()
            };
            return controller;
        }

        [Fact]
        public async Task Create_POST_ValidModel_AddsAndRedirects()
        {
            var medItem = new MedicalItem { MedItemId = 1, MedItemName = "Aspirin" };
            var viewModel = new Web.ViewModels.MedicalItemViewModel { MedicalItem = medItem };
            _mockRepo.Setup(r => r.AddAsync(medItem, It.IsAny<CancellationToken>())).ReturnsAsync(medItem);
            var controller = CreateController();

            var result = await controller.Create(viewModel);

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
        }

        [Fact]
        public async Task Create_POST_InvalidModel_ReturnsView()
        {
            var controller = CreateController();
            controller.ModelState.AddModelError("MedItemName", "Required");

            var result = await controller.Create(new Web.ViewModels.MedicalItemViewModel());

            Assert.IsType<ViewResult>(result);
        }

        [Fact]
        public async Task DeleteConfirmed_WithValidId_DeletesAndRedirects()
        {
            var medItem = new MedicalItem { MedItemId = 1, MedItemName = "Test" };
            _mockRepo.Setup(r => r.GetByIdAsync(1, It.IsAny<CancellationToken>())).ReturnsAsync(medItem);
            _mockRepo.Setup(r => r.DeleteAsync(medItem, It.IsAny<CancellationToken>())).Returns(Task.CompletedTask);
            var controller = CreateController();

            var result = await controller.DeleteConfirmed(1);

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
        }
    }
}
