package com.apostorial.tmdlbackend.entities.level;

import com.apostorial.tmdlbackend.entities.Player;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document @Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
public class PlatformerLevel extends Level {
    @Min(0)
    private int ranking;
    @DBRef
    private Player recordHolder;

    @JsonProperty("recordHolder")
    public String getRecordHolderForSerialization() {
        if (recordHolder == null) {
            return null;
        }
        return recordHolder.getUsername();
    }
}
