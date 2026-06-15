package com.climaquest.cqapi.dto;

import com.climaquest.cqapi.entity.Player;

import java.util.UUID;

public record PlayerResponse(
        UUID id,
        String codename,
        String scientistType,
        Integer avatarIndex,
        Integer xp,
        Integer level,
        Integer totalPlayTimeSeconds
) {

    public static PlayerResponse from (Player player){
        return new PlayerResponse(
                player.getId(),
                player.getCodename(),
                player.getScientisType(),
                player.getAvatarIndex(),
                player.getXp(),
                player.getLevel(),
                player.getTotalPlayTimeSeconds()
        );
    }
}
