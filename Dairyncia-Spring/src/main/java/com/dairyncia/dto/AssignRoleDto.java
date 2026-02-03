package com.dairyncia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class AssignRoleDto {
	@NotBlank(message="email is required")
	@Email(message="invalid email format")
	public String Email;
	
	@NotBlank(message="role is required")
	public String Role;
	
	public String ManagerId;
}
