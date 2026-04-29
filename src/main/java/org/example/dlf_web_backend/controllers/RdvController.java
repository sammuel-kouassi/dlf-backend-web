package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.entities.Rdv;
import org.example.dlf_web_backend.repositories.RdvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rdvs")
@CrossOrigin(origins = "*")
public class RdvController {

    @Autowired
    private RdvRepository rdvRepository;

    @GetMapping
    public List<Rdv> getAllRdvs() {
        return rdvRepository.findAll();
    }

    @GetMapping("/{id}")
    public Rdv getRdvById(@PathVariable Long id) {
        return rdvRepository.findById(id).orElse(null);
    }

    @GetMapping("/seance/{seanceId}")
    public List<Rdv> getRdvsBySeance(@PathVariable Long seanceId) {
        return rdvRepository.findBySeanceId(seanceId);
    }

    @PostMapping
    public Rdv createRdv(@RequestBody Rdv rdv) {
        return rdvRepository.save(rdv);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rdv> updateRdv(@PathVariable Long id, @RequestBody Rdv updated) {
        return rdvRepository.findById(id).map(r -> {
            updated.setId(id);
            return ResponseEntity.ok(rdvRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRdv(@PathVariable Long id) {
        if (!rdvRepository.existsById(id)) return ResponseEntity.notFound().build();
        rdvRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}