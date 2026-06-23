using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class Income
    {
        public Income()
        {
            IncomeDetail = new HashSet<IncomeDetail>();
        }

        [Required]
        [Display(Name = "Income Number")]
        public int IncomeId { get; set; }

        [Required]
        [Display(Name = "Pharmacy")]
        public int PharmId { get; set; }

        [Required]
        [Display(Name = "Income Date and Time")]
        public DateTime IncomeDate { get; set; }

        public virtual Pharmacy Pharm { get; set; }
        public virtual ICollection<IncomeDetail> IncomeDetail { get; set; }
    }
}
