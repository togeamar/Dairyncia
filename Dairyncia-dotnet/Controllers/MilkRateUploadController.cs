using Dairyncia.DTOs;
using Dairyncia.Enums;
using Dairyncia.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using OfficeOpenXml;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/milk-rate")]
    [Authorize(Roles ="Admin")]
    public class MilkRateUplaodController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly ILogger<MilkRateUplaodController> _logger;

        public MilkRateUplaodController(AppDbContext context,ILogger<MilkRateUplaodController> logger)
        {
            _context=context;
            _logger=logger;
        } 

        [HttpPost("upload")]
        public async Task<IActionResult> UploadMilkRates([FromForm] IFormFile file,[FromQuery] MilkType milktype)
        {
            if(file==null || file.Length == 0)
            {
                return BadRequest("Uplaod MilkRates in Excel");
            }
            var path=Path.GetExtension(file.FileName).ToLower();
            if (path != ".xlsx" && path != ".xls")
            {
                return BadRequest("only Excel Files Are Allowed");
            }

            try
            {
                ExcelPackage.License.SetNonCommercialPersonal("Amar");

                var MilkRates=new List<MilkRate>();
                var errors=new List<String>();

                using(var stream=new MemoryStream())
                {
                    await file.CopyToAsync(stream);
                    stream.Position=0;
                    using(var package=new ExcelPackage(stream))
                    {
                        var worksheet=package.Workbook.Worksheets.FirstOrDefault();
                        if(worksheet==null)
                            return BadRequest("Excel file is Empty or invalid");
                        var rowcount=worksheet.Dimension?.Rows??0;
                        var colcount=worksheet.Dimension?.Columns??0;

                        if(rowcount<2)
                            BadRequest("excel file must contain headers or at least one data row");
                        

                        for(int row = 2; row <= rowcount; row++)
                        {
                            try
                            {
                                var fatvalue=worksheet.Cells[row,1].Value?.ToString()?.Trim();
                                if (string.IsNullOrEmpty(fatvalue)) continue; 
                                

                                for(int col = 2; col <= colcount; col++)
                                {
                                    var snfvalue=worksheet.Cells[1,col].Value?.ToString()?.Trim();
                                    if(String.IsNullOrEmpty(snfvalue)) continue;
                                    

                                    var ratevalue=worksheet.Cells[row,col].Value?.ToString()?.Trim();

                                    if (!String.IsNullOrEmpty(ratevalue))
                                    {
                                        

                                        if (!decimal.TryParse(fatvalue, out decimal fat))
                                            {
                                                errors.Add($"Row {row}: Invalid Fat value '{fatvalue}'");
                                                continue;
                                            }

                                        if (!decimal.TryParse(snfvalue, out decimal snf))
                                            {
                                                errors.Add($"Row {row}: Invalid SNF value '{snfvalue}'");
                                                continue;
                                            }

                                        if (!decimal.TryParse(ratevalue, out decimal rate))
                                            {
                                                errors.Add($"Row {row}: Invalid Rate value '{ratevalue}'");
                                                continue;
                                            }
                                        
                                        if (snf < 0 || snf > 12)
                                            {
                                                errors.Add($"Row {row}: SNF must be between 0 and 12");
                                                continue;
                                            }

                                        if (rate < 0 || rate > 1000)
                                            {
                                                errors.Add($"Row {row}: Rate must be between 0 and 1000");
                                                continue;
                                            }

                                        MilkRates.Add(
                                            new MilkRate
                                            {
                                                Fat=fat,
                                                Snf=snf,
                                                Rate=rate,
                                                RateType=milktype
                                            }
                                        );
                                    }


                                }
                            }
                            catch(Exception ex)
                            {
                                errors.Add($"Row {row}: {ex.Message}");
                            }
                        }
                    }
                }

                if (MilkRates.Count == 0)
                {
                    return BadRequest(new
                    {
                        message = "No valid data found in the Excel file",
                        errors = errors
                    });
                }

                _context.MilkRates.RemoveRange(_context.MilkRates.Where(x=>x.RateType==milktype));
                await _context.MilkRates.AddRangeAsync(MilkRates);
                await _context.SaveChangesAsync();
                _logger.LogInformation($"Successfully imported {MilkRates.Count} milk rates");
                return Ok(new { message = $"Successfully uploaded {MilkRates.Count} rate entries." });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error uploading milk rates");
                return StatusCode(500, $"Error processing file: {ex.Message}");
            }
        }
        [HttpGet("getallrates")]
        [Authorize]
        public async Task<IActionResult> GetMilkRates(
            [FromQuery] int pageNumber,
            [FromQuery] int pageSize,
            [FromQuery] MilkType milkType
        )
        {
            var query=_context.MilkRates.AsQueryable();
            query=query.Where(r=>r.RateType==milkType);

            var totalcount=await query.CountAsync();

            var milkRates=await query.
                OrderBy(r=>r.Fat)
                .ThenBy(r=>r.Snf)
                .Skip((pageNumber-1)*pageSize)
                .Take(pageSize)
                .Select(r => new
                {
                    r.Snf,
                    r.Fat,
                    r.Rate
                })
                .ToListAsync();
            return Ok(new
            {
                totalcount,
                pageNumber,
                pageSize,
                data = milkRates
            });
        }

        [HttpPost("calculate")]
        [Authorize]
        public async Task<IActionResult> GetRate(
            CalculateRateDto dto
        )
        {
            var milkRate=await _context.MilkRates
                .Where(r=>r.Snf==dto.snf && r.Fat==dto.fat&&r.RateType==dto.milkType)
                .FirstOrDefaultAsync();
            
            if (milkRate == null)
            {
                
                var nearestRate = await _context.MilkRates
                    .Where(m => m.RateType == dto.milkType)
                    .OrderBy(m => Math.Abs(m.Fat - dto.fat) + Math.Abs(m.Snf - dto.snf))
                    .ThenBy(m => m.Fat)
                    .ThenBy(m => m.Snf)
                    .FirstOrDefaultAsync();

                if (nearestRate == null)
                {
                    return NotFound($"No rate found for Fat: {dto.fat}, SNF: {dto.snf}, Type: {dto.milkType}");
                }

                return Ok(new
                {
                    message = "Exact rate not found, returning nearest match",
                    isExactMatch = false,
                    requestedFat = dto.fat,
                    requestedSnf = dto.snf,
                    actualFat = nearestRate.Fat,
                    actualSnf = nearestRate.Snf,
                    rate = nearestRate.Rate,
                    rateType = nearestRate.RateType
                });
            }

            return Ok(new
            {
                isExactMatch = true,
                fat = milkRate.Fat,
                snf = milkRate.Snf,
                rate = milkRate.Rate,
                rateType = milkRate.RateType
            });
        }
    }
}