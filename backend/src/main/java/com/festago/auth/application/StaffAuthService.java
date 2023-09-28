package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import com.festago.festival.domain.Festival;
import com.festago.staff.domain.StaffCode;
import com.festago.staff.repository.StaffCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StaffAuthService {

    private final AuthProvider authProvider;
    private final StaffCodeRepository staffCodeRepository;

    public StaffAuthService(AuthProvider authProvider, StaffCodeRepository staffCodeRepository) {
        this.authProvider = authProvider;
        this.staffCodeRepository = staffCodeRepository;
    }

    @Transactional(readOnly = true)
    public String login(StaffLoginRequest request) {
        StaffCode staffCode = findStaffCode(request.code());
        AuthPayload authPayload = createAuthPayload(staffCode.getFestival());
        return authProvider.provide(authPayload);
    }

    private StaffCode findStaffCode(String code) {
        return staffCodeRepository.findByCodeWithFetch(code)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INCORRECT_STAFF_CODE));
    }

    private AuthPayload createAuthPayload(Festival festival) {
        return new AuthPayload(festival.getId(), Role.STAFF);
    }
}
