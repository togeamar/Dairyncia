namespace Dairyncia.DTOs
{
    public class BankDetailsDto
    {
        public int? Id { get; set; }
        public string BankName { get; set; }
        public string AccountNumber { get; set; }
        public string AccountHolderName { get; set; }
        public string IFSC { get; set; }
    }
}
