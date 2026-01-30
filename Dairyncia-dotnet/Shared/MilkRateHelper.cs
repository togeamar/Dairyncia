using Dairyncia.Enums;
using Microsoft.EntityFrameworkCore;

public class MilkRateHelper
{
    private readonly AppDbContext _context;

    public MilkRateHelper(AppDbContext context)
    {
        _context = context;
    }

    public async Task<decimal> GetRatePerLiter(decimal fat, decimal snf, MilkType milkType)
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

        return rate;
    }



    public async Task<decimal> CalculateAmount(decimal fat, decimal snf, decimal litres, MilkType milkType)
    {
        decimal rate = await GetRatePerLiter(fat, snf, milkType);
        return Math.Round(rate * litres, 2);
    }
}
