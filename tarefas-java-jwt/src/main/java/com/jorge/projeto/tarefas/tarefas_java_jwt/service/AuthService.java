package com.jorge.projeto.tarefas.tarefas_java_jwt.service;

import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.AuthRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.AuthResponseDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.RegisterRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.infra.security.TokenService;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

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
}
