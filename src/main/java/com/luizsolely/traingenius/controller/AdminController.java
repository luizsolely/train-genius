package com.luizsolely.traingenius.controller;

import com.luizsolely.traingenius.dto.AdminRequest;
import com.luizsolely.traingenius.dto.AdminResponse;
import com.luizsolely.traingenius.service.AdminService;
import jakarta.validation.Valid;
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
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest adminRequest) {
        AdminResponse newAdmin = adminService.createAdmin(adminRequest);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
        AdminResponse admin = adminService.getAdminById(id);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @GetMapping("/by-email")
    public ResponseEntity<AdminResponse> getAdminByEmail(@RequestParam String email) {
        AdminResponse admin = adminService.getAdminByEmail(email);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

}
