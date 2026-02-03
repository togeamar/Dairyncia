package com.dairyncia.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairyncia.dto.FarmerListDTO;
import com.dairyncia.repository.FarmerRepository;
import com.dairyncia.repository.MilkCollectionRepository;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
	private final MilkCollectionRepository milkCollectionRepository;
    private final FarmerRepository farmerRepository;

    public ManagerController(MilkCollectionRepository milkCollectionRepository,
                             FarmerRepository farmerRepository) {
        this.milkCollectionRepository = milkCollectionRepository;
        this.farmerRepository = farmerRepository;
    }

    // ================= Farmer Milk Collection =================
    @GetMapping("/get-farmer-milk-collection/{managerId}")
    public List<?> getAllFarmersWithMilkCollection(@PathVariable String managerId) {
        return milkCollectionRepository.getFarmerMilkCollection(managerId);
    }

    // ================= Farmer List =================
    @GetMapping("/get-farmer-list/{managerId}")
    public List<FarmerListDTO> getAllFarmers(@PathVariable String managerId) {
        return farmerRepository.findFarmersByManagerId(managerId);
    }
}
