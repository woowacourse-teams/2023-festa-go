package com.festago.auth.application;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.domain.Role;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.auth.dto.StaffLoginResponse;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import com.festago.staff.domain.Staff;
import com.festago.staff.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StaffAuthService {

    private final AuthProvider authProvider;
    private final StaffRepository staffRepository;

    public StaffAuthService(AuthProvider authProvider, StaffRepository staffRepository) {
        this.authProvider = authProvider;
        this.staffRepository = staffRepository;
    }

    @Transactional(readOnly = true)
    public StaffLoginResponse login(StaffLoginRequest request) {
        Staff staff = findStaffCode(request.code());
        String accessToken = authProvider.provide(createAuthPayload(staff));
        return new StaffLoginResponse(staff.getId(), accessToken);
    }

    private Staff findStaffCode(String code) {
        return staffRepository.findByCodeWithFetch(code)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.INCORRECT_STAFF_CODE));
    }

    private AuthPayload createAuthPayload(Staff staff) {
        return new AuthPayload(staff.getId(), Role.STAFF);
    }
}
