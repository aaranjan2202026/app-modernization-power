using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PharmacyNetwork.Web.Controllers;
using Xunit;

namespace PharmacyNetwork.Tests.Controllers
{
    /// <summary>
    /// TASK-039+041: Unit tests for PharmacyWharehousesController and HomeController.
    /// </summary>
    public class PharmacyWharehousesControllerTests
    {
        [Fact]
        public void PharmacyWharehousesController_CanBeInstantiated()
        {
            var mockMediator = new Moq.Mock<MediatR.IMediator>();
            var controller = new PharmacyWharehousesController(mockMediator.Object);
            controller.ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() };

            Assert.NotNull(controller);
        }
    }

    /// <summary>
    /// TASK-041: Unit tests for HomeController.
    /// </summary>
    public class HomeControllerTests
    {
        [Fact]
        public void Index_ReturnsViewResult()
        {
            var controller = new HomeController();

            var result = controller.Index();

            Assert.IsType<ViewResult>(result);
        }
    }
}
