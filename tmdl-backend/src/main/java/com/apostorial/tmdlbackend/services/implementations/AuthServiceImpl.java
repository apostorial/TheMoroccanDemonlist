package com.apostorial.tmdlbackend.services.implementations;

import com.apostorial.tmdlbackend.config.JwtTokenProvider;
import com.apostorial.tmdlbackend.dtos.player.LoginPlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.RegisterPlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.Region;
import com.apostorial.tmdlbackend.entities.VerificationToken;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.RegionRepository;
import com.apostorial.tmdlbackend.repositories.VerificationTokenRepository;
import com.apostorial.tmdlbackend.services.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service @AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationTokenRepository tokenRepository;
    private final EmailServiceImpl emailService;
    private final RegionRepository regionRepository;

    @Override
    public String login(LoginPlayerRequest loginPlayerRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginPlayerRequest.getUsername(), loginPlayerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override @Transactional
    public String register(RegisterPlayerRequest request) throws EntityNotFoundException {
        if (playerRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (playerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        Player player = new Player();
        player.setUsername(request.getUsername());
        player.setEmail(request.getEmail());
        player.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRegionId() != null) {
            Region region = regionRepository.findById(request.getRegionId())
                    .orElseThrow(() -> new EntityNotFoundException("Region with id " + request.getRegionId() + " not found"));
            player.setRegion(region);
        }
        playerRepository.save(player);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, player);
        tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(player.getEmail(), token);

        Authentication authentication = new UsernamePasswordAuthenticationToken(player.getUsername(), request.getPassword());
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override @Transactional
    public boolean verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return false;
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            return false;
        }

        Player player = verificationToken.getPlayer();
        player.setEmailVerified(true);
        playerRepository.save(player);
        tokenRepository.delete(verificationToken);

        return true;
    }
}
