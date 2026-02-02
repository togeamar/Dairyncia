namespace Dairyncia.DTOs
{
    public class FarmerListDTO
    {
        public int Id {  get; set; }
        public string Email { get; set; }
        public string? FullName { get; set; }
        public DateTime CreatedAt { get; set; }
    }
}
