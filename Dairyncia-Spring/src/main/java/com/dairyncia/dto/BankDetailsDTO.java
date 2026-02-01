package com.dairyncia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsDTO {
	private String bankName;
	private String accountHolderName;
	private String accountNumber;
	private String ifsc;
}
