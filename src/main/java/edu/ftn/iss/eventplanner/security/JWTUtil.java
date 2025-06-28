package edu.ftn.iss.eventplanner.security;

import edu.ftn.iss.eventplanner.entities.User;
import io.jsonwebtoken.*;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class JWTUtil {

    private static final String SECRET_KEY = "bXlzdHJvbmdzZWNyZXRrZXlmb3JldmVuY2UjfewnjJhjbhnVBDGHKJnjmebnjkwn",  // This is a Base64-encoded key
        AUTH_HEADER = "Authorization";

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
        } catch (ExpiredJwtException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    public static @Nullable String getUsername(String token) { return extractEmail(token); }


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

    @Nullable
    public static String getToken(HttpServletRequest request) {
        var auth = request.getHeader(AUTH_HEADER);
        return auth != null && auth.startsWith("Bearer ") ? auth.substring(7) : null;
    }

    public static boolean isTokenValid(String token, UserDetails userDetails) {
        return Objects.equals(getUsername(token), userDetails.getUsername()); // maybe check if the password has been changed after the token issuance
    }

}