package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "rdv")
public class Rdv {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "\"seanceId\"")
  private long seanceId;

  private String titre;
  private String contact;

  @Column(name = "\"dateRdv\"")
  private java.sql.Timestamp dateRdv;

  private String heure;
  private String lieu;
  private String statut;
}