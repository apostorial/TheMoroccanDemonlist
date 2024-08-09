package com.apostorial.tmdlbackend.controllers;

import com.apostorial.tmdlbackend.dtos.JwtAuthResponseRequest;
import com.apostorial.tmdlbackend.dtos.player.LoginPlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.RegisterPlayerRequest;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterPlayerRequest request) {
       try {
           String token = authService.register(request);
           return new ResponseEntity<>(token, HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseRequest> login(@RequestBody LoginPlayerRequest request) {
        String token = authService.login(request);
        JwtAuthResponseRequest jwtAuthResponseRequest = new JwtAuthResponseRequest();
        jwtAuthResponseRequest.setAccessToken(token);
        return new ResponseEntity<>(jwtAuthResponseRequest, HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean verified = authService.verifyEmail(token);
        if (verified) {
            return new ResponseEntity<>("Email verified successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid or expired token.", HttpStatus.BAD_REQUEST);
        }
    }
}
