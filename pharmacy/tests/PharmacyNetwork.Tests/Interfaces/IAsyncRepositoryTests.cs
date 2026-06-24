using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Moq;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using Xunit;

namespace PharmacyNetwork.Tests.Interfaces
{
    /// <summary>
    /// TASK-002: Unit tests for IAsyncRepository contract validation.
    /// Verifies that the interface contract correctly declares CancellationToken on all methods.
    /// </summary>
    public class IAsyncRepositoryTests
    {
        private readonly Mock<IAsyncRepository<Firm>> _mockRepo;

        public IAsyncRepositoryTests()
        {
            _mockRepo = new Mock<IAsyncRepository<Firm>>();
        }

        [Fact]
        public async Task GetByIdAsync_WithCancellationToken_CallsRepositoryWithToken()
        {
            var cts = new CancellationTokenSource();
            var expectedFirm = new Firm { FirmId = 1, FirmName = "Test Firm" };
            _mockRepo.Setup(r => r.GetByIdAsync(1, cts.Token)).ReturnsAsync(expectedFirm);

            var result = await _mockRepo.Object.GetByIdAsync(1, cts.Token);

            Assert.Equal(expectedFirm, result);
            _mockRepo.Verify(r => r.GetByIdAsync(1, cts.Token), Times.Once);
        }

        [Fact]
        public async Task GetAllAsync_WithCancellationToken_CallsRepositoryWithToken()
        {
            var cts = new CancellationTokenSource();
            var firms = new List<Firm> { new Firm { FirmId = 1 } };
            _mockRepo.Setup(r => r.GetAllAsync(cts.Token)).ReturnsAsync(firms);

            var result = await _mockRepo.Object.GetAllAsync(cts.Token);

            Assert.Equal(firms, result);
            _mockRepo.Verify(r => r.GetAllAsync(cts.Token), Times.Once);
        }

        [Fact]
        public async Task AddAsync_WithCancellationToken_CallsRepositoryWithToken()
        {
            var cts = new CancellationTokenSource();
            var firm = new Firm { FirmId = 1, FirmName = "New Firm" };
            _mockRepo.Setup(r => r.AddAsync(firm, cts.Token)).ReturnsAsync(firm);

            var result = await _mockRepo.Object.AddAsync(firm, cts.Token);

            Assert.Equal(firm, result);
            _mockRepo.Verify(r => r.AddAsync(firm, cts.Token), Times.Once);
        }

        [Fact]
        public async Task UpdateAsync_WithCancellationToken_CallsRepositoryWithToken()
        {
            var cts = new CancellationTokenSource();
            var firm = new Firm { FirmId = 1, FirmName = "Updated Firm" };
            _mockRepo.Setup(r => r.UpdateAsync(firm, cts.Token)).Returns(Task.CompletedTask);

            await _mockRepo.Object.UpdateAsync(firm, cts.Token);

            _mockRepo.Verify(r => r.UpdateAsync(firm, cts.Token), Times.Once);
        }

        [Fact]
        public async Task DeleteAsync_WithCancellationToken_CallsRepositoryWithToken()
        {
            var cts = new CancellationTokenSource();
            var firm = new Firm { FirmId = 1 };
            _mockRepo.Setup(r => r.DeleteAsync(firm, cts.Token)).Returns(Task.CompletedTask);

            await _mockRepo.Object.DeleteAsync(firm, cts.Token);

            _mockRepo.Verify(r => r.DeleteAsync(firm, cts.Token), Times.Once);
        }

        [Fact]
        public async Task GetByIdAsync_DefaultToken_CallsRepositoryWithDefaultToken()
        {
            var expectedFirm = new Firm { FirmId = 1 };
            _mockRepo.Setup(r => r.GetByIdAsync(1, default)).ReturnsAsync(expectedFirm);

            var result = await _mockRepo.Object.GetByIdAsync(1);

            Assert.Equal(expectedFirm, result);
        }
    }
}
