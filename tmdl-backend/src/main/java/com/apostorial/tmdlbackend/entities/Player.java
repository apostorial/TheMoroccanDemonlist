package com.apostorial.tmdlbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document @Data @NoArgsConstructor @AllArgsConstructor
public class Player {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String avatar;
    private boolean isStaff = false;
    private boolean isActive = true;
    private LocalDateTime dateJoined = LocalDateTime.now();
    @DBRef
    private Region region;
    private Double classicPoints = 0.0;
    private Double platformerPoints = 0.0;
    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;

    @JsonProperty("region")
    public String getRegionForSerialization() {
        return region != null ? region.getId() : null;
    }
}
