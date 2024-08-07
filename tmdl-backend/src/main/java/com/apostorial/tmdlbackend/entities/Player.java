package com.apostorial.tmdlbackend.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Document @Data @NoArgsConstructor @AllArgsConstructor
public class Player implements UserDetails {
    @Id
    private String id;
    @NotNull @Indexed(unique = true) @Email
    private String email;
    @NotNull @Indexed(unique = true)
    private String username;
    @NotNull
    private String password;
    private boolean isStaff = false;
    private boolean isActive = true;
    private boolean isEmailVerified = false;
    private LocalDateTime dateJoined = LocalDateTime.now();
    private Region region;
    private float classicPoints = 0;
    private float platformerPoints = 0;
    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isStaff) {
            return List.of(new SimpleGrantedAuthority("ROLE_STAFF"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive && isEmailVerified;
    }
}
