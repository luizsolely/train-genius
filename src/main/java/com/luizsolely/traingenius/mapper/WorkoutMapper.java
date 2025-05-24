package com.luizsolely.traingenius.mapper;

import com.luizsolely.traingenius.dto.WorkoutGeneratedResponse;
import com.luizsolely.traingenius.dto.WorkoutRequest;
import com.luizsolely.traingenius.dto.WorkoutResponse;
import com.luizsolely.traingenius.model.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    Workout toEntity(WorkoutRequest request);

    WorkoutResponse toResponse(Workout workout);

    List<WorkoutResponse> toResponseList(List<Workout> workouts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Workout toEntity(WorkoutGeneratedResponse generated);

}
