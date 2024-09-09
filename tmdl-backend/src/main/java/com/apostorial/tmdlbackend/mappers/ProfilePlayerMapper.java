package com.apostorial.tmdlbackend.mappers;

import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import org.springframework.stereotype.Component;

@Component
public class ProfilePlayerMapper {
    public ProfilePlayerRequest toDTO(Player player) {
        ProfilePlayerRequest dto = new ProfilePlayerRequest();
        dto.setId(player.getId());
        dto.setUsername(player.getUsername());
        dto.setDateJoined(player.getDateJoined());
        dto.setRegion(player.getRegion());
        dto.setClassicPoints(player.getClassicPoints());
        dto.setPlatformerPoints(player.getPlatformerPoints());
        dto.setDiscord(player.getDiscord());
        dto.setYoutube(player.getYoutube());
        dto.setTwitter(player.getTwitter());
        dto.setTwitch(player.getTwitch());
        dto.setIsStaff(player.isStaff());
        return dto;
    }
}
