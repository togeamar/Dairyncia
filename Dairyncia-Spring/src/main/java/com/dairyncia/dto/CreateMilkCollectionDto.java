package com.dairyncia.dto;

import java.math.BigDecimal;

import com.dairyncia.enums.MilkShift;
import com.dairyncia.enums.MilkType;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMilkCollectionDto {

    @NotBlank(message = "Farmer email is required")
    @Email(message = "Invalid email format")
    private String farmerEmail;

    @NotNull(message = "Milk type is required")
    private MilkType milkType;

    @NotNull(message = "Milk shift is required")
    private MilkShift milkShift;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.1", message = "Quantity must be between 0.1 and 1000 liters")
    @DecimalMax(value = "1000.0", message = "Quantity must be between 0.1 and 1000 liters")
    private BigDecimal quantity;

    @NotNull(message = "Fat percentage is required")
    @DecimalMin(value = "0.0", message = "Fat percentage must be between 0 and 10")
    @DecimalMax(value = "10.0", message = "Fat percentage must be between 0 and 10")
    private BigDecimal fatPercentage;

    @NotNull(message = "SNF is required")
    @DecimalMin(value = "0.0", message = "SNF must be between 0 and 10")
    @DecimalMax(value = "10.0", message = "SNF must be between 0 and 10")
    private BigDecimal snf;
}
