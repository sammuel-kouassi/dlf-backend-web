package org.example.dlf_web_backend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;


@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Value("${app.upload.dir:./uploads/images}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImages(
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<String> urls = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try {
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Impossible de créer le dossier d'upload : " + e.getMessage()
            ));
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            try {
                // Nom unique pour éviter les collisions
                String originalName = file.getOriginalFilename() != null
                        ? file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_")
                        : "image";
                String extension = getExtension(originalName);
                String uniqueName = UUID.randomUUID().toString() + extension;

                // Sauvegarde physique
                Path filePath = Paths.get(uploadDir, uniqueName);
                Files.write(filePath, file.getBytes());

                // URL publique accessible depuis le web et le mobile
                String publicUrl = baseUrl + "/api/images/files/" + uniqueName;
                urls.add(publicUrl);

            } catch (IOException e) {
                errors.add(file.getOriginalFilename() + " : " + e.getMessage());
            }
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", !urls.isEmpty());
        response.put("urls", urls);
        response.put("count", urls.size());
        if (!errors.isEmpty()) response.put("errors", errors);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {
        try {
            // Sécurité : empêche le path traversal
            String safeName = Paths.get(filename).getFileName().toString();
            Path filePath   = Paths.get(uploadDir, safeName);

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] data        = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Cache-Control", "public, max-age=86400")
                    .body(data);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Helpers

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot).toLowerCase() : ".jpg";
    }
}