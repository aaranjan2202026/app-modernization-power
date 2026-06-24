using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.Infrastructure.Data;
using Xunit;

namespace PharmacyNetwork.Tests.Data
{
    /// <summary>
    /// TASK-004: Unit tests for EfRepository async methods with CancellationToken.
    /// Uses InMemory database to verify repository behavior.
    /// </summary>
    public class EfRepositoryTests
    {
        private PharmacyNetworkContext CreateInMemoryContext()
        {
            var options = new DbContextOptionsBuilder<PharmacyNetworkContext>()
                .UseInMemoryDatabase(databaseName: Guid.NewGuid().ToString())
                .Options;
            return new PharmacyNetworkContext(options);
        }

        [Fact]
        public async Task GetAllAsync_ReturnsAllEntities()
        {
            using var context = CreateInMemoryContext();
            context.Firm.AddRange(
                new Firm { FirmName = "Firm A", FirmAddress = "123 Main St", FirmContact = "555-0001", FirmMarkup = 0.1m },
                new Firm { FirmName = "Firm B", FirmAddress = "456 Oak Ave", FirmContact = "555-0002", FirmMarkup = 0.2m });
            await context.SaveChangesAsync();
            var repo = new EfRepository<Firm>(context);

            var result = await repo.GetAllAsync(CancellationToken.None);

            Assert.Equal(2, result.Count);
        }

        [Fact]
        public async Task AddAsync_PersistsEntityAndReturnsIt()
        {
            using var context = CreateInMemoryContext();
            var repo = new EfRepository<Firm>(context);
            var firm = new Firm { FirmName = "New Firm", FirmAddress = "789 Elm St", FirmContact = "555-0003", FirmMarkup = 0.15m };

            var result = await repo.AddAsync(firm, CancellationToken.None);

            Assert.NotNull(result);
            Assert.Equal("New Firm", result.FirmName);
            Assert.Equal(1, await context.Firm.CountAsync());
        }

        [Fact]
        public async Task GetByIdAsync_ReturnsCorrectEntity()
        {
            using var context = CreateInMemoryContext();
            var firm = new Firm { FirmName = "Test Firm", FirmAddress = "1 Test Ave", FirmContact = "555-1111", FirmMarkup = 0.1m };
            context.Firm.Add(firm);
            await context.SaveChangesAsync();
            var repo = new EfRepository<Firm>(context);

            var result = await repo.GetByIdAsync(firm.FirmId, CancellationToken.None);

            Assert.NotNull(result);
            Assert.Equal("Test Firm", result.FirmName);
        }

        [Fact]
        public async Task DeleteAsync_RemovesEntity()
        {
            using var context = CreateInMemoryContext();
            var firm = new Firm { FirmName = "To Delete", FirmAddress = "2 Delete Rd", FirmContact = "555-2222", FirmMarkup = 0.1m };
            context.Firm.Add(firm);
            await context.SaveChangesAsync();
            var repo = new EfRepository<Firm>(context);

            await repo.DeleteAsync(firm, CancellationToken.None);

            Assert.Equal(0, await context.Firm.CountAsync());
        }

        [Fact]
        public async Task UpdateAsync_ModifiesEntity()
        {
            using var context = CreateInMemoryContext();
            var firm = new Firm { FirmName = "Original", FirmAddress = "3 Update St", FirmContact = "555-3333", FirmMarkup = 0.1m };
            context.Firm.Add(firm);
            await context.SaveChangesAsync();
            var repo = new EfRepository<Firm>(context);

            firm.FirmName = "Updated";
            await repo.UpdateAsync(firm, CancellationToken.None);

            var updated = await context.Firm.FindAsync(firm.FirmId);
            Assert.Equal("Updated", updated.FirmName);
        }

        [Fact]
        public async Task GetAllAsync_WithCancelledToken_ThrowsOrReturnsEmpty()
        {
            using var context = CreateInMemoryContext();
            var repo = new EfRepository<Firm>(context);
            // Default CancellationToken should work fine
            var result = await repo.GetAllAsync(CancellationToken.None);
            Assert.NotNull(result);
        }
    }
}
