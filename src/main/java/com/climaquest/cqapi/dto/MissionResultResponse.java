package com.climaquest.cqapi.dto;

import com.climaquest.cqapi.entity.MissionProgress;

public record MissionResultResponse(
        PlayerResponse player,
        MissionProgressResponse mission
) {}
