package com.dairyncia.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class FarmerDashboardDto {

    // Basic info
    private Long farmerId;
    private String fullName;
    private String email;
    private String phone;

    // Bank details
    private String bankName;
    private String accountNumber;
    private String ifsc;
    private String accountHolderName;

    // Milk data
    private List<FarmerMilkDto> milkCollections;

    // Summary
    private BigDecimal totalPaidAmount;
    private BigDecimal totalPendingAmount;
}
