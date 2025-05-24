package org.tfg.timetrackapi.security.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final Key secretKey;

    public JwtService() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // Generar Token recibe dni y rol del empleado
    public String generateToken(String dni, String role) {
        // Se crea un mapa con los campos recibidos y la fecha actual
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", dni);
        claims.put("role", role);
        claims.put("iat", new Date());

        // Se define el tiempo de expiración del token, en este caso 1h desde que se creo
        long expirationTimeMs = 3600000; // 1 hora
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTimeMs);
        claims.put("exp", expiryDate);

        // Se retorna el token con todos los datos
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public String extractDni(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
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
                .setSigningKey(secretKey)
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
