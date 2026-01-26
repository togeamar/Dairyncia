using Dairyncia.Enums;

namespace Dairyncia.DTOs
{
    public class ManagerPaymentSummaryDto
    {
        public string ManagerId { get; set; }
        public string ManagerName { get; set; }

        public decimal TotalAmount { get; set; }
        public decimal PaidAmount { get; set; }
        public decimal PendingAmount { get; set; }

        public PaymentStatus PaymentStatus { get; set; }
    }
}
