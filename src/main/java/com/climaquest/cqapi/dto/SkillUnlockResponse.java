package com.climaquest.cqapi.dto;

import com.climaquest.cqapi.entity.SkillUnlock;

import java.time.LocalDateTime;
import java.util.UUID;

public record SkillUnlockResponse(
        UUID id,
        String skillId,
        LocalDateTime unlockedAt
) {
    public static SkillUnlockResponse from(SkillUnlock skill) {
        return new SkillUnlockResponse(
                skill.getId(),
                skill.getSkillId(),
                skill.getUnlockedAt()
        );
    }
}