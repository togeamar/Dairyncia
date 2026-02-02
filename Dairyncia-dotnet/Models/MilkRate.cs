using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Dairyncia.Enums;
namespace Dairyncia.Models
{
    public class MilkRate : BaseEntity
    {
        [Required(ErrorMessage = "Fat percentage is required")]
        [Range(0, 15, ErrorMessage = "Fat percentage must be between 0 and 15")]
        [Column(TypeName = "decimal(5,2)")]
        public decimal Fat { get; set; }

        [Required(ErrorMessage = "SNF is required")]
        [Range(0, 12, ErrorMessage = "SNF must be between 0 and 12")]
        [Column(TypeName = "decimal(5,2)")]
        public decimal Snf { get; set; }

        [Required(ErrorMessage = "Rate is required")]
        [Range(0, 1000, ErrorMessage = "Rate must be between 0 and 1000")]
        [Column(TypeName = "decimal(10,2)")]
        public decimal Rate { get; set; }

        [Required(ErrorMessage = "Rate type is required")]
        [MaxLength(20)]
        public MilkType RateType { get; set; } // "Cow" or "Buffalo"
        
    }
}
