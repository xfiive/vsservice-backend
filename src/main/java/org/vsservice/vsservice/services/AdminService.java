package org.vsservice.vsservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vsservice.vsservice.models.errors.AuthenticationException;
import org.vsservice.vsservice.models.errors.VsserviceException;
import org.vsservice.vsservice.models.roles.Admin;
import org.vsservice.vsservice.repositories.AdminRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Optional<Admin> registerAdmin(@NotNull Admin admin) {
        if (admin.getUsername() != null && adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            throw new VsserviceException("Failed to register new admin", "Such admin already exists");
        }

        return Optional.of(adminRepository.save(Admin.builder().username(admin.getUsername()).password(passwordEncoder.encode(admin.getPassword())).build()));
    }

    public Optional<Admin> authenticateAdmin(String username, String password) {
        return adminRepository.findByUsername(username)
                .map(existingAdmin -> {
                    if (passwordEncoder.matches(password, existingAdmin.getPassword())) {
                        return Optional.of(existingAdmin);
                    } else {
                        return Optional.<Admin>empty();
                    }
                })
                .orElseThrow(() -> new AuthenticationException("Failed to authenticate user", "Invalid login data"));
    }


}
