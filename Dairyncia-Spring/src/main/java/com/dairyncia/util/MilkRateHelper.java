package com.dairyncia.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.dairyncia.dto.RateCalculationResult;
import com.dairyncia.dto.RateData;
import com.dairyncia.entities.MilkRate;
import com.dairyncia.enums.MilkType;
import com.dairyncia.service.MilkRateService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MilkRateHelper {

    private final MilkRateService milkRateService;

    /**
     * Get rate per liter for given fat, snf, and milk type
     * Returns exact match if available, otherwise nearest match
     */
    public RateCalculationResult getRatePerLiter(BigDecimal fat, BigDecimal snf, MilkType milkType) {
        
        // Try to find exact rate
        MilkRate exactRate = milkRateService.findExactRate(fat, snf, milkType);
        
        if (exactRate != null) {
            return RateCalculationResult.success(
                RateData.builder()
                    .rate(exactRate.getRate())
                    .fat(exactRate.getFat())
                    .snf(exactRate.getSnf())
                    .isExactMatch(true)
                    .build()
            );
        }

        // Find nearest rate
        MilkRate nearestRate = milkRateService.findNearestRate(fat, snf, milkType);
        
        if (nearestRate == null) {
            return RateCalculationResult.failure(
                String.format("No rate found for Fat: %s, SNF: %s, Type: %s", fat, snf, milkType)
            );
        }

        return RateCalculationResult.success(
            RateData.builder()
                .rate(nearestRate.getRate())
                .fat(nearestRate.getFat())
                .snf(nearestRate.getSnf())
                .isExactMatch(false)
                .requestedFat(fat)
                .requestedSnf(snf)
                .message("Exact rate not found, using nearest match")
                .build()
        );
    }
}
