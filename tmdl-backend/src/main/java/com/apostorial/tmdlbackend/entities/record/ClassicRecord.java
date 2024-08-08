package com.apostorial.tmdlbackend.entities.record;

import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document @Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
public class ClassicRecord extends Record {
    @Min(1) @Max(100)
    private int recordPercentage;
    @DBRef
    private ClassicLevel level;

    @JsonProperty("level")
    public String getLevelForSerialization() {
        return level.getId();
    }
}