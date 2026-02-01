package com.dairyncia.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerResponseDTO {
	private Long id;
	private String fullName;
	private String email;
	private LocalDateTime createdAt;
}
