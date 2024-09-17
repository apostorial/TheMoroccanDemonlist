package com.apostorial.tmdlbackend.mappers;

import com.apostorial.tmdlbackend.dtos.player.DataPlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import org.springframework.stereotype.Component;

@Component
public class DataPlayerMapper {
    public DataPlayerRequest toDto(Player player) {
        DataPlayerRequest dto = new DataPlayerRequest();
        dto.setId(player.getId());
        dto.setUsername(player.getUsername());
        dto.setClassicPoints(player.getClassicPoints());
        dto.setPlatformerPoints(player.getPlatformerPoints());
        return dto;
    }
}

