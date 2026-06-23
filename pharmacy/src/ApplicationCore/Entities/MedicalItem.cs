using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class MedicalItem
    {
        public MedicalItem()
        {
            Check = new HashSet<Check>();
            IncomeDetail = new HashSet<IncomeDetail>();
            PharmacyWharehouse = new HashSet<PharmacyWharehouse>();
        }

        public int MedItemId { get; set; }

        [Required]
        [Display(Name = "Manufacturer Firm")]
        public int FirmId { get; set; }

        [Required]
        [Display(Name = "Product Category")]
        public int CategId { get; set; }

        [Required(ErrorMessage = "Enter product name!")]
        [Display(Name = "Product Name")]
        public string MedItemName { get; set; }

        [DataType(DataType.MultilineText)]
        [Display(Name = "Description")]
        public string MedItemDescrip { get; set; }

        [Required(ErrorMessage = "Enter purchase price!")]
        [Display(Name = "Price (Purchase)")]
        public decimal MedItemPrice { get; set; }

        [Display(Name = "Price")]
        public decimal? MedItemPriceMarkup { get; set; }

        public virtual ProductCategory Categ { get; set; }
        public virtual Firm Firm { get; set; }
        public virtual ICollection<Check> Check { get; set; }
        public virtual ICollection<IncomeDetail> IncomeDetail { get; set; }
        public virtual ICollection<PharmacyWharehouse> PharmacyWharehouse { get; set; }
    }
}
