using System.ComponentModel.DataAnnotations;
using Dairyncia.Enums;
namespace Dairyncia.Models
{
    public class BankDetails : BaseEntity
    {
        [Required, MaxLength(100)]
        public string BankName { get; set; }

        [Required, MaxLength(20)]
        public string AccountNumber { get; set; }

        [Required, MaxLength(100)]
        public string AccountHolderName { get; set; }

        [Required, MaxLength(11)]
        public string IFSC { get; set; }

        [Required]
        public string UserId { get; set; }
        public ApplicationUser User { get; set; } // Identity user
    }
}
