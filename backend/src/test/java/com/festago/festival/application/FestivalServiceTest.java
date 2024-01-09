package com.festago.festival.application;

import static com.festago.common.exception.ErrorCode.INVALID_FESTIVAL_START_DATE;
import static com.festago.common.exception.ErrorCode.SCHOOL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.repository.StageRepository;
import com.festago.support.SchoolFixture;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
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
class FestivalServiceTest {

    @Mock
    FestivalRepository festivalRepository;

    @Mock
    StageRepository stageRepository;

    @Spy
    Clock clock = Clock.systemDefaultZone();

    @Mock
    SchoolRepository schoolRepository;

    @InjectMocks
    FestivalService festivalService;

    @Nested
    class 축제_생성 {

        @Test
        void 학교가_없으면_예외() {
            // given
            LocalDate today = LocalDate.now();
            Long schoolId = 1L;
            FestivalCreateRequest request = new FestivalCreateRequest("테코대학교", today, today, "http://image.png", 1L);

            given(schoolRepository.findById(schoolId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> festivalService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(SCHOOL_NOT_FOUND.getMessage());
        }

        @Test
        void 축제_생성시_시작일자가_과거이면_예외() {
            // given
            LocalDate today = LocalDate.now();
            School school = SchoolFixture.school().build();
            FestivalCreateRequest request = new FestivalCreateRequest("테코대학교", today.minusDays(1), today,
                "http://image.png", 1L);
            given(schoolRepository.findById(anyLong()))
                .willReturn(Optional.of(school));

            // when & then
            assertThatThrownBy(() -> festivalService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(INVALID_FESTIVAL_START_DATE.getMessage());
        }

        @Test
        void 성공() {
            // given
            LocalDate today = LocalDate.now();
            String name = "테코대학교";
            String thumbnail = "http://image.png";
            Long schoolId = 1L;
            School school = SchoolFixture.school().id(schoolId).build();
            FestivalCreateRequest request = new FestivalCreateRequest(name, today, today, thumbnail, schoolId);
            Festival festival = new Festival(1L, name, today, today, thumbnail, school);
            FestivalResponse expected = new FestivalResponse(1L, 1L, name, today, today, thumbnail);
            given(schoolRepository.findById(schoolId))
                .willReturn(Optional.of(school));
            given(festivalRepository.save(any()))
                .willReturn(festival);

            // when
            FestivalResponse actual = festivalService.create(request);

            // then
            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
        }
    }
}
