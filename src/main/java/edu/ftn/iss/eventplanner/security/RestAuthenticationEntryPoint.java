package edu.ftn.iss.eventplanner.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String json = String.format(
                """
                {
                  "timestamp": "%s",
                  "status": %d,
                  "error": "Unauthorized",
                  "message": "%s",
                  "path": "%s"
                }
                """,
                java.time.ZonedDateTime.now().toString(),
                HttpServletResponse.SC_UNAUTHORIZED,
                authException.getMessage(),
                request.getRequestURI()
        );

        response.getWriter().write(json);
    }
}

