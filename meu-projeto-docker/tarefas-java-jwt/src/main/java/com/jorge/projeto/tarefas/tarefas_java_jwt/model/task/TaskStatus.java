package com.jorge.projeto.tarefas.tarefas_java_jwt.model.task;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}