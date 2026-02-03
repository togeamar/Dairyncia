package com.dairyncia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingUserDto {
	private int SrNo;
	private Long UserId;
	private String Email;
	private String FullName;
	private String Phone;
}
