namespace Dairyncia.Models
{
    public abstract class BaseEntity
    {
        public int Id { get; set; } // PK automatically
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        public DateTime? UpdatedAt { get; set; }
    }

}
