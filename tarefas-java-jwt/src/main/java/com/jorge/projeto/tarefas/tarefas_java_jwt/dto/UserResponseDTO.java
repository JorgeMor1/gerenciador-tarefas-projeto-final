package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;


public record UserResponseDTO(Long id, String name, String email, String role) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole().name());

    }
}
