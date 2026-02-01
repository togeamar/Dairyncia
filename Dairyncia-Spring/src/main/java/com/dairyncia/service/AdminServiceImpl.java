package com.dairyncia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyncia.custom_exception.BadRequestException;
import com.dairyncia.custom_exception.ResourceNotFoundException;
import com.dairyncia.dto.AddressDTO;
import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.AssignRoleDto;
import com.dairyncia.dto.BankDetailsDTO;
import com.dairyncia.dto.FarmerProfileUpdateDTO;
import com.dairyncia.dto.FarmerResponseDTO;
import com.dairyncia.dto.ManagerResponseDTO;
import com.dairyncia.dto.ManagerUpdateDTO;
import com.dairyncia.entities.Address;
import com.dairyncia.entities.BankDetails;
import com.dairyncia.entities.Farmer;
import com.dairyncia.entities.Role;
import com.dairyncia.entities.Role.RoleType;
import com.dairyncia.entities.User;
import com.dairyncia.repository.AddressRepository;
import com.dairyncia.repository.BankDetailsRepository;
import com.dairyncia.repository.FarmerRepository;
import com.dairyncia.repository.RoleRepository;
import com.dairyncia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	
	private final UserRepository userRepository;

	private final RoleRepository roleRepository;
	
	private final FarmerRepository farmerRepository;
	
	private final AddressRepository addressRepository;
	
	private final ModelMapper modelMapper;
	
	private final BankDetailsRepository bankDetailsRepository;
	
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
	    
	    if (roleType == Role.RoleType.ROLE_FARMER) {

	        boolean exists = farmerRepository.existsByUser(user);
	        if (!exists) {
	            Farmer farmer = Farmer.builder()
	                    .user(user)
	                    .build();
	            farmerRepository.save(farmer);
	        }
	    }
	
	    String message="Role" + dto.getRole() + "assigned successfully";
	    ApiResponse response=new ApiResponse(message,"200");
	    //response.put();
	    //response.put("email", user.getEmail());
	    return response;
		}

	@Override
	public List<ManagerResponseDTO> getAllManagers() {
		
		 return userRepository.findAllManagers()
	                .stream()
	                .map(user -> {
	                    ManagerResponseDTO dto = new ManagerResponseDTO();
	                    dto.setId(user.getId());
	                    dto.setFullName(user.getFullName());
	                    dto.setEmail(user.getEmail());
	                    dto.setPhoneNumber(user.getPhoneNumber());
	                    return dto;
	                })
	                .collect(Collectors.toList());
	}
	
	// update manager
	@Override
	public void updateManager(Long managerId, ManagerUpdateDTO dto) {
		User user = userRepository.findById(managerId)
				.orElseThrow( () -> new RuntimeException("Manager not found"));
		
		user.setFullName(dto.getFullName());
		user.setPhoneNumber(dto.getPhoneNumber());
		userRepository.save(user);
		
	}

	@Override
	public void deleteManager(Long managerId) {
	
		if(!userRepository.existsById(managerId)) {
			throw new RuntimeException("Manager not found");
		}
			
		userRepository.deleteById(managerId);
		
	}

	@Override
	public List<FarmerResponseDTO> getAllFarmers() {
		List<User> farmers =
                userRepository.findUsersByRole(RoleType.ROLE_FARMER);

        return farmers.stream()
                .map(user -> FarmerResponseDTO.builder()
                        .id(user.getId())             
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .createdAt(user.getCreatedAt())
                        .build())
                .toList();
	}

	@Override
    public FarmerResponseDTO getFarmerById(Long farmerId) {
        Farmer farmer = farmerRepository.findByUserId(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        User user = farmer.getUser(); // <-- use getUser() directly

        return FarmerResponseDTO.builder()
                .id(farmer.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
               
                .createdAt(farmer.getCreatedAt())
                .build();
    }

    @Override
    public ApiResponse updateFarmer(Long farmerId, FarmerProfileUpdateDTO dto) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        User user = farmer.getUser(); // <-- get associated User

        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());

        userRepository.save(user);

        return new ApiResponse("Farmer profile updated successfully", "200");
    }

	@Override
	public ApiResponse saveOrUpdateFarmerAddress(Long userId, AddressDTO dto) {
		
		Farmer farmer = farmerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
		
		Address address;
		
		if(farmer.getAddress() != null) {
			address = farmer.getAddress();
		}
		else {
			address = new Address();
		}
		
		address.setVillage(dto.getVillage());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setPincode(dto.getPincode());
		
		addressRepository.save(address);
		
		farmer.setAddressId(address.getId());
		farmerRepository.save(farmer);
		
		return new ApiResponse("Address saved successfullly", "200");
	}

	@Override
	public ApiResponse saveOrUpdateBankDetails(Long userId, BankDetailsDTO dto) {
		
		Farmer farmer = farmerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));
		
		BankDetails bankDetails = bankDetailsRepository.findByUserId(userId)
                .orElseGet(BankDetails::new);
		
		User user = userRepository.findById(userId)
				.orElseThrow( () -> new RuntimeException("User not found"));
		
		
		bankDetails.setBankName(dto.getBankName());
		bankDetails.setAccountHolderName(dto.getAccountHolderName());
		bankDetails.setAccountNumber(dto.getAccountNumber());
		bankDetails.setIfsc(dto.getIfsc());
		bankDetails.setUser(user);
		
		bankDetailsRepository.save(bankDetails);
		
		
		return new ApiResponse("Bank Details Updated Successfully","200");
	}

	@Override
    public ApiResponse deleteFarmerByUserId(Long userId) {

        Farmer farmer = farmerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        bankDetailsRepository.findByUserId(userId)
                .ifPresent(bankDetailsRepository::delete);

        farmerRepository.delete(farmer);

        userRepository.deleteById(userId);

        return new ApiResponse("Farmer deleted successfully", "200");
    }

    



    
    

	
	
	
	
	
}
