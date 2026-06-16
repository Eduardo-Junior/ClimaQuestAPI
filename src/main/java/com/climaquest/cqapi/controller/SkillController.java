// controller/SkillController.java
package com.climaquest.cqapi.controller;

import com.climaquest.cqapi.dto.SkillUnlockResponse;
import com.climaquest.cqapi.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players/{playerId}/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public List<SkillUnlockResponse> getPlayerSkills(@PathVariable UUID playerId) {
        return skillService.getPlayerSkills(playerId);
    }

    @PostMapping("/{skillId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SkillUnlockResponse unlockSkill(
            @PathVariable UUID playerId,
            @PathVariable String skillId
    ) {
        return skillService.unlockSkill(playerId, skillId);
    }
}