package com.dairyncia.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class FarmerMilkDto {

    private String dairyCenterName;
    private String milkType;
    private String milkShift;

    private BigDecimal quantity;
    private BigDecimal ratePerLiter;
    private BigDecimal totalAmount;

    private String paymentStatus; // PAID / PENDING
}
