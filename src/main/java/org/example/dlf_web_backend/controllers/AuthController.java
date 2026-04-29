package org.example.dlf_web_backend.controllers;

import jakarta.validation.Valid;
import org.example.dlf_web_backend.config.AuthService;
import org.example.dlf_web_backend.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints publics (non protégés par JWT).
 * POST /api/auth/register  → Créer le premier compte admin web
 * POST /api/auth/login     → Se connecter (retourne un JWT)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}