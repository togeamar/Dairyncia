using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Dairyncia.Enums;

namespace Dairyncia.Models
{
    public class PaymentSettlement : BaseEntity
    {
        [Required]
        public int FarmerId { get; set; }
        public Farmer Farmer { get; set; }

        [Required]
        public string ManagerId { get; set; }
        public ApplicationUser Manager { get; set; }

        [Required]
        public DateTime FromDate { get; set; }

        [Required]
        public DateTime ToDate { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal TotalAmount { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal PaidAmount { get; set; }

        [Column(TypeName = "decimal(10,2)")]
        public decimal PendingAmount { get; set; }

        [Required]
        public PaymentStatus Status { get; set; }
    }
}
