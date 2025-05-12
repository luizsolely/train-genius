package com.luizsolely.traingenius.service;

import com.luizsolely.traingenius.dto.WorkoutRequest;
import com.luizsolely.traingenius.dto.WorkoutResponse;
import com.luizsolely.traingenius.exception.ResourceNotFoundException;
import com.luizsolely.traingenius.mapper.WorkoutMapper;
import com.luizsolely.traingenius.model.User;
import com.luizsolely.traingenius.model.Workout;
import com.luizsolely.traingenius.repository.WorkoutRepository;
import com.luizsolely.traingenius.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserService userService;
    private final WorkoutMapper workoutMapper;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    public WorkoutService(
            WorkoutRepository workoutRepository,
            UserService userService,
            WorkoutMapper workoutMapper,
            JwtService jwtService,
            HttpServletRequest request) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
        this.workoutMapper = workoutMapper;
        this.jwtService = jwtService;
        this.request = request;
    }

    public WorkoutResponse createWorkout(WorkoutRequest workoutRequest) {
        User user = userService.getUserEntityById(workoutRequest.userId());

        if (!user.getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to create workouts for this user.");
        }

        Workout workout = workoutMapper.toEntity(workoutRequest);
        workout.setUser(user);

        return workoutMapper.toResponse(workoutRepository.save(workout));
    }

    public List<WorkoutResponse> getWorkoutsByUserId(Long userId) {
        User user = userService.getUserEntityById(userId);

        if (!user.getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to access workouts of this user.");
        }

        return workoutMapper.toResponseList(workoutRepository.findByUserId(userId));
    }

    public WorkoutResponse getWorkoutById(Long id) {
        Workout workout = getWorkoutEntityById(id);

        if (!workout.getUser().getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to access this workout.");
        }

        return workoutMapper.toResponse(workout);
    }

    public WorkoutResponse updateWorkoutById(WorkoutRequest workoutRequest, Long id) {
        Workout workout = getWorkoutEntityById(id);

        if (!workout.getUser().getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to update this workout.");
        }

        User user = userService.getUserEntityById(workoutRequest.userId());

        if (!user.getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to assign this workout to this user.");
        }

        workout.setTitle(workoutRequest.title());
        workout.setDescription(workoutRequest.description());
        workout.setTrainingDate(workoutRequest.trainingDate());
        workout.setExercises(workoutRequest.exercises());
        workout.setUser(user);

        return workoutMapper.toResponse(workoutRepository.save(workout));
    }

    public void deleteWorkoutById(Long id) {
        Workout workout = getWorkoutEntityById(id);

        if (!workout.getUser().getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to delete this workout.");
        }

        workoutRepository.delete(workout);
    }

    private Workout getWorkoutEntityById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + id));
    }

    private Long getAuthenticatedAdminId() {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7);
        return jwtService.extractAdminId(token);
    }
}
