package br.com.trainingapi.workoutplanner.controller;

import br.com.trainingapi.workoutplanner.dto.AdminRequest;
import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminRequest adminRequest) {
        Admin newAdmin = adminService.createAdmin(adminRequest);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Admin> getAdminByEmail(@PathVariable String email) {
        Admin admin = adminService.getAdminByEmail(email);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

}
