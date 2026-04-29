package org.example.dlf_web_backend.dto;

import org.example.dlf_web_backend.entities.Utilisateur;

/** Représentation publique d'un utilisateur (sans mot de passe) */
public class UserResponse {
    public Long id;
    public String nom;
    public String email;
    public String role;

    public static UserResponse from(Utilisateur u) {
        UserResponse r = new UserResponse();
        r.id    = u.getId();
        r.nom   = u.getNom();
        r.email = u.getEmail();
        r.role  = u.getRole();
        return r;
    }
}