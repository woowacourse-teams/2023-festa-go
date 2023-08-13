package com.festago.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

import com.festago.auth.domain.Admin;
import com.festago.auth.domain.AdminRepository;
import com.festago.auth.domain.AuthProvider;
import com.festago.auth.domain.SocialType;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.AdminSignupRequest;
import com.festago.auth.dto.AdminSignupResponse;
import com.festago.domain.Member;
import com.festago.exception.BadRequestException;
import com.festago.exception.UnauthorizedException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AdminAuthServiceTest {

    @Mock
    AuthProvider authProvider;

    @Mock
    AdminRepository adminRepository;

    @InjectMocks
    AdminAuthService adminAuthService;

    @Nested
    class 로그인 {

        @Test
        void 계정이_없으면_예외() {
            // given
            AdminLoginRequest request = new AdminLoginRequest("admin", "admin");
            given(adminRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> adminAuthService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("비밀번호가 틀렸거나, 해당 계정이 없습니다.");
        }

        @Test
        void 비밀번호가_틀리면_예외() {
            // given
            Member member = new Member(1L, "admin", SocialType.FESTAGO, "admin", "");
            Admin admin = new Admin(1L, "admin", "admin", member);
            AdminLoginRequest request = new AdminLoginRequest("admin", "password");
            given(adminRepository.findByUsername(anyString()))
                .willReturn(Optional.of(admin));

            // when & then
            assertThatThrownBy(() -> adminAuthService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("비밀번호가 틀렸거나, 해당 계정이 없습니다.");
        }

        @Test
        void 성공() {
            // given
            Member member = new Member(1L, "admin", SocialType.FESTAGO, "admin", "");
            Admin admin = new Admin(1L, "admin", "admin", member);
            AdminLoginRequest request = new AdminLoginRequest("admin", "admin");
            given(adminRepository.findByUsername(anyString()))
                .willReturn(Optional.of(admin));
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
            AdminSignupRequest request = new AdminSignupRequest("admin", "admin");
            given(adminRepository.existsByUsername(anyString()))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> adminAuthService.signup(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 계정이 존재합니다.");
        }

        @Test
        void 성공() {
            // given
            Member member = new Member(1L, "admin", SocialType.FESTAGO, "admin", "");
            Admin admin = new Admin(1L, "admin", "admin", member);
            AdminSignupRequest request = new AdminSignupRequest("admin", "admin");
            given(adminRepository.save(any(Admin.class)))
                .willReturn(admin);

            // when
            AdminSignupResponse response = adminAuthService.signup(request);

            // then
            assertThat(response.username()).isEqualTo("admin");
        }
    }
}
