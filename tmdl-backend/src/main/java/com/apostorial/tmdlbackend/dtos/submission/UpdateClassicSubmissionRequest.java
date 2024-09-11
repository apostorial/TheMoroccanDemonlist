package com.apostorial.tmdlbackend.dtos.submission;

import lombok.Data;

import java.net.URL;

@Data
public class UpdateClassicSubmissionRequest {
    private URL link;
    private String comment;
    private String level;
    private int recordPercentage;
}