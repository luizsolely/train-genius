package com.luizsolely.traingenius.dto;

import com.luizsolely.traingenius.model.enums.AvailableDays;
import com.luizsolely.traingenius.model.enums.Goal;
import com.luizsolely.traingenius.model.enums.TrainingLevel;
import jakarta.validation.constraints.*;

import java.util.List;

public record UserRequest(
        @NotBlank String name,
        @NotNull @Positive Double weight,
        @NotNull @Positive Double height,
        @NotNull TrainingLevel trainingLevel,
        @NotNull List<String> restrictions,
        @NotNull List<AvailableDays> availableDays,
        @NotNull Goal goal,
        @NotNull Long adminId
) {}
