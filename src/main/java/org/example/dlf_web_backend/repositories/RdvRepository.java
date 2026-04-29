package org.example.dlf_web_backend.repositories;

import org.example.dlf_web_backend.entities.Rdv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RdvRepository extends JpaRepository<Rdv, Long> {
    List<Rdv> findBySeanceId(Long seanceId);
}
