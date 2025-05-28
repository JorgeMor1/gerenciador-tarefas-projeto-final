package com.jorge.projeto.tarefas.tarefas_java_jwt.service;


import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.ResponsibleDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.TaskRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.TaskResponseDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.TaskStatus;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.Tasks;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;
import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository.UserRepository;
import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;


    public TaskResponseDTO createTask(TaskRequestDTO dto, Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();

        Long jwtUserId = Long.valueOf(jwt.getClaimAsString("id"));
        String roleStr = jwt.getClaimAsString("role");

        if (roleStr == null) {
            throw new IllegalArgumentException("Claim 'role' não encontrado no token JWT.");
        }

        Role role = Role.valueOf(roleStr);

        Tasks task = new Tasks();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(dto.getStatus());

        if (role == Role.USER) {
            User currentUser = userRepo.findById(jwtUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            task.setResponsible(currentUser);
        } else if (role == Role.ADMIN) {
            if (dto.getResponsibleId() != null) {
                User responsible = userRepo.findById(dto.getResponsibleId())
                        .orElseThrow(() -> new UsernameNotFoundException("Responsável não encontrado"));
                task.setResponsible(responsible);
            } else {
                User currentUser = userRepo.findById(jwtUserId)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
                task.setResponsible(currentUser);
            }
        } else {
            throw new AccessDeniedException("Role não autorizada para criar tarefas");
        }

        Tasks saved = taskRepo.save(task);
        return toDTO(saved);
    }



    public List<TaskResponseDTO> filterTasks(TaskStatus status, LocalDate start, LocalDate end, Long userId, Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();

        Long jwtUserId = Long.valueOf(jwt.getClaimAsString("id"));

        String role = jwt.getClaimAsString("role");

        if (Role.USER.name().equals(role)) {
            userId = jwtUserId;
        }

        List<Tasks> tasks = taskRepo.findByFilters(status, start, end, userId);

        return tasks.stream().map(this::toDTO).toList();
    }


    private TaskResponseDTO toDTO(Tasks task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setRole(task.getResponsible().getRole());

        dto.setDueDate(task.getDueDate().toString());
        if (task.getResponsible() != null) {
            ResponsibleDTO res = new ResponsibleDTO();
            res.setId(task.getResponsible().getId());
            res.setName(task.getResponsible().getName());
            dto.setResponsible(res);
        }
        return dto;
    }

    //Trocar de responsável na tarefa
    public void updateResponsible(Long taskId, Long newResponsibleId) {
        Tasks task = taskRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));

        User responsible = userRepo.findById(newResponsibleId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        task.setResponsible(responsible);
        taskRepo.save(task);
    }




    public TaskResponseDTO convertToResponseDTO(Tasks task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreatedAt(task.getCreatedAt() != null ? task.getCreatedAt().toString() : null);
        dto.setDueDate(task.getDueDate() != null ? task.getDueDate().toString() : null);
        if (task.getResponsible() != null) {
            ResponsibleDTO respDto = new ResponsibleDTO();
            respDto.setId(task.getResponsible().getId());
            respDto.setName(task.getResponsible().getName());
            dto.setResponsible(respDto);
        }
        dto.setStatus(task.getStatus());
        return dto;
    }


}

