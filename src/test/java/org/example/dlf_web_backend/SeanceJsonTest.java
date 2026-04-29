package org.example.dlf_web_backend;

import org.example.dlf_web_backend.entities.Seance;
import org.example.dlf_web_backend.repositories.SeanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SeanceJsonTest {

    @Autowired
    private SeanceRepository seanceRepository;

    @Test
    @Transactional
    public void testSaveSeanceWithJson() {
        Seance seance = new Seance();
        seance.setNom("Test Seance");
        seance.setAssistants("[\"Assistant 1\", \"Assistant 2\"]");
        seance.setOrganisateur("Test Organisateur");
        seance.setPresentateur("Test Presentateur");
        seance.setTypeSeance("Test Type");
        seance.setCible("Test Cible");
        seance.setZone("Test Zone");
        seance.setVille("Test Ville");
        seance.setQuartier("Test Quartier");
        seance.setDatePrevue("2026-04-27T14:11:00");
        
        Seance saved = seanceRepository.save(seance);
        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        
        Seance retrieved = seanceRepository.findById(saved.getId()).orElse(null);
        assertNotNull(retrieved);
        assertEquals("[\"Assistant 1\", \"Assistant 2\"]", retrieved.getAssistants());
    }
}
