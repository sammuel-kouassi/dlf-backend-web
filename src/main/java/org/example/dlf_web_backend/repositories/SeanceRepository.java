package org.example.dlf_web_backend.repositories;

import jakarta.transaction.Transactional;
import org.example.dlf_web_backend.dto.MonthlyGadgetStat;
import org.example.dlf_web_backend.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {

    @Query(value = "SELECT COUNT(*) FROM seance WHERE \"estTerminee\" = false", nativeQuery = true)
    long countByEstTermineeFalse();

    // Somme des gadgets distribués
    @Query("SELECT COALESCE(SUM(s.gadgetsDistribues), 0) FROM Seance s")
    long getTotalGadgetsDistribues();

    @Modifying
    @Transactional
    @Query(value = "UPDATE seance SET \"estTerminee\" = true WHERE \"estTerminee\" = false " +
            "AND (\"datePrevue\" + \"heureFin\"::time) < :now", nativeQuery = true)
    int markTerminatedSeances(@Param("now") LocalDateTime now);


    @Query(value = "SELECT COUNT(*) FROM seance WHERE \"estTerminee\" = false " +
            "AND EXTRACT(YEAR FROM \"datePrevue\") = :year AND EXTRACT(MONTH FROM \"datePrevue\") = :month", nativeQuery = true)
    long countActiveByMonth(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT COALESCE(SUM(\"gadgetsDistribues\"), 0) FROM seance " +
            "WHERE EXTRACT(YEAR FROM \"datePrevue\") = :year AND EXTRACT(MONTH FROM \"datePrevue\") = :month", nativeQuery = true)
    long getGadgetsDistribuesByMonth(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT TO_CHAR(s.\"datePrevue\", 'YYYY-MM') AS month, " +
            "COALESCE(SUM(s.\"gadgetsDistribues\"), 0) AS count " +
            "FROM seance s GROUP BY month ORDER BY month", nativeQuery = true)
    List<MonthlyGadgetStat> getMonthlyGadgetDistribution();


    //Total gadgets distribués depuis le mobile
    @Query("SELECT COALESCE(SUM(s.gadgetsDistribues), 0) FROM Seance s")
    long totalGadgetsDistribuesMobile();

    //Gadgets mobile distribués ce mois
    @Query(value = """
        SELECT COALESCE(SUM(s."gadgetsDistribues"), 0) FROM seance s
        WHERE DATE_TRUNC('month', s."datePrevue") = DATE_TRUNC('month', CURRENT_DATE)
        """, nativeQuery = true)
    long gadgetsMobileCeMois();

    //Gadgets mobile distribués le mois dernier
    @Query(value = """
        SELECT COALESCE(SUM(s."gadgetsDistribues"), 0) FROM seance s
        WHERE DATE_TRUNC('month', s."datePrevue")
            = DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
        """, nativeQuery = true)
    long gadgetsMobileMoisDernier();
}
