package com.apostorial.tmdlbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document @Data @NoArgsConstructor @AllArgsConstructor
public class Region {
    @Id
    private String id;
    private String name;
    private Double classicPoints = 0.0;
    private Double platformerPoints = 0.0;
}
