package com.apostorial.tmdlbackend.dtos.region;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRegionRequest {
    @NotNull(message = "Name is required.")
    private String name;
}
