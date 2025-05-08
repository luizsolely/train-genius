package br.com.trainingapi.workoutplanner.service;

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
    private AdminRepository adminRepository;

    @Mock
    private AdminMapper adminMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminService adminService;

    private AdminRequest adminRequest;
    private Admin admin;
    private AdminResponse adminResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminRequest = new AdminRequest("Admin Name", "admin@email.com", "password123");

        admin = new Admin();
        admin.setId(1L);
        admin.setName("Admin Name");
        admin.setEmail("admin@email.com");
        admin.setPassword("password123");

        adminResponse = new AdminResponse(1L, "Admin Name", "admin@email.com");
    }

    @Test
    void createAdmin_shouldReturnAdminResponse() {
        when(adminMapper.toEntity(adminRequest)).thenReturn(admin);
        when(adminRepository.findAdminByEmail(admin.getEmail())).thenReturn(Optional.empty());
        when(adminRepository.save(admin)).thenReturn(admin);
        when(adminMapper.toResponse(admin)).thenReturn(adminResponse);

        AdminResponse result = adminService.createAdmin(adminRequest);

        assertNotNull(result);
        assertEquals(adminResponse.email(), result.email());
        verify(adminRepository).save(admin);
    }

    @Test
    void createAdmin_shouldThrowExceptionWhenEmailExists() {
        when(adminMapper.toEntity(adminRequest)).thenReturn(admin);
        when(adminRepository.findAdminByEmail(admin.getEmail())).thenReturn(Optional.of(admin));

        assertThrows(EmailAlreadyInUseException.class, () -> adminService.createAdmin(adminRequest));

        verify(adminRepository, never()).save(any());
    }

    @Test
    void getAdminById_shouldReturnAdminResponse() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(adminMapper.toResponse(admin)).thenReturn(adminResponse);

        AdminResponse result = adminService.getAdminById(1L);

        assertNotNull(result);
        assertEquals(adminResponse.id(), result.id());
    }

    @Test
    void getAdminById_shouldThrowExceptionIfNotFound() {
        when(adminRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminById(99L));
    }

    @Test
    void getAdminByEmail_shouldReturnAdminResponse() {
        when(adminRepository.findAdminByEmail("admin@email.com")).thenReturn(Optional.of(admin));
        when(adminMapper.toResponse(admin)).thenReturn(adminResponse);

        AdminResponse result = adminService.getAdminByEmail("admin@email.com");

        assertNotNull(result);
        assertEquals("admin@email.com", result.email());
    }

    @Test
    void getAdminByEmail_shouldThrowExceptionIfNotFound() {
        when(adminRepository.findAdminByEmail("notfound@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminByEmail("notfound@email.com"));
    }

    @Test
    void getUsersByAdmin_shouldReturnUserResponseList() {
        User user = new User();
        user.setId(1L);
        user.setName("User Test");

        admin.setUsers(List.of(user));

        UserResponse userResponse = new UserResponse(
                1L, "User Test", null, null, null, null, null, null, null
        );

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userMapper.toResponseList(admin.getUsers())).thenReturn(List.of(userResponse));

        List<UserResponse> result = adminService.getUsersByAdmin(1L);

        assertEquals(1, result.size());
        assertEquals("User Test", result.get(0).name());
    }
}
