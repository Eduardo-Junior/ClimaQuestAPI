package com.climaquest.cqapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mission_progress",
            uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "mission_id"}))
@Getter @Setter @NoArgsConstructor
public class MissionProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private String missionId;

    private Integer correctAnswers = 0;
    private Integer wrongAnswers = 0;
    private Boolean completed = false;
    private LocalDateTime completedAt;
}
