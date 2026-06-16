package com.climaquest.cqapi.controller;

import com.climaquest.cqapi.dto.AchievementUnlockResponse;
import com.climaquest.cqapi.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/players/{playerId}/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public List<AchievementUnlockResponse> getPlayerAchievements(@PathVariable UUID playerId) {
        return achievementService.getPlayerAchievements(playerId);
    }

    @PostMapping("/{achievementId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AchievementUnlockResponse unlockAchievement(
            @PathVariable UUID playerId,
            @PathVariable String achievementId
    ) {
        return achievementService.unlockAchievement(playerId, achievementId);
    }
}
