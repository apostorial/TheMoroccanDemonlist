package com.apostorial.tmdlbackend;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication @AllArgsConstructor
public class TmdlBackendApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("spring.data.mongodb.host", dotenv.get("MONGODB_HOST"));
        System.setProperty("spring.data.mongodb.port", dotenv.get("MONGODB_PORT"));
        System.setProperty("spring.data.mongodb.database", dotenv.get("MONGODB_DATABASE"));
        System.setProperty("jwt.expiration", dotenv.get("JWT_EXPIRATION"));
        System.setProperty("spring.security.oauth2.client.registration.google.client-id", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("spring.security.oauth2.client.registration.google.client-secret", dotenv.get("GOOGLE_CLIENT_SECRET"));
        System.setProperty("spring.security.oauth2.client.registration.google.scope", dotenv.get("GOOGLE_SCOPE"));


        SpringApplication.run(TmdlBackendApplication.class, args);
    }
}
