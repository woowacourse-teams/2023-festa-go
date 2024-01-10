package com.festago.auth.application;

import static com.festago.common.exception.ErrorCode.DUPLICATE_ACCOUNT_USERNAME;
import static com.festago.common.exception.ErrorCode.INCORRECT_PASSWORD_OR_ACCOUNT;
import static com.festago.common.exception.ErrorCode.NOT_ENOUGH_PERMISSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.reset;

import com.festago.admin.domain.Admin;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminAuthServiceTest {

    AuthProvider authProvider = mock();

    MemoryAdminRepository adminRepository = new MemoryAdminRepository();

    AdminAuthService adminAuthService = new AdminAuthService(
        authProvider,
        adminRepository,
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
    );

    @BeforeEach
    void setUp() {
        adminRepository.clear();
        reset(authProvider);
    }

    @Nested
    class 로그인 {

        @Test
        void 계정이_없으면_예외() {
            // given
            AdminLoginRequest request = new AdminLoginRequest("admin", "password");

            // when & then
            assertThatThrownBy(() -> adminAuthService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(INCORRECT_PASSWORD_OR_ACCOUNT.getMessage());
        }

        @Test
        void 비밀번호가_틀리면_예외() {
            // given
            adminRepository.save(new Admin("admin", "{noop}password"));
            AdminLoginRequest request = new AdminLoginRequest("admin", "admin");

            // when & then
            assertThatThrownBy(() -> adminAuthService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(INCORRECT_PASSWORD_OR_ACCOUNT.getMessage());
        }

        @Test
        void 성공() {
            // given
            adminRepository.save(new Admin("admin", "{noop}password"));
            AdminLoginRequest request = new AdminLoginRequest("admin", "password");
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
            Admin rootAdmin = adminRepository.save(new Admin("admin", "{noop}password"));
            AdminSignupRequest request = new AdminSignupRequest("admin", "password");

            // when & then
            Long rootAdminId = rootAdmin.getId();
            assertThatThrownBy(() -> adminAuthService.signup(rootAdminId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(DUPLICATE_ACCOUNT_USERNAME.getMessage());
        }

        @Test
        void Root_어드민이_아니면_예외() {
            // given
            Admin admin = adminRepository.save(new Admin("glen", "{noop}password"));
            AdminSignupRequest request = new AdminSignupRequest("newAdmin", "password");

            // when & then
            Long adminId = admin.getId();
            assertThatThrownBy(() -> adminAuthService.signup(adminId, request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(NOT_ENOUGH_PERMISSION.getMessage());
        }

        @Test
        void 성공() {
            // given
            Admin rootAdmin = adminRepository.save(new Admin("admin", "{noop}password"));
            AdminSignupRequest request = new AdminSignupRequest("newAdmin", "password");

            // when
            AdminSignupResponse response = adminAuthService.signup(rootAdmin.getId(), request);

            // then
            assertThat(adminRepository.existsByUsername(response.username())).isTrue();
        }
    }
}
