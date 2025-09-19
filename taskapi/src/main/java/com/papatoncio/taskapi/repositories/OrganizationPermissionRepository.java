package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.entities.OrganizationPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationPermissionRepository extends JpaRepository<OrganizationPermission, Long> {
    Optional<OrganizationPermission> findByUserIdAndOrganizationId(Long userId, Long organizationId);

    @Query("""
            SELECT op FROM OrganizationPermission op
                        JOIN Project p ON op.organization = p.organization
                        WHERE op.user.id=:userId AND p.id=:projectId
            """)
    Optional<OrganizationPermission> findByUserIdAndProjectId(Long userId, Long projectId);

    @Query("""
            SELECT op FROM OrganizationPermission op
                        JOIN Project p ON op.organization = p.organization
                                    JOIN Section s ON p = s.project
                        WHERE op.user.id=:userId AND s.id=:sectionId
            """)
    Optional<OrganizationPermission> findByUserIdAndSectionId(Long userId, Long sectionId);

    @Query("""
            SELECT op FROM OrganizationPermission op
                        JOIN Project p ON op.organization = p.organization
                                    JOIN Section s ON p = s.project
                                                JOIN Task t ON s = t.section
                        WHERE op.user.id=:userId AND t.id=:taskId
            """)
    Optional<OrganizationPermission> findByUserIdAndTaskId(Long userId, Long taskId);

    @Query("""
            SELECT op FROM OrganizationPermission op
                        JOIN Project p ON op.organization = p.organization
                                    JOIN Section s ON p = s.project
                                                JOIN Task t ON s = t.section
                                                            JOIN TaskComment tc ON t = tc.task
                        WHERE op.user.id=:userId AND tc.id=:taskCommentId
            """)
    Optional<OrganizationPermission> findByUserIdAndTaskCommentId(Long userId, Long taskCommentId);

    void deleteAllByOrganizationId(Long organizationId);
}
