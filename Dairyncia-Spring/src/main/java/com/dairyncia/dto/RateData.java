package com.dairyncia.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateData {
    
    private BigDecimal rate;
    private BigDecimal fat;
    private BigDecimal snf;
    private boolean isExactMatch;
    
    // For nearest match responses
    private BigDecimal requestedFat;
    private BigDecimal requestedSnf;
    private String message;
}
