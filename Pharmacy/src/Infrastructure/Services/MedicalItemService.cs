using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;

namespace PharmacyNetwork.Infrastructure.Services
{
    /// <summary>
    /// Service implementation for MedicalItem business logic.
    /// Encapsulates business rules and data access for MedicalItem entities.
    /// </summary>
    public class MedicalItemService : IMedicalItemService
    {
        private readonly IAsyncRepository<MedicalItem> _repository;

        public MedicalItemService(IAsyncRepository<MedicalItem> repository)
        {
            _repository = repository;
        }

        public async Task<IEnumerable<MedicalItem>> GetAllMedicalItemsAsync()
        {
            return await _repository.GetAllAsync();
        }

        public async Task<MedicalItem> GetMedicalItemByIdAsync(int? id)
        {
            if (!id.HasValue) return null;
            return await _repository.GetByIdAsync(id.Value);
        }

        public async Task<MedicalItem> CreateMedicalItemAsync(MedicalItem medicalItem)
        {
            return await _repository.AddAsync(medicalItem);
        }

        public async Task UpdateMedicalItemAsync(MedicalItem medicalItem)
        {
            await _repository.UpdateAsync(medicalItem);
        }

        public async Task DeleteMedicalItemAsync(int id)
        {
            var medicalItem = await _repository.GetByIdAsync(id);
            if (medicalItem != null)
            {
                await _repository.DeleteAsync(medicalItem);
            }
        }

        public async Task<bool> MedicalItemExistsAsync(int id)
        {
            var medicalItems = await _repository.GetAllAsync();
            return medicalItems.Any(m => m.MedItemId == id);
        }
    }
}
