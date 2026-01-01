using System;

namespace Dairyncia.Models
{
    public class ContactMessage
    {
        public int Id { get; set; }

        public string Name { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string Purpose { get; set; } = null!;

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
