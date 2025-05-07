package org.tfg.timetrackapi.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final Key secretKey;

    public JwtService() {
        this.secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    public String generateToken(String dni) {
        // ... (tu código de generación de token como antes)
        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("sub", dni);
        claims.put("iat", new Date());
        long expirationTimeMs = 3600000; // 1 hora
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTimeMs);
        claims.put("exp", expiryDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public String extractDni(String token) {
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
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Usa la misma clave para parsear
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String dni) {
        final String extractedDni = extractDni(token);
        return (extractedDni.equals(dni) && !isTokenExpired(token));
    }
}

