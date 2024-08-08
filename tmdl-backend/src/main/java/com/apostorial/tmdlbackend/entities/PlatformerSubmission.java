package com.apostorial.tmdlbackend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;

@Document @Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
public class PlatformerSubmission extends Submission {
    @DBRef
    private PlatformerLevel level;
    private Duration recordTime;
}
