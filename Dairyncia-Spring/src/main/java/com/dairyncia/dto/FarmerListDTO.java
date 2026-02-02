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

    public FarmerListDTO(Farmer farmer) {
        this.farmerId = farmer.getId();
        this.createdAt = farmer.getCreatedAt();

        if (farmer.getUser() != null) {
            this.email = farmer.getUser().getEmail();
            this.fullName = farmer.getUser().getFullName();
        }
    }
}
