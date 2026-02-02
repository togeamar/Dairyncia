package com.dairyncia.service;

import java.util.List;

import com.dairyncia.dto.AddressDTO;
import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.AssignRoleDto;
import com.dairyncia.dto.BankDetailsDTO;
import com.dairyncia.dto.FarmerProfileUpdateDTO;
import com.dairyncia.dto.FarmerResponseDTO;
import com.dairyncia.dto.ManagerResponseDTO;
import com.dairyncia.dto.ManagerUpdateDTO;

public interface AdminService {
	ApiResponse assignRole(AssignRoleDto dto);
	
	List<ManagerResponseDTO> getAllManagers();
	void updateManager(Long managerId, ManagerUpdateDTO dto);
	void deleteManager(Long managerId);
	
	List<FarmerResponseDTO> getAllFarmers();
	
	FarmerResponseDTO getFarmerById(Long farmerId);

    ApiResponse updateFarmer(Long farmerId, FarmerProfileUpdateDTO dto);
    
    ApiResponse saveOrUpdateFarmerAddress(Long userId, AddressDTO dto);
    ApiResponse saveOrUpdateBankDetails(Long userId, BankDetailsDTO dto);
    ApiResponse deleteFarmerByUserId(Long userId);
}
