package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.config.JwtTokenProvider;
import com.apostorial.tmdlbackend.dtos.LoginDTO;
import com.apostorial.tmdlbackend.dtos.RegisterDTO;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.VerificationToken;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.VerificationTokenRepository;
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
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationTokenRepository tokenRepository;
    private final EmailServiceImpl emailService;

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override @Transactional
    public String register(RegisterDTO registerDTO) {
        if (playerRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (playerRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        Player player = new Player();
        player.setUsername(registerDTO.getUsername());
        player.setEmail(registerDTO.getEmail());
        player.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        playerRepository.save(player);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, player);
        tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(player.getEmail(), token);

        Authentication authentication = new UsernamePasswordAuthenticationToken(player.getUsername(), registerDTO.getPassword());
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
