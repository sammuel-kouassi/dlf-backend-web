package org.example.dlf_web_backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Table partagée avec Serverpod.
 * Créée via notification.yaml côté Serverpod.
 *
 * source : 'web' ou 'mobile'
 * cible  : 'web', 'mobile' ou 'tous'
 * type   : 'rdv', 'seance', 'sync'
 */
@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String corps;
    private String type;
    private String source;
    private String cible;

    @Column(name = "\"entityId\"")
    private Long entityId;

    @Column(name = "\"isRead\"")
    private boolean isRead = false;

    @Column(name = "\"createdAt\"")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}