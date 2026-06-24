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
    /// TASK-021+035: Unit tests for ProductCategoriesController - duplicate guard fix + CancellationToken.
    /// </summary>
    public class ProductCategoriesControllerTests
    {
        private readonly Mock<IAsyncRepository<ProductCategory>> _mockRepo;

        public ProductCategoriesControllerTests()
        {
            _mockRepo = new Mock<IAsyncRepository<ProductCategory>>();
        }

        private ProductCategoriesController CreateController()
        {
            var controller = new ProductCategoriesController(_mockRepo.Object);
            controller.ControllerContext = new ControllerContext
            {
                HttpContext = new DefaultHttpContext()
            };
            return controller;
        }

        [Fact]
        public async Task Create_POST_ValidModel_AddsOnceAndRedirects()
        {
            // TASK-020: Verifies the duplicate guard was removed — AddAsync called only once
            var category = new ProductCategory { CategName = "Test Category" };
            _mockRepo.Setup(r => r.AddAsync(category, It.IsAny<CancellationToken>())).ReturnsAsync(category);
            var controller = CreateController();

            var result = await controller.Create(category);

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
            _mockRepo.Verify(r => r.AddAsync(category, It.IsAny<CancellationToken>()), Times.Once);
        }

        [Fact]
        public async Task Create_POST_InvalidModel_ReturnsViewOnce()
        {
            var controller = CreateController();
            controller.ModelState.AddModelError("CategName", "Required");

            var result = await controller.Create(new ProductCategory());

            Assert.IsType<ViewResult>(result);
            // Ensure repository was never called
            _mockRepo.Verify(r => r.AddAsync(It.IsAny<ProductCategory>(), It.IsAny<CancellationToken>()), Times.Never);
        }

        [Fact]
        public async Task Index_ReturnsViewWithCategories()
        {
            var categories = new List<ProductCategory> { new ProductCategory { CategId = 1, CategName = "Cat A" } };
            _mockRepo.Setup(r => r.GetAllAsync(It.IsAny<CancellationToken>())).ReturnsAsync(categories);
            var controller = CreateController();

            var result = await controller.Index();

            var viewResult = Assert.IsType<ViewResult>(result);
            Assert.Equal(categories, viewResult.Model);
        }

        [Fact]
        public async Task Edit_POST_InvalidModel_ReturnsView()
        {
            var controller = CreateController();
            controller.ModelState.AddModelError("CategName", "Required");

            var result = await controller.Edit(new ProductCategory());

            Assert.IsType<ViewResult>(result);
        }

        [Fact]
        public async Task Edit_POST_ValidModel_UpdatesAndRedirects()
        {
            _mockRepo.Setup(r => r.UpdateAsync(It.IsAny<ProductCategory>(), It.IsAny<CancellationToken>()))
                .Returns(Task.CompletedTask);
            var controller = CreateController();

            var result = await controller.Edit(new ProductCategory { CategId = 1, CategName = "Updated" });

            var redirect = Assert.IsType<RedirectToActionResult>(result);
            Assert.Equal("Index", redirect.ActionName);
        }
    }
}
