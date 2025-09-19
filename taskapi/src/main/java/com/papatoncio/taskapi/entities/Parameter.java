package com.papatoncio.taskapi.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "parameters")
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    @Id
    @GeneratedValue
            (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String key;

    @Column(nullable = false, length = 100)
    private String value;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
