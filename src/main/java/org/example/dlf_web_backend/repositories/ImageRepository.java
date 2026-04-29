package org.example.dlf_web_backend.repositories;

import org.example.dlf_web_backend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findBySeanceId(Long seanceId);
}