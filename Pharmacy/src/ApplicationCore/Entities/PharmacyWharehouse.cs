using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class PharmacyWharehouse
    {
        public PharmacyWharehouse()
        {
            ReservedMedItem = new HashSet<ReservedMedItem>();
        }

        [Required]
        [Display(Name = "Pharmacy")]
        public int PharmId { get; set; }

        [Required]
        [Display(Name = "Medical Item")]
        public int MedItemId { get; set; }

        [Required]
        [Display(Name = "Stock Quantity")]
        public int ItemCount { get; set; }

        public virtual MedicalItem MedItem { get; set; }
        public virtual Pharmacy Pharm { get; set; }
        public virtual ICollection<ReservedMedItem> ReservedMedItem { get; set; }
    }
}
