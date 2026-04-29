package org.example.dlf_web_backend.repositories;

import org.example.dlf_web_backend.dto.CibleStat;
import org.example.dlf_web_backend.dto.LocaliteStat;
import org.example.dlf_web_backend.dto.MonthlyParticipantStat;
import org.example.dlf_web_backend.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query(value = "SELECT TO_CHAR(p.\"dateInscription\", 'YYYY-MM') AS month, COUNT(*) AS count " +
            "FROM participant p " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<MonthlyParticipantStat> getMonthlyStats();

    // Participants groupés par cible
    @Query(value = """
    SELECT COALESCE(s.cible, 'Non défini') AS cible, COUNT(*) AS count
    FROM participant p
    LEFT JOIN seance s ON p."seanceId" = s.id
    GROUP BY COALESCE(s.cible, 'Non défini')
    ORDER BY count DESC
    """, nativeQuery = true)
    List<CibleStat> getStatsByCible();

    // Participants groupés par localité
    @Query(value = """
    SELECT COALESCE(p.localite, 'Non défini') AS localite, COUNT(*) AS count
    FROM participant p
    GROUP BY COALESCE(p.localite, 'Non défini')
    ORDER BY count DESC
    """, nativeQuery = true)
    List<LocaliteStat> getStatsByLocalite();

    @Query(value = "SELECT COUNT(*) FROM participant WHERE EXTRACT(YEAR FROM \"dateInscription\") = :year AND EXTRACT(MONTH FROM \"dateInscription\") = :month", nativeQuery = true)
    long countByMonth(@Param("year") int year, @Param("month") int month);

    List<Participant> findBySeanceId(Long seanceId);
}
