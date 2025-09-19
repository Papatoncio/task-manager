package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.entities.ProjectPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectPermissionRepository extends JpaRepository<ProjectPermission, Long> {
    Optional<ProjectPermission> findByUserIdAndProjectId(Long userId, Long organizationId);

    @Query("""
            SELECT pp FROM ProjectPermission pp
                        JOIN Section s ON pp.project = s.project
                        WHERE pp.user.id=:userId AND s.id=:sectionId
            """)
    Optional<ProjectPermission> findByUserIdAndSectionId(Long userId, Long sectionId);

    @Query("""
            SELECT pp FROM ProjectPermission pp
                        JOIN Section s ON pp.project = s.project
                                    JOIN Task t on s = t.section
                        WHERE pp.user.id=:userId AND t.id=:taskId
            """)
    Optional<ProjectPermission> findByUserIdAndTaskId(Long userId, Long taskId);

    @Query("""
            SELECT pp FROM ProjectPermission pp
                        JOIN Section s ON pp.project = s.project
                                    JOIN Task t on s = t.section
                                                JOIN TaskComment tc ON t = tc.task
                        WHERE pp.user.id=:userId AND tc.id=:taskCommentId
            """)
    Optional<ProjectPermission> findByUserIdAndTaskCommentId(Long userId, Long taskCommentId);

    void deleteAllByProjectId(Long organizationId);
}
