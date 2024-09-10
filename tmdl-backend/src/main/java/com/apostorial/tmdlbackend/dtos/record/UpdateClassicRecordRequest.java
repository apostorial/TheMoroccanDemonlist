package com.apostorial.tmdlbackend.dtos.record;

import lombok.Data;

import java.net.URL;

@Data
public class UpdateClassicRecordRequest {
    private URL link;
    private String player;
    private int recordPercentage;
    private String level;
}
