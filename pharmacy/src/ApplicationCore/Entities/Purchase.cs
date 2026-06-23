using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class Purchase
    {
        public Purchase()
        {
            Check = new HashSet<Check>();
        }

        [Required]
        [Display(Name = "Purchase Code")]
        public int PurchId { get; set; }

        [Required]
        [Display(Name = "Pharmacy")]
        public int PharmId { get; set; }

        [Required]
        [Display(Name = "Purchase Date and Time")]
        public DateTime PurchDate { get; set; }

        [Required]
        [Display(Name = "Purchase Amount")]
        public decimal PurchAmount { get; set; }

        [Required]
        [Display(Name = "Discount Percent")]
        public decimal PurchDiscountPercent { get; set; }

        public virtual Pharmacy Pharm { get; set; }
        public virtual ICollection<Check> Check { get; set; }
    }
}
