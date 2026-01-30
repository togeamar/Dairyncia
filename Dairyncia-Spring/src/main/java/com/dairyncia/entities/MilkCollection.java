package com.dairyncia.entities;

import java.math.BigDecimal;

import com.dairyncia.enums.MilkShift;
import com.dairyncia.enums.MilkType;
import com.dairyncia.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "milk_collections")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class MilkCollection extends BaseEntity {

    // Many milk collections belong to one Farmer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    // Milk type (COW / BUFFALO)
    @Enumerated(EnumType.STRING)
    @Column(name = "milk_type", nullable = false)
    private MilkType milkType;

    // Milk shift (MORNING / EVENING)
    @Enumerated(EnumType.STRING)
    @Column(name = "milk_shift", nullable = false)
    private MilkShift milkShift;

    // Quantity in liters
    @NotNull
    @DecimalMin(value = "0.1", message = "Quantity must be greater than zero")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "fat_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal fatPercentage;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal snf;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
}
