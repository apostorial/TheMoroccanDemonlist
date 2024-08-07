package com.apostorial.tmdlbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;

@Document @Data @NoArgsConstructor @AllArgsConstructor
public class Record {
    @Id
    private String id;
    private URL link;
    @DBRef
    private Player player;
}
