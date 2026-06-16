package com.climaquest.cqapi.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "codename é obrigatório")
        String codename
) {
}
