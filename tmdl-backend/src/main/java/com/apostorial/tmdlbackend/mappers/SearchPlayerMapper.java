package com.apostorial.tmdlbackend.mappers;

import com.apostorial.tmdlbackend.dtos.player.SearchPlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import org.springframework.stereotype.Component;

@Component
public class SearchPlayerMapper {
    public SearchPlayerRequest toDTO(Player player) {
        SearchPlayerRequest dto = new SearchPlayerRequest();
        dto.setUsername(player.getUsername());
        return dto;
    }
}
