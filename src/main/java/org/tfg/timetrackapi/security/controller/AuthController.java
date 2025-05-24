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

        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getDni(), request.getPassword())
        );

        // Se asocia el usuario autenticado a User para obtener todos sus atributos
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        // Se genera el token con el dni, nombre y rol
        String token = jwtService.generateToken(user.getDni(), user.getRole().name());

        // Se devuelve la respuesta en un código 200 OK
        // La respuesta incluye el token y el rol del usuario
        return ResponseEntity.ok(new AuthResponse(token, user.getRole()));
    }
}



