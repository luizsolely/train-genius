package br.com.trainingapi.workoutplanner.auth;

import br.com.trainingapi.workoutplanner.model.Admin;
import br.com.trainingapi.workoutplanner.repository.AdminRepository;
import br.com.trainingapi.workoutplanner.security.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationManager authenticationManager, AdminRepository adminRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();

            Admin admin = adminRepository.findAdminByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

            String jwtToken = jwtService.generateToken(admin);


            return ResponseEntity.ok(new LoginResponse(jwtToken));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
