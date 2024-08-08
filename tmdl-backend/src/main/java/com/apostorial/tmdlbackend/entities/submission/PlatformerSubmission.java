package com.apostorial.tmdlbackend.entities.submission;

import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("level")
    public String getLevelForSerialization() {
        return level.getId();
    }
}
