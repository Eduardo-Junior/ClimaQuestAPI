package com.climaquest.cqapi.controller;

import com.climaquest.cqapi.dto.AuthResponse;
import com.climaquest.cqapi.dto.LoginRequest;
import com.climaquest.cqapi.repository.PlayerRepository;
import com.climaquest.cqapi.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PlayerRepository playerRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        var player = playerRepository.findByCodename(request.codename())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));

        var token = jwtService.generateToken(player.getId(), player.getCodename());
        return new AuthResponse(token, player.getId(), player.getCodename());
    }
}
