package com.jorge.projeto.tarefas.tarefas_java_jwt.service;

import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.AuthRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.AuthResponseDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.RegisterRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public AuthResponseDTO login(AuthRequestDTO dto) {
        var auth = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        authenticationManager.authenticate(auth); // lança exceção se inválido

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role() != null ? dto.role() : Role.USER);

        userRepository.save(user);
        String token = tokenService.generateToken(user);
        return new AuthResponseDTO(token);
    }

    @PostConstruct
    public void createAdminUserIfNotExists() {
        String adminEmail = "admin@email.com";
        String adminPassword = System.getenv("ADMIN_PASSWORD");

        if (adminPassword == null || adminPassword.isBlank()) {
            adminPassword = "123456";
        }

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());

            userRepository.save(admin);
            log.info(" Usuário ADMIN criado automaticamente: {}", adminEmail);
        } else {
            log.info(" Usuário ADMIN já existe: {}", adminEmail);
        }
    }
}