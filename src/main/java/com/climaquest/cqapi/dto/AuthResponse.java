package com.climaquest.cqapi.dto;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID playerId,
        String codename
) {
}
