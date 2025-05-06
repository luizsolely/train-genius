package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.UserRequest;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AdminService adminService;

    public UserService(UserRepository userRepository, AdminService adminService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    public User createUser(UserRequest userRequest, Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        User newUser = new User();

        newUser.setName(userRequest.name());
        newUser.setAdmin(admin);
        newUser.setWeight(userRequest.weight());
        newUser.setHeight(userRequest.height());
        newUser.setTrainingLevel(userRequest.trainingLevel());
        newUser.setRestrictions(userRequest.restrictions());
        newUser.setAvailableDays(userRequest.availableDays());
        newUser.setGoal(userRequest.goal());

        userRepository.save(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("O ID informado não está relacionado a nenhum Aluno."));
    }

    public User updateUser(Long userId, UserRequest userRequest) {
        User updatedUser = getUserById(userId);

        updatedUser.setName(userRequest.name());
        updatedUser.setWeight(userRequest.weight());
        updatedUser.setHeight(userRequest.height());
        updatedUser.setTrainingLevel(userRequest.trainingLevel());
        updatedUser.setRestrictions(userRequest.restrictions());
        updatedUser.setAvailableDays(userRequest.availableDays());
        updatedUser.setGoal(userRequest.goal());

        userRepository.save(updatedUser);
        return updatedUser;

    }

    public List<User> getUsersByAdminId(Long adminId) {
        return userRepository.findByAdminId(adminId);
    }

    public void deleteUser(Long userId) {
        User deletedUser = getUserById(userId);
        userRepository.delete(deletedUser);
    }

}
