package com.dairyncia.controllers;

import com.dairyncia.dto.auth.LoginDto;
import com.dairyncia.dto.auth.LoginResponseDto;
import com.dairyncia.dto.auth.RegisterDto;
import com.dairyncia.entities.Role;
import com.dairyncia.entities.User;
import com.dairyncia.repository.RoleRepository;
import com.dairyncia.repository.UserRepository;
import com.dairyncia.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        System.out.println("➡️ API HIT: /api/auth/register");
        
        // Check if user exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest()
                .body(createError("Email already in use"));
        }

        // Validate password match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                .body(createError("Passwords do not match"));
        }

        // Create user
        User user = User.builder()
            .email(dto.getEmail())
            .fullName(dto.getFullName())
            .phoneNumber(dto.getPhone())
            .password(passwordEncoder.encode(dto.getPassword()))
            .emailConfirmed(false)
            .build();

        userRepository.save(user);

        return ResponseEntity.ok("Registered successfully. Wait for admin role assignment.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        System.out.println("➡️ API HIT: /api/auth/login");
        System.out.println("Email: " + dto.getEmail());

        User user = userRepository.findByEmail(dto.getEmail())
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Authenticate
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Check if user has role
        if (user.getRoles().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(createError("Role not assigned"));
        }

        // Get first role
        String roleName = user.getRoles().iterator().next()
            .getName().name().replace("ROLE_", "");

        // Generate token
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .roles(roleName)
            .build();

        String token = jwtTokenProvider.generateToken(userDetails, roleName, user.getFullName());

        return ResponseEntity.ok(new LoginResponseDto(token, roleName));
    }

    private Map<String, String> createError(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        return error;
    }
}