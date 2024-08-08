package com.apostorial.tmdlbackend.dtos;

import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.net.URL;

@Data
public class CreateClassicLevelRequest {
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
    @NotNull(message = "Duration is required.")
    private Duration duration;
    @NotNull(message = "Minimum completion is required.")
    @Positive(message = "Minimum completion must be positive.")
    private int minimumCompletion;
}
