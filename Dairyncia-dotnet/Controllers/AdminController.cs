using Dairyncia.Data;
using Dairyncia.DTOs;
using Dairyncia.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/admin")]
    [Authorize(Roles = "Admin")]
    public class AdminController : ControllerBase
    {
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly RoleManager<IdentityRole> _roleManager;
        private readonly AppDbContext _context;

        public AdminController(
            UserManager<ApplicationUser> userManager,
            RoleManager<IdentityRole> roleManager,
            AppDbContext context)
        {
            _userManager = userManager;
            _roleManager = roleManager;
            _context = context;
        }

        // ================= ASSIGN ROLE =================
        [HttpPost("assign-role")]
        public async Task<IActionResult> AssignRole([FromBody] AssignRoleDto dto)
        {
            // 1️⃣ Find user
            var user = await _userManager.FindByEmailAsync(dto.Email);
            if (user == null)
                return NotFound("User not found");

            // 2️⃣ Validate role
            if (!await _roleManager.RoleExistsAsync(dto.Role))
                return BadRequest("Role does not exist");

            // 3️⃣ Remove existing roles (single-role system)
            var currentRoles = await _userManager.GetRolesAsync(user);
            if (currentRoles.Any())
                await _userManager.RemoveFromRolesAsync(user, currentRoles);

            // 4️⃣ Assign new role
            var roleResult = await _userManager.AddToRoleAsync(user, dto.Role);
            if (!roleResult.Succeeded)
                return BadRequest(roleResult.Errors);

            // 5️⃣ DOMAIN LOGIC (IMPORTANT)
            if (dto.Role == "Farmer")
            {
                var farmerExists = await _context.Farmers
                    .AnyAsync(f => f.UserId == user.Id);

                if (!farmerExists)
                {
                    var farmer = new Farmer
                    {
                        UserId = user.Id,
                        AddressId = null // Address can be added later
                    };

                    _context.Farmers.Add(farmer);
                    await _context.SaveChangesAsync();
                }
            }

            // (Optional) If role changed from Farmer → remove Farmer row
            if (currentRoles.Contains("Farmer") && dto.Role != "Farmer")
            {
                var farmer = await _context.Farmers
                    .FirstOrDefaultAsync(f => f.UserId == user.Id);

                if (farmer != null)
                {
                    _context.Farmers.Remove(farmer);
                    await _context.SaveChangesAsync();
                }
            }

            return Ok(new
            {
                message = $"Role '{dto.Role}' assigned successfully",
                user.Email
            });
        }
    }
}
