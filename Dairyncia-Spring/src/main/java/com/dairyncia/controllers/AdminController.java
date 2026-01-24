package com.dairyncia.controllers;

import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.AssignRoleDto;
import com.dairyncia.dto.FarmerListDTO;
import com.dairyncia.dto.PendingUserDto;
import com.dairyncia.entities.*;
import com.dairyncia.custom_exception.BadRequestException;
import com.dairyncia.custom_exception.ResourceNotFoundException;
import com.dairyncia.repository.*;
import com.dairyncia.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FarmerRepository farmerRepository;
    //private final AddressRepository addressRepository;
    //private final BankDetailsRepository bankDetailsRepository;
    //private final MilkCollectionRepository milkCollectionRepository;
    private final AdminService adminService;

    // ================= ASSIGN ROLE =================
    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRole(@Valid @RequestBody AssignRoleDto dto) {
        ApiResponse response=adminService.assignRole(dto);
        
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // ================= PENDING USERS =================
    /*@GetMapping("/pending-users")
    public ResponseEntity<List<PendingUserDto>> getPendingUsers() {
        List<User> pendingUsers = userRepository.findPendingUsers();
        
        List<PendingUserDto> result = new ArrayList<>();
        for (int i = 0; i < pendingUsers.size(); i++) {
            User u = pendingUsers.get(i);
            result.add(new PendingUserDto(i+1,u.getId(),u.getEmail(),u.getFullName(),u.getPhoneNumber()));
        }
        
        return ResponseEntity.ok(result);
    }

    // ================= FARMERS =================
    @GetMapping("/farmers")
    public ResponseEntity<List<FarmerListDTO>> getAllFarmers() {
        List<Farmer> farmers = farmerRepository.findAllWithUser();
        
        List<FarmerListDTO> result=farmers.stream()
        		.map(FarmerListDTO::new)
        		.toList();
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/farmers/{id}")
    public ResponseEntity<FarmerDetailsDto> getFarmerDetails(@PathVariable Integer id) {
        Farmer farmer = farmerRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        BankDetails bank = bankDetailsRepository.findByUserId(farmer.getUserId())
            .orElse(null);

        FarmerDetailsDto dto = FarmerDetailsDto.builder()
            .farmerId(farmer.getId())
            .userId(farmer.getUserId())
            .fullName(farmer.getUser().getFullName())
            .email(farmer.getUser().getEmail())
            .phone(farmer.getUser().getPhoneNumber())
            .build();

        if (farmer.getAddress() != null) {
            dto.setAddress(AddressDto.fromEntity(farmer.getAddress()));
        }

        if (bank != null) {
            dto.setBankDetails(BankDetailsDto.fromEntity(bank));
        }

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/farmers/{id}")
    public ResponseEntity<?> updateFarmer(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateFarmerDto dto) {
        
        Farmer farmer = farmerRepository.findByIdWithUser(id)
            .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        farmer.getUser().setFullName(dto.getFullName());
        farmer.getUser().setPhoneNumber(dto.getPhoneNumber());
        
        userRepository.save(farmer.getUser());

        return ResponseEntity.ok("Farmer profile updated successfully");
    }

    @DeleteMapping("/farmers/{id}")
    public ResponseEntity<?> deleteFarmer(@PathVariable Integer id) {
        Farmer farmer = farmerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        // Check if farmer has milk collections
        boolean hasMilk = milkCollectionRepository.existsByFarmerId(id);
        if (hasMilk) {
            throw new BadRequestException("Farmer cannot be deleted. Milk records exist.");
        }

        farmerRepository.delete(farmer);
        return ResponseEntity.ok("Farmer deleted");
    }

    // ================= MANAGERS =================
    @GetMapping("/managers")
    public ResponseEntity<List<ManagerListDto>> getAllManagers() {
        Role managerRole = roleRepository.findByName(Role.RoleType.ROLE_MANAGER)
            .orElseThrow(() -> new ResourceNotFoundException("Manager role not found"));

        List<User> managers = userRepository.findByRolesContaining(managerRole);

        List<ManagerListDto> result = managers.stream()
            .map(u -> ManagerListDto.builder()
                .managerId(u.getId())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .phone(u.getPhoneNumber())
                .build())
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/managers/{id}")
    public ResponseEntity<?> getManagerById(@PathVariable String id) {
        User manager = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", manager.getId());
        response.put("email", manager.getEmail());
        response.put("fullName", manager.getFullName());
        response.put("phoneNumber", manager.getPhoneNumber());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/managers/{id}")
    public ResponseEntity<?> updateManager(
            @PathVariable String id,
            @Valid @RequestBody UpdateManagerDto dto) {
        
        User manager = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        manager.setFullName(dto.getFullName());
        manager.setPhoneNumber(dto.getPhoneNumber());
        
        userRepository.save(manager);

        return ResponseEntity.ok("Manager updated successfully");
    }

    @DeleteMapping("/managers/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable String id) {
        User manager = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        // Check if manager has milk collection records
        boolean isUsed = milkCollectionRepository.existsByManagerId(id);
        if (isUsed) {
            throw new BadRequestException("Manager cannot be deleted. Records exist.");
        }

        userRepository.delete(manager);
        return ResponseEntity.ok("Manager deleted successfully");
    }*/

    // Continue in next artifact...
}