package com.example.bookstore.controller;

import com.example.bookstore.dto.AuthRequest;
import com.example.bookstore.dto.RegistrationRequest;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepo;

    public AuthController(AuthenticationManager authManager, JwtUtils jwtUtils, UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtils    = jwtUtils;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Username already in use"));
        }
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Email already in use"));
        }

        String role = "USER";

        User user = new User(req.getEmail(), req.getUsername(), req.getPassword(),role);
        user = userRepo.save(user);

        String token = jwtUtils.generateToken(user.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "id",       user.getId(),
                        "username", user.getUsername(),
                        "email",    user.getEmail(),
                        "token",    token
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest creds) {
        var token = new UsernamePasswordAuthenticationToken(
                creds.getUsername(), creds.getPassword());
        Authentication auth = authManager.authenticate(token);
        String jwt = jwtUtils.generateToken(auth.getName());
        String role = userRepo
                .findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No user “" + auth.getName() + "”"))
                .getRole();
        Long userId = userRepo
                .findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No user “" + auth.getName() + "”"))
                .getId();
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "role",  role,
                "userId", userId
        ));
    }
}
