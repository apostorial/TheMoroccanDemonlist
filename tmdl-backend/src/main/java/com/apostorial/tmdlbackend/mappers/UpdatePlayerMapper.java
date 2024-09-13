package com.apostorial.tmdlbackend.mappers;

import com.apostorial.tmdlbackend.dtos.player.UpdatePlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import org.springframework.stereotype.Component;

@Component
public class UpdatePlayerMapper {
    public UpdatePlayerRequest toDTO(Player player) {
        UpdatePlayerRequest dto = new UpdatePlayerRequest();
        dto.setUsername(player.getUsername());
        dto.setDiscord(player.getDiscord());
        dto.setYoutube(player.getYoutube());
        dto.setTwitter(player.getTwitter());
        dto.setTwitch(player.getTwitch());
        dto.setIsActive(player.isActive());
        dto.setRegion(String.valueOf(player.getRegion()));
        return dto;
    }
}