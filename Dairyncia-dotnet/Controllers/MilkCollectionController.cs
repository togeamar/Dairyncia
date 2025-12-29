
using Dairyncia.Data;
using Dairyncia.DTOs;
using Dairyncia.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/milk-collection")]
    [Authorize(Roles = "Manager,Admin")]
    public class MilkCollectionController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly UserManager<ApplicationUser> _userManager;

        public MilkCollectionController(
            AppDbContext context,
            UserManager<ApplicationUser> userManager)
        {
            _context = context;
            _userManager = userManager;
        }

        // create milk collection
        [HttpPost("create")]
        public async Task<IActionResult> CreateMilkCollection(
            [FromBody] CreateMilkCollectionDto dto)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            //  Get Manager from JWT Token
            var managerId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (managerId == null)
                return Unauthorized("Invalid token");

            // Find Farmer by Email
            var farmer = await _context.Farmers
                .Include(f => f.User)
                .FirstOrDefaultAsync(f => f.User.Email == dto.FarmerEmail);

            if (farmer == null)
                return NotFound("Farmer not found");

            // Calculate Rate & Total
            decimal ratePerLiter =
                (7 * dto.FatPercentage) + (3 * dto.SNF);

            decimal totalAmount =
                ratePerLiter * dto.Quantity;

            // Create MilkCollection Entity
            var milkCollection = new MilkCollection
            {
                FarmerId = farmer.Id,
                ManagerId = managerId,
                MilkType = dto.MilkType,
                MilkShift = dto.MilkShift,
                Quantity = dto.Quantity,
                FatPercentage = dto.FatPercentage,
                SNF = dto.SNF,
                RatePerLiter = ratePerLiter,
                TotalAmount = totalAmount
            };

            // Save to DB
            _context.MilkCollections.Add(milkCollection);
            await _context.SaveChangesAsync();

            return Ok(new
            {
                message = "Milk collection added successfully",
                milkCollection.Id,
                milkCollection.TotalAmount
            });
        }

        // get all entries
        [HttpGet("all")]
        [Authorize(Roles = "Admin,Manager")]
        public async Task<IActionResult> GetAllMilkCollections()
        {
            var data = await _context.MilkCollections
                .Include(m => m.Farmer).ThenInclude(f => f.User)
                .Include(m => m.Manager)
                .OrderByDescending(m => m.CreatedAt)
                .Select(m => new
                {
                    m.Id,
                    Farmer = m.Farmer.User.Email,
                    Manager = m.Manager.Email,
                    m.MilkType,
                    m.MilkShift,
                    m.Quantity,
                    m.RatePerLiter,
                    m.TotalAmount,
                    m.PaymentStatus,
                    m.CreatedAt
                })
                .ToListAsync();

            return Ok(data);
        }
    }
}
