package com.dairyncia.dto.admin;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.dairyncia.entities.Farmer;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmerListDTO {
	private int farmerID;
	private String email;
	private String fullName;
    private LocalDateTime createdAt;
    
    public FarmerListDTO(Farmer farmer) {
    	this.farmerID=farmer.getId();
    	this.createdAt=farmer.getCreatedAt();
    	if(farmer.getUser()!=null) {
    		this.email=farmer.getUser().getEmail();
    		this.fullName=farmer.getUser().getFullName();
    	}
    }
}
