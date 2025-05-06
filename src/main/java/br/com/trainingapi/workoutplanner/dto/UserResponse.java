package br.com.trainingapi.workoutplanner.dto;

import br.com.trainingapi.workoutplanner.model.enums.*;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        Double weight,
        Double height,
        TrainingLevel trainingLevel,
        List<String> restrictions,
        List<AvailableDays> availableDays,
        Goal goal,
        String workoutMessage
) {}
