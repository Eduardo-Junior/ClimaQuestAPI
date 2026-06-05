// service/MissionService.java
package com.climaquest.cqapi.service;

import com.climaquest.cqapi.dto.MissionProgressResponse;
import com.climaquest.cqapi.dto.MissionResultResponse;
import com.climaquest.cqapi.dto.PlayerResponse;
import com.climaquest.cqapi.dto.SubmitMissionRequest;
import com.climaquest.cqapi.entity.MissionProgress;
import com.climaquest.cqapi.repository.MissionProgressRepository;
import com.climaquest.cqapi.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionProgressRepository missionProgressRepository;
    private final PlayerRepository playerRepository;

    public List<MissionProgressResponse> getPlayerMissions(UUID playerId) {
        // Verifica se o jogador existe antes de buscar
        if (!playerRepository.existsById(playerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado");
        }

        return missionProgressRepository.findByPlayerId(playerId)
                .stream()
                .map(MissionProgressResponse::from)
                .toList();
    }

    @Transactional
    public MissionResultResponse submitMission(UUID playerId, String missionId,
                                               SubmitMissionRequest request) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Jogador não encontrado"));

        // Busca progresso existente ou cria um novo
        var progress = missionProgressRepository
                .findByPlayerIdAndMissionId(playerId, missionId)
                .orElse(new MissionProgress());

        progress.setPlayer(player);
        progress.setMissionId(missionId);
        progress.setCorrectAnswers(request.correctAnswers());
        progress.setWrongAnswers(request.wrongAnswers());

        if (request.completed()) {
            progress.setCompleted(true);
            progress.setCompletedAt(LocalDateTime.now());
        }

        missionProgressRepository.save(progress);

        // Atualiza XP do jogador
        player.setXp(player.getXp() + request.gainedXp());
        playerRepository.save(player);

        return new MissionResultResponse(
                PlayerResponse.from(player),
                MissionProgressResponse.from(progress)
        );
    }
}