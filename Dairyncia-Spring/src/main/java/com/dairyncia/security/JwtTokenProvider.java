package com.dairyncia.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;
    
    private SecretKey Key;

    @PostConstruct
    private void init() {
    	if(secret==null || secret.length()<32) {
    		throw new IllegalStateException("key too short");
    	}
    	this.Key=Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate token matching .NET format
    public String generateToken(CustomUserDetails userDetails, String role, String fullName) {
        Map<String, Object> claims = new HashMap<>();
        
        // Match .NET ClaimTypes format
        claims.put("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress", 
                   userDetails.getUser().getEmail());
        claims.put("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name", fullName);
        claims.put("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role", role);
        claims.put("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier", userDetails.getUser().getId());
        
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(Key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}