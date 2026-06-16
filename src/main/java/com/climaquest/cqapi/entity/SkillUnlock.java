// entity/SkillUnlock.java
package com.climaquest.cqapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "skill_unlocks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "skill_id"}))
// Um jogador não pode desbloquear a mesma skill duas vezes
@Getter @Setter @NoArgsConstructor
public class SkillUnlock {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "skill_id", nullable = false)
    private String skillId;

    @Column(nullable = false)
    private LocalDateTime unlockedAt = LocalDateTime.now();
}