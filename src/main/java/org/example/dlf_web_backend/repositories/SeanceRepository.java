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


    @Query(value = "SELECT COUNT(*) FROM seance WHERE \"estTerminee\" != 'true'", nativeQuery = true)
    long countByEstTermineeFalse();

    @Query("SELECT COALESCE(SUM(s.gadgetsDistribues), 0) FROM Seance s")
    long getTotalGadgetsDistribues();

    @Modifying
    @Transactional
    @Query(value = "UPDATE seance SET \"estTerminee\" = 'true' WHERE \"estTerminee\" != 'true' " +
            "AND (\"datePrevue\"::timestamp + \"heureFin\"::time) < :now", nativeQuery = true)
    int markTerminatedSeances(@Param("now") LocalDateTime now);

    @Query(value = "SELECT COUNT(*) FROM seance WHERE \"estTerminee\" != 'true' " +
            "AND EXTRACT(YEAR FROM \"datePrevue\"::timestamp) = :year " +
            "AND EXTRACT(MONTH FROM \"datePrevue\"::timestamp) = :month", nativeQuery = true)
    long countActiveByMonth(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT COALESCE(SUM(\"gadgetsDistribues\"), 0) FROM seance " +
            "WHERE EXTRACT(YEAR FROM \"datePrevue\"::timestamp) = :year " +
            "AND EXTRACT(MONTH FROM \"datePrevue\"::timestamp) = :month", nativeQuery = true)
    long getGadgetsDistribuesByMonth(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT TO_CHAR(s.\"datePrevue\"::timestamp, 'YYYY-MM') AS month, " +
            "COALESCE(SUM(s.\"gadgetsDistribues\"), 0) AS count " +
            "FROM seance s GROUP BY month ORDER BY month", nativeQuery = true)
    List<MonthlyGadgetStat> getMonthlyGadgetDistribution();

    @Query("SELECT COALESCE(SUM(s.gadgetsDistribues), 0) FROM Seance s")
    long totalGadgetsDistribuesMobile();

    @Query(value = """
        SELECT COALESCE(SUM(s."gadgetsDistribues"), 0) FROM seance s
        WHERE DATE_TRUNC('month', s."datePrevue"::timestamp) = DATE_TRUNC('month', CURRENT_DATE)
        """, nativeQuery = true)
    long gadgetsMobileCeMois();

    @Query(value = """
        SELECT COALESCE(SUM(s."gadgetsDistribues"), 0) FROM seance s
        WHERE DATE_TRUNC('month', s."datePrevue"::timestamp)
            = DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
        """, nativeQuery = true)
    long gadgetsMobileMoisDernier();
}