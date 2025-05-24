package com.luizsolely.traingenius.controller;

import com.luizsolely.traingenius.dto.WorkoutRequest;
import com.luizsolely.traingenius.dto.WorkoutResponse;
import com.luizsolely.traingenius.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse generateWorkout(@PathVariable Long userId) {
        return workoutService.generateWorkout(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse createWorkout(
            @PathVariable Long userId,
            @RequestBody WorkoutRequest request
    ) {
        return workoutService.createWorkout(userId, request);
    }

    @GetMapping
    public List<WorkoutResponse> getAllWorkouts(@PathVariable Long userId) {
        return workoutService.getAllWorkoutsByUserId(userId);
    }

    @GetMapping("/{workoutId}")
    public WorkoutResponse getWorkoutById(
            @PathVariable Long userId,
            @PathVariable Long workoutId
    ) {
        return workoutService.getWorkoutById(workoutId);
    }

    @DeleteMapping("/{workoutId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkout(
            @PathVariable Long userId,
            @PathVariable Long workoutId
    ) {
        workoutService.deleteWorkout(workoutId);
    }
}
