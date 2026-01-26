namespace Dairyncia.DTOs
{
    public class FarmerDashboardDto
    {
        public int FarmerId { get; set; }

        public string FullName { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }

        public BankDetailsDto? BankDetails { get; set; }

        public List<ManagerPaymentSummaryDto> ManagerSummaries { get; set; }
    }
}
