package com.climaquest.cqapi.service;

import com.climaquest.cqapi.dto.AchievementUnlockResponse;
import com.climaquest.cqapi.entity.Achievement;
import com.climaquest.cqapi.repository.AchievementRepository;
import com.climaquest.cqapi.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final PlayerRepository playerRepository;

    public List<AchievementUnlockResponse> getPlayerAchievements(UUID playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado");
        }

        return achievementRepository.findByPlayerId(playerId)
                .stream()
                .map(AchievementUnlockResponse::from)
                .toList();
    }

    public AchievementUnlockResponse unlockAchievement(UUID playerId, String achievementId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado");
        }

        // Impede desbloquear a mesma skill duas vezes
        if (achievementRepository.existsByPlayerIdAndAchievementId(playerId, achievementId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Conquista '" + achievementId + "' já foi desbloqueada");
        }

        var player = playerRepository.getReferenceById(playerId);

        var unlock = new Achievement();
        unlock.setPlayer(player);
        unlock.setAchievementId(achievementId);

        return AchievementUnlockResponse.from(achievementRepository.save(unlock));
    }
}
