package br.com.trainingapi.workoutplanner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdminRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password
) {}
