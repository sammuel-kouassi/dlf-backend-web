package org.example.dlf_web_backend.scheduler;

import org.example.dlf_web_backend.repositories.SeanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class SeanceScheduler {

    private final SeanceRepository seanceRepository;

    public SeanceScheduler(SeanceRepository seanceRepository) {
        this.seanceRepository = seanceRepository;
    }

    @Scheduled(fixedRate = 60_000)
    public void updateTerminatedSeances() {
        int updated = seanceRepository.markTerminatedSeances(LocalDateTime.now());
        if (updated > 0) {
            System.out.println("✅ " + updated + " séance(s) marquée(s) comme terminée(s).");
        }
    }
}