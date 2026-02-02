namespace Dairyncia.DTOs
{
    public class FarmerDetailsDto
    {
        public int Id { get; set; }
        public string UserId { get; set; }

        public string FullName { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }

        public AddressDto? Address { get; set; }
        public BankDetailsDto? BankDetails { get; set; }
    }
}
