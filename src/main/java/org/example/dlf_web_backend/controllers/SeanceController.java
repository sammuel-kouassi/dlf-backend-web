package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.services.NotificationService;
import org.example.dlf_web_backend.entities.Seance;
import org.example.dlf_web_backend.repositories.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seances")
@CrossOrigin(origins = "*")
public class SeanceController {

    @Autowired
    private SeanceRepository seanceRepository;

    private final NotificationService notificationService;

    public SeanceController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Seance getSeanceById(@PathVariable Long id) {
        return seanceRepository.findById(id).orElse(null);
    }

    /** Crée une séance ET notifie le mobile */
    @PostMapping
    public ResponseEntity<Seance> createSeance(@RequestBody Seance seance) {
        Seance saved = seanceRepository.save(seance);

        try {

            String date = saved.getDatePrevue() != null
                    ? saved.getDatePrevue().substring(0, Math.min(10, saved.getDatePrevue().length()))
                    : "";
            notificationService.notifyMobile(
                    "Nouvelle séance — " + saved.getNom(),
                    "Prévue le " + date
                            + (saved.getZone() != null ? " · " + saved.getZone() : ""),
                    "seance",
                    saved.getId()
            );
        } catch (Exception e) {
            System.out.println("⚠️ Notif séance échouée : " + e.getMessage());
        }

        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seance> updateSeance(@PathVariable Long id, @RequestBody Seance updated) {
        return seanceRepository.findById(id).map(s -> {
            updated.setId(id);
            return ResponseEntity.ok(seanceRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        if (!seanceRepository.existsById(id)) return ResponseEntity.notFound().build();
        seanceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/clore")
    public ResponseEntity<?> cloreSeance(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body
    ) {
        return seanceRepository.findById(id).map(seance -> {

            if ("1".equals(seance.getEstTerminee()) || "true".equalsIgnoreCase(seance.getEstTerminee())) {
                Map<String, String> err = new HashMap<>();
                err.put("message", "Cette séance est déjà terminée.");
                return ResponseEntity.badRequest().body(err);
            }

            Object val = body.get("nbParticipantsEstime");
            if (val instanceof Number) {
                seance.setNbParticipantsEstime(((Number) val).longValue());
            }

            seance.setEstTerminee("true");
            seance.setEvaluation("true");

            Seance saved = seanceRepository.save(seance);

            try {
                notificationService.notifyMobile(
                        "Séance clôturée — " + saved.getNom(),
                        "La séance a été marquée comme terminée depuis la plateforme web.",
                        "seance",
                        saved.getId()
                );
            } catch (Exception e) {
                System.out.println("⚠️ Notif clôture échouée : " + e.getMessage());
            }

            return ResponseEntity.ok((Object) saved);

        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/clore-toutes")
    public ResponseEntity<Map<String, Object>> cloreToutesSeances() {

        List<Seance> actives = seanceRepository.findAll()
                .stream()
                .filter(s -> !"true".equalsIgnoreCase(s.getEstTerminee())
                        && !"1".equals(s.getEstTerminee()))
                .toList();

        for (Seance s : actives) {
            s.setEstTerminee("true");
            s.setEvaluation("true");
        }
        seanceRepository.saveAll(actives);

        if (!actives.isEmpty()) {
            try {
                notificationService.notifyMobile(
                        actives.size() + " séance(s) clôturée(s)",
                        "Toutes les séances actives ont été clôturées depuis la plateforme web.",
                        "seance",
                        null
                );
            } catch (Exception e) {
                System.out.println("⚠️ Notif clore-toutes échouée : " + e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", actives.size() + " séance(s) clôturée(s).");
        result.put("count", actives.size());
        return ResponseEntity.ok(result);
    }
}