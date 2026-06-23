using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class IncomeDetail
    {
        [Required]
        [Display(Name = "Medical Item")]
        public int MedItemId { get; set; }

        [Required]
        [Display(Name = "Income Number")]
        public int IncomeId { get; set; }

        [Required]
        [Display(Name = "Quantity")]
        public int Count { get; set; }

        [Required]
        [Display(Name = "Amount")]
        public decimal Price { get; set; }

        public virtual Income Income { get; set; }
        public virtual MedicalItem MedItem { get; set; }
    }
}
