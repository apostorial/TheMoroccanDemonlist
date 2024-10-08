package com.apostorial.tmdlbackend.services.implementations;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service @RequiredArgsConstructor
public class PlayerDetailsServiceImpl implements UserDetailsService {
    private final PlayerRepository playerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Player player = playerRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Player with email " + email + " not found"));
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (player.isStaff()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
            }

            return new User(
                    player.getEmail(),
                    "",
                    authorities
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error occurred while fetching user: " + email, e);
        }
    }
}
