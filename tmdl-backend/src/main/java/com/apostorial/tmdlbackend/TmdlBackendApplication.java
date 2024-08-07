package com.apostorial.tmdlbackend;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication @AllArgsConstructor
public class TmdlBackendApplication {
    private final MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("spring.data.mongodb.host", dotenv.get("SPRING_DATA_MONGODB_HOST"));
        System.setProperty("spring.data.mongodb.port", dotenv.get("SPRING_DATA_MONGODB_PORT"));
        System.setProperty("spring.data.mongodb.database", dotenv.get("SPRING_DATA_MONGODB_DATABASE"));
        System.setProperty("jwt.expiration", dotenv.get("JWT_EXPIRATION"));

        SpringApplication.run(TmdlBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner run(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            dropDatabase();
            Player userPlayer = new Player();
            userPlayer.setUsername("user");
            userPlayer.setPassword(passwordEncoder.encode("123"));
            userPlayer.setEmail("user@email.com");
            playerRepository.save(userPlayer);

            Player adminPlayer = new Player();
            adminPlayer.setUsername("admin");
            adminPlayer.setPassword(passwordEncoder.encode("123"));
            adminPlayer.setEmail("admin@email.com");
            adminPlayer.setStaff(true);
            playerRepository.save(adminPlayer);
        };
    }

    public void dropDatabase() {
        mongoTemplate.getDb().drop();
    }

}
