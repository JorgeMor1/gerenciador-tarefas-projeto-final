package com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.TaskStatus;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.Tasks;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepositoryCustom {
    List<Tasks> findByStatusAndDueDateBetweenAndResponsibleId(TaskStatus status, LocalDate startDate, LocalDate endDate, Long responsibleId);
}