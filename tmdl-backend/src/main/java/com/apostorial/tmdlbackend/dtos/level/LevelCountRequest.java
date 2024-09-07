package com.apostorial.tmdlbackend.dtos.level;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class LevelCountRequest {
    private long main;
    private long extended;
    private long legacy;
}
