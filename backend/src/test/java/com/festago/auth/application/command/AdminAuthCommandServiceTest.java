package com.festago.auth.application.command;

import static com.festago.common.exception.ErrorCode.DUPLICATE_ACCOUNT_USERNAME;
import static com.festago.common.exception.ErrorCode.INCORRECT_PASSWORD_OR_ACCOUNT;
import static com.festago.common.exception.ErrorCode.NOT_ENOUGH_PERMISSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.festago.admin.domain.Admin;
import com.festago.admin.repository.AdminRepository;
import com.festago.admin.repository.MemoryAdminRepository;
import com.festago.auth.application.AuthProvider;
import com.festago.auth.dto.command.AdminLoginCommand;
import com.festago.auth.dto.command.AdminSignupCommand;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.UnauthorizedException;
import com.festago.support.fixture.AdminFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminAuthCommandServiceTest {

    AdminRepository adminRepository;

    AuthProvider authProvider;

    AdminAuthCommandService adminAuthCommandService;

    @BeforeEach
    void setUp() {
        adminRepository = new MemoryAdminRepository();
        authProvider = mock(AuthProvider.class);
        adminAuthCommandService = new AdminAuthCommandService(
            authProvider,
            adminRepository,
            PasswordEncoderFactories.createDelegatingPasswordEncoder()
        );
    }

    @Nested
    class 로그인 {

        @Test
        void 계정이_없으면_예외() {
            // given
            var command = new AdminLoginCommand("admin", "password");

            // when & then
            assertThatThrownBy(() -> adminAuthCommandService.login(command))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(INCORRECT_PASSWORD_OR_ACCOUNT.getMessage());
        }

        @Test
        void 비밀번호가_틀리면_예외() {
            // given
            adminRepository.save(AdminFixture.builder()
                .username("admin")
                .password("{noop}password")
                .build());
            var command = new AdminLoginCommand("admin", "admin");

            // when & then
            assertThatThrownBy(() -> adminAuthCommandService.login(command))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(INCORRECT_PASSWORD_OR_ACCOUNT.getMessage());
        }

        @Test
        void 성공() {
            // given
            adminRepository.save(AdminFixture.builder()
                .username("admin")
                .password("{noop}password")
                .build());
            var command = new AdminLoginCommand("admin", "password");
            given(authProvider.provide(any()))
                .willReturn("token");

            // when
            var result = adminAuthCommandService.login(command);

            // then
            assertThat(result.accessToken()).isEqualTo("token");
        }
    }

    @Nested
    class 가입 {

        @Test
        void 닉네임이_중복이면_예외() {
            // given
            Admin rootAdmin = adminRepository.save(Admin.createRootAdmin("{noop}password"));
            var command = new AdminSignupCommand("admin", "password");

            // when & then
            Long rootAdminId = rootAdmin.getId();
            assertThatThrownBy(() -> adminAuthCommandService.signup(rootAdminId, command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(DUPLICATE_ACCOUNT_USERNAME.getMessage());
        }

        @Test
        void Root_어드민이_아니면_예외() {
            // given
            Admin admin = adminRepository.save(AdminFixture.builder()
                .username("glen")
                .password("{noop}password")
                .build());
            var command = new AdminSignupCommand("newAdmin", "password");

            // when & then
            Long adminId = admin.getId();
            assertThatThrownBy(() -> adminAuthCommandService.signup(adminId, command))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(NOT_ENOUGH_PERMISSION.getMessage());
        }

        @Test
        void 성공() {
            // given
            Admin rootAdmin = adminRepository.save(Admin.createRootAdmin("{noop}password"));
            var command = new AdminSignupCommand("newAdmin", "password");

            // when
            adminAuthCommandService.signup(rootAdmin.getId(), command);

            // then
            assertThat(adminRepository.existsByUsername(command.username())).isTrue();
        }
    }

    @Nested
    class 루트_어드민_초기화 {

        @Test
        void 루트_어드민을_활성화하면_저장된다() {
            // when
            adminAuthCommandService.initializeRootAdmin("1234");

            // then
            Admin rootAdmin = Admin.createRootAdmin("1234");
            assertThat(adminRepository.existsByUsername(rootAdmin.getUsername()))
                .isTrue();
        }

        @Test
        void 루트_어드민이_존재하는데_초기화하면_예외() {
            // given
            adminAuthCommandService.initializeRootAdmin("1234");

            // when & then
            assertThatThrownBy(() -> adminAuthCommandService.initializeRootAdmin("1234"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.DUPLICATE_ACCOUNT_USERNAME.getMessage());
        }
    }
}
