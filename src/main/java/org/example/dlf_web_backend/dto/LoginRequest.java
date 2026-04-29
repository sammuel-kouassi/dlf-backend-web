package org.example.dlf_web_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Payload pour se connecter */
public class LoginRequest {

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    public String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    public String motDePasse;
}