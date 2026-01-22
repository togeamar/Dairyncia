package com.dairyncia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="bank_details")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper=true, exclude={"user"})
public class BankDetails extends BaseEntity{
	@NotBlank(message="Bank name is required")
	@Size(min=3,max=100, message = "Bank name is between 3 to 100 characters")
	@Column(name="bank_name",length=100,nullable=false)
	private String bankName;
	
	@NotBlank(message="Account number is required")
	@Size(max=20, message = "Account number can not exceed 20 characters")
	@Column(name="account_number",length=20,nullable=false)
	private String accountNumber;
	
	@NotBlank(message="Account holder name is required")
	@Size(max=100, message = "Account holder name can not exceed 100 characters")
	@Column(name="account_holder_name",length=100,nullable=false)
	private String accountHolderName;
	
	@NotBlank(message="IFSC code is required")
    @Pattern(regexp="^[A-Z]{4}0[A-Z0-9]{6}$", message="Invalid IFSC code")
	@Column(length=11,nullable=false)
	private String ifsc;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private User user;
}
