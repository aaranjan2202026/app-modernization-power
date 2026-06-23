using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class Check
    {   
        [Required]
        [Display(Name = "Medical Item")]
        public int MedItemId { get; set; }

        [Required]
        [Display(Name = "Purchase Code")]
        public int PurchId { get; set; }

        [Required]
        [Display(Name = "Quantity")]
        public int ItemCount { get; set; }

        public virtual MedicalItem MedItem { get; set; }
        public virtual Purchase Purch { get; set; }
    }
}
