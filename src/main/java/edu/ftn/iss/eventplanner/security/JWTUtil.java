package edu.ftn.iss.eventplanner.security;

import edu.ftn.iss.eventplanner.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class JWTUtil {

    private static final String SECRET_KEY = "bXlzdHJvbmdzZWNyZXRrZXlmb3JldmVuY2UjfewnjJhjbhnVBDGHKJnjmebnjkwn";  // This is a Base64-encoded key

    // Generate token
    public static String generateToken(User user) {
        Objects.requireNonNull(user);

        return Jwts.builder()
                .claims(
                        Map.of(
                                "id", user.getId(),
                                "role", user.getClass().getSimpleName()
                        )
                )
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Extract email from token
    public static String extractEmail(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build();

            Claims claims = parser.parseClaimsJws(token).getBody();
            return claims.getSubject(); // The subject is the email
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }


    // Validate if the token has expired
    public static boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    // Extract expiration date from token
    private static Date extractExpiration(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build();

        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
}