package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "participant")
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "\"seanceId\"")
  private long seanceId;

  private String nom;
  private String prenom;
  private String telephone;
  private String profession;

  @Column(name = "\"statutLogement\"")
  private String statutLogement;

  private String lieu;
  private String localite;
  private String quartier;

  @Column(name = "\"besoinsExprimes\"")
  private String besoinsExprimes;

  private String ressenti;
  private String consentement;
  private String statut;

  @Column(name = "\"dateInscription\"")
  private java.sql.Timestamp dateInscription;
}