package com.dairyncia.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="dairy_centers")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true,exclude={"address","manager"})

public class DairyCenter extends BaseEntity{
	@NotBlank(message="Dairy center name is required")
	@Size(min=3,max=100,message ="Center name must be between 3 to 100 characters")
	@Column(name="center_name",length=100,nullable=false)
	private String centerName;
	
	// one dairy has one address
	@OneToOne(fetch= FetchType.LAZY, cascade= CascadeType.ALL)
	@JoinColumn(name="address_id")
	private Address address;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name= "manager_id")
	private User manager;
	
}
