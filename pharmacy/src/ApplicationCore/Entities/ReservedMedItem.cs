using System;
using System.ComponentModel.DataAnnotations;

namespace PharmacyNetwork.ApplicationCore.Entities
{
    public partial class ReservedMedItem
    {
        [Required]      
        [Display(Name = "Reservation Number")]
        public int ReservedId { get; set; }

        [Required]
        [Display(Name = "Reservation Start Date and Time")]
        public DateTime DateStart { get; set; }

        [Required]
        [Display(Name = "Reservation End Date and Time")]
        public DateTime DateFinish { get; set; }

        [Required]
        [Display(Name = "Medical Item")]
        public int MedItemId { get; set; }

        [Required]
        [Display(Name = "Pharmacy")]
        public int PharmId { get; set; }

        [Required]
        [Display(Name = "Quantity")]
        public int Count { get; set; }

        [Required]
        [Display(Name = "Client Phone Number")]
        public string Telephone { get; set; }

        public virtual PharmacyWharehouse PharmacyWharehouse { get; set; }
    }
}
