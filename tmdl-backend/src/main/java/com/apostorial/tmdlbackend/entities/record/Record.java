package com.apostorial.tmdlbackend.entities.record;

import com.apostorial.tmdlbackend.entities.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.net.URL;

@Data @NoArgsConstructor @AllArgsConstructor
public abstract class Record {
    @Id
    private String id;
    private URL link;
    @DBRef
    private Player player;
}
