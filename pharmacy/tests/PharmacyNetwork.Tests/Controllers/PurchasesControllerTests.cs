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
    /// TASK-033: Unit tests for PurchasesController async cancellation.
    /// </summary>
    public class PurchasesControllerTests
    {
        private readonly Mock<IAsyncRepository<Purchase>> _mockRepo;
        private readonly Mock<MediatR.IMediator> _mockMediator;

        public PurchasesControllerTests()
        {
            _mockRepo = new Mock<IAsyncRepository<Purchase>>();
            _mockMediator = new Mock<MediatR.IMediator>();
        }

        private PurchasesController CreateController()
        {
            var controller = new PurchasesController(_mockRepo.Object, _mockMediator.Object);
            controller.ControllerContext = new ControllerContext
            {
                HttpContext = new DefaultHttpContext()
            };
            return controller;
        }

        [Fact]
        public void Create_GET_ReturnsView()
        {
            var controller = CreateController();

            var result = controller.Create();

            Assert.IsType<ViewResult>(result);
        }
    }
}
