package com.apostorial.tmdlbackend.dtos.record;

import lombok.Data;

import java.net.URL;
import java.time.Duration;

@Data
public class UpdatePlatformerRecordRequest {
    private URL link;
    private String player;
    private Duration recordTime;
    private String level;
}
