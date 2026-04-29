package org.example.dlf_web_backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilitaire JWT : génération et validation des tokens.
 * Dans application.properties, ajoutez :
 *   app.jwt.secret=VotreSecretDe256BitsMinimumIciChangezMoi1234567890ABCDEF
 *   app.jwt.expiration-ms=86400000   # 24h
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** Génère un token JWT pour un utilisateur. */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    /** Extrait l'email (subject) du token. */
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /** Extrait le rôle du token. */
    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /** Valide le token (signature + expiration). */
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}