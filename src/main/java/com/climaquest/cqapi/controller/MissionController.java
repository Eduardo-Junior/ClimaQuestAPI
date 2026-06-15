package com.climaquest.cqapi.controller;

import com.climaquest.cqapi.dto.MissionProgressResponse;
import com.climaquest.cqapi.dto.MissionResultResponse;
import com.climaquest.cqapi.dto.SubmitMissionRequest;
import com.climaquest.cqapi.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players/{playerId}/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping
    public List<MissionProgressResponse> getPlayerMissions(@PathVariable UUID playerId) {
        return missionService.getPlayerMissions(playerId);
    }

    @PostMapping("/{missionId}")
    public MissionResultResponse submitMission(
            @PathVariable UUID playerId,
            @PathVariable String missionId,
            @RequestBody @Valid SubmitMissionRequest request
    ) {
        return missionService.submitMission(playerId, missionId, request);
    }
}