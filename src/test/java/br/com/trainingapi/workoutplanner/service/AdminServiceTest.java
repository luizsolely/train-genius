package br.com.trainingapi.workoutplanner.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.trainingapi.workoutplanner.dto.AdminRequest;
import br.com.trainingapi.workoutplanner.dto.AdminResponse;
import br.com.trainingapi.workoutplanner.dto.UserResponse;
import br.com.trainingapi.workoutplanner.exception.EmailAlreadyInUseException;
import br.com.trainingapi.workoutplanner.exception.ResourceNotFoundException;
import br.com.trainingapi.workoutplanner.mapper.AdminMapper;
import br.com.trainingapi.workoutplanner.mapper.UserMapper;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AdminMapper adminMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminService adminService;

    private final Long adminId = 1L;
    private final String adminEmail = "admin@email.com";

    private AdminRequest adminRequest;
    private Admin admin;
    private AdminResponse adminResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminRequest = new AdminRequest("Admin Name", adminEmail, "password123");

        admin = new Admin();
        admin.setId(adminId);
        admin.setName("Admin Name");
        admin.setEmail(adminEmail);
        admin.setPassword("encodedPassword123");


        adminResponse = new AdminResponse(adminId, "Admin Name", adminEmail);
    }

    @Test
    void createAdmin_whenEmailIsNew_shouldReturnAdminResponse() {
        when(adminMapper.toEntity(adminRequest)).thenReturn(admin);
        when(adminRepository.findAdminByEmail(adminEmail)).thenReturn(Optional.empty());
        when(adminRepository.save(admin)).thenReturn(admin);
        when(adminMapper.toResponse(admin)).thenReturn(adminResponse);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        AdminResponse result = adminService.createAdmin(adminRequest);

        assertNotNull(result);
        assertEquals(adminEmail, result.email());
        verify(adminRepository).save(admin);
    }

    @Test
    void createAdmin_whenEmailExists_shouldThrowException() {
        when(adminMapper.toEntity(adminRequest)).thenReturn(admin);
        when(adminRepository.findAdminByEmail(adminEmail)).thenReturn(Optional.of(admin));

        assertThrows(EmailAlreadyInUseException.class, () -> adminService.createAdmin(adminRequest));
        verify(adminRepository, never()).save(any());
    }

    @Test
    void getAdminById_whenExists_shouldReturnAdminResponse() {
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(adminMapper.toResponse(admin)).thenReturn(adminResponse);

        AdminResponse result = adminService.getAdminById(adminId);

        assertNotNull(result);
        assertEquals(adminId, result.id());
    }

    @Test
    void getAdminById_whenNotFound_shouldThrowException() {
        when(adminRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminById(99L));
    }

    @Test
    void getAdminByEmail_whenExists_shouldReturnAdminResponse() {
        when(adminRepository.findAdminByEmail(adminEmail)).thenReturn(Optional.of(admin));
        when(adminMapper.toResponse(admin)).thenReturn(adminResponse);

        AdminResponse result = adminService.getAdminByEmail(adminEmail);

        assertNotNull(result);
        assertEquals(adminEmail, result.email());
    }

    @Test
    void getAdminByEmail_whenNotFound_shouldThrowException() {
        when(adminRepository.findAdminByEmail("notfound@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminByEmail("notfound@email.com"));
    }

    @Test
    void getUsersByAdmin_shouldReturnMappedUserList() {
        User user = new User();
        user.setId(10L);
        user.setName("User Test");

        admin.setUsers(List.of(user));

        UserResponse userResponse = new UserResponse(
                10L, "User Test", null, null, null, null, null, null, null
        );

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(userMapper.toResponseList(admin.getUsers())).thenReturn(List.of(userResponse));

        List<UserResponse> result = adminService.getUsersByAdmin(adminId);

        assertEquals(1, result.size());
        assertEquals("User Test", result.get(0).name());
    }
}
