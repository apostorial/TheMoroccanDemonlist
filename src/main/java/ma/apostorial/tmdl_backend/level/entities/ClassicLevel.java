package ma.apostorial.tmdl_backend.level.entities;

import java.net.URL;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;
import ma.apostorial.tmdl_backend.player.entities.Player;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClassicLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String levelId;

    private String name;

    private String publisher;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private Duration duration;

    private URL videoLink;

    private URL thumbnailLink;

    private Double points;

    private Double minimumPoints;

    private int ranking;

    private int minimumCompletion;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player firstVictor;

    @JsonProperty("firstVictor")
    protected UUID getFirstVictorForSerialization() {
        return (firstVictor != null) ? firstVictor.getId() : null;
    }
}