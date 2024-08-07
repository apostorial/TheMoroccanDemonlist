package com.apostorial.tmdlbackend.controllers;

import com.apostorial.tmdlbackend.dtos.JwtAuthResponseDTO;
import com.apostorial.tmdlbackend.dtos.LoginDTO;
import com.apostorial.tmdlbackend.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @AllArgsConstructor @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        jwtAuthResponseDTO.setAccessToken(token);
        return new ResponseEntity<>(jwtAuthResponseDTO, HttpStatus.OK);
    }
}
