package com.dairyncia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="contact_messages")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper=true)
public class ContactMessage extends BaseEntity{
	@NotBlank(message="Name is required")
	@Size(min=3,max=100, message="Name must be between 3 to 100 characters")
	@Column(length=100,nullable=false)
	private String name;
	
	@NotBlank(message="Email is required")
	@Email(message="Invalid email address")
	@Column(length=100,nullable=false)
	private String email;
	
	@NotBlank(message="Purpose is required")
	@Size(min=5,max=250, message="Purpose must be between 5 to 250 characters")
	@Column(length=250,nullable=false)
	private String purpose;
}
