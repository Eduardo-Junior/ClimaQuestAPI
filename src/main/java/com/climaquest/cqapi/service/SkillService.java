package com.climaquest.cqapi.service;

import com.climaquest.cqapi.dto.SkillUnlockResponse;
import com.climaquest.cqapi.entity.SkillUnlock;
import com.climaquest.cqapi.repository.PlayerRepository;
import com.climaquest.cqapi.repository.SkillUnlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillUnlockRepository skillUnlockRepository;
    private final PlayerRepository playerRepository;

    public List<SkillUnlockResponse> getPlayerSkills(UUID playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado");
        }

        return skillUnlockRepository.findByPlayerId(playerId)
                .stream()
                .map(SkillUnlockResponse::from)
                .toList();
    }

    public SkillUnlockResponse unlockSkill(UUID playerId, String skillId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado");
        }

        // Impede desbloquear a mesma skill duas vezes
        if (skillUnlockRepository.existsByPlayerIdAndSkillId(playerId, skillId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Skill '" + skillId + "' já foi desbloqueada");
        }

        var player = playerRepository.getReferenceById(playerId);

        var unlock = new SkillUnlock();
        unlock.setPlayer(player);
        unlock.setSkillId(skillId);

        return SkillUnlockResponse.from(skillUnlockRepository.save(unlock));
    }
}
