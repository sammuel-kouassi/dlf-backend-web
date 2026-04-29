package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "image")
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "\"seanceId\"")
  private long seanceId;

  private String url;
  private String legende;
  private java.sql.Timestamp date;
}