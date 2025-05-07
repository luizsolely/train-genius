package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.WorkoutRequest;
import br.com.trainingapi.workoutplanner.exception.ResourceNotFoundException;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.model.Workout;
import br.com.trainingapi.workoutplanner.model.enums.AvailableDays;
import br.com.trainingapi.workoutplanner.model.enums.Goal;
import br.com.trainingapi.workoutplanner.model.enums.TrainingLevel;
import br.com.trainingapi.workoutplanner.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkoutService workoutService;

    private User user;
    private Workout workout;
    private WorkoutRequest workoutRequest;
    private List<Workout> workoutList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Test Admin");

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setAdmin(admin);
        user.setWeight(75.0);
        user.setHeight(180.0);
        user.setTrainingLevel(TrainingLevel.INTERMEDIATE);
        user.setRestrictions(List.of("None"));
        user.setAvailableDays(List.of(AvailableDays.MONDAY, AvailableDays.WEDNESDAY));
        user.setGoal(Goal.HYPERTROPHY);

        workout = new Workout();
        workout.setId(1L);
        workout.setTitle("Workout A");
        workout.setDescription("Chest and triceps workout");
        workout.setTrainingDate("2025-05-10");
        workout.setExercises(Arrays.asList("Bench press", "Cable fly", "Triceps rope"));
        workout.setUser(user);

        workoutRequest = new WorkoutRequest(
                "Workout A",
                "Chest and triceps workout",
                "2025-05-10",
                Arrays.asList("Bench press", "Cable fly", "Triceps rope"),
                1L
        );

        Workout workout2 = new Workout();
        workout2.setId(2L);
        workout2.setTitle("Workout B");
        workout2.setDescription("Back and biceps workout");
        workout2.setTrainingDate("2025-05-12");
        workout2.setExercises(Arrays.asList("Lat pulldown", "Bent-over row", "Barbell curl"));
        workout2.setUser(user);

        workoutList = Arrays.asList(workout, workout2);
    }

    @Test
    void createWorkout_shouldReturnWorkout() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        Workout result = workoutService.createWorkout(workoutRequest);

        assertNotNull(result);
        assertEquals(workout.getId(), result.getId());
        assertEquals(workout.getTitle(), result.getTitle());
        assertEquals(workout.getDescription(), result.getDescription());
        assertEquals(workout.getTrainingDate(), result.getTrainingDate());
        assertEquals(workout.getExercises(), result.getExercises());
        assertEquals(workout.getUser(), result.getUser());

        verify(userService).getUserById(workoutRequest.userId());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void getWorkoutsByUserId_shouldReturnWorkoutList() {
        when(workoutRepository.findByUserId(anyLong())).thenReturn(workoutList);

        List<Workout> result = workoutService.getWorkoutsByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(workoutList, result);

        verify(workoutRepository).findByUserId(1L);
    }

    @Test
    void getWorkoutById_shouldReturnWorkout() {
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(workout));

        Workout result = workoutService.getWorkoutById(1L);

        assertNotNull(result);
        assertEquals(workout.getId(), result.getId());
        assertEquals(workout.getTitle(), result.getTitle());

        verify(workoutRepository).findById(1L);
    }

    @Test
    void getWorkoutById_shouldThrowResourceNotFoundExceptionIfNotFound() {
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            workoutService.getWorkoutById(999L);
        });

        assertEquals("The ID does not belong to any workout.", exception.getMessage());

        verify(workoutRepository).findById(999L);
    }

    @Test
    void updateWorkoutById_shouldReturnUpdatedWorkout() {
        Long workoutId = 1L;

        WorkoutRequest updatedRequest = new WorkoutRequest(
                "Updated Workout A",
                "Updated chest and triceps workout",
                "2025-05-15",
                Arrays.asList("Incline bench press", "Dumbbell fly", "French press"),
                1L
        );

        Workout existingWorkout = new Workout();
        existingWorkout.setId(workoutId);
        existingWorkout.setTitle("Old Workout");
        existingWorkout.setUser(user);

        Workout updatedWorkout = new Workout();
        updatedWorkout.setId(workoutId);
        updatedWorkout.setTitle("Updated Workout A");
        updatedWorkout.setDescription("Updated chest and triceps workout");
        updatedWorkout.setTrainingDate("2025-05-15");
        updatedWorkout.setExercises(Arrays.asList("Incline bench press", "Dumbbell fly", "French press"));
        updatedWorkout.setUser(user);

        when(workoutRepository.findById(updatedRequest.userId())).thenReturn(Optional.of(existingWorkout));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(workoutRepository.save(any(Workout.class))).thenReturn(updatedWorkout);

        Workout result = workoutService.updateWorkoutById(updatedRequest, workoutId);

        assertNotNull(result);
        assertEquals(updatedWorkout.getId(), result.getId());
        assertEquals(updatedWorkout.getTitle(), result.getTitle());
        assertEquals(updatedWorkout.getDescription(), result.getDescription());
        assertEquals(updatedWorkout.getTrainingDate(), result.getTrainingDate());
        assertEquals(updatedWorkout.getExercises(), result.getExercises());

        verify(workoutRepository).findById(updatedRequest.userId());
        verify(userService).getUserById(workoutId);
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    void deleteWorkoutById_shouldDeleteWorkout() {
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(workout));
        doNothing().when(workoutRepository).delete(any(Workout.class));

        workoutService.deleteWorkoutById(1L);

        verify(workoutRepository).findById(1L);
        verify(workoutRepository).delete(workout);
    }

    @Test
    void deleteWorkoutById_shouldThrowResourceNotFoundExceptionIfNotFound() {
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            workoutService.deleteWorkoutById(999L);
        });

        assertEquals("The ID does not belong to any workout.", exception.getMessage());

        verify(workoutRepository).findById(999L);
        verify(workoutRepository, never()).delete(any(Workout.class));
    }
}