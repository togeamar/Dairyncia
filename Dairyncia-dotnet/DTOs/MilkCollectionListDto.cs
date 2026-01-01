using Dairyncia.Enums;

namespace Dairyncia.DTOs
{
    public class MilkCollectionListDto
    {
        public int Id { get; set; }
        public string FarmerName { get; set; }
        public string ManagerName { get; set; }

        public MilkType MilkType { get; set; }
        public MilkShift MilkShift { get; set; }

        public decimal Quantity { get; set; }
        public decimal TotalAmount { get; set; }
        public PaymentStatus PaymentStatus { get; set; }

        public DateTime CreatedAt { get; set; }
    }
}
