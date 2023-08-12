package com.festago.auth.application;

import com.festago.auth.domain.Admin;
import com.festago.auth.domain.AdminRepository;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.Role;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.exception.ErrorCode;
import com.festago.exception.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminAuthService {

    private final AuthProvider authProvider;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthService(AuthProvider authProvider, AdminRepository adminRepository,
                            MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.authProvider = authProvider;
        this.adminRepository = adminRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public String login(AdminLoginRequest request) {
        Admin admin = findAdmin(request);
        validatePassword(request.password(), admin.getPassword());
        AuthPayload authPayload = getAuthPayload(admin);
        return authProvider.provide(authPayload);
    }

    private Admin findAdmin(AdminLoginRequest request) {
        return adminRepository.findByUsername(request.username())
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_PASSWORD));
    }

    private void validatePassword(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new UnauthorizedException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private AuthPayload getAuthPayload(Admin admin) {
        Member member = memberRepository.findBySocialIdAndSocialType(admin.getUsername(), SocialType.ADMIN)
            .orElseGet(() -> {
                Member newMember = new Member(admin.getUsername(), SocialType.ADMIN, admin.getUsername(), null);
                return memberRepository.save(newMember);
            });
        return new AuthPayload(member.getId(), Role.ADMIN);
    }
}
