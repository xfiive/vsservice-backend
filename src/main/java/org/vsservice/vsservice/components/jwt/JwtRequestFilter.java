package org.vsservice.vsservice.components.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vsservice.vsservice.models.errors.AuthenticationException;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        log.info("Request path: {}", path);

        if ("/api/admin/login".equals(path) || "/api/admin/refresh".equals(path)) {
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            log.info("JWT from Authorization header: {}", jwt);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                jwt = Arrays.stream(cookies)
                        .filter(cookie -> "access_token".equals(cookie.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
                log.info("JWT from cookies: {}", jwt);
            }
        }

        log.info("Authorization Header or Cookie JWT: {}", jwt);

        String username = null;

        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
                log.info("Extracted Username: {}", username);
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT Token found.", e);
                throw new AuthenticationException("Failed to do a request", "Expired JWT Token was found");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                log.info("Authentication set for user: {}", username);
                log.info("User roles: {}", userDetails.getAuthorities());
            } else {
                log.warn("Invalid JWT Token");
            }

        }

        chain.doFilter(request, response);
    }
}
