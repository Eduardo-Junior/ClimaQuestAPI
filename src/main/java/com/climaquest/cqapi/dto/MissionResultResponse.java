package com.climaquest.cqapi.dto;

public record MissionResultResponse(
        PlayerResponse player,
        MissionProgressResponse mission
) {}
