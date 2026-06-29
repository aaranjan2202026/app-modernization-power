package com.org.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderConfigTest {

    @Test
    void passwordEncoderBeanIsCreated() {
        PasswordEncoderConfig config = new PasswordEncoderConfig();
        BCryptPasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void encodeAndMatchesWork() {
        PasswordEncoderConfig config = new PasswordEncoderConfig();
        BCryptPasswordEncoder encoder = config.passwordEncoder();
        String hash = encoder.encode("secret");
        assertTrue(encoder.matches("secret", hash));
        assertFalse(encoder.matches("wrong", hash));
    }
}
