package com.apostorial.tmdlbackend.dtos.submission;

import lombok.Data;

import java.net.URL;
import java.time.Duration;

@Data
public class CreatePlatformerSubmissionRequest {
    private URL link;
    private String comment;
    private String level;
    private Duration recordTime;
}
