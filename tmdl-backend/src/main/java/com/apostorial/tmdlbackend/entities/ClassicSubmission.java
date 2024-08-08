package com.apostorial.tmdlbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document @Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
public class ClassicSubmission extends Submission {
    @DBRef
    private ClassicLevel level;
    @Min(1) @Max(100)
    private int recordPercentage;

    @JsonProperty("level")
    public String getLevelForSerialization() {
        return level.getId();
    }

}
