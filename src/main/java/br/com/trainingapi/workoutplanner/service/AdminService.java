package br.com.trainingapi.workoutplanner.service;

import br.com.trainingapi.workoutplanner.dto.AdminRequest;
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
            throw new IllegalArgumentException("E-mail informado em uso.");
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
                .orElseThrow(() -> new RuntimeException("O ID informado não está relacionado a nenhum Admin."));
    }

    public List<User> getUsersByAdmin(Long adminId) {
        return getAdminById(adminId).getUsers();
    }

    public Admin getAdminByEmail(String adminEmail) {
        return adminRepository.findAdminByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("O E-mail informado não está relacionado a nenhum Admin."));
    }

}
