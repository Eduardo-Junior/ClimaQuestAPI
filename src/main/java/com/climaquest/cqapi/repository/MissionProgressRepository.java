package com.climaquest.cqapi.repository;

import com.climaquest.cqapi.entity.MissionProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MissionProgressRepository extends JpaRepository<MissionProgress, UUID> {

    //Lista progresso de missão para um jogador específico. Busca feito por ID
    List<MissionProgress> findByPlayerId(UUID playerId);

    //Busca por Id do jogador e Id de missão específica
    Optional<MissionProgress> findByPlayerIdAndMissionId(UUID playerId, String missionId);
}
