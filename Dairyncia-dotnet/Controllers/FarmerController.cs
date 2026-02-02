using Dairyncia.DTOs;
using Dairyncia.Models;
using Dairyncia.Enums; 
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/farmer")]
    [Authorize(Roles = "Farmer")]
    public class FarmerController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly UserManager<ApplicationUser> _userManager;

        public FarmerController(AppDbContext context, UserManager<ApplicationUser> userManager)
        {
            _context = context;
            _userManager = userManager;
        }

        // GET: api/farmer/profile
        [HttpGet("profile")]
        public async Task<IActionResult> GetProfile()
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (string.IsNullOrEmpty(userId))
                return Unauthorized();

            var user = await _userManager.FindByIdAsync(userId);
            if (user == null)
                return NotFound("User not found");

            var farmer = await _context.Farmers
                .Include(f => f.Address)   
                .AsNoTracking()
                .FirstOrDefaultAsync(f => f.UserId == userId);

            if (farmer == null)
                return NotFound("Farmer record not found");

            var response = new FarmerBasicProfileDto
            {
                FullName = user.FullName,
                Email = user.Email,
                PhoneNumber = user.PhoneNumber,

                Village = farmer.Address?.Village,
                City = farmer.Address?.City,
                State = farmer.Address?.State,
                Pincode = farmer.Address?.Pincode
            };

            return Ok(response);
        }


        // GET: api/farmer/milk-collections
        [HttpGet("milk-collections")]
        public async Task<IActionResult> GetMilkCollections()
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (string.IsNullOrEmpty(userId))
                return Unauthorized("User not authenticated");



            var farmer = await _context.Farmers
                .AsNoTracking()
                .FirstOrDefaultAsync(f => f.UserId == userId);

            if (farmer == null)
                return NotFound("Farmer record not found");

            var now = DateTime.Now;
            var startOfMonth = new DateTime(now.Year, now.Month, 1);
            var startOfNextMonth = startOfMonth.AddMonths(1);

            var collections = await _context.MilkCollections
                .AsNoTracking()
                .Where(
                mc => mc.FarmerId == farmer.Id &&
                      mc.CreatedAt >= startOfMonth &&
                      mc.CreatedAt < startOfNextMonth

                )
                .OrderBy(mc => mc.CreatedAt)
                .Select(mc => new MilkCollectionDto
                {
                    Id = mc.Id,
                    MilkType = ((MilkType)mc.MilkType).ToString(),      
                    MilkShift = ((MilkShift)mc.MilkShift).ToString(),     
                    Quantity = mc.Quantity,
                    FatPercentage = mc.FatPercentage,
                    SNF = mc.SNF,
                    RatePerLiter = mc.RatePerLiter,
                    TotalAmount = mc.TotalAmount,
                    PaymentStatus = ((PaymentStatus)mc.PaymentStatus).ToString(), 
                    CreatedAt = mc.CreatedAt
                })
                .ToListAsync();

            return Ok(collections);
        }
    }
}
