package edu.ftn.iss.eventplanner.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        try {
            String token = JWTUtil.getToken(request);
            if (token == null || token.isEmpty())
                return;

            String username = JWTUtil.getUsername(token);
            var userDetails = userDetailsService.loadUserByUsername(username);

            if (!JWTUtil.isTokenValid(token, userDetails))
                return;

            var authentication = new AbstractAuthenticationToken(userDetails.getAuthorities()) {
                {
                    setAuthenticated(true);
                    setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                }
                public String getCredentials() { return token; }
                public UserDetails getPrincipal() { return userDetails; }
            };

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NullPointerException | ExpiredJwtException e) {
            log.debug(e.getMessage());
        }
        finally {
            filterChain.doFilter(request, response);
        }
    }


}
