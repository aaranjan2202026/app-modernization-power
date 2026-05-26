using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using PharmacyNetwork.ApplicationCore.Constants;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Infrastructure.Data;

namespace PharmacyNetwork.Web.Controllers
{
    [Authorize]
    public class ProductCategoriesController : Controller
    {
        private readonly IProductCategoryService _productCategoryService;

        public ProductCategoriesController(IProductCategoryService productCategoryService)
        {
            _productCategoryService = productCategoryService;
        }

        // GET: ProductCategories
        public async Task<IActionResult> Index()
        {
            var productCategories= await _productCategoryService.GetAllProductCategoriesAsync();
            return View(productCategories);
        }

        // GET: ProductCategories/Create
        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public IActionResult Create() => View();

        // POST: ProductCategories/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public async Task<IActionResult> Create(ProductCategory productCategory)
        {
            if (!ModelState.IsValid) return View(productCategory);

            await _productCategoryService.CreateProductCategoryAsync(productCategory);
            return RedirectToAction(nameof(Index));
        }

        // GET: ProductCategories/Edit/5
        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null) return NotFound();

            var productCategory = await _productCategoryService.GetProductCategoryByIdAsync(id);
            if (productCategory == null) return NotFound();
            
            return View(productCategory);
        }

        // POST: ProductCategories/Edit/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public async Task<IActionResult> Edit(ProductCategory productCategory)
        {
            if (ModelState.IsValid)
            {
                try
                {
                    await _productCategoryService.UpdateProductCategoryAsync(productCategory);
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!await _productCategoryService.ProductCategoryExistsAsync(productCategory.CategId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            return View(productCategory);
        }

        // GET: ProductCategories/Delete/5
        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null) return NotFound();

            var productCategory = await _productCategoryService.GetProductCategoryByIdAsync(id);
            if (productCategory == null) return NotFound();

            return View(productCategory);
        }

        // POST: ProductCategories/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            await _productCategoryService.DeleteProductCategoryAsync(id);
            return RedirectToAction(nameof(Index));
        }
    }
}
