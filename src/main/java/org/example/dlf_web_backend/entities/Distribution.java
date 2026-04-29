package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "distribution")
public class Distribution {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "\"seanceId\"")
  private long seanceId;

  @Column(name = "\"participantId\"")
  private long participantId;

  @Column(name = "\"gadgetId\"")
  private long gadgetId;

  private long quantite;

  @Column(name = "\"dateDistribution\"")
  private java.sql.Timestamp dateDistribution;

  @Column(name = "\"agentId\"")
  private long agentId;
}