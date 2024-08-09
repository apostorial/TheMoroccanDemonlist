package com.apostorial.tmdlbackend.config;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component @AllArgsConstructor
public class SecurityUtils {
    private final PlayerRepository playerRepository;

    public Player getAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return playerRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Player with username " + username + " not found"));
        }
        throw new UnauthorizedException("No authenticated user found");
    }
}