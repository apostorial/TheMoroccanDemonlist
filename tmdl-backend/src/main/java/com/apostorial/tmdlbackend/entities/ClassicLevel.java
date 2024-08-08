package com.apostorial.tmdlbackend.entities;

import com.apostorial.tmdlbackend.enums.Duration;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document @Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
public class ClassicLevel extends Level {
    @Min(0)
    private int ranking;
    private Duration duration;
    private float minPoints;
    @Min(1) @Max(100)
    private int minimumCompletion;
    @DBRef
    private Player firstVictor;
}
