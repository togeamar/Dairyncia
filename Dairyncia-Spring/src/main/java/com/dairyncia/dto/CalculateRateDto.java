package com.dairyncia.dto;

import java.math.BigDecimal;

import com.dairyncia.enums.MilkType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculateRateDto {
    
    // Response fields
    private Boolean isExactMatch;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal rate;
    private MilkType rateType;
    
    // Additional fields for nearest match response
    private String message;
    private BigDecimal requestedFat;
    private BigDecimal requestedSnf;
    private BigDecimal actualFat;
    private BigDecimal actualSnf;
}