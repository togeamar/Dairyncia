using Dairyncia.Enums;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace Dairyncia.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ManagerController : ControllerBase
    {
        public readonly AppDbContext _context;

        public ManagerController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        [Route("get-farmer-milk-collection")]
        public async Task<IActionResult> getAllFarmersWithMilkCollection()
        {
            var farmerMilkData = await _context.MilkCollections
                .GroupBy(m => new
                {
                    m.FarmerId,
                    m.Farmer.UserId,
                    m.Farmer.User.FullName
                })
                .Select(g => new
                {
                    FarmerId = g.Key.FarmerId,
                    UserId = g.Key.UserId,
                    FarmerName = g.Key.FullName,

                    BuffaloMorningMilkCount = g.Sum(m =>
                        m.MilkType == MilkType.Buffalo && m.MilkShift == MilkShift.Morning
                            ? m.Quantity : 0),

                    BuffaloEveningMilkCount = g.Sum(m =>
                        m.MilkType == MilkType.Buffalo && m.MilkShift == MilkShift.Evening
                            ? m.Quantity : 0),

                    CowMorningMilkCount = g.Sum(m =>
                        m.MilkType == MilkType.Cow && m.MilkShift == MilkShift.Morning
                            ? m.Quantity : 0),

                    CowEveningMilkCount = g.Sum(m =>
                        m.MilkType == MilkType.Cow && m.MilkShift == MilkShift.Evening
                            ? m.Quantity : 0)
                })
                .OrderBy(o => o.FarmerId)
                .ToListAsync();

            return Ok(farmerMilkData);
        }
       
    }
}
