using Dairyncia.DTOs;
using Dairyncia.Enums;
using Dairyncia.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/manager/milk-collection")]
    [Authorize(Roles = "Manager,Admin")]
    public class MilkCollectionController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly MilkRateHelper _milkRateHelper;

        public MilkCollectionController(
            AppDbContext context,
            UserManager<ApplicationUser> userManager,
            MilkRateHelper milkRateHelper)
        {
            _context = context;
            _userManager = userManager;
            _milkRateHelper = milkRateHelper;
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

            var today = DateTime.Today;
            var tomorrow = today.AddDays(1);

            var alreadySubmitted = await _context.MilkCollections
                .AnyAsync(x =>
                    x.FarmerId == farmer.Id &&
                    x.MilkShift == dto.MilkShift &&
                    x.MilkType == dto.MilkType &&
                    x.CreatedAt >= today &&
                    x.CreatedAt < tomorrow);
                

            if (alreadySubmitted)
            {
                return BadRequest("Milk collection for this farmer is already submitted today.");
            }

            var ratePerLiter = await _milkRateHelper
                .GetRatePerLiter(dto.FatPercentage, dto.SNF, dto.MilkType);

            if (!ratePerLiter.IsSuccess)
            {
                return NotFound(new{message=ratePerLiter.Error});
            }

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
                RatePerLiter = ratePerLiter.Data.Rate,
                TotalAmount = ratePerLiter.Data.Rate * dto.Quantity,
                PaymentStatus = PaymentStatus.Pending
            };

            // Save to DB
            _context.MilkCollections.Add(milkCollection);        
            await _context.SaveChangesAsync();
         
            return Ok(new
            {
                message = "Milk collection added successfully",
                milkCollection.Id,
                milkCollection.TotalAmount,
                isExactMatch = ratePerLiter.Data.IsExactMatch
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
                    m.FatPercentage,
                    m.SNF,
                    m.RatePerLiter,
                    m.TotalAmount,
                    m.PaymentStatus,
                    m.CreatedAt
                })
                .ToListAsync();

            return Ok(data);
        }

        [HttpGet("todays")]
        [Authorize(Roles = "Admin,Manager")]
        public async Task<IActionResult> GetTodaysMilkCollections()
        {
            var today = DateTime.Today;
            var tomorrow = today.AddDays(1);

            var data = await _context.MilkCollections
                .Include(m => m.Farmer).ThenInclude(f => f.User)
                .Include(m => m.Manager)
                .Where(c => c.CreatedAt >= today &&
                    c.CreatedAt < tomorrow)
                .OrderByDescending(m => m.CreatedAt)
                .Select(m => new
                {
                    m.Id,
                    Farmer = m.Farmer.User.FullName,
                    Manager = m.Manager.FullName,
                    m.MilkType,
                    m.MilkShift,
                    m.Quantity,
                    m.FatPercentage,
                    m.SNF,
                    m.RatePerLiter,
                    m.TotalAmount,
                    m.PaymentStatus,
                    m.CreatedAt
                })
                .ToListAsync();

            return Ok(data);
        }


        [HttpGet("{id}")]
        public async Task<IActionResult> GetMilkCollectionById(int id)
        {
            var milk = await _context.MilkCollections
                .Include(m => m.Farmer).ThenInclude(f => f.User)
                .Include(m => m.Manager)
                .FirstOrDefaultAsync(m => m.Id == id);

            if (milk == null)
                return NotFound("Milk collection not found");

            return Ok(milk);
        }

         
        [HttpPut("update/{id}")]
        public async Task<IActionResult> UpdateMilkCollection(
            int id,
            [FromBody] UpdateMilkCollectionDto dto)
        {
            var milk = await _context.MilkCollections.FindAsync(id);
            if (milk == null)
                return NotFound("Milk collection not found");

            var ratePerLiter = await _milkRateHelper
                .GetRatePerLiter(dto.FatPercentage, dto.SNF, milk.MilkType);

            milk.Quantity = dto.Quantity;
            milk.FatPercentage = dto.FatPercentage;
            milk.SNF = dto.SNF;
            milk.RatePerLiter = ratePerLiter.Data.Rate;
            milk.TotalAmount = ratePerLiter.Data.Rate * dto.Quantity;

            await _context.SaveChangesAsync();

            return Ok(new
            {
                message = "Milk collection updated successfully",
                milk.Id,
                milk.TotalAmount
            });
        }

           
        [HttpDelete("delete/{id}")]
        public async Task<IActionResult> DeleteMilkCollection(int id)
        {
            var milk = await _context.MilkCollections.FindAsync(id);
            if (milk == null)
                return NotFound("Milk collection not found");

            _context.MilkCollections.Remove(milk);
            await _context.SaveChangesAsync();

            return Ok("Milk collection deleted successfully");
        }

    }
}