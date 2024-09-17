package com.apostorial.tmdlbackend.dtos.submission;

import lombok.Data;

import java.net.URL;
import java.time.Duration;

@Data
public class UpdatePlatformerSubmissionRequest {
    private URL link;
    private URL rawFootage;
    private String comment;
    private String level;
    private Duration recordTime;
}
