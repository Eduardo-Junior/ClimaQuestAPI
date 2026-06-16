package com.climaquest.cqapi.controller;
import com.climaquest.cqapi.dto.PlayerResponse;
import com.climaquest.cqapi.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final PlayerService playerService;

    @GetMapping
    public List<PlayerResponse> getRanking(
            @RequestParam(defaultValue = "10") int limit
    ){
        return playerService.getRanking(Math.min(limit, 50));
    }
}