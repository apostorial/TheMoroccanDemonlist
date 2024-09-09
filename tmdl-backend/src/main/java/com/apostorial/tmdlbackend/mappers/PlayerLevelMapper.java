package com.apostorial.tmdlbackend.mappers;

import com.apostorial.tmdlbackend.dtos.level.PlayerLevelRequest;
import com.apostorial.tmdlbackend.entities.level.Level;
import org.springframework.stereotype.Component;

@Component
public class PlayerLevelMapper {
    public <T extends Level> PlayerLevelRequest toDto(T level) {
        PlayerLevelRequest dto = new PlayerLevelRequest();
        dto.setId(level.getId());
        dto.setName(level.getName());
        return dto;
    }
}
