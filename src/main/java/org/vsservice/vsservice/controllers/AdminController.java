package org.vsservice.vsservice.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vsservice.vsservice.components.jwt.JwtUtil;
import org.vsservice.vsservice.models.dtos.AdminDto;
import org.vsservice.vsservice.models.errors.AuthenticationException;
import org.vsservice.vsservice.models.errors.TokenRefreshException;
import org.vsservice.vsservice.models.roles.Admin;
import org.vsservice.vsservice.models.security.RefreshToken;
import org.vsservice.vsservice.services.moderation.AdminService;
import org.vsservice.vsservice.services.moderation.RefreshTokenService;

import java.util.Arrays;
import java.util.Optional;

@Validated
@RestController
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminController {

    private final AdminService adminService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@Validated @RequestBody Admin admin) {
        return Optional.ofNullable(this.adminService.registerAdmin(admin))
                .map(registeredAdmin -> new ResponseEntity<>(registeredAdmin, HttpStatus.CREATED))
                .orElseThrow(() -> new AuthenticationException("Failed to register a new admin", "Such admin already exists"));
    }

    @PostMapping("/login")
    public ResponseEntity<AdminDto> authenticateAdmin(@Validated @RequestBody Admin admin, HttpServletResponse response) {
        return adminService.authenticateAdmin(admin.getUsername(), admin.getPassword())
                .map(authenticatedAdmin -> {
                    log.info("Login successful for user: {}", authenticatedAdmin.getUsername());

                    String accessToken = jwtUtil.generateAccessToken(authenticatedAdmin.getUsername());
                    String refreshToken = refreshTokenService.createRefreshToken(authenticatedAdmin.getUsername()).getToken();

                    Cookie accessTokenCookie = new Cookie("access_token", accessToken);
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setSecure(true);
                    accessTokenCookie.setPath("/");
                    accessTokenCookie.setMaxAge(15 * 60);

                    Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
                    refreshTokenCookie.setHttpOnly(true);
                    refreshTokenCookie.setSecure(true);
                    refreshTokenCookie.setPath("/");
                    refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

                    response.addCookie(accessTokenCookie);
                    response.addCookie(refreshTokenCookie);

                    log.info("authenticatedAdminDto: {}", AdminDto.builder()
                            .name(authenticatedAdmin.getUsername())
                            .build());

                    return new ResponseEntity<>(AdminDto.builder()
                            .name(authenticatedAdmin.getUsername())
                            .build(),
                            HttpStatus.OK);
                })
                .orElseThrow(() -> {
                    log.info("Login failed for user: {}", admin.getUsername());
                    return new AuthenticationException("Failed to authenticate user", "Invalid login data");
                });
    }


    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new TokenRefreshException("No refresh token found", "Refresh token is missing"));

        return Optional.ofNullable(refreshTokenService.findByToken(refreshToken))
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsername)
                .map(username -> {
                    String accessToken = jwtUtil.generateAccessToken(username);

                    Cookie accessTokenCookie = new Cookie("access_token", accessToken);
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setSecure(true);
                    accessTokenCookie.setPath("/");
                    accessTokenCookie.setMaxAge(15 * 60);

                    response.addCookie(accessTokenCookie);

                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not valid!"));
    }

}
