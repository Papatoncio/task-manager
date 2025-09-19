package com.papatoncio.taskapi.entities;


import com.papatoncio.taskapi.common.PermissionLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "project_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
@Getter
@Setter
@ToString(exclude = {"project", "user"})
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionLevel level;
}
