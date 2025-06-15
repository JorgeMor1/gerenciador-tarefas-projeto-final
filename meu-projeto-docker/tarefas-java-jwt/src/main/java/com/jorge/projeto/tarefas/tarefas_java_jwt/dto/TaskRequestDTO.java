package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;

import java.time.LocalDate;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate dueDate;

    private TaskStatus status;

    private Long responsibleId;
}