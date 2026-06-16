package com.climaquest.cqapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//
public record CreatePlayerRequest(
        @NotBlank(message = "Codename é obrigatório")
        @Size(min = 3, max = 50)
        String codename,

        @NotBlank
        String scientistType,

        @Min(1) @Max(12)
        Integer avatarIndex
) {}
