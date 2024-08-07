package com.apostorial.tmdlbackend.entities;

import com.apostorial.tmdlbackend.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Document @Data @NoArgsConstructor @AllArgsConstructor
public class Submission {
    @Id
    private String id;
    private Player player;
    private URL link;
    private String comment;
    private ZonedDateTime submissionDate = ZonedDateTime.now(ZoneId.of("UTC+1"));
    private ZonedDateTime approvalDate;
    private Status status;
}
