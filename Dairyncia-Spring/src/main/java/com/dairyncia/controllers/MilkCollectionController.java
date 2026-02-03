package com.dairyncia.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.CreateMilkCollectionDto;
import com.dairyncia.dto.MilkCollectionResponseDto;
import com.dairyncia.dto.UpdateMilkCollectionDto;
import com.dairyncia.service.MilkCollectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/manager/milk-collection")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Milk Collection Management", description = "APIs for managing daily milk collections")
public class MilkCollectionController {

    private final MilkCollectionService milkCollectionService;

    @Operation(
        summary = "Create milk collection",
        description = "Record a new milk collection entry for a farmer. Manager ID is extracted from JWT token."
    )
    @PostMapping("/create")
    public ResponseEntity<?> createMilkCollection(
            @Valid @RequestBody CreateMilkCollectionDto dto,
            Authentication authentication) {
        
        MilkCollectionResponseDto response = milkCollectionService.createMilkCollection(dto, authentication);
        
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get all milk collections",
        description = "Retrieve all milk collection records with farmer and manager details"
    )
    @GetMapping("/all/{managerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<MilkCollectionResponseDto>> getAllMilkCollections(@PathVariable String managerId) {
    	Long lngManagerId = Long.parseLong(managerId);
    	
        List<MilkCollectionResponseDto> collections = milkCollectionService.getMilkCollectionByManagerId(lngManagerId);
        return ResponseEntity.ok(collections);
    }

    @Operation(
        summary = "Get today's milk collections",
        description = "Retrieve all milk collections recorded today"
    )
    @GetMapping("/todays/{managerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<MilkCollectionResponseDto>> getTodaysMilkCollections(@PathVariable String managerId) {
    	LocalDate today = LocalDate.now();
    	Long lngManagerId = Long.parseLong(managerId);
    	
        List<MilkCollectionResponseDto> collections = milkCollectionService.getTodaysMilkCollectionsByManagerId(lngManagerId, today);
        return ResponseEntity.ok(collections);
    }

    @Operation(
        summary = "Get milk collection by ID",
        description = "Retrieve a specific milk collection record by its ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<MilkCollectionResponseDto> getMilkCollectionById(
            @Parameter(description = "Milk collection ID") @PathVariable Long id) {
        
        MilkCollectionResponseDto collection = milkCollectionService.getMilkCollectionById(id);
        return ResponseEntity.ok(collection);
    }

    @Operation(
        summary = "Update milk collection",
        description = "Update quantity, fat percentage, and SNF of an existing milk collection. Rate is recalculated automatically."
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<MilkCollectionResponseDto> updateMilkCollection(
            @Parameter(description = "Milk collection ID") @PathVariable Long id,
            @Valid @RequestBody UpdateMilkCollectionDto dto) {
        
        MilkCollectionResponseDto response = milkCollectionService.updateMilkCollection(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete milk collection",
        description = "Delete a milk collection record by ID"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMilkCollection(
            @Parameter(description = "Milk collection ID") @PathVariable Long id) {
        
        milkCollectionService.deleteMilkCollection(id);
        return ResponseEntity.ok(new ApiResponse("Milk collection deleted successfully", "SUCCESS"));
    }
}
