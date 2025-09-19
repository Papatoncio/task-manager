package com.papatoncio.taskapi.repositories;

import com.papatoncio.taskapi.dto.organization.OrganizationWithPermissionsResponse;
import com.papatoncio.taskapi.entities.Organization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @EntityGraph(value = "Organization.projects")
    @Query("""
            SELECT new com.papatoncio.taskapi.dto.organization.OrganizationWithPermissionsResponse(
                        new com.papatoncio.taskapi.dto.organization.OrganizationResponse(o.id, o.name, o.description, o.createdAt), 
                                    op.level)
                        FROM Organization o 
                                    JOIN OrganizationPermission op 
                                    ON op.organization = o 
                                                WHERE op.user.id = :userId
            """)
    List<OrganizationWithPermissionsResponse> findAllOrganizationsWithPermissionsByUserId(@Param("userId") Long userId);
}
