package com.papatoncio.taskapi.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(exclude = "user")
@Table(name = "user_password_history",
        indexes = @Index(name = "idx_user_password_history_user_id", columnList = "user_id"))
@Getter
@Setter
@ToString(exclude = "user")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String oldPassword;

    @Getter
    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now();
    }
}