package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.entities.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    @Query("""
            SELECT t FROM TaskComment t
                        WHERE t.task.id = :taskId
                                    AND t.parent IS NULL
            """)
    Optional<List<TaskComment>> findAllParentCommentsByTaskId(Long taskId);
}
