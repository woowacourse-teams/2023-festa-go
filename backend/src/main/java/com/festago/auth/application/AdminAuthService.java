package com.festago.auth.application;

import com.festago.admin.domain.Admin;
import com.festago.admin.repository.AdminRepository;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.UnauthorizedException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Deprecated(forRemoval = true)
@Service
@Transactional
@RequiredArgsConstructor
public class AdminAuthService {

    private final AuthProvider authProvider;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public String login(AdminLoginRequest request) {
        Admin admin = findAdminWithAuthenticate(request);
        AuthPayload authPayload = new AuthPayload(admin.getId(), Role.ADMIN);
        return authProvider.provide(authPayload);
    }

    private Admin findAdminWithAuthenticate(AdminLoginRequest request) {
        return adminRepository.findByUsername(request.username())
            .filter(admin -> passwordEncoder.matches(request.password(), admin.getPassword()))
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INCORRECT_PASSWORD_OR_ACCOUNT));
    }

    public void initializeRootAdmin(String password) {
        adminRepository.findByUsername(Admin.ROOT_ADMIN_NAME).ifPresentOrElse(admin -> {
            throw new BadRequestException(ErrorCode.DUPLICATE_ACCOUNT_USERNAME);
        }, () -> adminRepository.save(new Admin(Admin.ROOT_ADMIN_NAME, passwordEncoder.encode(password))));
    }

    public AdminSignupResponse signup(Long adminId, AdminSignupRequest request) {
        validateRootAdmin(adminId);
        String username = request.username();
        String password = passwordEncoder.encode(request.password());
        validateExistsUsername(username);
        Admin admin = adminRepository.save(new Admin(username, password));
        return new AdminSignupResponse(admin.getUsername());
    }

    private void validateExistsUsername(String username) {
        if (adminRepository.existsByUsername(username)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_ACCOUNT_USERNAME);
        }
    }

    private void validateRootAdmin(Long adminId) {
        adminRepository.findById(adminId)
            .map(Admin::getUsername)
            .filter(username -> Objects.equals(username, Admin.ROOT_ADMIN_NAME))
            .ifPresentOrElse(username -> {
            }, () -> {
                throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
            });
    }
}
