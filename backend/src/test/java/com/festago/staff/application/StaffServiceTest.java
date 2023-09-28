package com.festago.staff.application;

import static com.festago.common.exception.ErrorCode.FESTIVAL_NOT_FOUND;
import static com.festago.common.exception.ErrorCode.STAFF_CODE_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.staff.domain.StaffCode;
import com.festago.staff.dto.StaffCodeResponse;
import com.festago.staff.repository.StaffCodeRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.SchoolFixture;
import com.festago.support.SetUpMockito;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Spy
    StaffCodeProvider codeProvider;

    @Mock
    StaffCodeRepository staffCodeRepository;

    @Mock
    FestivalRepository festivalRepository;

    @InjectMocks
    StaffService staffService;


    @Nested
    class 스태프_코드_생성 {

        @BeforeEach
        void setUp() {
            School school = SchoolFixture.school().build();
            Festival festival = FestivalFixture.festival().school(school).build();

            SetUpMockito
                .given(festivalRepository.findById(anyLong()))
                .willReturn(Optional.of(festival));

            SetUpMockito
                .given(staffCodeRepository.existsByFestival(any(Festival.class)))
                .willReturn(false);

            SetUpMockito
                .given(staffCodeRepository.existsByCode(anyString()))
                .willReturn(false);

            SetUpMockito
                .given(staffCodeRepository.save(any(StaffCode.class)))
                .willAnswer(invocation -> {
                    StaffCode staffCode = invocation.getArgument(0);
                    return new StaffCode(1L, staffCode.getCode(), staffCode.getFestival());
                });

        }

        @Test
        void 축제가_없으면_예외() {
            // given
            given(festivalRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> staffService.createStaffCode(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(FESTIVAL_NOT_FOUND.getMessage());
        }

        @Test
        void 이미_스태프코드가_존재하면_예외() {
            // given
            given(staffCodeRepository.existsByFestival(any(Festival.class)))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> staffService.createStaffCode(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(STAFF_CODE_EXIST.getMessage());
        }

        @Test
        void staffCode가_중복되지_않을때까지_반복한다() {
            // given
            String firstCode = "festa1234";
            String secondCode = "festa5678";
            given(codeProvider.provide(any(Festival.class)))
                .willReturn(firstCode)
                .willReturn(secondCode);

            given(staffCodeRepository.existsByCode(anyString()))
                .willAnswer(invocation -> {
                    String code = invocation.getArgument(0);
                    return Objects.equals(code, firstCode);
                });

            // when
            StaffCodeResponse response = staffService.createStaffCode(1L);

            // then
            assertThat(response.code()).isEqualTo(secondCode);
        }
    }
}
