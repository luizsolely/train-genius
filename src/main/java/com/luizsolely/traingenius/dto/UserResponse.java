package com.luizsolely.traingenius.dto;

import com.luizsolely.traingenius.model.enums.AvailableDays;
import com.luizsolely.traingenius.model.enums.Goal;
import com.luizsolely.traingenius.model.enums.TrainingLevel;

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

)

{}
