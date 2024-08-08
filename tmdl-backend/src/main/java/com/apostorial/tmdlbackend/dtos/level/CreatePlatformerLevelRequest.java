package com.apostorial.tmdlbackend.dtos.level;

import com.apostorial.tmdlbackend.enums.Difficulty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.net.URL;

@Data
public class CreatePlatformerLevelRequest {
    @NotNull(message = "Level ID is required.")
    private String levelId;
    @NotNull(message = "Name is required.")
    private String name;
    @NotNull(message = "Publisher is required.")
    private String publisher;
    @NotNull(message = "Difficulty is required.")
    private Difficulty difficulty;
    @NotNull(message = "Link is required.")
    private URL link;
    @NotNull(message = "Thumbnail is required.")
    private URL thumbnail;
}
