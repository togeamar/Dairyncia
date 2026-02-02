package com.dairyncia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateCalculationResult {
    
    private boolean success;
    private RateData data;
    private String error;

    public static RateCalculationResult success(RateData data) {
        return new RateCalculationResult(true, data, null);
    }

    public static RateCalculationResult failure(String error) {
        return new RateCalculationResult(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }
}
