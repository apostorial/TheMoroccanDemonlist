package com.apostorial.tmdlbackend.dtos.record;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.net.URL;

@Data
public class UpdateClassicRecordRequest {
    @NotNull(message = "Link is required.")
    private URL link;
    @NotNull(message = "Player ID is required.")
    private String playerId;
    @NotNull(message = "Record Percentage is required.")
    private int recordPercentage;
    @NotNull(message = "Level ID is required.")
    private String levelId;
}
