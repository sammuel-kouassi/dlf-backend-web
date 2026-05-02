package org.example.dlf_web_backend.services;

import org.example.dlf_web_backend.entities.Notification;
import org.example.dlf_web_backend.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

/**
 * Service interne pour créer des notifications depuis n'importe quel controller.
 * Injecté dans SeanceController et RdvController.
 */
@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    /**
     * Crée une notification destinée au mobile.
     * Appelée quand le web crée une séance ou un RDV.
     */
    public void notifyMobile(String titre, String corps, String type, Long entityId) {
        Notification n = new Notification();
        n.setTitre(titre);
        n.setCorps(corps);
        n.setType(type);
        n.setSource("web");
        n.setCible("mobile");
        n.setEntityId(entityId);
        n.setRead(false);
        repo.save(n);
    }

    /**
     * Crée une notification destinée au web.
     * Peut être appelée manuellement si besoin.
     */
    public void notifyWeb(String titre, String corps, String type, Long entityId) {
        Notification n = new Notification();
        n.setTitre(titre);
        n.setCorps(corps);
        n.setType(type);
        n.setSource("web");
        n.setCible("web");
        n.setEntityId(entityId);
        n.setRead(false);
        repo.save(n);
    }
}