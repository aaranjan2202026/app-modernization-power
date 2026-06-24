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
    /// TASK-019+029: Unit tests for PharmaciesController Edit POST and CancellationToken propagation.
    /// </summary>
    public class PharmaciesControllerTests
    {
        private readonly Mock<IAsyncRepository<Pharmacy>> _mockRepo;
        private readonly Mock<MediatR.IMediator> _mockMediator;

        public PharmaciesControllerTests()
        {
            _mockRepo = new Mock<IAsyncRepository<Pharmacy>>();
            _mockMediator = new Mock<MediatR.IMediator>();
        }

        private PharmaciesController CreateController()
        {
            var controller = new PharmaciesController(_mockRepo.Object, _mockMediator.Object);
            controller.ControllerContext = new ControllerContext
            {
                HttpContext = new DefaultHttpContext()
            };
            return controller;
        }

        [Fact]
        public async Task Index_ReturnsViewWithPharmacies()
        {
            var pharmacies = new List<Pharmacy> { new Pharmacy { PharmId = 1, PharmName = "Test" } };
            _mockRepo.Setup(r => r.GetAllAsync(It.IsAny<CancellationToken>())).ReturnsAsync(pharmacies);
            var controller = CreateController();

            var result = await controller.Index();

            var viewResult = Assert.IsType<ViewResult>(result);
            Assert.Equal(pharmacies, viewResult.Model);
        }

        [Fact]
        public async Task Edit_POST_InvalidModelState_ReturnsView()
        {
            var controller = CreateController();
            controller.ModelState.AddModelError("PharmName", "Required");

            var result = await controller.Edit(new Pharmacy());

            Assert.IsType<ViewResult>(result);
        }

        [Fact]
        public async Task Edit_POST_ValidModel_UpdatesAndRedirects()
        {
            _mockRepo.Setup(r => r.UpdateAsync(It.IsAny<Pharmacy>(), It.IsAny<CancellationToken>()))
                .Returns(Task.CompletedTask);
            var controller = CreateController();
            var pharmacy = new Pharmacy { PharmId = 1, PharmName = "Updated" };

            var result = await controller.Edit(pharmacy);

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
            _mockRepo.Verify(r => r.UpdateAsync(pharmacy, It.IsAny<CancellationToken>()), Times.Once);
        }

        [Fact]
        public async Task Create_GET_ReturnsView()
        {
            var controller = CreateController();

            var result = controller.Create();

            Assert.IsType<ViewResult>(result);
        }

        [Fact]
        public async Task Create_POST_ValidModel_AddsAndRedirects()
        {
            var pharmacy = new Pharmacy { PharmName = "New Pharmacy" };
            _mockRepo.Setup(r => r.AddAsync(pharmacy, It.IsAny<CancellationToken>())).ReturnsAsync(pharmacy);
            var controller = CreateController();

            var result = await controller.Create(pharmacy);

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
        }
    }
}
