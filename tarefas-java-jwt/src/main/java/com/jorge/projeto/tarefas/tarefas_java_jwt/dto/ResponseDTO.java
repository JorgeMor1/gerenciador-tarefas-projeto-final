package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;

public record ResponseDTO (Long id, String name, String email, Role role, String token){
}
