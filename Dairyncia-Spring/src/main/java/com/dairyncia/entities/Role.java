package com.dairyncia.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity{

    

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 20)
    private RoleType name;

    public enum RoleType {
        ROLE_ADMIN,
        ROLE_MANAGER,
        ROLE_FARMER
    }

    
}