package com.festago.auth.application;

import static com.festago.common.exception.ErrorCode.DUPLICATE_ACCOUNT_USERNAME;
import static com.festago.common.exception.ErrorCode.INCORRECT_PASSWORD_OR_ACCOUNT;
import static com.festago.common.exception.ErrorCode.NOT_ENOUGH_PERMISSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import com.festago.admin.domain.Admin;
import com.festago.admin.repository.AdminRepository;
import com.festago.admin.repository.MemoryAdminRepository;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminAuthServiceTest {

    AdminRepository adminRepository;

    AuthProvider authProvider = mock(AuthProvider.class);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    AdminAuthService adminAuthService;

    @BeforeEach
    void setUp() {
        adminRepository = new MemoryAdminRepository();
        adminAuthService = new AdminAuthService(authProvider, adminRepository, passwordEncoder);
    }

    @Nested
    class 로그인 {

        @Test
        void 계정이_없으면_예외() {
            // given
            AdminLoginRequest request = new AdminLoginRequest("admin", "admin");

            // when & then
            assertThatThrownBy(() -> adminAuthService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(INCORRECT_PASSWORD_OR_ACCOUNT.getMessage());
        }

        @Test
        void 비밀번호가_틀리면_예외() {
            // given
            adminRepository.save(new Admin(1L, "admin", passwordEncoder.encode("admin")));
            AdminLoginRequest request = new AdminLoginRequest("admin", "password");

            // when & then
            assertThatThrownBy(() -> adminAuthService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(INCORRECT_PASSWORD_OR_ACCOUNT.getMessage());
        }

        @Test
        void 성공() {
            // given
            adminRepository.save(new Admin(1L, "admin", passwordEncoder.encode("admin")));
            AdminLoginRequest request = new AdminLoginRequest("admin", "admin");
            given(authProvider.provide(any()))
                .willReturn("token");

            // when
            String token = adminAuthService.login(request);

            // then
            assertThat(token).isEqualTo("token");
        }
    }

    @Nested
    class 가입 {

        @Test
        void 닉네임이_중복이면_예외() {
            // given
            adminRepository.save(new Admin(1L, "admin", passwordEncoder.encode("admin")));
            AdminSignupRequest request = new AdminSignupRequest("admin", "admin");

            // when & then
            assertThatThrownBy(() -> adminAuthService.signup(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(DUPLICATE_ACCOUNT_USERNAME.getMessage());
        }

        @Test
        void Root_어드민이_아니면_예외() {
            // given
            adminRepository.save(new Admin(1L, "glen", passwordEncoder.encode("admin")));
            AdminSignupRequest request = new AdminSignupRequest("newAdmin", "newAdmin");

            // when & then
            assertThatThrownBy(() -> adminAuthService.signup(1L, request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(NOT_ENOUGH_PERMISSION.getMessage());
        }

        @Test
        void 성공() {
            // given
            adminRepository.save(new Admin(1L, "admin", passwordEncoder.encode("admin")));
            AdminSignupRequest request = new AdminSignupRequest("newAdmin", "newAdmin");

            // when
            AdminSignupResponse response = adminAuthService.signup(1L, request);

            // then
            assertThat(adminRepository.existsByUsername(response.username())).isTrue();
        }
    }
}
