using System.Collections.Generic;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;

namespace PharmacyNetwork.ApplicationCore.Interfaces
{
    /// <summary>
    /// Service interface for Pharmacy business logic operations.
    /// Provides abstraction layer between controllers and data access.
    /// </summary>
    public interface IPharmacyService
    {
        /// <summary>
        /// Retrieve all pharmacies from the database
        /// </summary>
        Task<IEnumerable<Pharmacy>> GetAllPharmaciesAsync();

        /// <summary>
        /// Retrieve a specific pharmacy by ID
        /// </summary>
        Task<Pharmacy> GetPharmacyByIdAsync(int? id);

        /// <summary>
        /// Create a new pharmacy
        /// </summary>
        Task<Pharmacy> CreatePharmacyAsync(Pharmacy pharmacy);

        /// <summary>
        /// Update an existing pharmacy
        /// </summary>
        Task UpdatePharmacyAsync(Pharmacy pharmacy);

        /// <summary>
        /// Delete a pharmacy by ID
        /// </summary>
        Task DeletePharmacyAsync(int id);

        /// <summary>
        /// Check if a pharmacy exists by ID
        /// </summary>
        Task<bool> PharmacyExistsAsync(int id);
    }
}
