package com.dairyncia.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ApiResponse {
	private String message;
	private String status;
	
	private LocalDateTime timestamp;
	
	public ApiResponse(String message,String status) {
		this.message=message;
		this.status=status;
		this.timestamp=LocalDateTime.now();
	}
}
