package org.example.dlf_web_backend.config;

import org.example.dlf_web_backend.dto.*;
import org.example.dlf_web_backend.entities.Utilisateur;
import org.example.dlf_web_backend.repositories.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service d'authentification web.
 * Gère : register, login, création de comptes mobiles, liste/suppression.
 */
@Service
public class AuthService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder        encoder;
    private final JwtUtil                jwtUtil;

    public AuthService(UtilisateurRepository repo,
                       PasswordEncoder encoder,
                       JwtUtil jwtUtil) {
        this.repo    = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un compte avec cet email existe déjà");
        }

        Utilisateur u = new Utilisateur();
        u.setNom(req.nom);
        u.setEmail(req.email);
        u.setMotDePasse(encoder.encode(req.motDePasse));
        u.setRole(req.role);
        repo.save(u);

        String token = jwtUtil.generateToken(u.getEmail(), u.getRole());
        return new AuthResponse(token, u.getId(), u.getEmail(), u.getNom(), u.getRole());
    }

    public AuthResponse login(LoginRequest req) {
        Utilisateur u = repo.findByEmail(req.email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect"));

        if (!encoder.matches(req.motDePasse, u.getMotDePasse())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect");
        }

        if (!u.getRole().equals("administrateur") && !u.getRole().equals("superviseur")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ce compte n'a pas accès à la plateforme web");
        }

        String token = jwtUtil.generateToken(u.getEmail(), u.getRole());
        return new AuthResponse(token, u.getId(), u.getEmail(), u.getNom(), u.getRole());
    }

    // Gestion des comptes mobiles

    public UserResponse createMobileUser(CreateUserRequest req) {
        if (repo.existsByEmail(req.email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Un compte avec cet email existe déjà");
        }

        Utilisateur u = new Utilisateur();
        u.setNom(req.nom);
        u.setEmail(req.email);
        u.setMotDePasse(encoder.encode(req.motDePasse));
        u.setRole(req.role);
        repo.save(u);

        return UserResponse.from(u);
    }

    public List<UserResponse> getAllUsers() {
        return repo.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    public List<UserResponse> getUsersByRole(String role) {
        return repo.findByRole(role).stream()
                .map(UserResponse::from)
                .toList();
    }

    public void deleteUser(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Utilisateur introuvable");
        }
        repo.deleteById(id);
    }

    public UserResponse updateUser(Long id, CreateUserRequest req) {
        Utilisateur u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        u.setNom(req.nom);
        u.setRole(req.role);

        // Si un nouveau mot de passe est fourni, on le rehash
        if (req.motDePasse != null && !req.motDePasse.isBlank()) {
            u.setMotDePasse(encoder.encode(req.motDePasse));
        }

        repo.save(u);
        return UserResponse.from(u);
    }
}