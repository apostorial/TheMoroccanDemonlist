package com.apostorial.tmdlbackend.dtos;

import com.apostorial.tmdlbackend.entities.Region;
import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;
    private Region region;
}
