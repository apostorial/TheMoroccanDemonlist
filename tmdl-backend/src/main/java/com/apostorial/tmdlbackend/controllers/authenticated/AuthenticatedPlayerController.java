package com.apostorial.tmdlbackend.controllers.authenticated;

import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.UpdatePlayerRequest;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.services.interfaces.PlayerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor @PreAuthorize("isAuthenticated()") @RequestMapping("/api/authenticated/players") @SecurityRequirement(name = "bearer-jwt")
public class AuthenticatedPlayerController {
    private final PlayerService playerService;

    @GetMapping("/profile")
    public ResponseEntity<ProfilePlayerRequest> getAuthenticatedPlayer() {
        try {
            ProfilePlayerRequest player = playerService.getAuthenticatedPlayer();
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/profile/update")
    public ResponseEntity<UpdatePlayerRequest> updateAuthenticatedPlayer(@RequestBody UpdatePlayerRequest request) {
        try {
            UpdatePlayerRequest player = playerService.updateAuthenticatedPlayer(request);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
