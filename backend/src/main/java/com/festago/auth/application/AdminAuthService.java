package com.festago.auth.application;

import com.festago.auth.domain.Admin;
import com.festago.auth.domain.AdminRepository;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
import com.festago.domain.Member;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.UnauthorizedException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminAuthService {

    private static final String ADMIN = "admin";

    private final AuthProvider authProvider;
    private final AdminRepository adminRepository;

    public AdminAuthService(AuthProvider authProvider, AdminRepository adminRepository) {
        this.authProvider = authProvider;
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    public String login(AdminLoginRequest request) {
        Admin admin = findAdmin(request);
        validatePassword(admin.getPassword(), request.password());
        AuthPayload authPayload = getAuthPayload(admin);
        return authProvider.provide(authPayload);
    }

    private Admin findAdmin(AdminLoginRequest request) {
        return adminRepository.findByUsername(request.username())
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_PASSWORD));
    }

    private void validatePassword(String password, String comparePassword) {
        if (!Objects.equals(password, comparePassword)) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private AuthPayload getAuthPayload(Admin admin) {
        Long memberId = admin.getMemberId();
        return new AuthPayload(memberId, Role.ADMIN);
    }

    public void initialFirstAdminAccount(String password) {
        Optional<Admin> admin = adminRepository.findByUsername(ADMIN);
        Member member = new Member(ADMIN, SocialType.FESTAGO, ADMIN, "");
        if (admin.isEmpty()) {
            adminRepository.save(new Admin(ADMIN, password, member));
        }
    }

    public AdminSignupResponse signup(AdminSignupRequest request) {
        String username = request.username();
        String password = request.password();
        validateExistsUsername(username);
        Member member = new Member(username, SocialType.FESTAGO, username, "");
        Admin admin = adminRepository.save(new Admin(username, password, member));
        return new AdminSignupResponse(admin.getUsername());
    }

    private void validateExistsUsername(String username) {
        if (adminRepository.existsByUsername(username)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_ADMIN_ACCOUNT);
        }
    }
}
