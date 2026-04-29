package org.example.dlf_web_backend.repositories;

import org.example.dlf_web_backend.entities.PriseContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriseContactRepository extends JpaRepository<PriseContact, Long> {

    @Query(value = "SELECT COUNT(*) FROM prise_contact WHERE EXTRACT(YEAR FROM date) = :year AND EXTRACT(MONTH FROM date) = :month", nativeQuery = true)
    long countByMonth(@Param("year") int year, @Param("month") int month);

    List<PriseContact> findBySeanceId(Long seanceId);
}


