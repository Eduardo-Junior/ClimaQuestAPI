package com.climaquest.cqapi.service;

import com.climaquest.cqapi.dto.CreatePlayerRequest;
import com.climaquest.cqapi.dto.PlayerResponse;
import com.climaquest.cqapi.entity.Player;
import com.climaquest.cqapi.repository.MissionProgressRepository;
import com.climaquest.cqapi.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final MissionProgressRepository missionProgressRepository;

    public PlayerResponse createPlayer(CreatePlayerRequest request){
        if (playerRepository.existsByCodename(request.codename())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Codename '" + request.codename() + "' já está em uso");
        }

        var player = new Player();
        player.setCodename(request.codename());
        player.setScientisType(request.scientistType());
        player.setAvatarIndex(request.avatarIndex());

        var saved = playerRepository.save(player);
        return PlayerResponse.from(saved);
    }

    public PlayerResponse getPlayer(UUID id){
        var player = playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));
        return PlayerResponse.from(player);
    }

    public List<PlayerResponse> getRanking(int limit){
        return playerRepository.findAllByOrderByXpDesc()
                .stream()
                .limit(limit)
                .map(PlayerResponse::from)
                .toList();
    }

    @Transactional
    public PlayerResponse claimDailyReward(UUID playerId){
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));

        var now = LocalDateTime.now();
        var lastClaim = player.getDailyRewardClaimedAt();

        if(lastClaim != null && Duration.between(lastClaim, now).toHours() < 24){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Recompensa diária já coletada. Tente novamente em " +
                            (24 - Duration.between(lastClaim, now).toHours()) + "h");
        }

        player.setXp(player.getXp() + 50);
        player.setDailyRewardClaimedAt(now);
        playerRepository.save(player);

        return PlayerResponse.from(player);
    }
}
