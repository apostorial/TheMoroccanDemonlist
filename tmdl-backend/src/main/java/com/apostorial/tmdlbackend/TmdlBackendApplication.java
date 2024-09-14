package com.apostorial.tmdlbackend;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication @AllArgsConstructor
public class TmdlBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmdlBackendApplication.class, args);
    }
}
