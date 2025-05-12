package com.luizsolely.traingenius.service;

import com.luizsolely.traingenius.dto.UserRequest;
import com.luizsolely.traingenius.dto.UserResponse;
import com.luizsolely.traingenius.exception.ResourceNotFoundException;
import com.luizsolely.traingenius.mapper.UserMapper;
import com.luizsolely.traingenius.model.User;
import com.luizsolely.traingenius.model.Admin;
import com.luizsolely.traingenius.model.enums.AvailableDays;
import com.luizsolely.traingenius.model.enums.Goal;
import com.luizsolely.traingenius.model.enums.TrainingLevel;
import com.luizsolely.traingenius.repository.AdminRepository;
import com.luizsolely.traingenius.repository.UserRepository;
import com.luizsolely.traingenius.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private AdminRepository adminRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private JwtService jwtService;
    @Mock private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    private Admin admin;
    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    private final String fakeToken = "Bearer mock.jwt.token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Admin();
        admin.setId(1L);
        admin.setName("Admin Name");
        admin.setEmail("admin@example.com");

        userRequest = new UserRequest(
                "User Name", 70.0, 1.75,
                TrainingLevel.BEGINNER,
                List.of("Knee pain"),
                List.of(AvailableDays.MONDAY, AvailableDays.WEDNESDAY),
                Goal.HYPERTROPHY,
                admin.getId()
        );

        user = new User();
        user.setId(1L);
        user.setName("User Name");
        user.setWeight(70.0);
        user.setHeight(1.75);
        user.setTrainingLevel(TrainingLevel.BEGINNER);
        user.setRestrictions(List.of("Knee pain"));
        user.setAvailableDays(List.of(AvailableDays.MONDAY, AvailableDays.WEDNESDAY));
        user.setGoal(Goal.HYPERTROPHY);
        user.setAdmin(admin);

        userResponse = new UserResponse(
                1L, "User Name", 70.0, 1.75,
                TrainingLevel.BEGINNER,
                List.of("Knee pain"),
                List.of(AvailableDays.MONDAY, AvailableDays.WEDNESDAY),
                Goal.HYPERTROPHY,
                null
        );

        when(request.getHeader("Authorization")).thenReturn(fakeToken);
        when(jwtService.extractAdminId("mock.jwt.token")).thenReturn(admin.getId());
    }

    @Test
    void createUser_shouldReturnUserResponse() {
        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals("User Name", result.name());
        verify(adminRepository).findById(admin.getId());
        verify(userRepository).save(user);
    }

    @Test
    void getAllUsers_shouldReturnListOfUserResponse() {
        when(userRepository.findByAdminId(admin.getId())).thenReturn(List.of(user));
        when(userMapper.toResponseList(List.of(user))).thenReturn(List.of(userResponse));

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findByAdminId(admin.getId());
    }

    @Test
    void getUserById_shouldReturnUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getUserById_shouldThrowExceptionWhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void updateUserById_shouldReturnUpdatedUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateUserById(1L, userRequest);

        assertEquals("User Name", result.name());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUserById_shouldDeleteSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserById_shouldThrowExceptionIfNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(99L));
    }
}
