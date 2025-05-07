package br.com.trainingapi.workoutplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record WorkoutRequest(

        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotBlank
        String trainingDate,

        @NotNull
        @Size(min = 1)
        List<String> exercises,

        @NotNull
        Long userId

)
{}
