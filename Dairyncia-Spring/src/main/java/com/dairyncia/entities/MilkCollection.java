package com.dairyncia.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dairyncia.enums.MilkShift;
import com.dairyncia.enums.MilkType;
import com.dairyncia.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "milk_collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilkCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Farmer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @NotNull(message = "Manager is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @NotNull(message = "Milk type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "milk_type", nullable = false, length = 20)
    private MilkType milkType;

    @NotNull(message = "Milk shift is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "milk_shift", nullable = false, length = 20)
    private MilkShift milkShift;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.1", message = "Quantity must be at least 0.1 liters")
    @DecimalMax(value = "1000.0", message = "Quantity must not exceed 1000 liters")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @NotNull(message = "Fat percentage is required")
    @DecimalMin(value = "0.0", message = "Fat percentage must be at least 0")
    @DecimalMax(value = "20.0", message = "Fat percentage must not exceed 20")
    @Column(name = "fat_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal fatPercentage;

    @NotNull(message = "SNF is required")
    @DecimalMin(value = "0.0", message = "SNF must be at least 0")
    @DecimalMax(value = "20.0", message = "SNF must not exceed 20")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal snf;

    @NotNull(message = "Rate per liter is required")
    @DecimalMin(value = "0.0", message = "Rate per liter must be at least 0")
    @DecimalMax(value = "1000.0", message = "Rate per liter must not exceed 1000")
    @Column(name = "rate_per_liter", nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerLiter;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be at least 0")
    @DecimalMax(value = "100000.0", message = "Total amount must not exceed 100000")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Payment status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
