package com.dairyncia.entities;

import java.math.BigDecimal;

import com.dairyncia.enums.MilkType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="milk_rates")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper=true)
public class MilkRate extends BaseEntity{
	@NotNull(message="Milk Type is required")
	@Enumerated(EnumType.STRING)
	@Column(name="milk_type",nullable=false)
	private MilkType milkType;
	
	@NotNull(message="Rate per liter is required")
	@DecimalMin(value="1.0",message="Rate must b greater than 0")
	@Column(name="rate_per_liter",precision=10,scale=2,nullable=false)
	private BigDecimal ratePerLiter;
}
