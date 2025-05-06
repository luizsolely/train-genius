package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.AdminRequest;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAdmin_shouldReturnAdmin() {
        AdminRequest request = new AdminRequest("Jane Doe", "jane@example.com", "testpasswd");

        when(adminRepository.save(any(Admin.class))).thenAnswer(i -> i.getArgument(0));

        Admin admin = adminService.createAdmin(request);

        assertNotNull(admin);
        assertEquals("Jane Doe", admin.getName());
        assertEquals("jane@example.com", admin.getEmail());
    }

    @Test
    void getAdminById_shouldReturnAdmin() {
        Admin admin = new Admin();
        admin.setId(1L);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Admin result = adminService.getAdminById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getAdminById_shouldThrowExceptionIfNotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.getAdminById(1L);
        });

        assertEquals("O ID informado não está relacionado a nenhum Admin.", exception.getMessage());
    }
}
