package com.jorge.projeto.tarefas.specifications;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.TaskStatus;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.Tasks;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecification {

    public static Specification<Tasks> hasStatus(TaskStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Tasks> hasDueDateAfter(LocalDate startDate) {
        return (root, query, cb) -> startDate == null ? null : cb.greaterThanOrEqualTo(root.get("dueDate"), startDate);
    }

    public static Specification<Tasks> hasDueDateBefore(LocalDate endDate) {
        return (root, query, cb) -> endDate == null ? null : cb.lessThanOrEqualTo(root.get("dueDate"), endDate);
    }

    public static Specification<Tasks> hasResponsibleId(Long userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.get("responsible").get("id"), userId);
    }
}