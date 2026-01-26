using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Dairyncia.Enums;
namespace Dairyncia.Models
{
    public class MilkCollection : BaseEntity
    {
        [Required(ErrorMessage = "Farmer is required for milk collection.")]
        
        public int FarmerId { get; set; }
        
        [ForeignKey(nameof(FarmerId))]
        public Farmer Farmer { get; set; }

        [Required(ErrorMessage = "Manager is required to record milk collection.")]
        public string ManagerId { get; set; }
        [ForeignKey(nameof(ManagerId))]
        public ApplicationUser Manager { get; set; }

        [Required(ErrorMessage = "Milk type is required.")]
        [EnumDataType(typeof(MilkType), ErrorMessage = "Invalid milk type selected.")]
        public MilkType MilkType { get; set; }

        [Required(ErrorMessage = "Milk shift is required.")]
        [EnumDataType(typeof(MilkShift), ErrorMessage = "Invalid milk shift selected.")]
        public MilkShift MilkShift { get; set; }

        [Required(ErrorMessage = "Milk quantity is required.")]
        [Range(0.1, 100, ErrorMessage = "Quantity must be between 0.1 and 1000 liters.")]
        [Column(TypeName = "decimal(10,2)")]
        public decimal Quantity { get; set; }

        [Required(ErrorMessage = "Fat percentage is required.")]
        [Range(0, 10, ErrorMessage = "Fat percentage must be between 0 and 20.")]
        [Column(TypeName = "decimal(5,2)")]
        public decimal FatPercentage { get; set; }

        [Required(ErrorMessage = "SNF percentage is required.")]
        [Range(0, 10, ErrorMessage = "SNF must be between 0 and 20.")]
        [Column(TypeName = "decimal(5,2)")]
        public decimal SNF { get; set; }

        [Range(0, 500, ErrorMessage = "Rate per liter must be between 0 and 1000.")]
        [Column(TypeName = "decimal(10,2)")]
        public decimal RatePerLiter { get; set; }

        [Range(0, 100000, ErrorMessage = "Total amount must be between 0 and 100000.")]
        [Column(TypeName = "decimal(10,2)")]
        public decimal TotalAmount { get; set; }

        [Required]
        [EnumDataType(typeof(PaymentStatus))]
        public PaymentStatus PaymentStatus { get; set; } = PaymentStatus.Pending;
        public int? PaymentSettlementId { get; set; }
public PaymentSettlement PaymentSettlement { get; set; }

    }
}
