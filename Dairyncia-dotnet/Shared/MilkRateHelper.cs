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
        fat = Math.Floor(fat * 10) / 10;
        snf = Math.Floor(snf * 10) / 10;
        var milkRate=await _context.MilkRates
            .Where(r=>r.Snf==snf && r.Fat==fat&&r.RateType==milkType)
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
