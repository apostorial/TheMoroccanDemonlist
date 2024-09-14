package com.apostorial.tmdlbackend.config;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.services.implementations.PlayerDetailsServiceImpl;
import com.apostorial.tmdlbackend.utilities.UsernameGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component @RequiredArgsConstructor @Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${redirect.url}")
    private String redirectURL;
    private final JwtTokenProvider tokenProvider;
    private final PlayerRepository playerRepository;
    private final PlayerDetailsServiceImpl playerDetailsService;
    private final UsernameGenerator usernameGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        playerRepository.findByEmail(email)
                .orElseGet(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setEmail(email);
                    newPlayer.setUsername(usernameGenerator.generateUsername());
                    return playerRepository.save(newPlayer);
                });

        UserDetails userDetails = playerDetailsService.loadUserByUsername(email);
        String token = tokenProvider.generateToken(email, userDetails.getAuthorities());
        log.error(token);

        String url = determineTargetUrl(request, response, authentication);
        String targetUrl = url + "?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return redirectURL;
    }
}
