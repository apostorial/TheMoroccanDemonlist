package com.apostorial.tmdlbackend.controllers.open;

import com.apostorial.tmdlbackend.dtos.player.DataPlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.SearchPlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController @AllArgsConstructor @RequestMapping("/api/public/players")
public class PublicPlayerController {
    private final PlayerService playerService;

    @GetMapping("/{playerId}")
    public ResponseEntity<ProfilePlayerRequest> findById(@PathVariable("playerId") String playerId) {
        try {
            ProfilePlayerRequest player = playerService.findById(playerId);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ProfilePlayerRequest> findByUsername(@PathVariable("username") String username) {
        try {
            ProfilePlayerRequest player = playerService.findByUsername(username);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<ProfilePlayerRequest>> findAll() {
        try {
            List<ProfilePlayerRequest> players = playerService.findAll();
            if (players.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/classic-points")
    public ResponseEntity<List<DataPlayerRequest>> findAllByClassicPoints() {
        try {
            List<DataPlayerRequest> players = playerService.findAllByClassicPoints();
            if (players.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/platformer-points")
    public ResponseEntity<List<DataPlayerRequest>> findAllByPlatformerPoints() {
        try {
            List<DataPlayerRequest> players = playerService.findAllByPlatformerPoints();
            if (players.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<DataPlayerRequest>> findAllRequestsByRegionId(@PathVariable("regionId") String regionId) {
        try {
            List<DataPlayerRequest> players = playerService.findAllRequestsByRegionId(regionId);
            if (players.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/staff")
    public ResponseEntity<List<SearchPlayerRequest>> findAllStaff() {
        try {
            List<SearchPlayerRequest> players = playerService.findAllStaff();
            if (players.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{playerId}/avatar")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String playerId) {
        try {
            byte[] imageData = playerService.getAvatar(playerId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
