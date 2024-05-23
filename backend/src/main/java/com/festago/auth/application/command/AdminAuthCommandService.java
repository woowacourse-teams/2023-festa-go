package com.festago.auth.application.command;

import com.festago.admin.domain.Admin;
import com.festago.admin.repository.AdminRepository;
import com.festago.auth.domain.AuthType;
import com.festago.auth.domain.authentication.AdminAuthentication;
import com.festago.auth.dto.command.AdminLoginCommand;
import com.festago.auth.dto.command.AdminLoginResult;
import com.festago.auth.dto.command.AdminSignupCommand;
import com.festago.auth.infrastructure.AdminAuthenticationTokenProvider;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminAuthCommandService {

    private final AdminAuthenticationTokenProvider adminAuthenticationTokenProvider;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AdminLoginResult login(AdminLoginCommand command) {
        Admin admin = findAdminWithAuthenticate(command);
        AdminAuthentication adminAuthentication = new AdminAuthentication(admin.getId());
        String accessToken = adminAuthenticationTokenProvider.provide(adminAuthentication).token();
        return new AdminLoginResult(
            admin.getUsername(),
            getAuthType(admin),
            accessToken
        );
    }

    private Admin findAdminWithAuthenticate(AdminLoginCommand request) {
        return adminRepository.findByUsername(request.username())
            .filter(admin -> passwordEncoder.matches(request.password(), admin.getPassword()))
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INCORRECT_PASSWORD_OR_ACCOUNT));
    }

    private AuthType getAuthType(Admin admin) {
        if (admin.isRootAdmin()) {
            return AuthType.ROOT;
        }
        return AuthType.ADMIN;
    }

    public void signup(Long adminId, AdminSignupCommand command) {
        validateRootAdmin(adminId);
        String username = command.username();
        String password = passwordEncoder.encode(command.password());
        validateExistsUsername(username);
        adminRepository.save(new Admin(username, password));
    }

    private void validateRootAdmin(Long adminId) {
        adminRepository.findById(adminId)
            .filter(Admin::isRootAdmin)
            .ifPresentOrElse(ignore -> {
            }, () -> {
                throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
            });
    }

    private void validateExistsUsername(String username) {
        if (adminRepository.existsByUsername(username)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_ACCOUNT_USERNAME);
        }
    }

    public void initializeRootAdmin(String password) {
        adminRepository.findByUsername(Admin.ROOT_ADMIN_NAME)
            .ifPresentOrElse(ignore -> {
                throw new BadRequestException(ErrorCode.DUPLICATE_ACCOUNT_USERNAME);
            }, () -> adminRepository.save(Admin.createRootAdmin(passwordEncoder.encode(password))));
    }
}
