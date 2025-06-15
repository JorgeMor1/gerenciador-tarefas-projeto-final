package com.jorge.projeto.tarefas.tarefas_java_jwt.controllers;

import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.LoginRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.RegisterRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.ResponseDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;
import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository.UserRepository;
import com.jorge.projeto.tarefas.tarefas_java_jwt.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body ) {
        System.out.println( "Endpoint /login chamado");
        System.out.println("Email recebido: " + body.email());
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not Found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())){
            System.out.println(" Senha confere! Gerando token...");
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    token));
        }
        System.out.println(" Senha inválida!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO body ) {
        Optional<User> user = this.repository.findByEmail(body.email());
        if(user.isEmpty()){
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            newUser.setRole(Role.USER);
            newUser.setCreatedAt(LocalDateTime.now());
            this.repository.save(newUser);
            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getRole(), token));
        }
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("E-mail já existe");
    }
}