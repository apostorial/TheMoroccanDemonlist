package com.apostorial.tmdlbackend.controllers;

import com.apostorial.tmdlbackend.entities.Player;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @AllArgsConstructor @RequestMapping("/api")
public class SimpleController {

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff")
    public ResponseEntity<String> staff(@AuthenticationPrincipal Player player) {
        return new ResponseEntity<>("Staff", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<String> user(@AuthenticationPrincipal Player player) {
        return new ResponseEntity<>("User", HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<String> info(@AuthenticationPrincipal Player player) {
        String roleInfo = player.isStaff() ? "Staff" : "User";
        return new ResponseEntity<>("Hello " + player.getUsername() + ". You are a " + roleInfo, HttpStatus.OK);
    }
}
