

using Dairyncia.Enums;

namespace Dairyncia.DTOs
{
    public class MilkRateResultDto
{
    public bool IsExactMatch { get; set; }

 
    public decimal RequestedFat { get; set; }
    public decimal RequestedSnf { get; set; }

   
    public decimal ActualFat { get; set; }
    public decimal ActualSnf { get; set; }

    public decimal Rate { get; set; }
    public MilkType RateType { get; set; }
}

}