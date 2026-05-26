using System.Collections.Generic;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;

namespace PharmacyNetwork.ApplicationCore.Interfaces
{
    /// <summary>
    /// Service interface for Firm business logic operations.
    /// Provides abstraction layer between controllers and data access.
    /// </summary>
    public interface IFirmService
    {
        /// <summary>
        /// Retrieve all firms from the database
        /// </summary>
        Task<IEnumerable<Firm>> GetAllFirmsAsync();

        /// <summary>
        /// Retrieve a specific firm by ID
        /// </summary>
        Task<Firm> GetFirmByIdAsync(int? id);

        /// <summary>
        /// Create a new firm
        /// </summary>
        Task<Firm> CreateFirmAsync(Firm firm);

        /// <summary>
        /// Update an existing firm
        /// </summary>
        Task UpdateFirmAsync(Firm firm);

        /// <summary>
        /// Delete a firm by ID
        /// </summary>
        Task DeleteFirmAsync(int id);

        /// <summary>
        /// Check if a firm exists by ID
        /// </summary>
        Task<bool> FirmExistsAsync(int id);
    }
}
