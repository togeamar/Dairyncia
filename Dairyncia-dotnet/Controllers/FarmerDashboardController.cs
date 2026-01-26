using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Dairyncia.DTOs;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/farmer")]
    public class FarmerDashboardController : ControllerBase
    {
        private readonly AppDbContext _context;

        public FarmerDashboardController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet("dashboard/{farmerId}")]
        public async Task<IActionResult> GetFarmerDashboard(int farmerId)
        {
            var farmer = await _context.Farmers
                .Include(f => f.User)
                .FirstOrDefaultAsync(f => f.Id == farmerId);

            if (farmer == null)
                return NotFound("Farmer not found");

            var bank = await _context.BankDetails
                .FirstOrDefaultAsync(b => b.UserId == farmer.UserId);

            var managerWiseData = await _context.MilkCollections
                .Where(m => m.FarmerId == farmerId)
                .GroupBy(m => new
                {
                    m.ManagerId,
                    m.Manager.FullName
                })
                .Select(g => new
                {
                    ManagerId = g.Key.ManagerId,
                    ManagerName = g.Key.FullName,
                    TotalAmount = g.Sum(x => x.TotalAmount),
                    PaidAmount = g.Sum(x =>
                        x.PaymentStatus == Enums.PaymentStatus.Paid ? x.TotalAmount : 0),
                    PendingAmount = g.Sum(x =>
                        x.PaymentStatus == Enums.PaymentStatus.Pending ? x.TotalAmount : 0)
                })
                .ToListAsync();

            return Ok(new
            {
                FarmerName = farmer.User.FullName,
                farmer.User.Email,
                farmer.User.PhoneNumber,
                BankName = bank?.BankName,
                AccountNumber = bank?.AccountNumber,
                Managers = managerWiseData
            });
        }
    }
}
