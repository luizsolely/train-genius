package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.WorkoutRequest;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.model.Workout;
import br.com.trainingapi.workoutplanner.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    public WorkoutService(WorkoutRepository workoutRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
    }

    public Workout createWorkout(WorkoutRequest workoutRequest) {
        User user = userService.getUserById(workoutRequest.userId());

        Workout workout = new Workout();

        workout.setTitle(workoutRequest.title());
        workout.setDescription(workoutRequest.description());
        workout.setTrainingDate(workoutRequest.trainingDate());
        workout.setExercises(workoutRequest.exercises());
        workout.setUser(user);

        return workoutRepository.save(workout);

    }

    public List<Workout> getWorkoutsByUserId(Long userId) {
        return workoutRepository.findByUserId(userId);
    }

    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("O ID informado nao esta relacionado a nenhum Treino."));
    }

    public Workout updateWorkoutById(WorkoutRequest workoutRequest, Long id) {
        Workout workout = getWorkoutById(workoutRequest.userId());

        workout.setTitle(workoutRequest.title());
        workout.setDescription(workoutRequest.description());
        workout.setTrainingDate(workoutRequest.trainingDate());
        workout.setExercises(workoutRequest.exercises());
        workout.setUser(userService.getUserById(id));

        return workoutRepository.save(workout);

    }

    public void deleteWorkoutById(Long id) {
        Workout workout = getWorkoutById(id);
        workoutRepository.delete(workout);
    }

}
