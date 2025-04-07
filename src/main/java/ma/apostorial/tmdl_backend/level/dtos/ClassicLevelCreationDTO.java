package ma.apostorial.tmdl_backend.level.dtos;

import java.net.URL;

import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;

public record ClassicLevelCreationDTO(
    String levelId,
    String name,
    String publisher,
    Difficulty difficulty,
    Duration duration,
    URL videoLink,
    URL thumbnailLink,
    int minimumCompletion
) { }