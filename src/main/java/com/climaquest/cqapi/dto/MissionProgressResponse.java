package com.climaquest.cqapi.dto;

import com.climaquest.cqapi.entity.MissionProgress;

import java.time.LocalDateTime;
import java.util.UUID;

public record MissionProgressResponse(
        UUID id,
        String missionId,
        Integer correctAnswers,
        Integer wrongAnswers,
        Boolean completed,
        LocalDateTime completedAt
) {
    public static MissionProgressResponse from(MissionProgress mp){
        return new MissionProgressResponse(
                mp.getId(),
                mp.getMissionId(),
                mp.getCorrectAnswers(),
                mp.getWrongAnswers(),
                mp.getCompleted(),
                mp.getCompletedAt()
        );
    }
}
