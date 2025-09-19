package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.dto.project.ProjectWithPermissionsResponse;
import com.papatoncio.taskapi.entities.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(value = "Organization.projects")
    @Query("""
            SELECT new com.papatoncio.taskapi.dto.project.ProjectWithPermissionsResponse(
                new com.papatoncio.taskapi.dto.project.ProjectResponse(
                    p.id, p.name, p.description, p.active, p.createdAt, p.organization.id
                ),
                COALESCE(pp.level, op.level)
            )
            FROM Project p
            JOIN p.organization o
            LEFT JOIN ProjectPermission pp ON pp.project = p AND pp.user.id = :userId
            LEFT JOIN OrganizationPermission op ON op.organization = p.organization AND op.user.id = :userId
            WHERE p.organization.id = :organizationId
              AND (pp.level IS NOT NULL OR op.level IS NOT NULL)
            """)
    List<ProjectWithPermissionsResponse> findAllProjectsWithPermissionsByUserIdAndOrganizationId(
            @Param("userId") Long userId,
            @Param("organizationId") Long organizationId
    );

    @EntityGraph(value = "Organization.projects")
    @Query("""
            SELECT new com.papatoncio.taskapi.dto.project.ProjectWithPermissionsResponse(
                        new com.papatoncio.taskapi.dto.project.ProjectResponse(p.id, p.name, p.description, p.active, p.createdAt, p.organization.id), 
                                    com.papatoncio.taskapi.common.PermissionLevel.ADMIN) 
                        FROM Project p
                                    JOIN Organization o
                                                ON p.organization = o
                                                            WHERE p.organization.id = :organizationId
            """)
    List<ProjectWithPermissionsResponse> findAllProjectsByOrganizationId(
            @Param("organizationId") Long organizationId
    );
}
