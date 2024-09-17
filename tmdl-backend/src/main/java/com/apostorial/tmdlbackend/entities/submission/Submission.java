package com.apostorial.tmdlbackend.entities.submission;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.net.URL;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public abstract class Submission {
    @Id
    private String id;
    @DBRef
    private Player player;
    private URL link;
    private URL rawFootage;
    private String comment;
    private LocalDateTime submissionDate = LocalDateTime.now();
    private LocalDateTime approvalDate;
    private Status status = Status.PENDING;

    @JsonProperty("player")
    public String getPlayerForSerialization() {
        return player.getUsername();
    }
}
