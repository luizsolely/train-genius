package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.UserRequest;
import br.com.trainingapi.workoutplanner.dto.UserResponse;
import br.com.trainingapi.workoutplanner.exception.ResourceNotFoundException;
import br.com.trainingapi.workoutplanner.mapper.UserMapper;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        return userMapper.toResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any user."));
        return userMapper.toResponse(user);
    }

    public UserResponse updateUserById(Long userId, UserRequest userRequest) {
        User user = getUserEntityById(userId);

        user.setName(userRequest.name());
        user.setWeight(userRequest.weight());
        user.setHeight(userRequest.height());
        user.setTrainingLevel(userRequest.trainingLevel());
        user.setRestrictions(userRequest.restrictions());
        user.setAvailableDays(userRequest.availableDays());
        user.setGoal(userRequest.goal());

        return userMapper.toResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsersByAdminId(Long adminId) {
        return userMapper.toResponseList(userRepository.findByAdminId(adminId));
    }

    public void deleteUserById(Long userId) {
        User user = getUserEntityById(userId);
        userRepository.delete(user);
    }

    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any user."));
    }
}
