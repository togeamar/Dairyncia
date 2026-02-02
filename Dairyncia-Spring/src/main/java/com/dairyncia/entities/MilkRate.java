package com.dairyncia.entities;

import java.math.BigDecimal;

import com.dairyncia.enums.MilkType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "milk_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilkRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Fat percentage is required")
    @DecimalMin(value = "0.0", message = "Fat must be at least 0")
    @DecimalMax(value = "15.0", message = "Fat must not exceed 15")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fat;

    @NotNull(message = "SNF is required")
    @DecimalMin(value = "0.0", message = "SNF must be at least 0")
    @DecimalMax(value = "12.0", message = "SNF must not exceed 12")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal snf;

    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.0", message = "Rate must be at least 0")
    @DecimalMax(value = "1000.0", message = "Rate must not exceed 1000")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rate;

    @NotNull(message = "Milk type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "rate_type", nullable = false, length = 20)
    private MilkType rateType;
}