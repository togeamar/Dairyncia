package com.dairyncia.config;

import com.dairyncia.entities.Role;
import com.dairyncia.entities.User;
import com.dairyncia.repository.RoleRepository;
import com.dairyncia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        for (Role.RoleType roleType : Role.RoleType.values()) {
            if (!roleRepository.existsByName(roleType)) {
                Role role = new Role(roleType);
                roleRepository.save(role);
                log.info("✅ Created role: {}", roleType);
            }
        }
    }

    private void seedAdminUser() {
        String adminEmail = "admin@dairyncia.com";
        
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.info("ℹ️ Admin user already exists");
            return;
        }

        User admin = User.builder()
            .email(adminEmail)
            .fullName("System Admin")
            .password(passwordEncoder.encode("Admin@123"))
            .emailConfirmed(true)
            .build();

        Role adminRole = roleRepository.findByName(Role.RoleType.ROLE_ADMIN)
            .orElseThrow(() -> new RuntimeException("Admin role not found"));

        admin.addRole(adminRole);
        userRepository.save(admin);
        
        log.info("✅ Admin user created: {}", adminEmail);
    }
}