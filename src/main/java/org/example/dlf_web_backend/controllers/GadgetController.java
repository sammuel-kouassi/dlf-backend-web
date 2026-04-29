package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.dto.CategorieGadgetStat;
import org.example.dlf_web_backend.dto.MonthlyGadgetStat;
import org.example.dlf_web_backend.entities.Distribution;
import org.example.dlf_web_backend.entities.Gadget;
import org.example.dlf_web_backend.repositories.DistributionRepository;
import org.example.dlf_web_backend.repositories.GadgetRepository;
import org.example.dlf_web_backend.repositories.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/gadgets")
@CrossOrigin(origins = "*")
public class GadgetController {

    @Autowired
    private GadgetRepository gadgetRepo;

    @Autowired
    private DistributionRepository distributionRepo;

    @Autowired
    private SeanceRepository seanceRepo;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Gadget> gadgets = gadgetRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Gadget g : gadgets) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",           g.getId());
            map.put("nom",          g.getNom());
            map.put("categorie",    g.getCategorie());
            map.put("stockInitial", g.getStockInitial());
            long distribues = g.getDistribues()
                    + distributionRepo.totalDistribueByGadgetId(g.getId());
            map.put("distribues", distribues);
            result.add(map);
        }
        return result;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalDist = distributionRepo.totalQuantiteDistribuee()
                + seanceRepo.totalGadgetsDistribuesMobile()
                + gadgetRepo.totalDistribues();

        stats.put("totalDistribuesGlobal", totalDist);
        stats.put("totalStockInitial",     gadgetRepo.totalStockInitial());
        stats.put("nbArticles",            gadgetRepo.count());
        stats.put("nbCategories",          gadgetRepo.countDistinctCategories());
        return stats;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gadget> getById(@PathVariable Long id) {
        return gadgetRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Gadget create(@RequestBody Gadget gadget) {
        Gadget saved = gadgetRepo.save(gadget);


        if (saved.getDistribues() > 0) {
            Distribution d = new Distribution();
            d.setGadgetId(saved.getId());
            d.setQuantite(saved.getDistribues());
            d.setDateDistribution(Timestamp.from(Instant.now()));
            d.setSeanceId(0L);
            d.setParticipantId(0L);
            d.setAgentId(0L);
            distributionRepo.save(d);
        }

        return saved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gadget> update(@PathVariable Long id,
                                         @RequestBody Gadget updated) {
        return gadgetRepo.findById(id).map(existing -> {
            long ancienDistribues = existing.getDistribues();
            updated.setId(id);
            Gadget saved = gadgetRepo.save(updated);

            long delta = saved.getDistribues() - ancienDistribues;
            if (delta > 0) {
                Distribution d = new Distribution();
                d.setGadgetId(saved.getId());
                d.setQuantite(delta);
                d.setDateDistribution(Timestamp.from(Instant.now()));
                d.setSeanceId(0L);
                d.setParticipantId(0L);
                d.setAgentId(0L);
                distributionRepo.save(d);
            }

            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!gadgetRepo.existsById(id)) return ResponseEntity.notFound().build();
        gadgetRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/distribution-mensuelle")
    public List<MonthlyGadgetStat> distributionMensuelle() {
        return distributionRepo.getDistributionMensuelle();
    }

    @GetMapping("/stats-par-categorie")
    public List<CategorieGadgetStat> statsParCategorie() {
        return distributionRepo.statsParCategorie();
    }

}