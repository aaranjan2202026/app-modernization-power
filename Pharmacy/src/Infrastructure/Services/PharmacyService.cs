using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;

namespace PharmacyNetwork.Infrastructure.Services
{
    /// <summary>
    /// Service implementation for Pharmacy business logic.
    /// Encapsulates business rules and data access for Pharmacy entities.
    /// </summary>
    public class PharmacyService : IPharmacyService
    {
        private readonly IAsyncRepository<Pharmacy> _repository;

        public PharmacyService(IAsyncRepository<Pharmacy> repository)
        {
            _repository = repository;
        }

        public async Task<IEnumerable<Pharmacy>> GetAllPharmaciesAsync()
        {
            return await _repository.GetAllAsync();
        }

        public async Task<Pharmacy> GetPharmacyByIdAsync(int? id)
        {
            if (!id.HasValue) return null;
            return await _repository.GetByIdAsync(id.Value);
        }

        public async Task<Pharmacy> CreatePharmacyAsync(Pharmacy pharmacy)
        {
            return await _repository.AddAsync(pharmacy);
        }

        public async Task UpdatePharmacyAsync(Pharmacy pharmacy)
        {
            await _repository.UpdateAsync(pharmacy);
        }

        public async Task DeletePharmacyAsync(int id)
        {
            var pharmacy = await _repository.GetByIdAsync(id);
            if (pharmacy != null)
            {
                await _repository.DeleteAsync(pharmacy);
            }
        }

        public async Task<bool> PharmacyExistsAsync(int id)
        {
            var pharmacies = await _repository.GetAllAsync();
            return pharmacies.Any(p => p.PharmId == id);
        }
    }
}
