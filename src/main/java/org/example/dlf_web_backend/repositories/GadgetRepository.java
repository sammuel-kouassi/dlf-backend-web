package org.example.dlf_web_backend.repositories;

import org.example.dlf_web_backend.entities.Gadget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface GadgetRepository extends JpaRepository<Gadget, Long> {

    @Query("SELECT COALESCE(SUM(g.stockInitial), 0) FROM Gadget g")
    long totalStockInitial();

    @Query("SELECT COALESCE(SUM(g.distribues), 0) FROM Gadget g")
    long totalDistribues();

    @Query("SELECT COUNT(DISTINCT g.categorie) FROM Gadget g")
    long countDistinctCategories();

    @Query("SELECT g.categorie, SUM(g.distribues) FROM Gadget g GROUP BY g.categorie")
    List<Object[]> statsByCategorie();
}