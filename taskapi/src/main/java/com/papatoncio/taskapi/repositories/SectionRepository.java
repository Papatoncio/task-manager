package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<List<Section>> findAllByProjectId(Long projectId);
}
