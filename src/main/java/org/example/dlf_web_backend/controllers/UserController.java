package org.example.dlf_web_backend.controllers;

import jakarta.validation.Valid;
import org.example.dlf_web_backend.config.AuthService;
import org.example.dlf_web_backend.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints protégés (JWT requis) pour la gestion des utilisateurs.
 * Accessible uniquement aux administrateurs.
 * GET    /api/users              → Tous les utilisateurs
 * GET    /api/users ? Role=xxx     → Filtrer par rôle
 * POST   /api/users              → Créer un compte (mobile ou web)
 * PUT    /api/users/{id}         → Modifier nom/rôle/mot de passe
 * DELETE /api/users/{id}         → Supprimer un compte
 */
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> list(
            @RequestParam(required = false) String role
    ) {
        List<UserResponse> users = (role != null && !role.isBlank())
                ? authService.getUsersByRole(role)
                : authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @Valid @RequestBody CreateUserRequest req
    ) {
        UserResponse created = authService.createMobileUser(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "utilisateur",     created,
                "motDePasseClair", req.motDePasse,
                "message",         "Compte créé. Transmettez ces identifiants à l'utilisateur."
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @RequestBody CreateUserRequest req
    ) {
        return ResponseEntity.ok(authService.updateUser(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}