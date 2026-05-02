package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.entities.Notification;
import org.example.dlf_web_backend.repositories.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints de notifications pour le frontend web.
 *
 * GET  /api/notifications          → liste des notifs web
 * GET  /api/notifications/unread-count → nombre non lues
 * PUT  /api/notifications/{id}/read    → marquer une notif lue
 * PUT  /api/notifications/read-all     → tout marquer comme lu
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    /** Liste toutes les notifications destinées au web */
    @GetMapping
    public List<Notification> getWebNotifications() {
        return repo.findWebNotifications();
    }

    /** Nombre de notifications non lues (pour le badge de la cloche) */
    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount() {
        return Map.of("count", repo.countUnreadWeb());
    }

    /** Marque une notification spécifique comme lue */
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return repo.findById(id).map(n -> {
            n.setRead(true);
            return ResponseEntity.ok(repo.save(n));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Marque toutes les notifications web comme lues */
    @PutMapping("/read-all")
    public ResponseEntity<Map<String, Object>> markAllAsRead() {
        repo.markAllWebAsRead();
        return ResponseEntity.ok(Map.of("success", true, "message", "Toutes les notifications ont été lues"));
    }
}