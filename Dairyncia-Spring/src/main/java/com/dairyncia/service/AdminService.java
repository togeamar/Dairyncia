package com.dairyncia.service;

import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.AssignRoleDto;

public interface AdminService {
	ApiResponse assignRole(AssignRoleDto dto);
	
	
}
