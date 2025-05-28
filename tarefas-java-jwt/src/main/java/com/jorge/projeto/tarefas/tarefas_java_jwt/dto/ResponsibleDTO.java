package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsibleDTO {
    private Long id;
    private String name;
    private Role role;

}
