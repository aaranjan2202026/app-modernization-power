using System.Collections.Generic;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;

namespace PharmacyNetwork.ApplicationCore.Interfaces
{
    /// <summary>
    /// Service interface for MedicalItem business logic operations.
    /// Provides abstraction layer between controllers and data access.
    /// </summary>
    public interface IMedicalItemService
    {
        /// <summary>
        /// Retrieve all medical items from the database
        /// </summary>
        Task<IEnumerable<MedicalItem>> GetAllMedicalItemsAsync();

        /// <summary>
        /// Retrieve a specific medical item by ID
        /// </summary>
        Task<MedicalItem> GetMedicalItemByIdAsync(int? id);

        /// <summary>
        /// Create a new medical item
        /// </summary>
        Task<MedicalItem> CreateMedicalItemAsync(MedicalItem medicalItem);

        /// <summary>
        /// Update an existing medical item
        /// </summary>
        Task UpdateMedicalItemAsync(MedicalItem medicalItem);

        /// <summary>
        /// Delete a medical item by ID
        /// </summary>
        Task DeleteMedicalItemAsync(int id);

        /// <summary>
        /// Check if a medical item exists by ID
        /// </summary>
        Task<bool> MedicalItemExistsAsync(int id);
    }
}
