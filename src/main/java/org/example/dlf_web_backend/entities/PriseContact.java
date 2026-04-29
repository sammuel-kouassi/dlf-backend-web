package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "prise_contact")
public class PriseContact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "\"seanceId\"")
  private long seanceId;

  @Column(name = "\"nomContact\"")
  private String nomContact;

  private String telephone;
  private java.sql.Timestamp date;

  @Column(name = "\"objetMission\"")
  private String objetMission;

  @Column(name = "\"directionRegionale\"")
  private String directionRegionale;

  private String agence;
  private String quartier;
  private String site;

  @Column(name = "\"pointsAbordes\"")
  private String pointsAbordes;

  private String observations;

  @Column(name = "\"signatureBase64\"")
  private String signatureBase64;
}