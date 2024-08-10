package org.vsservice.vsservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vsservice.vsservice.components.jwt.JwtUtil;
import org.vsservice.vsservice.models.errors.AuthenticationException;
import org.vsservice.vsservice.models.errors.TokenRefreshException;
import org.vsservice.vsservice.models.roles.Admin;
import org.vsservice.vsservice.models.security.RefreshToken;
import org.vsservice.vsservice.services.AdminService;
import org.vsservice.vsservice.services.RefreshTokenService;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminController {

    private final AdminService adminService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@Validated @RequestBody Admin admin) {
        return this.adminService.registerAdmin(admin)
                .map(registeredAdmin -> new ResponseEntity<>(registeredAdmin, HttpStatus.CREATED))
                .orElseThrow(() -> new AuthenticationException("Failed to register a new admin", "Such admin already exists"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateAdmin(@Validated @RequestBody Admin admin) {
        return adminService.authenticateAdmin(admin.getUsername(), admin.getPassword())
                .map(authenticatedAdmin -> {
                    String accessToken = jwtUtil.generateAccessToken(authenticatedAdmin.getUsername());
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedAdmin.getUsername());

                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("access_token", accessToken);
                    tokens.put("refresh_token", refreshToken.getToken());
                    return new ResponseEntity<>(tokens, HttpStatus.OK);
                })
                .orElseThrow(() -> new AuthenticationException("Failed to authenticate user", "Invalid login data"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsername)
                .map(username -> {
                    String accessToken = jwtUtil.generateAccessToken(username);
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("access_token", accessToken);
                    return new ResponseEntity<>(tokens, HttpStatus.OK);
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not valid!"));
    }
}
