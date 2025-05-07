package br.com.trainingapi.workoutplanner.mapper;

import br.com.trainingapi.workoutplanner.dto.WorkoutRequest;
import br.com.trainingapi.workoutplanner.dto.WorkoutResponse;
import br.com.trainingapi.workoutplanner.model.Workout;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    Workout toEntity(WorkoutRequest request);

    WorkoutResponse toResponse(Workout workout);

    List<WorkoutResponse> toResponseList(List<Workout> workouts);
}
