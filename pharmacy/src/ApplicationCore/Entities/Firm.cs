using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class Firm
    {
        public Firm()
        {
            MedicalItem = new HashSet<MedicalItem>();
        }

        public int FirmId { get; set; }

        [Required]
        [Display(Name = "Firm Name")]
        public string FirmName { get; set; }

        [Required]
        [Display(Name = "Firm Address")]
        public string FirmAddress { get; set; }

        [Required]
        [Display(Name = "Firm Contacts")]
        public string FirmContact { get; set; }

        [Required]
        [Display(Name = "Firm Markup Percent")]
        [Range(typeof(decimal), "0,1", "99,99")]
        public decimal FirmMarkup { get; set; }

        public virtual ICollection<MedicalItem> MedicalItem { get; set; }
    }
}
