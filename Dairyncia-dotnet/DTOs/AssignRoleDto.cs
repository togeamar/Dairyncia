using System.ComponentModel.DataAnnotations;

namespace Dairyncia.DTOs
{
    public class AssignRoleDto
    {
        [Required, EmailAddress]
        public string Email { get; set; }

        [Required]
        public string Role { get; set; } // Admin, Manager, Farmer
    }
}
