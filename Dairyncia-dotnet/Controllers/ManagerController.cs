using Dairyncia.DTOs;
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
        [Route("get-farmer-milk-collection/{managerId}")]
        public async Task<IActionResult> getAllFarmersWithMilkCollection(string managerId)
        {
            var farmerMilkData = await _context.MilkCollections
                .Where(m => m.Farmer.ManagerId == managerId)
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

        [HttpGet]
        [Route("get-farmer-list/{managerId}")]
        public async Task<IActionResult> getAllFarmers(string managerId)
        {
            var farmers = await _context.Farmers
            .Include(f => f.User)
            .Include(f => f.Address)
            .Where(f => f.ManagerId == managerId)
            .OrderByDescending(f => f.CreatedAt)
            .Select(f => new FarmerListDTO
            {
                Id = f.Id,
                Email = f.User.Email,
                FullName = f.User.FullName,
                CreatedAt = f.CreatedAt
            })
            .ToListAsync();
            return Ok(farmers);
        }
       
    }
}
