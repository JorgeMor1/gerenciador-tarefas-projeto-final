package com.jorge.projeto.tarefas.tarefas_java_jwt.controllers;

import com.jorge.projeto.tarefas.specifications.TaskSpecification;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.ResponsibleUpdateDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.TaskRequestDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.TaskResponseDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.dto.TaskUpdateDTO;
import com.jorge.projeto.tarefas.tarefas_java_jwt.infra.UserDetailsImpl;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.TaskStatus;
import com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository.TaskRepository;
import com.jorge.projeto.tarefas.tarefas_java_jwt.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.task.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private TaskService taskService;

    private final TaskRepository taskRepo;

    @PreAuthorize("hasRole('USER')")
@GetMapping("/user")
public List<TaskResponseDTO> getTasksForUser(Authentication auth) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    Long userId = Long.valueOf(jwt.getClaimAsString("id"));

    // Filtra só pelas tarefas do usuário
    var spec = TaskSpecification.hasResponsibleId(userId);

    List<Tasks> tasks = taskRepo.findAll(spec);
    return tasks.stream()
                .map(taskService::convertToResponseDTO)
                .toList();
}


    /*@PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public List<TaskResponseDTO> getTasksForUser(Authentication auth) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long userId = userDetails.getId();
        List<Tasks> tasks =  taskRepo.findByStatusAndDueDateBetweenAndResponsibleId(null, null, null, userId);
        return tasks.stream()
                .map(taskService::convertToResponseDTO)
                .toList();
    }*/

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public List<Tasks> getAllTasks() {
        return taskRepo.findAll();
    }


    @GetMapping("/admin/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        Optional<Tasks> optionalTask = taskRepo.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Tasks task = optionalTask.get();
        TaskResponseDTO dto = taskService.convertToResponseDTO(task);
        return ResponseEntity.ok(dto);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<ResponseEntity<Void>> response = taskRepo.findById(id)
                .map(task -> {
                    taskRepo.delete(task);
                    return ResponseEntity.noContent().build();
                });
        return response.orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO dto, Authentication authentication) {
        TaskResponseDTO created = taskService.createTask(dto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

   /*  @PutMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> updateTaskByUser(
            @PathVariable Long id,
            @RequestBody TaskRequestDTO dto,
            Authentication auth) {

        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado corretamente.");
        }

        Jwt jwt = (Jwt) auth.getPrincipal();

        //String userIdStr = jwt.getClaimAsString("user_id");
        String userIdStr = jwt.getClaimAsString("id");

        if (userIdStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID do usuário não encontrado no token.");
        }

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID do usuário inválido no token.");
        }

        Optional<Tasks> optionalTask = taskRepo.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tasks task = optionalTask.get();

        // Verifica se o usuário autenticado é responsável pela tarefa
        if (task.getResponsible() == null || !task.getResponsible().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar esta tarefa.");
        }

        // Atualiza os campos permitidos
        task.setStatus(dto.getStatus());

        taskRepo.save(task);
        return ResponseEntity.ok("Tarefa atualizada com sucesso.");
    }*/

    @PutMapping("/user/{id}")
   @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
   public ResponseEntity<?> updateTaskByUser(
           @PathVariable Long id,
           @RequestBody TaskRequestDTO dto,
           Authentication auth) {

       if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado corretamente.");
       }

       Jwt jwt = (Jwt) auth.getPrincipal();

       String userIdStr = jwt.getClaimAsString("id");
       if (userIdStr == null) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID do usuário não encontrado no token.");
       }

       Long userId;
       try {
           userId = Long.parseLong(userIdStr);
       } catch (NumberFormatException e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID do usuário inválido no token.");
       }

       String role = jwt.getClaimAsString("role");
       boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

       Optional<Tasks> optionalTask = taskRepo.findById(id);
       if (optionalTask.isEmpty()) {
           return ResponseEntity.notFound().build();
       }

       Tasks task = optionalTask.get();

       // ✅ Verificação: ADMIN pode tudo, USER só se for responsável
       if (!isAdmin && (task.getResponsible() == null || !task.getResponsible().getId().equals(userId))) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar esta tarefa.");
       }

       // Atualiza os campos permitidos
       task.setStatus(dto.getStatus());

       taskRepo.save(task);
       return ResponseEntity.ok("Tarefa atualizada com sucesso.");
   }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/responsible")
    public ResponseEntity<?> updateResponsible(
            @PathVariable Long id,
            @RequestBody ResponsibleUpdateDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String role = jwt.getClaimAsString("role");

        if (!"ADMIN".equals(role)) {
            throw new AccessDeniedException("Apenas administradores podem trocar o responsável de uma tarefa.");
        }

        taskService.updateResponsible(id, dto.getResponsibleId());
        return ResponseEntity.ok(Map.of("message", "Responsável atualizado com sucesso."));
    }




    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<TaskResponseDTO>> filterTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long userId,
            Authentication authentication
    ) {
        TaskStatus taskStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                taskStatus = TaskStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build(); // status inválido
            }
        }
        List<TaskResponseDTO> filteredTasks = taskService.filterTasks(taskStatus, startDate, endDate, userId, authentication);
        return ResponseEntity.ok(filteredTasks);
    }


    @PatchMapping("/admin/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> updateTaskAsAdmin(
        @PathVariable Long id,
        @RequestBody TaskUpdateDTO dto
) {
    Optional<Tasks> optionalTask = taskRepo.findById(id);
    if (optionalTask.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Tasks task = optionalTask.get();

    // Atualiza somente os campos presentes
    if (dto.getTitle() != null) task.setTitle(dto.getTitle());
    if (dto.getDescription() != null) task.setDescription(dto.getDescription());
    if (dto.getDueDate() != null) task.setDueDate(dto.getDueDate());
    if (dto.getStatus() != null) task.setStatus(dto.getStatus());

    taskRepo.save(task);

    return ResponseEntity.ok("Tarefa atualizada com sucesso.");
}




}