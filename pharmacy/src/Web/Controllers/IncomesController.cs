using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Castle.Core.Internal;
using MediatR;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using PharmacyNetwork.ApplicationCore.Constants;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Infrastructure.Data;
using PharmacyNetwork.Web.Extensions;
using PharmacyNetwork.Web.Features.Incomes;
using PharmacyNetwork.Web.Models;
using PharmacyNetwork.Web.ViewModels;

namespace PharmacyNetwork.Web.Controllers
{
    [Authorize]
    public class IncomesController : Controller
    {
        private readonly IAsyncRepository<Income> _repository;
        private readonly IAsyncRepository<IncomeDetail> _incomeDetailRepository;
        private readonly IAsyncRepository<MedicalItem> _medicalItemRepository;
        private readonly IMediator _mediator;
        private readonly ILogger<IncomesController> _logger;

        public IncomesController(IAsyncRepository<Income> repository, 
            IAsyncRepository<IncomeDetail> incomeDetailRepository,
            IAsyncRepository<MedicalItem> medicalItemRepository,
            IMediator mediator,
            ILogger<IncomesController> logger)
        {
            _repository = repository;
            _incomeDetailRepository = incomeDetailRepository;
            _medicalItemRepository = medicalItemRepository;
            _mediator = mediator;
            _logger = logger;
        }

        // GET: Incomes
        public async Task<IActionResult> Index()
        {
            var incomes = await _repository.GetAllAsync();
            return View(incomes);
        }

        // GET: Incomes/Create
        public async Task<IActionResult> Create(int? pharmacyId)
        {
            var listIncomes = HttpContext.Session.Get<List<IncomeItem>>("Incomes");

            if (listIncomes == null || !listIncomes.Any())
            {
                listIncomes = new List<IncomeItem>();
                HttpContext.Session.Set<List<IncomeItem>>("Incomes", listIncomes);
            }

            var viewModel = await _mediator.Send(new GetIncomesCreateViewModel(listIncomes, pharmacyId));
            return View(viewModel);
        }

        // GET: Incomes/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null) return NotFound();

            var incomeDetailViewModel = await _mediator.Send(new GetIncomeDetail(id));
            if (incomeDetailViewModel.Income == null) return NotFound();

            return View(incomeDetailViewModel);
        }

        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public IActionResult AddToIncome(int medItemId, int count)
        {
            var incomeItem = new IncomeItem()
            {
                MedicalItemId = medItemId,
                Count = count
            };

            var incomeList = HttpContext.Session.Get<List<IncomeItem>>("Incomes");

            if (incomeList.Any(i => i.MedicalItemId == incomeItem.MedicalItemId))
            {
                incomeList.Find(i => i.MedicalItemId == incomeItem.MedicalItemId).Count += incomeItem.Count;
            }
            else
            {
                incomeList.Add(incomeItem);
            }
            
            HttpContext.Session.Set("Incomes", incomeList);

            return RedirectToAction(nameof(Create));
        }

        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public IActionResult DeleteFromIncome(int id)
        {
            var incomeList = HttpContext.Session.Get<List<IncomeItem>>("Incomes");

            incomeList.RemoveAll(i => i.MedicalItemId == id);

            HttpContext.Session.Set("Incomes", incomeList);

            return RedirectToAction(nameof(Create));
        }

        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        public async Task<IActionResult> CreateIncomes(int idPharm)
        {
            try
            {
                var incomeItems = HttpContext.Session.Get<List<IncomeItem>>("Incomes");

                if (incomeItems == null || !incomeItems.Any())
                {
                    return RedirectToAction(nameof(Create));
                }

                // Create the main Income record
                var newIncome = new Income
                {
                    PharmId = idPharm,
                    IncomeDate = DateTime.Now
                };

                var createdIncome = await _repository.AddAsync(newIncome);

                // Create income details for each item
                foreach (var item in incomeItems)
                {
                    // Get the medical item to calculate price
                    var medicalItem = await _medicalItemRepository.GetByIdAsync(item.MedicalItemId);
                    if (medicalItem != null)
                    {
                        var incomeDetail = new IncomeDetail
                        {
                            IncomeId = createdIncome.IncomeId,
                            MedItemId = item.MedicalItemId,
                            Count = item.Count,
                            Price = medicalItem.MedItemPrice * item.Count // Calculate total price
                        };

                        await _incomeDetailRepository.AddAsync(incomeDetail);
                    }
                }

                // Clear the session after successful creation
                ClearIncome();

                return RedirectToAction("Details", "Incomes", new { id = createdIncome.IncomeId });
            }
            catch (Exception ex)
            {
                // Previously swallowed silently, which hid the cause of failed
                // income creation from operators entirely.
                _logger.LogError(ex, "Failed to create income; redirecting back to Create.");
                return RedirectToAction(nameof(Create));
            }
        }

        [Authorize(Roles = AuthorizationConstants.Roles.ADMINSTRATORS)]
        private void ClearIncome()
        {
            var incomeList = HttpContext.Session.Get<List<IncomeItem>>("Incomes");
            incomeList.Clear();
            HttpContext.Session.Set("Incomes", incomeList);
        }
    }
}
