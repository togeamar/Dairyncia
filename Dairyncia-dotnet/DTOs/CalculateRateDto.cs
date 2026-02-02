using Dairyncia.Enums;

namespace Dairyncia.DTOs
{
    public class CalculateRateDto
    {
        public decimal snf{get;set;}
        public decimal fat{get;set;}

        public MilkType milkType{get;set;}
        
    }
}