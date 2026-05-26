using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;

namespace PharmacyNetwork.Infrastructure.Services
{
    /// <summary>
    /// Service implementation for Firm business logic.
    /// Encapsulates business rules and data access for Firm entities.
    /// </summary>
    public class FirmService : IFirmService
    {
        private readonly IAsyncRepository<Firm> _repository;

        public FirmService(IAsyncRepository<Firm> repository)
        {
            _repository = repository;
        }

        public async Task<IEnumerable<Firm>> GetAllFirmsAsync()
        {
            return await _repository.GetAllAsync();
        }

        public async Task<Firm> GetFirmByIdAsync(int? id)
        {
            if (!id.HasValue) return null;
            return await _repository.GetByIdAsync(id.Value);
        }

        public async Task<Firm> CreateFirmAsync(Firm firm)
        {
            return await _repository.AddAsync(firm);
        }

        public async Task UpdateFirmAsync(Firm firm)
        {
            await _repository.UpdateAsync(firm);
        }

        public async Task DeleteFirmAsync(int id)
        {
            var firm = await _repository.GetByIdAsync(id);
            if (firm != null)
            {
                await _repository.DeleteAsync(firm);
            }
        }

        public async Task<bool> FirmExistsAsync(int id)
        {
            var firms = await _repository.GetAllAsync();
            return firms.Any(f => f.FirmId == id);
        }
    }
}
