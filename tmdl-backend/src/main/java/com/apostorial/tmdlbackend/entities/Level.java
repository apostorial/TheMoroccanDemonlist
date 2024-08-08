package com.apostorial.tmdlbackend.entities;

import com.apostorial.tmdlbackend.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.net.URL;

@Data @NoArgsConstructor @AllArgsConstructor
public abstract class Level {
    @Id
    private String id;
    private String levelId;
    private String name;
    private String publisher;
    private Difficulty difficulty;
    private URL link;
    private URL thumbnail;
    private float points;
}
