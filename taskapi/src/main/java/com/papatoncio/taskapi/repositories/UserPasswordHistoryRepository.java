package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.entities.UserPasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordHistoryRepository extends JpaRepository<UserPasswordHistory, Long> {
}
