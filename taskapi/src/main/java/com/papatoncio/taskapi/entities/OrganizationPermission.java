package com.papatoncio.taskapi.entities;


import com.papatoncio.taskapi.common.PermissionLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "organization_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "user_id"}))
@Getter
@Setter
@ToString(exclude = {"organization", "user"})
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionLevel level;
}
