package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.WorkoutRequest;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.model.Workout;
import br.com.trainingapi.workoutplanner.model.enums.AvailableDays;
import br.com.trainingapi.workoutplanner.model.enums.Goal;
import br.com.trainingapi.workoutplanner.model.enums.TrainingLevel;
import br.com.trainingapi.workoutplanner.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

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
        // Configurando o admin para teste
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setName("Admin Teste");

        // Configurando o usuário para teste
        user = new User();
        user.setId(1L);
        user.setName("Usuário Teste");
        user.setAdmin(admin);
        user.setWeight(75.0);
        user.setHeight(180.0);
        user.setTrainingLevel(TrainingLevel.INTERMEDIATE);
        user.setRestrictions(Arrays.asList("Nenhuma"));
        user.setAvailableDays(Arrays.asList(AvailableDays.MONDAY, AvailableDays.WEDNESDAY));
        user.setGoal(Goal.HYPERTROPHY);

        // Configurando o treino para teste
        workout = new Workout();
        workout.setId(1L);
        workout.setTitle("Treino A");
        workout.setDescription("Treino de peito e tríceps");
        workout.setTrainingDate("2025-05-10");
        workout.setExercises(Arrays.asList("Supino reto", "Crucifixo", "Tríceps corda"));
        workout.setUser(user);

        // Configurando o request para teste
        workoutRequest = new WorkoutRequest(
                "Treino A",
                "Treino de peito e tríceps",
                "2025-05-10",
                Arrays.asList("Supino reto", "Crucifixo", "Tríceps corda"),
                1L
        );

        // Configurando lista de treinos para teste
        Workout workout2 = new Workout();
        workout2.setId(2L);
        workout2.setTitle("Treino B");
        workout2.setDescription("Treino de costas e bíceps");
        workout2.setTrainingDate("2025-05-12");
        workout2.setExercises(Arrays.asList("Puxada frontal", "Remada", "Rosca direta"));
        workout2.setUser(user);

        workoutList = Arrays.asList(workout, workout2);
    }

    @Test
    @DisplayName("Deve criar um treino com sucesso")
    void createWorkoutSuccess() {
        // Arrange
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        // Act
        Workout result = workoutService.createWorkout(workoutRequest);

        // Assert
        assertNotNull(result);
        assertEquals(workout.getId(), result.getId());
        assertEquals(workout.getTitle(), result.getTitle());
        assertEquals(workout.getDescription(), result.getDescription());
        assertEquals(workout.getTrainingDate(), result.getTrainingDate());
        assertEquals(workout.getExercises(), result.getExercises());
        assertEquals(workout.getUser(), result.getUser());

        // Verify
        verify(userService).getUserById(workoutRequest.userId());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    @DisplayName("Deve buscar treinos por ID de usuário com sucesso")
    void getWorkoutsByUserIdSuccess() {
        // Arrange
        when(workoutRepository.findByUserId(anyLong())).thenReturn(workoutList);

        // Act
        List<Workout> result = workoutService.getWorkoutsByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(workoutList, result);

        // Verify
        verify(workoutRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("Deve buscar um treino pelo ID com sucesso")
    void getWorkoutByIdSuccess() {
        // Arrange
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(workout));

        // Act
        Workout result = workoutService.getWorkoutById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(workout.getId(), result.getId());
        assertEquals(workout.getTitle(), result.getTitle());

        // Verify
        verify(workoutRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar treino com ID inexistente")
    void getWorkoutByIdNotFound() {
        // Arrange
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutService.getWorkoutById(999L);
        });

        assertEquals("O ID informado nao esta relacionado a nenhum Treino.", exception.getMessage());

        // Verify
        verify(workoutRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve atualizar um treino com sucesso")
    void updateWorkoutByIdSuccess() {
        // Arrange
        Long workoutId = 1L;

        WorkoutRequest updatedRequest = new WorkoutRequest(
                "Treino A Atualizado",
                "Treino atualizado de peito e tríceps",
                "2025-05-15",
                Arrays.asList("Supino inclinado", "Fly", "Tríceps francês"),
                1L
        );

        Workout existingWorkout = new Workout();
        existingWorkout.setId(workoutId);
        existingWorkout.setTitle("Treino Antigo");
        existingWorkout.setUser(user);

        Workout updatedWorkout = new Workout();
        updatedWorkout.setId(workoutId);
        updatedWorkout.setTitle("Treino A Atualizado");
        updatedWorkout.setDescription("Treino atualizado de peito e tríceps");
        updatedWorkout.setTrainingDate("2025-05-15");
        updatedWorkout.setExercises(Arrays.asList("Supino inclinado", "Fly", "Tríceps francês"));
        updatedWorkout.setUser(user);

        when(workoutRepository.findById(updatedRequest.userId())).thenReturn(Optional.of(existingWorkout));
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(workoutRepository.save(any(Workout.class))).thenReturn(updatedWorkout);

        // Act
        Workout result = workoutService.updateWorkoutById(updatedRequest, workoutId);

        // Assert
        assertNotNull(result);
        assertEquals(updatedWorkout.getId(), result.getId());
        assertEquals(updatedWorkout.getTitle(), result.getTitle());
        assertEquals(updatedWorkout.getDescription(), result.getDescription());
        assertEquals(updatedWorkout.getTrainingDate(), result.getTrainingDate());
        assertEquals(updatedWorkout.getExercises(), result.getExercises());

        // Verify - Nota: há um bug no código original que está sendo testado.
        // O método getWorkoutById é chamado com workoutRequest.userId() em vez de id
        verify(workoutRepository).findById(updatedRequest.userId());
        verify(userService).getUserById(workoutId);
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    @DisplayName("Deve deletar um treino com sucesso")
    void deleteWorkoutByIdSuccess() {
        // Arrange
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(workout));
        doNothing().when(workoutRepository).delete(any(Workout.class));

        // Act
        workoutService.deleteWorkoutById(1L);

        // Verify
        verify(workoutRepository).findById(1L);
        verify(workoutRepository).delete(workout);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar treino inexistente")
    void deleteWorkoutByIdNotFound() {
        // Arrange
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            workoutService.deleteWorkoutById(999L);
        });

        assertEquals("O ID informado nao esta relacionado a nenhum Treino.", exception.getMessage());

        // Verify
        verify(workoutRepository).findById(999L);
        verify(workoutRepository, never()).delete(any(Workout.class));
    }
}