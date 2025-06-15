package com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository;
//import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.TaskRepositoryCustom;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Long>, JpaSpecificationExecutor<Tasks> {
    List<Tasks> findByResponsibleId(Long userId);
}
