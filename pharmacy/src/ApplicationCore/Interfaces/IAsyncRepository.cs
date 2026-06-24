using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace PharmacyNetwork.ApplicationCore.Interfaces
{
    public interface IAsyncRepository<T>
    {
        Task<T> GetByIdAsync(int? id, CancellationToken cancellationToken = default);

        Task<List<T>> GetAllAsync(CancellationToken cancellationToken = default);

        Task<List<T>> ListAsync(ISpecification<T> spec, CancellationToken cancellationToken = default);

        Task<T> AddAsync(T entity, CancellationToken cancellationToken = default);

        Task UpdateAsync(T entity, CancellationToken cancellationToken = default);

        Task DeleteAsync(T entity, CancellationToken cancellationToken = default);

        Task ExecuteSqlRawAsync(string query, CancellationToken cancellationToken = default);
    }
}
