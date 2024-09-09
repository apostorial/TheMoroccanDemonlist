package com.apostorial.tmdlbackend.dtos.record;

import lombok.Data;

import java.net.URL;
import java.time.Duration;

@Data
public class CreatePlatformerRecordRequest {
    private URL link;
    private String playerId;
    private Duration recordTime;
    private String levelId;
}
