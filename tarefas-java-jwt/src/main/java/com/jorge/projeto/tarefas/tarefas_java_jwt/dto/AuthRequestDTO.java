package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;

public record AuthRequestDTO(String email, String password, Role role) {}
