package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(@NotBlank(message = "El email es obligatorio.") String email);
}
