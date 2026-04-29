package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.entities.PriseContact;
import org.example.dlf_web_backend.repositories.PriseContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prises-contact")
@CrossOrigin(origins = "*")
public class PriseContactController {

    @Autowired
    private PriseContactRepository priseContactRepository;

    @GetMapping
    public List<PriseContact> getAllPrisesContact() {
        return priseContactRepository.findAll();
    }

    @GetMapping("/{id}")
    public PriseContact getPriseContactById(@PathVariable Long id) {
        return priseContactRepository.findById(id).orElse(null);
    }

    @GetMapping("/seance/{seanceId}")
    public List<PriseContact> getPrisesContactBySeance(@PathVariable Long seanceId) {
        return priseContactRepository.findBySeanceId(seanceId);
    }

    @PostMapping
    public PriseContact createPriseContact(@RequestBody PriseContact priseContact) {
        return priseContactRepository.save(priseContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriseContact> updatePriseContact(@PathVariable Long id, @RequestBody PriseContact updated) {
        return priseContactRepository.findById(id).map(p -> {
            updated.setId(id);
            return ResponseEntity.ok(priseContactRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriseContact(@PathVariable Long id) {
        if (!priseContactRepository.existsById(id)) return ResponseEntity.notFound().build();
        priseContactRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}