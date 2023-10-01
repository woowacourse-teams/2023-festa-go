package com.festago.auth.application;

import static com.festago.common.exception.ErrorCode.INCORRECT_STAFF_CODE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.StaffLoginRequest;
import com.festago.auth.dto.StaffLoginResponse;
import com.festago.common.exception.UnauthorizedException;
import com.festago.staff.domain.Staff;
import com.festago.staff.repository.StaffRepository;
import com.festago.support.SetUpMockito;
import com.festago.support.StaffFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
class StaffAuthServiceTest {

    @Mock
    AuthProvider authProvider;

    @Mock
    StaffRepository staffRepository;

    @InjectMocks
    StaffAuthService staffAuthService;

    @Nested
    class 로그인 {

        StaffLoginRequest request;
        String token;
        Long staffId;

        @BeforeEach
        void setUp() {
            request = new StaffLoginRequest("festa1234");
            token = "staffToken";
            staffId = 1L;

            Staff staff = StaffFixture.staff().id(staffId).build();

            SetUpMockito
                .given(staffRepository.findByCodeWithFetch(anyString()))
                .willReturn(Optional.of(staff));

            SetUpMockito
                .given(authProvider.provide(any(AuthPayload.class)))
                .willReturn(token);
        }

        @Test
        void 일치하는_코드가_없으면_예외() {
            // given
            given(staffRepository.findByCodeWithFetch(anyString()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> staffAuthService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(INCORRECT_STAFF_CODE.getMessage());
        }

        @Test
        void 성공() {
            // when
            StaffLoginResponse response = staffAuthService.login(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.accessToken()).isEqualTo(token);
                softly.assertThat(response.staffId()).isEqualTo(staffId);
            });
        }
    }
}
