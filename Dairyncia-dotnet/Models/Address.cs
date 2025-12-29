namespace Dairyncia.Models {
using System.ComponentModel.DataAnnotations;

    public class Address : BaseEntity
    {
        [Required(ErrorMessage ="Village name is required" )]
        [MaxLength(100, ErrorMessage ="Village name can not exceeds 100 characters")]
        [RegularExpression(@"^[a-zA-Z\s]+$", ErrorMessage = "Village name can contain only letters and spaces.")]
        public string Village { get; set; }

        [Required(ErrorMessage = "City name is required")]
        [MaxLength(100, ErrorMessage = "City name can not exceeds 100 characters")]
        [RegularExpression(@"^[a-zA-Z\s]+$", ErrorMessage = "City name can contain only letters and spaces.")]
        public string City { get; set; }

        [Required(ErrorMessage = "State name is required")]
        [MaxLength(100, ErrorMessage = "State name can not exceeds 100 characters")]
        [RegularExpression(@"^[a-zA-Z\s]+$", ErrorMessage = "State name can contain only letters and spaces.")]
        public string State { get; set; }

        [Required(ErrorMessage = "Pincode is required.")]
        [RegularExpression(@"^[1-9][0-9]{5}$", ErrorMessage = "Pincode must be a valid 6-digit Indian pincode.")]
        public string Pincode { get; set; }
    }
}
