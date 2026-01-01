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
        // 🔑 ROUND DOWN to nearest 0.1
        fat = Math.Floor(fat * 10) / 10;
        snf = Math.Floor(snf * 10) / 10;

        var rate = await _context.MilkRateCells
            .Include(x => x.MilkRateChart)
            .Where(x =>
                x.Fat == fat &&
                x.Snf == snf &&
                x.MilkRateChart.MilkType == milkType &&
                x.MilkRateChart.IsActive)
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
