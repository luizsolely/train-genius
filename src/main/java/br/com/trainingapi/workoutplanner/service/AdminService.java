package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.AdminRequest;
import br.com.trainingapi.workoutplanner.dto.AdminResponse;
import br.com.trainingapi.workoutplanner.dto.UserResponse;
import br.com.trainingapi.workoutplanner.exception.EmailAlreadyInUseException;
import br.com.trainingapi.workoutplanner.exception.ResourceNotFoundException;
import br.com.trainingapi.workoutplanner.mapper.AdminMapper;
import br.com.trainingapi.workoutplanner.mapper.UserMapper;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    public AdminService(AdminRepository adminRepository, AdminMapper adminMapper, UserMapper userMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
    }

    public AdminResponse createAdmin(AdminRequest adminRequest) {
        Admin admin = adminMapper.toEntity(adminRequest);
        if(adminRepository.findAdminByEmail(admin.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("E-mail already in use: " + admin.getEmail());
        }

        return adminMapper.toResponse(adminRepository.save(admin));
    }

    public AdminResponse getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any admin."));
        return adminMapper.toResponse(admin);
    }

    public List<UserResponse> getUsersByAdmin(Long adminId, UserMapper userMapper) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any admin."));
        return userMapper.toResponseList(admin.getUsers());
    }

    public AdminResponse getAdminByEmail(String email) {
        Admin admin = adminRepository.findAdminByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The e-mail does not belong to any admin."));
        return adminMapper.toResponse(admin);
    }
}

