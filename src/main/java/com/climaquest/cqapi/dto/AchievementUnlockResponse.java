package com.climaquest.cqapi.dto;

import com.climaquest.cqapi.entity.Achievement;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementUnlockResponse(
        UUID id,
        String achievementId,
        LocalDateTime unlockedAt
) {
    public static AchievementUnlockResponse from(Achievement achievement){
        return new AchievementUnlockResponse(
                achievement.getId(),
                achievement.getAchievementId(),
                achievement.getUnlockedAt()
        );
    }
}
