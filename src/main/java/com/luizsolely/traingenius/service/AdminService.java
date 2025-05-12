package com.luizsolely.traingenius.service;

import com.luizsolely.traingenius.dto.AdminRequest;
import com.luizsolely.traingenius.dto.AdminResponse;
import com.luizsolely.traingenius.dto.UserResponse;
import com.luizsolely.traingenius.exception.EmailAlreadyInUseException;
import com.luizsolely.traingenius.exception.ResourceNotFoundException;
import com.luizsolely.traingenius.mapper.AdminMapper;
import com.luizsolely.traingenius.mapper.UserMapper;
import com.luizsolely.traingenius.model.Admin;
import com.luizsolely.traingenius.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    public AdminService(PasswordEncoder passwordEncoder, AdminRepository adminRepository, AdminMapper adminMapper, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
    }

    public AdminResponse createAdmin(AdminRequest adminRequest) {
        if(adminRepository.findAdminByEmail(adminRequest.email()).isPresent()) {
            throw new EmailAlreadyInUseException("E-mail already in use: " + adminRequest.email());
        }

        Admin admin = adminMapper.toEntity(adminRequest);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return adminMapper.toResponse(adminRepository.save(admin));
    }

    public AdminResponse getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any admin."));
        return adminMapper.toResponse(admin);
    }

    public List<UserResponse> getUsersByAdmin(Long adminId) {
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

