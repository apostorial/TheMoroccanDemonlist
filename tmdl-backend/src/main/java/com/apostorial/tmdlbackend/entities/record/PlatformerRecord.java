package com.apostorial.tmdlbackend.entities.record;

import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;

@Document @Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
@CompoundIndexes({
        @CompoundIndex(name = "player_level_index", def = "{'player': 1, 'level': 1}", unique = true)
})
public class PlatformerRecord extends Record {
    private Duration recordTime;
    @DBRef
    private PlatformerLevel level;

    @JsonProperty("level")
    public String getLevelForSerialization() {
        return level.getId();
    }
}
