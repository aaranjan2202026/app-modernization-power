using System.Collections.Generic;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;

namespace PharmacyNetwork.ApplicationCore.Interfaces
{
    /// <summary>
    /// Service interface for ProductCategory business logic operations.
    /// Provides abstraction layer between controllers and data access.
    /// </summary>
    public interface IProductCategoryService
    {
        /// <summary>
        /// Retrieve all product categories from the database
        /// </summary>
        Task<IEnumerable<ProductCategory>> GetAllProductCategoriesAsync();

        /// <summary>
        /// Retrieve a specific product category by ID
        /// </summary>
        Task<ProductCategory> GetProductCategoryByIdAsync(int? id);

        /// <summary>
        /// Create a new product category
        /// </summary>
        Task<ProductCategory> CreateProductCategoryAsync(ProductCategory productCategory);

        /// <summary>
        /// Update an existing product category
        /// </summary>
        Task UpdateProductCategoryAsync(ProductCategory productCategory);

        /// <summary>
        /// Delete a product category by ID
        /// </summary>
        Task DeleteProductCategoryAsync(int id);

        /// <summary>
        /// Check if a product category exists by ID
        /// </summary>
        Task<bool> ProductCategoryExistsAsync(int id);
    }
}
