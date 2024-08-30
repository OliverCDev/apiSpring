package com.prueba.login.autenticacion.login.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.login.autenticacion.login.config.UserDetailsServiceImpl;
import com.prueba.login.autenticacion.login.model.User;
import com.prueba.login.autenticacion.login.model.request.LoginRequest;
import com.prueba.login.autenticacion.login.repository.UserRepository;
import com.prueba.login.autenticacion.login.security.JwtUtils;

import io.jsonwebtoken.lang.Arrays;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository; // Inyección del repositorio de usuarios

    @PostMapping("/login")
public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginRequest loginRequest) {
    // Autenticación por nombre de usuario y contraseña
    if (userDetailsService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
        String token = jwtUtils.generateJwtToken(loginRequest.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid username or password"));
}

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }

        if (newUser.getIsAdmin() == null) {
            newUser.setIsAdmin(false); // Valor predeterminado
        }
        
        // Hashear la contraseña antes de guardarla
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        
        // Guardar el nuevo usuario en la base de datos
        userRepository.save(newUser);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

}
