using Dairyncia.DTOs;
using Dairyncia.Enums;
using Dairyncia.Models;
using Microsoft.AspNetCore.Mvc;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/milk-rate-matrix")]
    public class MilkRateMatrixController : Controller
    {
        private readonly AppDbContext _context;

        public MilkRateMatrixController(AppDbContext context)
        {
            _context = context;
        }

        [HttpPost("generate")]
        public async Task<IActionResult> GenerateChart([FromBody] MilkRateMatrixRequest request)
        {
            // 1️⃣ Save Chart Header
            MilkType milkType;
            if (!Enum.TryParse(request.MilkType, true, out milkType))
            {
                return BadRequest("Invalid MilkType value");
            }

            var newChart = new MilkRateChart
            {
                ChartName = request.ChartName,
                BaseFat = request.BaseFat,
                BaseSnf = request.BaseSnf,
                BaseRate = request.BaseRate,
                MilkType = milkType
            };

            var charts = _context.MilkRateCharts.Where(x=> x.MilkType == milkType);

            foreach(var c in charts)
            {
                c.IsActive = false;
            }

            newChart.IsActive = true;

            _context.MilkRateCharts.Add(newChart);
            await _context.SaveChangesAsync();

            // 2️⃣ Save FIRST ROW (SNF)
            foreach (var snf in request.FirstRowSnfRates)
            {
                _context.MilkRateFirstRows.Add(new MilkRateFirstRow
                {
                    MilkRateChartId = newChart.Id,
                    Snf = snf.Key,
                    Rate = snf.Value
                });
            }

            // 3️⃣ Save FIRST COLUMN (FAT)
            foreach (var fat in request.FirstColumnFatRates)
            {
                _context.MilkRateFirstColumns.Add(new MilkRateFirstColumn
                {
                    MilkRateChartId = newChart.Id,
                    Fat = fat.Key,
                    Rate = fat.Value
                });
            }

            // 4️⃣ Generate & Save FULL CHART
            foreach (var fat in request.FirstColumnFatRates)
            {
                foreach (var snf in request.FirstRowSnfRates)
                {
                    decimal rate =
                        request.BaseRate +
                        (fat.Value - request.BaseRate) +
                        (snf.Value - request.BaseRate);

                    _context.MilkRateCells.Add(new MilkRateCell
                    {
                        MilkRateChartId = newChart.Id,
                        Fat = fat.Key,
                        Snf = snf.Key,
                        Rate = Math.Round(rate, 2)
                    });
                }
            }

            await _context.SaveChangesAsync();

            return Ok(new
            {
                ChartId = newChart.Id,
                Message = "Milk rate chart generated and stored successfully"
            });
        }
    }
}
