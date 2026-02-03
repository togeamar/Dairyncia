package com.dairyncia.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.dairyncia.dto.CreateMilkCollectionDto;
import com.dairyncia.dto.MilkCollectionResponseDto;
import com.dairyncia.dto.UpdateMilkCollectionDto;

public interface MilkCollectionService {
    
    /**
     * Create a new milk collection entry
     */
    MilkCollectionResponseDto createMilkCollection(CreateMilkCollectionDto dto, Authentication authentication);
    
    /**
     * Get all milk collections
     */
    List<MilkCollectionResponseDto> getAllMilkCollections();
    
    /**
     * Get today's milk collections
     */
    List<MilkCollectionResponseDto> getTodaysMilkCollections();
    
    /**
     * Get milk collection by ID
     */
    MilkCollectionResponseDto getMilkCollectionById(Long id);
    
    /**
     * Get milk collection by Manager ID
     */
    List<MilkCollectionResponseDto> getMilkCollectionByManagerId(Long managerId);
    
    List<MilkCollectionResponseDto> getTodaysMilkCollectionsByManagerId(Long managerId, LocalDate today);
    
    /**
     * Update milk collection
     */
    MilkCollectionResponseDto updateMilkCollection(Long id, UpdateMilkCollectionDto dto);
    
    /**
     * Delete milk collection
     */
    void deleteMilkCollection(Long id);

	
}
