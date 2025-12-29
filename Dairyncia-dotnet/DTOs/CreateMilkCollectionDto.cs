using Dairyncia.Enums;
using System.ComponentModel.DataAnnotations;

public class CreateMilkCollectionDto
{
    [Required(ErrorMessage = "Farmer email is required")]
    [EmailAddress(ErrorMessage = "Invalid email format")]
    public string FarmerEmail { get; set; }

    [Required(ErrorMessage = "Milk type is required")]
    public MilkType MilkType { get; set; }

    [Required(ErrorMessage = "Milk shift is required")]
    public MilkShift MilkShift { get; set; }

    [Range(0.1, 1000, ErrorMessage = "Quantity must be between 0.1 and 1000 liters")]
    public decimal Quantity { get; set; }

    [Range(0, 10, ErrorMessage = "Fat percentage must be between 0 and 10")]
    public decimal FatPercentage { get; set; }

    [Range(0, 10, ErrorMessage = "SNF must be between 0 and 10")]
    public decimal SNF { get; set; }
}

