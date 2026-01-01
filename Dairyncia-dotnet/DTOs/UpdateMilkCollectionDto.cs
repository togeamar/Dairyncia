using System.ComponentModel.DataAnnotations;
using Dairyncia.Enums;

namespace Dairyncia.DTOs
{
    public class UpdateMilkCollectionDto
    {

        [Range(0.1, 100, ErrorMessage = "Quantity must be between 0.1 and 100 liters")]
        public decimal Quantity { get; set; }

        [Range(0, 10, ErrorMessage = "Fat percentage must be between 0 and 10")]
        public decimal FatPercentage { get; set; }

        [Range(0, 10, ErrorMessage = "SNF must be between 0 and 10")]
        public decimal SNF { get; set; }

        public decimal RatePerLiter { get; set; }

        public PaymentStatus PaymentStatus { get; set; }
    }
}