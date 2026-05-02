package org.example.dlf_web_backend.repositories;

import org.example.dlf_web_backend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /** Notifications destinées au web (non lues en premier) */
    @Query("SELECT n FROM Notification n WHERE n.cible IN ('web','tous') ORDER BY n.isRead ASC, n.createdAt DESC")
    List<Notification> findWebNotifications();

    /** Compte des non lues pour le web */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.cible IN ('web','tous') AND n.isRead = false")
    long countUnreadWeb();

    /** Marque toutes les notifs web comme lues */
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.cible IN ('web','tous') AND n.isRead = false")
    void markAllWebAsRead();
}