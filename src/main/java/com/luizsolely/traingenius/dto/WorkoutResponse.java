package com.luizsolely.traingenius.dto;

public record WorkoutResponse(

        Long id,
        String title,
        String description,
        String trainingDate,
        Long userId
)

{}
