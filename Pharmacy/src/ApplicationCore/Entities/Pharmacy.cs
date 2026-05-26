using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class Pharmacy
    {
        public Pharmacy()
        {
            Income = new HashSet<Income>();
            PharmacyWharehouse = new HashSet<PharmacyWharehouse>();
            Purchase = new HashSet<Purchase>();
        }

        public int PharmId { get; set; }

        [Required]
        [Display(Name = "Pharmacy Name")]
        public string PharmName { get; set; }

        [Required]
        [Display(Name = "Pharmacy Address")]
        public string PharmAddress { get; set; }

        public virtual ICollection<Income> Income { get; set; }
        public virtual ICollection<PharmacyWharehouse> PharmacyWharehouse { get; set; }
        public virtual ICollection<Purchase> Purchase { get; set; }
    }
}
