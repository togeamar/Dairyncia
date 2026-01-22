package com.dairyncia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="addresses")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper=true)
public class Address extends BaseEntity{
	@NotBlank(message="Village name is required")
	@Size(max=50,message="Village name cannot exceed 50 characters")
	@Pattern(regexp= "^[a-zA-Z\\s]+$", message="Village name only contains letters and spaces")
	@Column(length=50,nullable=false)
	private String village;
	
	@NotBlank(message="City name is required")
	@Size(max=50,message="City name cannot exceed 50 characters")
	@Pattern(regexp= "^[a-zA-Z\\s]+$", message="City name only contains letters and spaces")
	@Column(length=50,nullable=false)
	private String city;
	
	@NotBlank(message="State name is required")
	@Size(max=50,message="State name cannot exceed 50 characters")
	@Pattern(regexp= "^[a-zA-Z\\s]+$", message="State name only contains letters and spaces")
	@Column(length=50,nullable=false)
	private String state;
	
	@NotBlank(message="Pincode is required")
	@Pattern(regexp= "^[1-9][0-9]{5}$", message="Pincode must be a valid 6-digit Indian pincode")
	@Column(length=6,nullable=false)
	private String pincode;
}
