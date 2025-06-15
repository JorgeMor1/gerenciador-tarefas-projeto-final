package com.jorge.projeto.tarefas.tarefas_java_jwt.dto;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.TaskStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TaskUpdateDTO {
    private String title;
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    private TaskStatus status;
}
