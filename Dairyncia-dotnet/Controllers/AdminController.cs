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
    //[Authorize(Roles = "Admin")]
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
            if (dto.Role.ToUpper() == "FARMER")
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
            if (currentRoles.Contains("Farmer") && dto.Role.ToUpper() != "FARMER")
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

        // ================= PENDING USERS =================
        [HttpGet("pending-users")]
        public async Task<IActionResult> GetPendingUsers()
        {
            var pendingUsers = await (
                from user in _context.Users
                join userRole in _context.UserRoles
                    on user.Id equals userRole.UserId into ur
                from userRole in ur.DefaultIfEmpty()
                where userRole == null
                select new
                {
                    user.Id,
                    user.Email,
                    user.FullName,
                    user.PhoneNumber
                }
            ).ToListAsync();

            var result = pendingUsers.Select((u, index) => new PendingUserDto
            {
                SrNo = index + 1,
                UserId = u.Id,
                Email = u.Email,
                FullName = u.FullName,
                Phone = u.PhoneNumber
            });

            return Ok(result);
        }



        // list of farmers
        [HttpGet("farmers")]
        public async Task<IActionResult> GetAllFarmers()
        {
            var farmers = await _context.Farmers
            .Include(f => f.User)
            .Include(f => f.Address)
            .OrderByDescending(f => f.CreatedAt)
            .Select(f => new FarmerListDTO
            {
                FarmerId = f.Id,
                Email = f.User.Email,
                FullName = f.User.FullName,
                CreatedAt = f.CreatedAt
            })
            .ToListAsync();
            return Ok(farmers);
        }

        // famer details by id
        [HttpGet("farmers/{id}")]
        public async Task<IActionResult> GetFarmerDetails(int id)
        {
            var farmer = await _context.Farmers
                .Include(f => f.User)
                .Include(f => f.Address)
                .FirstOrDefaultAsync(f => f.Id == id);

            if (farmer == null)
                return NotFound("Farmer not found");

            var bank = await _context.BankDetails
                .FirstOrDefaultAsync(b => b.UserId == farmer.UserId);

            return Ok(new FarmerDetailsDto
            {
                FarmerId = farmer.Id,
                UserId = farmer.UserId,
                FullName = farmer.User.FullName,
                Email = farmer.User.Email,
                Phone = farmer.User.PhoneNumber,
                Address = farmer.Address == null ? null : new AddressDto
                {
                    AddressId = farmer.Address.Id,
                    Village = farmer.Address.Village,
                    City = farmer.Address.City,
                    State = farmer.Address.State,
                    Pincode = farmer.Address.Pincode
                },
                BankDetails = bank == null ? null : new BankDetailsDto
                {
                    BankDetailsId = bank.Id,
                    BankName = bank.BankName,
                    AccountNumber = bank.AccountNumber,
                    AccountHolderName = bank.AccountHolderName,
                    IFSC = bank.IFSC
                }
            });
        }

        // add or update address
        [HttpPost("farmers/{id}/address")]
        public async Task<IActionResult> AddOrUpdateAddress(int id, AddressDto dto)
        {
            var farmer = await _context.Farmers
                .Include(f => f.Address)
                .FirstOrDefaultAsync(f => f.Id == id);

            if (farmer == null)
                return NotFound("Farmer not found");

            if (farmer.Address == null)
            {
                var address = new Address
                {
                    Village = dto.Village,
                    City = dto.City,
                    State = dto.State,
                    Pincode = dto.Pincode
                };

                _context.Addresses.Add(address);
                await _context.SaveChangesAsync();

                farmer.AddressId = address.Id;
            }
            else
            {
                farmer.Address.Village = dto.Village;
                farmer.Address.City = dto.City;
                farmer.Address.State = dto.State;
                farmer.Address.Pincode = dto.Pincode;
            }

            await _context.SaveChangesAsync();
            return Ok("Address saved successfully");
        }

        //
        [HttpPost("farmers/{id}/bank")]
        public async Task<IActionResult> AddOrUpdateBank(int id, BankDetailsDto dto)
        {
            var farmer = await _context.Farmers.FindAsync(id);
            if (farmer == null)
                return NotFound("Farmer not found");

            var bank = await _context.BankDetails
                .FirstOrDefaultAsync(b => b.UserId == farmer.UserId);

            if (bank == null)
            {
                bank = new BankDetails
                {
                    UserId = farmer.UserId,
                    BankName = dto.BankName,
                    AccountNumber = dto.AccountNumber,
                    AccountHolderName = dto.AccountHolderName,
                    IFSC = dto.IFSC
                };
                _context.BankDetails.Add(bank);
            }
            else
            {
                bank.BankName = dto.BankName;
                bank.AccountNumber = dto.AccountNumber;
                bank.AccountHolderName = dto.AccountHolderName;
                bank.IFSC = dto.IFSC;
            }

            await _context.SaveChangesAsync();
            return Ok("Bank details saved successfully");
        }

        // delete farmer
        [HttpDelete("farmers/{id}")]
        public async Task<IActionResult> DeleteFarmer(int id)
        {
            var farmer = await _context.Farmers.FindAsync(id);
            if (farmer == null)
                return NotFound();

            var hasMilk = await _context.MilkCollections
                .AnyAsync(m => m.FarmerId == id);

            if (hasMilk)
                return BadRequest("Farmer cannot be deleted. Milk records exist.");

            _context.Farmers.Remove(farmer);
            await _context.SaveChangesAsync();

            return Ok("Farmer deleted");
        }

        // edit farmer profile
        [HttpPut("farmers/{id}")]
        public async Task<IActionResult> UpdateFarmer(int id, UpdateFarmerDto dto)
        {
            var farmer = await _context.Farmers
                .Include(f => f.User)
                .FirstOrDefaultAsync(f => f.Id == id);

            if (farmer == null)
                return NotFound("Farmer not found");

            farmer.User.FullName = dto.FullName;
            farmer.User.PhoneNumber = dto.PhoneNumber;

            await _context.SaveChangesAsync();
            return Ok("Farmer profile updated successfully");
        }






        // list of managers
        [HttpGet("managers")]
        public async Task<IActionResult> GetAllManagers()
        {
            var managers = await _userManager.GetUsersInRoleAsync("Manager");

            var result = managers
                .Select(u => new ManagerListDto
                {
                    ManagerId = u.Id,
                    Email = u.Email,
                    FullName = u.FullName,
                    Phone = u.PhoneNumber
                })
                .ToList();

            return Ok(result);
        }

        // get manager details by id
        [HttpGet("managers/{id}")]
        public async Task<IActionResult> GetManagerById(string id)
        {
            var manager = await _userManager.FindByIdAsync(id);
            if (manager == null)
                return NotFound("Manager not found");

            return Ok(new
            {
                manager.Id,
                manager.Email,
                manager.FullName,
                manager.PhoneNumber
            });
        }

        // update manager details
        [HttpPut("managers/{id}")]
        public async Task<IActionResult> UpdateManager(string id, UpdateManagerDto dto)
        {
            var manager = await _userManager.FindByIdAsync(id);
            if (manager == null)
                return NotFound("Manager not found");

            manager.FullName = dto.FullName;
            manager.PhoneNumber = dto.PhoneNumber;

            var result = await _userManager.UpdateAsync(manager);
            if (!result.Succeeded)
                return BadRequest(result.Errors);

            return Ok("Manager updated successfully");
        }

        // delete manager 
        [HttpDelete("managers/{id}")]
        public async Task<IActionResult> DeleteManager(string id)
        {
            var manager = await _userManager.FindByIdAsync(id);
            if (manager == null)
                return NotFound("Manager not found");

            // Check references
            var isUsed = await _context.MilkCollections
                .AnyAsync(m => m.ManagerId == id);

            if (isUsed)
                return BadRequest("Manager cannot be deleted. Records exist.");

            // Remove roles
            var roles = await _userManager.GetRolesAsync(manager);
            if (roles.Any())
                await _userManager.RemoveFromRolesAsync(manager, roles);

            var result = await _userManager.DeleteAsync(manager);
            if (!result.Succeeded)
                return BadRequest(result.Errors);

            return Ok("Manager deleted successfully");
        }

        // list of milk details
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var list = await _context.MilkCollections
                .Include(m => m.Farmer).ThenInclude(f => f.User)
                .Include(m => m.Manager)
                .OrderByDescending(m => m.CreatedAt)
                .Select(m => new MilkCollectionListDto
                {
                    Id = m.Id,
                    FarmerName = m.Farmer.User.FullName,
                    ManagerName = m.Manager.FullName,
                    MilkType = m.MilkType,
                    MilkShift = m.MilkShift,
                    Quantity = m.Quantity,
                    TotalAmount = m.TotalAmount,
                    PaymentStatus = m.PaymentStatus,
                    CreatedAt = m.CreatedAt
                })
                .ToListAsync();

            return Ok(list);
        }

        // update milk details
        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, UpdateMilkCollectionDto dto)
        {
            var record = await _context.MilkCollections.FindAsync(id);
            if (record == null)
                return NotFound();

            record.Quantity = dto.Quantity;
            record.FatPercentage = dto.FatPercentage;
            record.SNF = dto.SNF;
            record.RatePerLiter = dto.RatePerLiter;
            record.TotalAmount = dto.Quantity * dto.RatePerLiter;
            record.PaymentStatus = dto.PaymentStatus;

            await _context.SaveChangesAsync();
            return Ok("Milk collection updated");
        }

        // delete milk record
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            var record = await _context.MilkCollections.FindAsync(id);
            if (record == null)
                return NotFound();

            _context.MilkCollections.Remove(record);
            await _context.SaveChangesAsync();

            return Ok("Milk collection deleted");
        }


    }
}
