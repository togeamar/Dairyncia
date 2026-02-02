package com.dairyncia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dairyncia.enums.MilkShift;
import com.dairyncia.enums.MilkType;
import com.dairyncia.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilkCollectionResponseDto {

    private Long id;
    private String message;
    private String farmerName;
    private String farmerEmail;
    private String managerName;
    private String managerEmail;
    private MilkType milkType;
    private MilkShift milkShift;
    private BigDecimal quantity;
    private BigDecimal fatPercentage;
    private BigDecimal snf;
    private BigDecimal ratePerLiter;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private Boolean isExactMatch; // For create/update responses
}
