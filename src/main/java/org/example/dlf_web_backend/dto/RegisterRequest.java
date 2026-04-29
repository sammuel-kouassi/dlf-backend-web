package org.example.dlf_web_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ─────────────────────────────────────────────
// Requêtes
// ─────────────────────────────────────────────

/** Payload pour s'enregistrer (premier admin web) */
public class RegisterRequest {

    @NotBlank(message = "Le nom est obligatoire")
    public String nom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    public String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caractères")
    public String motDePasse;

    /**
     * Rôle : "administrateur" ou "superviseur" (web).
     * Si absent, "administrateur" par défaut.
     */
    public String role = "administrateur";
}