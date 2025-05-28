package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String createdAt;
    private String dueDate;
    private ResponsibleDTO responsible;
    private TaskStatus status;
    private Role role;

}