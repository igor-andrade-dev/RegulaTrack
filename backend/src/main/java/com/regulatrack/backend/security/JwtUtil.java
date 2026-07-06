package com.regulatrack.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // chave secreta (depois pode ir pro application.properties)
    private static final String SECRET =
            "regulatrack-super-secret-key-change-this-very-important";

    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // tempo de expiração (1 dia)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // 🔐 GERAR TOKEN
    public static String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // 🔥 importante
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String extractRole(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // 🔍 VALIDAR TOKEN E PEGAR USUÁRIO
    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 🔍 EXTRAIR USER SEM VALIDAR (opcional)
    public static String extractUsername(String token) {
        return validateToken(token);
    }
}