package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.entities.Distribution;
import org.example.dlf_web_backend.repositories.DistributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distributions")
@CrossOrigin(origins = "*")
public class DistributionController {

    @Autowired
    private DistributionRepository distributionRepository;

    @GetMapping
    public List<Distribution> getAllDistributions() {
        return distributionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Distribution getDistributionById(@PathVariable Long id) {
        return distributionRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Distribution createDistribution(@RequestBody Distribution distribution) {
        return distributionRepository.save(distribution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Distribution> updateDistribution(@PathVariable Long id, @RequestBody Distribution updated) {
        return distributionRepository.findById(id).map(d -> {
            updated.setId(id);
            return ResponseEntity.ok(distributionRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable Long id) {
        if (!distributionRepository.existsById(id)) return ResponseEntity.notFound().build();
        distributionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}