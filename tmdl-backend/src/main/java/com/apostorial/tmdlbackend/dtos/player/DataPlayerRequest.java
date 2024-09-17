package com.apostorial.tmdlbackend.dtos.player;

import lombok.Data;

@Data
public class DataPlayerRequest {
    private String id;
    private String username;
    private Double classicPoints;
    private Double platformerPoints;
}
