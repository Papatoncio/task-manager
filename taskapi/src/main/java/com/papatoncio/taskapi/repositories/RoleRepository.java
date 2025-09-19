package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.common.UserRole;
import com.papatoncio.taskapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
