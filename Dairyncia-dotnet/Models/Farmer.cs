using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
namespace Dairyncia.Models
{
    public class Farmer : BaseEntity
    {
        [Required(ErrorMessage = "UserId is required to link farmer with identity user.")]
        public string UserId { get; set; }
        [ForeignKey(nameof(UserId))]
        public ApplicationUser? User { get; set; }

       
        public int? AddressId { get; set; }
        
        [ForeignKey(nameof(AddressId))]
        public Address? Address { get; set; }

        [ForeignKey(nameof(UserId))]
        public string ManagerId { get; set; }

        public ICollection<MilkCollection>? MilkCollections { get; set; }
    }
}
