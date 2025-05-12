package com.luizsolely.traingenius.service;

import com.luizsolely.traingenius.dto.WorkoutRequest;
import com.luizsolely.traingenius.dto.WorkoutResponse;
import com.luizsolely.traingenius.exception.ResourceNotFoundException;
import com.luizsolely.traingenius.mapper.WorkoutMapper;
import com.luizsolely.traingenius.model.Admin;
import com.luizsolely.traingenius.model.User;
import com.luizsolely.traingenius.model.Workout;
import com.luizsolely.traingenius.repository.WorkoutRepository;
import com.luizsolely.traingenius.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock private WorkoutRepository workoutRepository;
    @Mock private UserService userService;
    @Mock private WorkoutMapper workoutMapper;
    @Mock private JwtService jwtService;
    @Mock private HttpServletRequest request;

    @InjectMocks
    private WorkoutService workoutService;

    private WorkoutRequest workoutRequest;
    private Workout workout;
    private WorkoutResponse workoutResponse;
    private User user;
    private Admin admin;

    private final String fakeToken = "Bearer mock.jwt.token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@example.com");

        user = new User();
        user.setId(1L);
        user.setName("User Name");
        user.setAdmin(admin);

        workoutRequest = new WorkoutRequest(
                "Upper Body",
                "Focus on chest and triceps",
                "2025-05-10",
                List.of("Bench Press", "Triceps Dips"),
                1L
        );

        workout = new Workout();
        workout.setId(1L);
        workout.setTitle("Upper Body");
        workout.setDescription("Focus on chest and triceps");
        workout.setTrainingDate("2025-05-10");
        workout.setExercises(List.of("Bench Press", "Triceps Dips"));
        workout.setUser(user);

        workoutResponse = new WorkoutResponse(
                1L,
                "Upper Body",
                "Focus on chest and triceps",
                "2025-05-10",
                1L
        );

        when(request.getHeader("Authorization")).thenReturn(fakeToken);
        when(jwtService.extractAdminId("mock.jwt.token")).thenReturn(1L);
    }

    @Test
    void createWorkout_shouldReturnWorkoutResponse() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(workoutMapper.toEntity(workoutRequest)).thenReturn(workout);
        when(workoutRepository.save(workout)).thenReturn(workout);
        when(workoutMapper.toResponse(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.createWorkout(workoutRequest);

        assertNotNull(result);
        assertEquals("Upper Body", result.title());
        verify(workoutRepository).save(workout);
    }

    @Test
    void getWorkoutsByUserId_shouldReturnWorkoutList() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(workoutRepository.findByUserId(1L)).thenReturn(List.of(workout));
        when(workoutMapper.toResponseList(List.of(workout))).thenReturn(List.of(workoutResponse));

        List<WorkoutResponse> result = workoutService.getWorkoutsByUserId(1L);

        assertEquals(1, result.size());
        verify(workoutRepository).findByUserId(1L);
    }

    @Test
    void getWorkoutById_shouldReturnWorkoutResponse() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(workoutMapper.toResponse(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.getWorkoutById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(workoutRepository).findById(1L);
    }

    @Test
    void getWorkoutById_shouldThrowExceptionIfNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workoutService.getWorkoutById(99L));
    }

    @Test
    void updateWorkoutById_shouldReturnUpdatedWorkoutResponse() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(workoutRepository.save(workout)).thenReturn(workout);
        when(workoutMapper.toResponse(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.updateWorkoutById(workoutRequest, 1L);

        assertNotNull(result);
        assertEquals("Upper Body", result.title());
        verify(workoutRepository).save(workout);
    }

    @Test
    void deleteWorkoutById_shouldDeleteSuccessfully() {
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));

        workoutService.deleteWorkoutById(1L);

        verify(workoutRepository).delete(workout);
    }

    @Test
    void deleteWorkoutById_shouldThrowExceptionIfNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workoutService.deleteWorkoutById(99L));
    }
}
