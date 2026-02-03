package com.dairyncia.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyncia.custom_exception.BadRequestException;
import com.dairyncia.custom_exception.ResourceNotFoundException;
import com.dairyncia.dto.CreateMilkCollectionDto;
import com.dairyncia.dto.MilkCollectionResponseDto;
import com.dairyncia.dto.RateCalculationResult;
import com.dairyncia.dto.UpdateMilkCollectionDto;
import com.dairyncia.entities.Farmer;
import com.dairyncia.entities.MilkCollection;
import com.dairyncia.entities.User;
import com.dairyncia.enums.PaymentStatus;
import com.dairyncia.repository.FarmerRepository;
import com.dairyncia.repository.MilkCollectionRepository;
import com.dairyncia.repository.UserRepository;
import com.dairyncia.util.MilkRateHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MilkCollectionServiceImpl implements MilkCollectionService {

    private final MilkCollectionRepository milkCollectionRepository;
    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;
    private final MilkRateHelper milkRateHelper;

    @Override
    public MilkCollectionResponseDto createMilkCollection(CreateMilkCollectionDto dto, Authentication authentication) {
        
        // Get manager ID from JWT token
        String managerEmail = authentication.getName();
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        // Find farmer by email
        User farmerUser = userRepository.findByEmail(dto.getFarmerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found"));

        Farmer farmer = farmerRepository.findByUserId(farmerUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer record not found"));

        // Check if already submitted today
        LocalDate today = LocalDate.now();
        boolean alreadySubmitted = milkCollectionRepository.existsTodaySubmission(
                farmer.getId(),
                dto.getMilkShift(),
                dto.getMilkType(),
                today
        );

        if (alreadySubmitted) {
            throw new BadRequestException("Milk collection for this farmer is already submitted today.");
        }

        // Get rate per liter
        RateCalculationResult rateResult = milkRateHelper.getRatePerLiter(
                dto.getFatPercentage(),
                dto.getSnf(),
                dto.getMilkType()
        );

        if (!rateResult.isSuccess()) {
            throw new ResourceNotFoundException(rateResult.getError());
        }

        BigDecimal ratePerLiter = rateResult.getData().getRate();
        BigDecimal totalAmount = ratePerLiter.multiply(dto.getQuantity());

        // Create milk collection entity
        MilkCollection milkCollection = MilkCollection.builder()
                .farmer(farmer)
                .manager(manager)
                .milkType(dto.getMilkType())
                .milkShift(dto.getMilkShift())
                .quantity(dto.getQuantity())
                .fatPercentage(dto.getFatPercentage())
                .snf(dto.getSnf())
                .ratePerLiter(ratePerLiter)
                .totalAmount(totalAmount)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        milkCollection = milkCollectionRepository.save(milkCollection);

        log.info("Milk collection created: ID={}, Farmer={}, Amount={}", 
                milkCollection.getId(), farmer.getUser().getFullName(), totalAmount);

        return MilkCollectionResponseDto.builder()
                .id(milkCollection.getId())
                .message("Milk collection added successfully")
                .totalAmount(totalAmount)
                .isExactMatch(rateResult.getData().isExactMatch())
                .farmerName(farmerUser.getFullName())
                .managerName(manager.getFullName())
                .milkType(milkCollection.getMilkType())
                .milkShift(milkCollection.getMilkShift())
                .quantity(milkCollection.getQuantity())
                .fatPercentage(milkCollection.getFatPercentage())
                .snf(milkCollection.getSnf())
                .ratePerLiter(milkCollection.getRatePerLiter())
                .paymentStatus(milkCollection.getPaymentStatus())
                .createdAt(milkCollection.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MilkCollectionResponseDto> getAllMilkCollections() {
        List<MilkCollection> collections = milkCollectionRepository.findAllWithDetails();
        
        return collections.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MilkCollectionResponseDto> getMilkCollectionByManagerId(Long managerId) {

        return milkCollectionRepository.findByManagerId(managerId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MilkCollectionResponseDto> getTodaysMilkCollections() {
        LocalDate today = LocalDate.now();
        List<MilkCollection> collections = milkCollectionRepository.findTodaysCollections(today);
        
        return collections.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MilkCollectionResponseDto getMilkCollectionById(Long id) {
        MilkCollection collection = milkCollectionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milk collection not found"));
        
        return mapToResponseDto(collection);
    }

    @Override
    public MilkCollectionResponseDto updateMilkCollection(Long id, UpdateMilkCollectionDto dto) {
        MilkCollection milkCollection = milkCollectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Milk collection not found"));

        // Recalculate rate
        RateCalculationResult rateResult = milkRateHelper.getRatePerLiter(
                dto.getFatPercentage(),
                dto.getSnf(),
                milkCollection.getMilkType()
        );

        if (!rateResult.isSuccess()) {
            throw new ResourceNotFoundException(rateResult.getError());
        }

        BigDecimal ratePerLiter = rateResult.getData().getRate();
        BigDecimal totalAmount = ratePerLiter.multiply(dto.getQuantity());

        // Update fields
        milkCollection.setQuantity(dto.getQuantity());
        milkCollection.setFatPercentage(dto.getFatPercentage());
        milkCollection.setSnf(dto.getSnf());
        milkCollection.setRatePerLiter(ratePerLiter);
        milkCollection.setTotalAmount(totalAmount);

        milkCollection = milkCollectionRepository.save(milkCollection);

        log.info("Milk collection updated: ID={}, New Amount={}", id, totalAmount);

        return MilkCollectionResponseDto.builder()
                .id(milkCollection.getId())
                .message("Milk collection updated successfully")
                .totalAmount(totalAmount)
                .isExactMatch(rateResult.getData().isExactMatch())
                .build();
    }

    @Override
    public void deleteMilkCollection(Long id) {
        if (!milkCollectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Milk collection not found");
        }

        milkCollectionRepository.deleteById(id);
        log.info("Milk collection deleted: ID={}", id);
    }
    
    @Transactional(readOnly = true)
    public List<MilkCollectionResponseDto> getTodaysMilkCollectionsByManagerId(Long managerId, LocalDate today) {

    	LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return milkCollectionRepository
                .findTodaysByManagerId(managerId, startOfDay, endOfDay)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to map entity to DTO
     */
    private MilkCollectionResponseDto mapToResponseDto(MilkCollection collection) {
        return MilkCollectionResponseDto.builder()
                .id(collection.getId())
                .farmerName(collection.getFarmer().getUser().getFullName())
                .farmerEmail(collection.getFarmer().getUser().getEmail())
                .managerName(collection.getManager().getFullName())
                .managerEmail(collection.getManager().getEmail())
                .milkType(collection.getMilkType())
                .milkShift(collection.getMilkShift())
                .quantity(collection.getQuantity())
                .fatPercentage(collection.getFatPercentage())
                .snf(collection.getSnf())
                .ratePerLiter(collection.getRatePerLiter())
                .totalAmount(collection.getTotalAmount())
                .paymentStatus(collection.getPaymentStatus())
                .createdAt(collection.getCreatedAt())
                .build();
    }
}
