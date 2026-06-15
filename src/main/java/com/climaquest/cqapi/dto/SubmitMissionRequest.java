package com.climaquest.cqapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SubmitMissionRequest(
        @NotNull @Min(0)
        Integer correctAnswers,

        @NotNull @Min(0)
        Integer wrongAnswers,

        @NotNull @Min(0)
        Integer gainedXp,

        @NotNull
        Boolean completed
) {}
