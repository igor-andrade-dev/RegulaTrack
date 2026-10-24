package com.regulatrack.backend.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private static final String TEST_SECRET = "test-only-secret-that-is-at-least-32-bytes-long";

    @Test
    void shouldValidateSubjectAndTokenVersion() {
        JwtUtil jwtUtil = new JwtUtil(TEST_SECRET);
        String token = jwtUtil.generateToken("test-user", "USER", 7L);

        assertEquals("test-user", jwtUtil.validateToken(token));
        assertEquals(7L, jwtUtil.extractTokenVersion(token));
    }

    @Test
    void shouldRejectShortSigningSecrets() {
        assertThrows(IllegalStateException.class, () -> new JwtUtil("too-short"));
    }
}
