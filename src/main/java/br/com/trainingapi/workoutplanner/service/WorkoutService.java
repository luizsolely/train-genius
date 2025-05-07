package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.WorkoutResponse;
import br.com.trainingapi.workoutplanner.mapper.WorkoutMapper;
import br.com.trainingapi.workoutplanner.dto.WorkoutRequest;
import br.com.trainingapi.workoutplanner.exception.ResourceNotFoundException;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.model.Workout;
import br.com.trainingapi.workoutplanner.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserService userService;
    private final WorkoutMapper workoutMapper;

    public WorkoutService(WorkoutRepository workoutRepository, UserService userService, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
        this.workoutMapper = workoutMapper;
    }

    public WorkoutResponse createWorkout(WorkoutRequest workoutRequest) {
        User user = userService.getUserEntityById(workoutRequest.userId());

        Workout workout = workoutMapper.toEntity(workoutRequest);
        workout.setUser(user);

        return workoutMapper.toResponse(workoutRepository.save(workout));
    }

    public List<WorkoutResponse> getWorkoutsByUserId(Long userId) {
        return workoutMapper.toResponseList(workoutRepository.findByUserId(userId));
    }

    public WorkoutResponse getWorkoutById(Long id) {
        return workoutMapper.toResponse(getWorkoutEntityById(id));
    }

    public WorkoutResponse updateWorkoutById(WorkoutRequest workoutRequest, Long id) {
        Workout workout = getWorkoutEntityById(id);

        workout.setTitle(workoutRequest.title());
        workout.setDescription(workoutRequest.description());
        workout.setTrainingDate(workoutRequest.trainingDate());
        workout.setExercises(workoutRequest.exercises());

        workout.setUser(userService.getUserEntityById(workoutRequest.userId()));

        return workoutMapper.toResponse(workoutRepository.save(workout));
    }

    public void deleteWorkoutById(Long id) {
        Workout workout = getWorkoutEntityById(id);
        workoutRepository.delete(workout);
    }

    private Workout getWorkoutEntityById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any workout."));
    }
}

