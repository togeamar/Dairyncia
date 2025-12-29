using System.ComponentModel.DataAnnotations;
using Dairyncia.Enums;
namespace Dairyncia.Models
{
    public class MilkRate : BaseEntity
    {
        [Required(ErrorMessage = "Milk type is required.")]
        public MilkType MilkType { get; set; }

        [Required(ErrorMessage = "Rate per liter is required.")]
        [Range(1, 500, ErrorMessage = "Rate per liter must be between 1 and 1000.")]
        [DataType(DataType.Currency)]
        public decimal RatePerLiter { get; set; }
    }
}
