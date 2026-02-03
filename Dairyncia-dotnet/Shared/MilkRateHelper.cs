using Dairyncia.DTOs;
using Dairyncia.Enums;
using Microsoft.EntityFrameworkCore;

public class MilkRateHelper
{
    private readonly AppDbContext _context;

    public MilkRateHelper(AppDbContext context)
    {
        _context = context;
    }

    public async Task<ServiceResult<MilkRateResultDto>> GetRatePerLiter(decimal fat, decimal snf, MilkType milkType)
    {
        fat = Math.Round(fat, 2);
        snf = Math.Round(snf, 2);

        var rate = await _context.MilkRateCells
            .Where(x =>
                x.MilkRateChart.MilkType == milkType &&
                x.MilkRateChart.IsActive)
            .OrderBy(x =>
                Math.Abs(x.Fat - fat) +
                Math.Abs(x.Snf - snf))
            .Select(x => x.Rate)
            .FirstOrDefaultAsync();
        
        if (milkRate == null)
        {
            
            var nearestRate = await _context.MilkRates
                .Where(m => m.RateType == milkType)
                .OrderBy(m => Math.Abs(m.Fat - fat) + Math.Abs(m.Snf - snf))
                .ThenBy(m => m.Fat)
                .ThenBy(m => m.Snf)
                .FirstOrDefaultAsync();

            if (nearestRate == null)
            {
                return ServiceResult<MilkRateResultDto>.Fail(
                $"No rate found for Fat: {fat}, SNF: {snf}, Type: {milkType}");
            }

            return ServiceResult<MilkRateResultDto>.Success(new MilkRateResultDto
            {
                IsExactMatch = false,
                RequestedFat = fat,
                RequestedSnf = snf,
                ActualFat = nearestRate.Fat,
                ActualSnf = nearestRate.Snf,
                Rate = nearestRate.Rate,
                RateType = nearestRate.RateType
            });
        }

        return ServiceResult<MilkRateResultDto>.Success(new MilkRateResultDto
        {
            IsExactMatch = true,
            RequestedFat = fat,
            RequestedSnf = snf,
            ActualFat = milkRate.Fat,
            ActualSnf = milkRate.Snf,
            Rate = milkRate.Rate,
            RateType = milkRate.RateType
        });
    }



    public async Task<decimal> CalculateAmount(decimal fat, decimal snf, decimal litres, MilkType milkType)
    {
        ServiceResult<MilkRateResultDto> rate = await GetRatePerLiter(fat, snf, milkType);
        
        return Math.Round(rate.Data.Rate * litres, 2);
        
    }
}
