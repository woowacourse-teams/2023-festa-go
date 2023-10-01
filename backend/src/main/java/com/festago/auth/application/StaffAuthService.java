package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.auth.dto.StaffLoginResponse;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import com.festago.festival.domain.Festival;
import com.festago.staff.domain.Staff;
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
    public StaffLoginResponse login(StaffLoginRequest request) {
        Staff staff = findStaffCode(request.code());
        Festival festival = staff.getFestival();
        String accessToken = authProvider.provide(createAuthPayload(festival));
        return new StaffLoginResponse(festival.getId(), accessToken);
    }

    private Staff findStaffCode(String code) {
        return staffCodeRepository.findByCodeWithFetch(code)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INCORRECT_STAFF_CODE));
    }

    private AuthPayload createAuthPayload(Festival festival) {
        return new AuthPayload(festival.getId(), Role.STAFF);
    }
}
