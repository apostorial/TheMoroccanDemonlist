package com.apostorial.tmdlbackend.utilities;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Component
public class UsernameGenerator {
    private static final String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateUsername() {
        return "user" + RANDOM.ints(10, 0, ALPHANUMERIC.length())
                .mapToObj(ALPHANUMERIC::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
