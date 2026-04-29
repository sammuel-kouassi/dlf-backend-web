package org.example.dlf_web_backend.controllers;

import org.example.dlf_web_backend.entities.Image;
import org.example.dlf_web_backend.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @GetMapping("/{id}")
    public Image getImageById(@PathVariable Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    @GetMapping("/seance/{seanceId}")
    public List<Image> getImagesBySeance(@PathVariable Long seanceId) {
        return imageRepository.findBySeanceId(seanceId);
    }

    @PostMapping
    public Image createImage(@RequestBody Image image) {
        return imageRepository.save(image);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable Long id, @RequestBody Image updated) {
        return imageRepository.findById(id).map(i -> {
            updated.setId(id);
            return ResponseEntity.ok(imageRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        if (!imageRepository.existsById(id)) return ResponseEntity.notFound().build();
        imageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}