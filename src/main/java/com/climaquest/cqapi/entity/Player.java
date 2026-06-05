package com.climaquest.cqapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="players")
@Getter @Setter @NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //Gera ID automaticamente
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String codename;

    @Column(nullable = false)
    private String scientisType;

    @Column(nullable = false)
    private Integer avatarIndex;

    @Column(nullable = false)
    private Integer xp = 0;

    @Column(nullable = false)
    private Integer totalPlayTimeSeconds = 0;

    private LocalDateTime dailyRewardClaimedAt; // null = nunca coletou

    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate // É chamado automaticamente pelo JPA antes de qualquer update
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public int getLevel(){
        return (xp/500) + 1;
    }
}
