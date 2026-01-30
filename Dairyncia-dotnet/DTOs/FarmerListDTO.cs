namespace Dairyncia.DTOs
{
    public class FarmerListDTO
    {
        public int FarmerId {  get; set; }
        public string Email { get; set; }
        public string? FullName { get; set; }
        public string ManagerName { get; set; }
        public DateTime CreatedAt { get; set; }
    }
}
