package com.apostorial.tmdlbackend.dtos.record;

import lombok.Data;

import java.net.URL;

@Data
public class UpdateClassicRecordRequest {
    private URL link;
    private String playerId;
    private int recordPercentage;
    private String levelId;
}
