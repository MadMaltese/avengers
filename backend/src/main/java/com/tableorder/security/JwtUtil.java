package com.tableorder.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration-hours}") int hours) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.expirationMs = hours * 3600000L;
    }

    public String generateToken(Long storeId, Long entityId, String role, Integer tableNo) {
        var builder = Jwts.builder()
                .subject(entityId.toString())
                .claim("storeId", storeId)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key);
        if (tableNo != null) builder.claim("tableNo", tableNo);
        return builder.compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
