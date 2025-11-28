package com.lul.user.infrastructure.security;

import com.lul.user.domain.auth.valueobject.Password;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Infrastructure implementation of Password.PasswordHasher using BCrypt algorithm.
 * BCrypt is a password hashing function designed with security in mind.
 * Strength of 10 provides good balance between security and performance.
 */
@Component
public class BCryptPasswordHasher implements Password.PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordHasher() {
        // BCrypt with strength 10 (2^10 = 1024 rounds)
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }

    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
