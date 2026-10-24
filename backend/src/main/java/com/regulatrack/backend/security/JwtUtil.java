package com.regulatrack.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME_MILLIS = 1000L * 60 * 60 * 8;

    private final Key signingKey;

    public JwtUtil(@Value("${app.jwt.secret}") String secret) {
        byte[] secretBytes = secret == null ? new byte[0] : secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must contain at least 32 bytes. Configure a unique secret outside source control."
            );
        }
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateToken(String username, String role, long tokenVersion) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("tv", tokenVersion)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_TIME_MILLIS))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public long extractTokenVersion(String token) {
        Object version = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("tv");
        return version instanceof Number number ? number.longValue() : -1L;
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
