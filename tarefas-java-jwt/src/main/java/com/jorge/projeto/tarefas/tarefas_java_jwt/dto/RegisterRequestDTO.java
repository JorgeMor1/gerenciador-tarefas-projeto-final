package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;

public record RegisterRequestDTO (
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String password,
        Role role
) { }