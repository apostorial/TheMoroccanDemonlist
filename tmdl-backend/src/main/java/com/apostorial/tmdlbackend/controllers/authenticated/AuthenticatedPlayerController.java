package com.apostorial.tmdlbackend.controllers.authenticated;

import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.UpdatePlayerRequest;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.services.interfaces.PlayerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PutMapping("/region/{regionId}")
    public ResponseEntity<Void> addRegion(@PathVariable String regionId) {
        try {
            playerService.addRegion(regionId);
            return new ResponseEntity<>(HttpStatus.OK);
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

    @PostMapping(value = "/upload/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfilePlayerRequest> uploadAvatar(
            @Parameter(
                    description = "File to upload",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam("file") MultipartFile file
    ) {
        try {
            ProfilePlayerRequest player = playerService.uploadAvatar(file);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
