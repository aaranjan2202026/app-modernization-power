using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using PharmacyNetwork.ApplicationCore.Interfaces;

namespace PharmacyNetwork.Infrastructure.Data
{
    public class EfRepository<T> : IAsyncRepository<T> where T : class
    {
        protected readonly PharmacyNetworkContext Context;

        public EfRepository(PharmacyNetworkContext context)
        {
            Context = context;
        }

        public async Task<T> GetByIdAsync(int? id, CancellationToken cancellationToken = default)
        {
            return await Context.Set<T>().FindAsync(new object[] { id }, cancellationToken).ConfigureAwait(false);
        }

        public async Task<List<T>> GetAllAsync(CancellationToken cancellationToken = default)
        {
            return await Context.Set<T>().ToListAsync(cancellationToken).ConfigureAwait(false);
        }

        public async Task<List<T>> ListAsync(ISpecification<T> spec, CancellationToken cancellationToken = default)
        {
            return await ApplySpecification(spec).ToListAsync(cancellationToken).ConfigureAwait(false);
        }

        public async Task<T> AddAsync(T entity, CancellationToken cancellationToken = default)
        {
            await Context.Set<T>().AddAsync(entity, cancellationToken).ConfigureAwait(false);
            await Context.SaveChangesAsync(cancellationToken).ConfigureAwait(false);

            return entity;
        }

        public async Task UpdateAsync(T entity, CancellationToken cancellationToken = default)
        {
            Context.Entry(entity).State = EntityState.Modified;
            await Context.SaveChangesAsync(cancellationToken).ConfigureAwait(false);
        }

        public async Task DeleteAsync(T entity, CancellationToken cancellationToken = default)
        {
            Context.Set<T>().Remove(entity);
            await Context.SaveChangesAsync(cancellationToken).ConfigureAwait(false);
        }

        public async Task ExecuteSqlRawAsync(string query, CancellationToken cancellationToken = default)
        {
            await Context.Database.ExecuteSqlRawAsync(query, cancellationToken).ConfigureAwait(false);
        }

        private IQueryable<T> ApplySpecification(ISpecification<T> spec)
        {
            return SpecificationEvaluator<T>.GetQuery(Context.Set<T>().AsQueryable(), spec);
        }
    }
}
