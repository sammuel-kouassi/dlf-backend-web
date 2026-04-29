package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.dlf_web_backend.converters.IsoStringToLocalDateTimeConverter;
import org.example.dlf_web_backend.converters.StringToBooleanConverter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name = "seance")
public class Seance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;
  private String motifs;

  @Column(name = "\"typeSeance\"")
  private String typeSeance;

  private String cible;
  private String zone;
  private String ville;
  private String quartier;

  @Column(name = "\"objectifParticipants\"")
  private Long objectifParticipants;

  private String organisateur;
  private String presentateur;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "assistants", columnDefinition = "json")
  private String assistants;

  @Convert(converter = IsoStringToLocalDateTimeConverter.class)
  @Column(name = "\"datePrevue\"")
  private String datePrevue;

  @Column(name = "\"heureDebut\"")
  private String heureDebut;

  @Column(name = "\"heureFin\"")
  private String heureFin;


  @Convert(converter = StringToBooleanConverter.class)
  @Column(name = "\"estTerminee\"")
  private String estTerminee;

  @Column(name = "\"gadgetsPrevus\"")
  private Long gadgetsPrevus;

  @Column(name = "\"gadgetsDistribues\"")
  private Long gadgetsDistribues;

  @Column(name = "\"totalLogistique\"")
  private Double totalLogistique;

  @Column(name = "\"nbParticipantsEstime\"")
  private Long nbParticipantsEstime;

  @Convert(converter = StringToBooleanConverter.class)
  private String evaluation;
}