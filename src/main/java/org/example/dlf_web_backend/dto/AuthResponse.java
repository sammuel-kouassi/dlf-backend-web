package org.example.dlf_web_backend.dto;

/** Réponse après login ou register réussi */
public class AuthResponse {
    public String token;
    public String email;
    public String nom;
    public String role;

    public AuthResponse(String token, String email, String nom, String role) {
        this.token = token;
        this.email = email;
        this.nom   = nom;
        this.role  = role;
    }
}