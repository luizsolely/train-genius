package br.com.trainingapi.workoutplanner.dto;

import br.com.trainingapi.workoutplanner.model.enums.*;
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
