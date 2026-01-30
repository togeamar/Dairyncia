package com.dairyncia.service;

import java.util.HashMap;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyncia.custom_exception.BadRequestException;
import com.dairyncia.custom_exception.ResourceNotFoundException;
import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.AssignRoleDto;
import com.dairyncia.entities.Farmer;
import com.dairyncia.entities.Role;
import com.dairyncia.entities.User;

import com.dairyncia.repository.RoleRepository;
import com.dairyncia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	
	private final UserRepository userRepository;

	private final RoleRepository roleRepository;
	

	
	private final ModelMapper modelMapper;
	
	@Override
	public ApiResponse assignRole(AssignRoleDto dto) {
		
		// Find user
	    User user = userRepository.findByEmail(dto.getEmail())
	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	
	    // Validate role
	    Role.RoleType roleType;
	    try {
	        roleType = Role.RoleType.valueOf("ROLE_" + dto.getRole().toUpperCase());
	    } catch (IllegalArgumentException e) {
	        throw new BadRequestException("Invalid role: " + dto.getRole());
	    }
	
	    Role role = roleRepository.findByName(roleType)
	        .orElseThrow(() -> new ResourceNotFoundException("Role does not exist"));
	
	    // Remove existing roles (single-role system)
	    user.clearRoles();
	
	    // Assign new role
	    user.addRole(role);
	    userRepository.save(user);
	
	    String message="Role" + dto.getRole() + "assigned successfully";
	    ApiResponse response=new ApiResponse(message,"200");
	    //response.put();
	    //response.put("email", user.getEmail());
	    return response;
		}
	
	
	
}
