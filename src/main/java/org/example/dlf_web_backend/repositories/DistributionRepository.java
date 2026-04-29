package org.example.dlf_web_backend.repositories;

import org.example.dlf_web_backend.dto.CategorieGadgetStat;
import org.example.dlf_web_backend.dto.MonthlyGadgetStat;
import org.example.dlf_web_backend.entities.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    @Query("SELECT COALESCE(SUM(d.quantite), 0) FROM Distribution d")
    long totalQuantiteDistribuee();

    @Query("SELECT COALESCE(SUM(d.quantite), 0) FROM Distribution d WHERE d.gadgetId = :gadgetId")
    long totalDistribueByGadgetId(@Param("gadgetId") long gadgetId);

    @Query(value = """
        SELECT g.categorie AS categorie,
               SUM(
                   g.distribues + COALESCE(
                       (SELECT SUM(d.quantite) FROM distribution d WHERE d."gadgetId" = g.id),
                       0
                   )
               ) AS total
        FROM gadget g
        GROUP BY g.categorie
        """, nativeQuery = true)
    List<CategorieGadgetStat> statsParCategorie();

    @Query(value = """
        SELECT COALESCE(SUM(d.quantite), 0) FROM distribution d
        WHERE DATE_TRUNC('month', d."dateDistribution") = DATE_TRUNC('month', CURRENT_DATE)
        """, nativeQuery = true)
    long totalQuantiteCeMois();

    @Query(value = """
        SELECT COALESCE(SUM(d.quantite), 0) FROM distribution d
        WHERE DATE_TRUNC('month', d."dateDistribution")
            = DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
        """, nativeQuery = true)
    long totalQuantiteMoisDernier();

    @Query(value = """
    SELECT month, SUM(count) AS count FROM (

        --Distributions web réelles (table distribution)
        SELECT TO_CHAR(d."dateDistribution", 'YYYY-MM') AS month,
               SUM(d.quantite) AS count
        FROM distribution d
        GROUP BY TO_CHAR(d."dateDistribution", 'YYYY-MM')

        UNION ALL

        --Distributions mobiles (seance.gadgetsDistribues, date = datePrevue)
        SELECT TO_CHAR(s."datePrevue", 'YYYY-MM') AS month,
               SUM(s."gadgetsDistribues") AS count
        FROM seance s
        WHERE s."gadgetsDistribues" > 0
        GROUP BY TO_CHAR(s."datePrevue", 'YYYY-MM')

        UNION ALL

        --Offset initial gadget. Distribues → rattaché au mois courant
        SELECT TO_CHAR(CURRENT_DATE, 'YYYY-MM') AS month,
               SUM(g.distribues) AS count
        FROM gadget g
        WHERE g.distribues > 0

    ) combined
    GROUP BY month
    ORDER BY month
    """, nativeQuery = true)
    List<MonthlyGadgetStat> getDistributionMensuelle();

}