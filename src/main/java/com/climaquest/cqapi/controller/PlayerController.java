package com.climaquest.cqapi.controller;

import com.climaquest.cqapi.dto.CreatePlayerRequest;
import com.climaquest.cqapi.dto.MissionResultResponse;
import com.climaquest.cqapi.dto.PlayerResponse;
import com.climaquest.cqapi.dto.SubmitMissionRequest;
import com.climaquest.cqapi.service.MissionService;
import com.climaquest.cqapi.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final MissionService missionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse createPlayer(@RequestBody @Valid CreatePlayerRequest request){
        return playerService.createPlayer(request);
    }

    @GetMapping("/{id}")
    public PlayerResponse getPLayer(@PathVariable UUID id){
        return playerService.getPlayer(id);
    }

    @PostMapping("{id}/daily-reward")
    public PlayerResponse claimDailyReward(@PathVariable UUID id){
        return playerService.claimDailyReward(id);
    }

}
