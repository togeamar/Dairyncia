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
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 20)
    private RoleType name;

    public enum RoleType {
        ROLE_ADMIN,
        ROLE_MANAGER,
        ROLE_FARMER
    }

    // Convenience constructors
    public Role(RoleType name) {
        this.name = name;
    }
}