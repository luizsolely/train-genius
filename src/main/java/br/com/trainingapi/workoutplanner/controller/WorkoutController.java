package br.com.trainingapi.workoutplanner.controller;

import br.com.trainingapi.workoutplanner.dto.WorkoutRequest;
import br.com.trainingapi.workoutplanner.model.Workout;
import br.com.trainingapi.workoutplanner.service.UserService;
import br.com.trainingapi.workoutplanner.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<Workout> createWorkout(@Valid @RequestBody WorkoutRequest workoutRequest) {
        Workout workout = workoutService.createWorkout(workoutRequest);
        return new ResponseEntity<>(workout, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Workout>> getWorkoutsByUserId(@PathVariable Long userId) {
        List<Workout> workouts = workoutService.getWorkoutsByUserId(userId);
        return new ResponseEntity<>(workouts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable Long id) {
        Workout workout = workoutService.getWorkoutById(id);
        return new ResponseEntity<>(workout, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkoutById(@Valid @RequestBody WorkoutRequest workoutRequest, @PathVariable Long id) {
        Workout updatedWorkout = workoutService.updateWorkoutById(workoutRequest, id);
        return new ResponseEntity<>(updatedWorkout, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutById(@PathVariable Long id) {
        workoutService.deleteWorkoutById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
