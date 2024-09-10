package com.apostorial.tmdlbackend.dtos.level;

import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;
import lombok.Data;

import java.net.URL;

@Data
public class UpdateClassicLevelRequest {
    private String id;
    private String name;
    private String publisher;
    private Difficulty difficulty;
    private int ranking;
    private URL link;
    private URL thumbnail;
    private Duration duration;
    private int minimumCompletion;
    private String firstVictor;
}
