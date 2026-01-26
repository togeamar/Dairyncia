using Microsoft.AspNetCore.Mvc;
using Dairyncia.Data;
using Dairyncia.DTOs;
using Dairyncia.Models;

namespace Dairyncia.Controllers
{
    [ApiController]
    [Route("api/contact")]
    public class ContactController : ControllerBase
    {
        private readonly AppDbContext _context;

        public ContactController(AppDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody]ContactMessageRequestDto dto)
        {
            Console.WriteLine("Received contact message:");
            // Custom validations 
            //To avoid NullReferenceException and handle invalid request bodies safely.
             if (dto == null)
    {
        return BadRequest("Invalid request body");
    }
            if (string.IsNullOrWhiteSpace(dto.Name))
            {
                return Problem(
                    title: "Validation Error",
                    detail: "Name is required",
                    statusCode: StatusCodes.Status400BadRequest
                );
            }

            if (string.IsNullOrWhiteSpace(dto.Email))
            {
                return Problem(
                    title: "Validation Error",
                    detail: "Email is required",
                    statusCode: StatusCodes.Status400BadRequest
                );
            }

            if (!dto.Email.Contains("@"))
            {
                return Problem(
                    title: "Validation Error",
                    detail: "Invalid email format",
                    statusCode: StatusCodes.Status400BadRequest
                );
            }

            if (string.IsNullOrWhiteSpace(dto.Purpose))
            {
                return Problem(
                    title: "Validation Error",
                    detail: "Purpose is required",
                    statusCode: StatusCodes.Status400BadRequest
                );
            }

            var message = new ContactMessage
            {
                Name = dto.Name,
                Email = dto.Email,
                Purpose = dto.Purpose
            };

            _context.ContactMessages.Add(message);
            await _context.SaveChangesAsync();

            return Ok(new
            {
                message = "Message received successfully"
            });
        }
    }
}
