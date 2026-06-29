package com.org;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PomDependencyTest {

    @Test
    void bcryptPasswordEncoderCanBeConstructed() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void bcryptEncodeAndMatches() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "testPassword";
        String hash = encoder.encode(raw);
        assertNotNull(hash);
        assertTrue(encoder.matches(raw, hash));
        assertFalse(encoder.matches("wrongPassword", hash));
    }
}
