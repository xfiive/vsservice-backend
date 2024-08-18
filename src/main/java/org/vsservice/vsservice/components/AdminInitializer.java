package org.vsservice.vsservice.components;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vsservice.vsservice.models.roles.Admin;
import org.vsservice.vsservice.services.moderation.AdminService;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminInitializer {

    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private final AdminService adminService;

    @PostConstruct
    public void init() {
        adminService.getAdminByUsername(ADMIN_LOGIN)
                .orElseGet(() -> adminService.registerAdmin(Admin.builder()
                        .username(ADMIN_LOGIN)
                        .password(ADMIN_PASSWORD)
                        .build()));
    }


}
