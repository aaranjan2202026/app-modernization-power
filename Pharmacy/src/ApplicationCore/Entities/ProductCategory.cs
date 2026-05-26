using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class ProductCategory
    {
        public ProductCategory()
        {
            MedicalItem = new HashSet<MedicalItem>();
        }

        public int CategId { get; set; }

        [Required]
        [Display(Name = "Category Name")]
        public string CategName { get; set; }

        [Required]
        [Display(Name = "Category Markup Percent")]
        public decimal CategMarkup { get; set; }

        public virtual ICollection<MedicalItem> MedicalItem { get; set; }
    }
}
