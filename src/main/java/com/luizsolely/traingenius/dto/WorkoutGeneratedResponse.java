package com.luizsolely.traingenius.dto;

import java.util.List;

public record WorkoutGeneratedResponse(
        String title,
        String description,
        String trainingDate,
        List<String> exercises
) {}