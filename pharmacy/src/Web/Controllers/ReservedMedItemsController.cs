using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using MediatR;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using PharmacyNetwork.ApplicationCore.Constants;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.ApplicationCore.Specifications;
using PharmacyNetwork.Infrastructure.Data;
using PharmacyNetwork.Web.Features.AppUserClient;

namespace PharmacyNetwork.Web.Controllers
{
    public class ReservedMedItemsController : Controller
    {
        private readonly IAsyncRepository<ReservedMedItem> _repository;
        private readonly IMediator _mediator;

        public ReservedMedItemsController(IAsyncRepository<ReservedMedItem> repository, IMediator mediator)
        {
            _repository = repository;
            _mediator = mediator;
        }

        // GET: ReservedMedItems
        public async Task<IActionResult> Index()
        {
            if (!ModelState.IsValid) return BadRequest(ModelState);
            if (User.IsInRole(AuthorizationConstants.Roles.USERS))
            {
                var idPharm = await _mediator.Send(new GetPharmIdByUser(User));
                var reservedMedItemsList = await _repository.ListAsync(new ReserveMedItemsByPharmacySpecification(idPharm), HttpContext.RequestAborted);
                return View(reservedMedItemsList);
            }

            var reservedItemsList = await _repository.GetAllAsync(HttpContext.RequestAborted);
            return View(reservedItemsList);
        }

        // GET: ReservedMedItems/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (!ModelState.IsValid) return BadRequest(ModelState);
            if (id == null) return NotFound();

            var reserved = await _repository.GetByIdAsync(id, HttpContext.RequestAborted);
            if (reserved == null) return NotFound();

            return View(reserved);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Remove(int? id)
        {
            if (id == null) return NotFound();

            try
            {
                var reservedItem = await _repository.GetByIdAsync(id.Value, HttpContext.RequestAborted);
                if (reservedItem == null) return NotFound();

                await _repository.DeleteAsync(reservedItem, HttpContext.RequestAborted);
                
            }
            catch (Exception)
            {
                return RedirectToAction(nameof(Index));
            }

            return RedirectToAction(nameof(Index));
        }
    }
}
