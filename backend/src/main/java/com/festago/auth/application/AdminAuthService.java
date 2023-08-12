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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminAuthService {

    private final AuthProvider authProvider;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;

    public AdminAuthService(AuthProvider authProvider, AdminRepository adminRepository,
                            MemberRepository memberRepository) {
        this.authProvider = authProvider;
        this.adminRepository = adminRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public String login(AdminLoginRequest request) {
        Admin admin = findAdmin(request);
        AuthPayload authPayload = getAuthPayload(admin);
        return authProvider.provide(authPayload);
    }

    private Admin findAdmin(AdminLoginRequest request) {
        return adminRepository.findByUsernameAndPassword(request.username(), request.password())
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_PASSWORD));
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
