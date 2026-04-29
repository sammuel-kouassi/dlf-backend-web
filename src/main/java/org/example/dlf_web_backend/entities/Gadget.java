package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "gadget")
public class Gadget {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String nom;
  private String categorie;

  @Column(name = "\"stockInitial\"")
  private long stockInitial;

  private long distribues;
}