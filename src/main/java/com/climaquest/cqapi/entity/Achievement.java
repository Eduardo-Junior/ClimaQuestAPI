package com.climaquest.cqapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "achievements",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "achievment_id"}))
@Getter @Setter @NoArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "achievement_id", nullable = false)
    private String achievementId;

    @Column(nullable = false)
    private LocalDateTime unlockedAt = LocalDateTime.now();


}
