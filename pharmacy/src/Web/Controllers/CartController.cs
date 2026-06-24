using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PharmacyNetwork.ApplicationCore.Constants;
using PharmacyNetwork.ApplicationCore.Entities;
using PharmacyNetwork.ApplicationCore.Interfaces;
using PharmacyNetwork.Infrastructure.Data;
using PharmacyNetwork.Web.Extensions;
using PharmacyNetwork.Web.Models;           
using PharmacyNetwork.Web.Services;
using PharmacyNetwork.Web.ViewModels;

namespace PharmacyNetwork.Web.Controllers
{
    [Authorize(Roles = AuthorizationConstants.Roles.USERS)]
    public class CartController : Controller
    {
        private readonly ICartService _cartService;
        private readonly IAsyncRepository<Purchase> _purchaseRepository;
        private readonly IAsyncRepository<Check> _checkRepository;

        public CartController(ICartService cartService,
            IAsyncRepository<Purchase> purchaseRepository,
            IAsyncRepository<Check> checkRepository)
        {
            _cartService = cartService;
            _purchaseRepository = purchaseRepository;
            _checkRepository = checkRepository;
        }

        public IActionResult Index()
        {
            var cartList = HttpContext.Session.Get<List<CartItem>>("Cart");
            return View(cartList);
        }

        [HttpPost]
        public async Task<IActionResult> Reserve(ReserveMedItemsViewModel viewModel)
        {
            var cart = HttpContext.Session.Get<List<CartItem>>("Cart");

            if (!ModelState.IsValid) return View(new ReserveMedItemsViewModel() { Items = cart });

            if (cart == null || cart.Count == 0)
            {
                return RedirectToAction("Index");
            }

            await _cartService.ReserveItemsAsync(cart, viewModel.Telephone, HttpContext.RequestAborted);
            ClearCart();

            return RedirectToAction("Index", "ReservedMedItems"); 
        }

        public IActionResult Reserve()
        {
            var cart = HttpContext.Session.Get<List<CartItem>>("Cart");

            ReserveMedItemsViewModel viewModel = new ReserveMedItemsViewModel()
            {
                Items = cart
            };

            return View(viewModel);
        }

        public async Task<IActionResult> ConfirmPurchase()
        {
            var cart = HttpContext.Session.Get<List<CartItem>>("Cart");

            if (cart == null || cart.Count == 0)
            {
                return RedirectToAction("Index");
            }

            // Create new purchase
            var purchase = new Purchase
            {
                PharmId = cart[0].PharmacyId,
                PurchDate = DateTime.Now,
                PurchAmount = 0,
                PurchDiscountPercent = 0
            };

            await _purchaseRepository.AddAsync(purchase, HttpContext.RequestAborted);

            // Add items to check (purchase details)
            decimal totalAmount = 0;
            foreach (var item in cart)
            {
                var check = new Check
                {
                    MedItemId = item.MedicalItemId,
                    PurchId = purchase.PurchId,
                    ItemCount = item.Count
                };

                await _checkRepository.AddAsync(check, HttpContext.RequestAborted);
                totalAmount += item.MedItemPrice * item.Count;
            }

            // Update purchase amount
            purchase.PurchAmount = totalAmount;
            await _purchaseRepository.UpdateAsync(purchase, HttpContext.RequestAborted);

            ClearCart();

            return RedirectToAction("Details", "Purchases", new {id = purchase.PurchId});
        }

        public IActionResult AddToCart(int medItemId, decimal medItemPrice, int pharmId, int count)
        {
            if (!ModelState.IsValid) return BadRequest(ModelState);
            var cartItem = new CartItem()
            {
                MedicalItemId = medItemId,
                MedItemPrice = medItemPrice,
                PharmacyId = pharmId,
                Count = count
            };

            var cartList = HttpContext.Session.Get<List<CartItem>>("Cart");

            if (cartList == null)
            {
                cartList = new List<CartItem> { cartItem };
            }
            else
            {
                if (cartList.Any(i => i.PharmacyId == cartItem.PharmacyId && i.MedicalItemId == cartItem.MedicalItemId))
                {
                    cartList.Find(i => i.PharmacyId == cartItem.PharmacyId && i.MedicalItemId == cartItem.MedicalItemId).Count += cartItem.Count;
                }
                else
                {
                    cartList.Add(cartItem);
                }
            }

            HttpContext.Session.Set("Cart", cartList);

            return RedirectToAction(nameof(Index), "PharmacyWharehouses");
        }

        public IActionResult DeleteFromCart(int id)
        {
            if (!ModelState.IsValid) return BadRequest(ModelState);
            var cartList = HttpContext.Session.Get<List<CartItem>>("Cart");
            
            cartList.RemoveAll(i => i.MedicalItemId == id);

            HttpContext.Session.Set("Cart", cartList);

            return RedirectToAction(nameof(Index));
        }

        private void ClearCart()
        {
            var cartList = HttpContext.Session.Get<List<CartItem>>("Cart");
            cartList.Clear();
            HttpContext.Session.Set("Cart", cartList);
        }
    }
}
