package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.UserRequest;
import br.com.trainingapi.workoutplanner.exception.ResourceNotFoundException;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.model.enums.AvailableDays;
import br.com.trainingapi.workoutplanner.model.enums.Goal;
import br.com.trainingapi.workoutplanner.model.enums.TrainingLevel;
import br.com.trainingapi.workoutplanner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldReturnUser() {
        Admin admin = new Admin();
        admin.setId(1L);

        UserRequest request = new UserRequest(
                "John Doe",
                80.0,
                1.80,
                TrainingLevel.BEGINNER,
                List.of("Knee"),
                List.of(AvailableDays.MONDAY, AvailableDays.WEDNESDAY),
                Goal.HYPERTROPHY,
                admin.getId()
        );

        when(adminService.getAdminById(1L)).thenReturn(admin);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.createUser(request);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(admin, user.getAdmin());
    }

    @Test
    void getUserById_shouldReturnUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_shouldThrowResourceNotFoundExceptionIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("The ID does not belong to any user.", exception.getMessage());
    }
}
