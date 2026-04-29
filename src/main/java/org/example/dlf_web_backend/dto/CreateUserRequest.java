package org.example.dlf_web_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload pour qu'un admin web crée un compte mobile.
 * Rôles autorisés : "administrateur" ou "utilisateur"
 */
public class CreateUserRequest {

    @NotBlank(message = "Le nom est obligatoire")
    public String nom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    public String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caractères")
    public String motDePasse;

    @NotBlank(message = "Le rôle est obligatoire")
    @Pattern(
            regexp = "administrateur|utilisateur|superviseur",
            message = "Rôle invalide. Valeurs acceptées : administrateur, utilisateur, superviseur"
    )
    public String role;
}