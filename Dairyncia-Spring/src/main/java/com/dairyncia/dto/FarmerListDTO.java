package com.dairyncia.dto;

import java.time.LocalDateTime;

import com.dairyncia.entities.Farmer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmerListDTO {

    private Long farmerId;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    private String managerName;

    public FarmerListDTO(Farmer farmer) {
        this.farmerId = farmer.getId();
        this.createdAt = farmer.getCreatedAt();

        if (farmer.getUser() != null) {
            this.email = farmer.getUser().getEmail();
            this.fullName = farmer.getUser().getFullName();
        }
        
     // Manager info
        if (farmer.getManager() != null) {
            this.managerName = farmer.getManager().getFullName();
        }
    }
    
    public FarmerListDTO(
            Long farmerID,
            String email,
            String fullName,
            LocalDateTime createdAt,
            String managerName
    ) {
        this.farmerId = farmerID;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.managerName = managerName;
    }

}
