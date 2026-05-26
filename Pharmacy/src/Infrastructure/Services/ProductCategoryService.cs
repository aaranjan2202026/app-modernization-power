using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;

namespace PharmacyNetwork.Infrastructure.Services
{
    /// <summary>
    /// Service implementation for ProductCategory business logic.
    /// Encapsulates business rules and data access for ProductCategory entities.
    /// </summary>
    public class ProductCategoryService : IProductCategoryService
    {
        private readonly IAsyncRepository<ProductCategory> _repository;

        public ProductCategoryService(IAsyncRepository<ProductCategory> repository)
        {
            _repository = repository;
        }

        public async Task<IEnumerable<ProductCategory>> GetAllProductCategoriesAsync()
        {
            return await _repository.GetAllAsync();
        }

        public async Task<ProductCategory> GetProductCategoryByIdAsync(int? id)
        {
            if (!id.HasValue) return null;
            return await _repository.GetByIdAsync(id.Value);
        }

        public async Task<ProductCategory> CreateProductCategoryAsync(ProductCategory productCategory)
        {
            return await _repository.AddAsync(productCategory);
        }

        public async Task UpdateProductCategoryAsync(ProductCategory productCategory)
        {
            await _repository.UpdateAsync(productCategory);
        }

        public async Task DeleteProductCategoryAsync(int id)
        {
            var productCategory = await _repository.GetByIdAsync(id);
            if (productCategory != null)
            {
                await _repository.DeleteAsync(productCategory);
            }
        }

        public async Task<bool> ProductCategoryExistsAsync(int id)
        {
            var categories = await _repository.GetAllAsync();
            return categories.Any(c => c.CategId == id);
        }
    }
}
