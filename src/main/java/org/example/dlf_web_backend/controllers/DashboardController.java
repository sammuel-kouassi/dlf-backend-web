package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.repositories.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ParticipantRepository participantRepo;
    private final SeanceRepository seanceRepo;
    private final PriseContactRepository priseContactRepo;
    private final GadgetRepository gadgetRepo;
    private final DistributionRepository distributionRepo;

    public DashboardController(ParticipantRepository participantRepo,
                               SeanceRepository seanceRepo,
                               PriseContactRepository priseContactRepo,
                               GadgetRepository gadgetRepo,
                               DistributionRepository distributionRepo) {
        this.participantRepo = participantRepo;
        this.seanceRepo = seanceRepo;
        this.priseContactRepo = priseContactRepo;
        this.gadgetRepo = gadgetRepo;
        this.distributionRepo = distributionRepo;
    }

    @GetMapping("/kpis")
    public Map<String, Object> getKpis() {

        long totalParticipants = participantRepo.count();
        long seancesActives    = seanceRepo.countByEstTermineeFalse();
        long prisesContact     = priseContactRepo.count();

        long totalGadgets = distributionRepo.totalQuantiteDistribuee()
                + seanceRepo.totalGadgetsDistribuesMobile()
                + gadgetRepo.totalDistribues();

        LocalDate now = LocalDate.now();
        int currentYear  = now.getYear();
        int currentMonth = now.getMonthValue();
        int previousMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int previousYear  = currentMonth == 1 ? currentYear - 1 : currentYear;

        long participantsCeMois      = participantRepo.countByMonth(currentYear, currentMonth);
        long participantsMoisDernier = participantRepo.countByMonth(previousYear, previousMonth);

        long seancesCeMois      = seanceRepo.countActiveByMonth(currentYear, currentMonth);
        long seancesMoisDernier = seanceRepo.countActiveByMonth(previousYear, previousMonth);

        long contactsCeMois      = priseContactRepo.countByMonth(currentYear, currentMonth);
        long contactsMoisDernier = priseContactRepo.countByMonth(previousYear, previousMonth);

        long gadgetsCeMois      = distributionRepo.totalQuantiteCeMois()
                + seanceRepo.gadgetsMobileCeMois();

        long gadgetsMoisDernier = distributionRepo.totalQuantiteMoisDernier()
                + seanceRepo.gadgetsMobileMoisDernier();

        Map<String, Object> result = new HashMap<>();
        result.put("totalParticipants",      totalParticipants);
        result.put("participantsCeMois",     participantsCeMois);
        result.put("participantsMoisDernier",participantsMoisDernier);
        result.put("seancesActives",         seancesActives);
        result.put("seancesCeMois",          seancesCeMois);
        result.put("seancesMoisDernier",     seancesMoisDernier);
        result.put("prisesContact",          prisesContact);
        result.put("contactsCeMois",         contactsCeMois);
        result.put("contactsMoisDernier",    contactsMoisDernier);
        result.put("totalGadgets",           totalGadgets);
        result.put("gadgetsCeMois",          gadgetsCeMois);
        result.put("gadgetsMoisDernier",     gadgetsMoisDernier);

        return result;
    }

}