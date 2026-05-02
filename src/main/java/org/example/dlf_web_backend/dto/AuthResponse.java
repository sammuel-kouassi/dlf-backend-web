package org.example.dlf_web_backend.dto;

public class AuthResponse {
    public String token;
    public Long id;          // nouveau champ
    public String email;
    public String nom;
    public String role;

    public AuthResponse(String token, Long id, String email, String nom, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.role = role;
    }
}