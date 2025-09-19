package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.entities.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {
}
