package org.tfg.timetrackapi.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.security.dto.AuthResponse;
import org.tfg.timetrackapi.security.dto.LoginRequest;
import org.tfg.timetrackapi.security.service.CustomUserDetails;
import org.tfg.timetrackapi.security.service.JwtService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("¡Petición de login recibida!");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getDni(), request.getPassword())
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        String token = jwtService.generateToken(user.getDni(), user.getRole().name());

        System.out.println(user.getRole());

        return ResponseEntity.ok(new AuthResponse(token, user.getRole()));
    }
}



