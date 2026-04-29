package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.dto.CibleStat;
import org.example.dlf_web_backend.dto.LocaliteStat;
import org.example.dlf_web_backend.dto.MonthlyParticipantStat;
import org.example.dlf_web_backend.entities.Participant;
import org.example.dlf_web_backend.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "*")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Participant getParticipantById(@PathVariable Long id) {
        return participantRepository.findById(id).orElse(null);
    }

    @GetMapping("/seance/{seanceId}")
    public List<Participant> getParticipantsBySeance(@PathVariable Long seanceId) {
        return participantRepository.findBySeanceId(seanceId);
    }

    @PostMapping
    public Participant createParticipant(@RequestBody Participant participant) {
        return participantRepository.save(participant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant updated) {
        return participantRepository.findById(id).map(p -> {
            updated.setId(id);
            return ResponseEntity.ok(participantRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        if (!participantRepository.existsById(id)) return ResponseEntity.notFound().build();
        participantRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/monthly-stats")
    public List<MonthlyParticipantStat> getMonthlyStats() {
        return participantRepository.getMonthlyStats();
    }

    @GetMapping("/stats-by-cible")
    public List<CibleStat> getStatsByCible() {
        return participantRepository.getStatsByCible();
    }

    @GetMapping("/stats-by-localite")
    public List<LocaliteStat> getStatsByLocalite() {
        return participantRepository.getStatsByLocalite();
    }
}