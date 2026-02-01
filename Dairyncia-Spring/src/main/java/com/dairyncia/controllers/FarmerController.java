package com.dairyncia.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairyncia.dto.FarmerDashboardDto;
import com.dairyncia.dto.FarmerListDTO;
import com.dairyncia.security.CustomUserDetails;
import com.dairyncia.service.FarmerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/farmer")
@PreAuthorize("hasRole('FARMER')")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @GetMapping("/dashboard")
    public FarmerDashboardDto dashboard(Authentication auth) {

        CustomUserDetails userDetails =
                (CustomUserDetails) auth.getPrincipal();

        return farmerService.getCompleteDashboard(userDetails.getUser());
    }
    
//    @GetMapping
//    public List<FarmerListDTO> getFarmers() {
//        return farmerService.getAllFarmers();
//    }
}