package com.jorge.projeto.tarefas.tarefas_java_jwt.interfaces.repository;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}