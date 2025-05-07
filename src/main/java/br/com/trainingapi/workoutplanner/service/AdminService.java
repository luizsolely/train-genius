package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.AdminRequest;
import br.com.trainingapi.workoutplanner.exception.EmailAlreadyInUseException;
import br.com.trainingapi.workoutplanner.exception.ResourceNotFoundException;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.model.User;
import br.com.trainingapi.workoutplanner.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin createAdmin(AdminRequest adminRequest) {
        if(adminRepository.findAdminByEmail(adminRequest.email()).isPresent()) {
            throw new EmailAlreadyInUseException("E-mail already in use: " + adminRequest.email());
        }

        Admin newAdmin = new Admin();

        newAdmin.setName(adminRequest.name());
        newAdmin.setEmail(adminRequest.email());
        newAdmin.setPassword(adminRequest.password());

        adminRepository.save(newAdmin);
        return newAdmin;
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The ID does not belong to any admin."));
    }

    public List<User> getUsersByAdmin(Long adminId) {
        return getAdminById(adminId).getUsers();
    }

    public Admin getAdminByEmail(String adminEmail) {
        return adminRepository.findAdminByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("The e-mail does not belong to any admin"));
    }

}
