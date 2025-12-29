using Dairyncia.Enums;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
namespace Dairyncia.Models
{
    public class DairyCenter : BaseEntity
    {
        [Required(ErrorMessage = "Dairy center name is required.")]
        [MaxLength(150, ErrorMessage = "Center name cannot exceed 150 characters.")]
        [MinLength(3, ErrorMessage = "Center name must be at least 3 characters long.")]
        public string CenterName { get; set; }

        [Range(1, int.MaxValue, ErrorMessage = "Invalid address reference.")]
        public int? AddressId { get; set; }
        
        [ForeignKey(nameof(AddressId))]
        public Address? Address { get; set; }

      
        public string? ManagerId { get; set; }

        [ForeignKey(nameof(ManagerId))]
        public ApplicationUser? Manager { get; set; }
    }
}
