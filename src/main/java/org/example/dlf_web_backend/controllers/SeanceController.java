package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.entities.Seance;
import org.example.dlf_web_backend.repositories.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seances")
@CrossOrigin(origins = "*")
public class SeanceController {

    @Autowired
    private SeanceRepository seanceRepository;

    @GetMapping
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Seance getSeanceById(@PathVariable Long id) {
        return seanceRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Seance> createSeance(@RequestBody Seance seance) {
        Seance saved = seanceRepository.save(seance);
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
}