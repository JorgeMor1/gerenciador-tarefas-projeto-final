package com.jorge.projeto.tarefas.tarefas_java_jwt.controllers;

import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.RegisterRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.UserDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;
import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = repository.findByEmail(body.email());
        if(user.isEmpty()) {
            User admin = new User();
            admin.setEmail(body.email());
            admin.setPassword(passwordEncoder.encode(body.password()));
            admin.setName(body.name());
            admin.setRole(Role.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());
            repository.save(admin);
            return ResponseEntity.ok("Usuário do tipo ADMIN cadastrado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Email já existe.");
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = repository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
}