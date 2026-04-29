package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité partagée avec le backend mobile Serverpod.
 * Table : utilisateur (existante en base)
 *
 * Rôles possibles :
 *   - "administrateur" → web + mobile
 *   - "superviseur"    → web uniquement
 *   - "utilisateur"    → mobile uniquement
 */
@Data
@Entity
@Table(name = "utilisateur")
public class Utilisateur {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  @Column(unique = true, nullable = false)
  private String email;

  /**
   * Mot de passe hashé BCrypt.
   * IMPORTANT : la colonne en base s'appelle "motDePasse" (avec guillemets PostgreSQL).
   */
  @Column(name = "\"motDePasse\"", nullable = false)
  private String motDePasse;

  /**
   * Rôle de l'utilisateur.
   * Valeurs : "administrateur", "superviseur", "utilisateur"
   */
  private String role;
}