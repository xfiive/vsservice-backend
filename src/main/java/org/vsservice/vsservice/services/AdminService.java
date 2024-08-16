package org.vsservice.vsservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vsservice.vsservice.models.errors.AuthenticationException;
import org.vsservice.vsservice.models.errors.VsserviceException;
import org.vsservice.vsservice.models.roles.Admin;
import org.vsservice.vsservice.repositories.AdminRepository;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "admins", key = "#username", unless = "#result == null")
    @SuppressWarnings("all")
    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username).get();
    }

    @Caching(
            evict = {@CacheEvict(value = "admins", key = "#admin.id")},
            put = {@CachePut(value = "admins", key = "#admin.id")}
    )
    public Admin registerAdmin(@NotNull Admin admin) {
        if (admin.getUsername() != null && adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            throw new VsserviceException("Failed to register new admin", "Such admin already exists");
        }

        return adminRepository.save(Admin.builder().username(admin.getUsername()).password(passwordEncoder.encode(admin.getPassword())).build());
    }

    @Cacheable(value = "admins", key = "#username", unless = "#result == null")
    public Admin authenticateAdmin(String username, String password) {
        return adminRepository.findByUsername(username)
                .map(existingAdmin -> {
                    if (passwordEncoder.matches(password, existingAdmin.getPassword())) {
                        return existingAdmin;
                    } else {
                        return null;
                    }
                })
                .orElseThrow(() -> new AuthenticationException("Failed to authenticate user", "Invalid login data"));
    }


}
