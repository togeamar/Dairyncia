package com.dairyncia.controllers;

import com.dairyncia.dto.ApiResponse;
import com.dairyncia.dto.LoginDto;
import com.dairyncia.dto.LoginResponseDto;
import com.dairyncia.dto.RegisterDto;
import com.dairyncia.entities.Role;
import com.dairyncia.entities.User;
import com.dairyncia.repository.RoleRepository;
import com.dairyncia.repository.UserRepository;
import com.dairyncia.security.CustomUserDetails;
import com.dairyncia.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        System.out.println("➡️ API HIT: /api/auth/register");
        
        // Check if user exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Email already in use","422"));
        }

        // Validate password match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse("Confirm Passwords do not match","409"));
        }

        // Create user
        User user = modelMapper.map(dto, User.class);

        String encoddedPassword=passwordEncoder.encode(user.getPassword());
        
        user.setPassword(encoddedPassword);

        userRepository.save(user);

        return ResponseEntity.ok("Registered successfully. Wait for admin role assignment.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        System.out.println("➡️ API HIT: /api/auth/login");
        System.out.println("Email: " + dto.getEmail());
        
        
        // Authenticate
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            
            
            
            CustomUserDetails user=(CustomUserDetails) authentication.getPrincipal();
            
            if(user.getUser().getRoles()==null) {
            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Role Not Assigned","401"));
            }
            
            String roleName=user.getUser().getRoles().iterator().next().getName().name().replace("ROLE_", "");
            
            String token = jwtTokenProvider.generateToken(user, roleName, user.getUsername());
            
            return ResponseEntity.ok(new LoginResponseDto(token, roleName));
            
        }
        catch (BadCredentialsException e) {
            // Specific error for wrong password/email
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            
        } catch (DisabledException e) {
            // Specific error if account is disabled
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is disabled");
            
        }
        catch(AuthenticationException e){
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        catch (Exception e) {
            // Catch-all for other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
        

        
 

        

        
    }
}