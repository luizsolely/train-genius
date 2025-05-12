package com.luizsolely.traingenius.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import com.luizsolely.traingenius.security.jwt.JwtService;
import com.luizsolely.traingenius.dto.UserRequest;
import com.luizsolely.traingenius.dto.UserResponse;
import com.luizsolely.traingenius.exception.ResourceNotFoundException;
import com.luizsolely.traingenius.mapper.UserMapper;
import com.luizsolely.traingenius.model.Admin;
import com.luizsolely.traingenius.model.User;
import com.luizsolely.traingenius.repository.AdminRepository;
import com.luizsolely.traingenius.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HttpServletRequest request;
    private final JwtService jwtService;


    public UserService(
            AdminRepository adminRepository,
            UserRepository userRepository,
            UserMapper userMapper,
            HttpServletRequest request,
            JwtService jwtService
    ) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.request = request;
        this.jwtService = jwtService;
    }

    public UserResponse createUser(UserRequest userRequest) {
        Admin admin = adminRepository.findById(userRequest.adminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + userRequest.adminId()));

        User user = userMapper.toEntity(userRequest);
        user.setAdmin(admin);

        return userMapper.toResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        Long adminId = getAuthenticatedAdminId();
        List<User> users = userRepository.findByAdminId(adminId);

        return userMapper.toResponseList(users);
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to access this user.");
        }

        return userMapper.toResponse(user);
    }

    public UserResponse updateUserById(Long userId, UserRequest userRequest) {
        User user = getUserEntityById(userId);

        if (!user.getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to access this user.");
        }

        user.setName(userRequest.name());
        user.setWeight(userRequest.weight());
        user.setHeight(userRequest.height());
        user.setTrainingLevel(userRequest.trainingLevel());
        user.setRestrictions(userRequest.restrictions());
        user.setAvailableDays(userRequest.availableDays());
        user.setGoal(userRequest.goal());

        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUserById(Long userId) {
        User user = getUserEntityById(userId);

        if (!user.getAdmin().getId().equals(getAuthenticatedAdminId())) {
            throw new AccessDeniedException("You are not allowed to access this user.");
        }

        userRepository.delete(user);
    }

    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any user."));
    }

    private Long getAuthenticatedAdminId() {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7); // Remove "Bearer "
        return jwtService.extractAdminId(token);
    }

}
