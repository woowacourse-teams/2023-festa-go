package com.festago.school.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.*;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.command.SchoolCreateCommand;
import com.festago.school.dto.command.SchoolUpdateCommand;
import com.festago.school.repository.MemorySchoolRepository;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.fixture.SchoolFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SchoolCommandServiceTest {

    SchoolCommandService schoolCommandService;

    SchoolRepository schoolRepository;

    @BeforeEach
    void setUp() {
        schoolRepository = new MemorySchoolRepository();
        schoolCommandService = new SchoolCommandService(schoolRepository, mock());
    }

    @Nested
    class createSchool {

        SchoolCreateCommand command = SchoolCreateCommand.builder()
            .name("테코대학교")
            .domain("teco.ac.kr")
            .region(SchoolRegion.서울)
            .build();

        @Test
        void 같은_도메인의_학교가_저장되어_있어도_예외가_발생하지_않는다() {
            // given
            schoolRepository.save(SchoolFixture.builder().domain("teco.ac.kr").build());

            // when
            Long schoolId = schoolCommandService.createSchool(command);

            // then
            assertThat(schoolRepository.getOrThrow(schoolId).getDomain()).isEqualTo("teco.ac.kr");
        }

        @Test
        void 같은_이름의_학교가_저장되어_있으면_예외가_발생한다() {
            // given
            schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());

            // when & then
            assertThatThrownBy(() -> schoolCommandService.createSchool(command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.DUPLICATE_SCHOOL_NAME.getMessage());
        }

        @Test
        void 예외가_발생하지_않으면_학교가_저장된다() {
            // when
            Long schoolId = schoolCommandService.createSchool(command);

            // then
            assertThat(schoolRepository.findById(schoolId)).isPresent();
        }
    }

    @Nested
    class updateSchool {

        School school;
        SchoolUpdateCommand command = SchoolUpdateCommand.builder()
            .name("테코대학교")
            .domain("teco.ac.kr")
            .region(SchoolRegion.서울)
            .logoUrl("https://image.com/newLogo.png")
            .backgroundImageUrl("https://image.com/newBackgroundImage.png")
            .build();

        @BeforeEach
        void setUp() {
            school = schoolRepository.save(SchoolFixture.builder()
                .name("우테대학교")
                .domain("wote.ac.kr")
                .region(SchoolRegion.대구)
                .logoUrl("https://image.com/logo.png")
                .backgroundImageUrl("https://image.com/backgroundImage.png")
                .build()
            );
        }

        @Test
        void 식별자에_대한_학교를_찾을수_없으면_예외가_발생한다() {
            // given
            Long schoolId = 4885L;

            // when & then
            assertThatThrownBy(() -> schoolCommandService.updateSchool(schoolId, command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.SCHOOL_NOT_FOUND.getMessage());
        }

        @Test
        void 같은_도메인의_학교가_저장되어_있어도_예외가_발생하지_않는다() {
            // given
            Long schoolId = school.getId();
            schoolRepository.save(SchoolFixture.builder().domain("teco.ac.kr").build());

            // when
            schoolCommandService.updateSchool(schoolId, command);

            //  then
            assertThat(schoolRepository.getOrThrow(schoolId).getDomain()).isEqualTo("teco.ac.kr");
        }

        @Test
        void 같은_이름의_학교가_저장되어_있으면_예외가_발생한다() {
            // given
            Long schoolId = school.getId();
            schoolRepository.save(SchoolFixture.builder().name("테코대학교").build());

            // when & then
            assertThatThrownBy(() -> schoolCommandService.updateSchool(schoolId, command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.DUPLICATE_SCHOOL_NAME.getMessage());
        }

        @Test
        void 수정할_이름이_수정할_학교의_이름과_같으면_이름은_수정되지_않는다() {
            // given
            Long schoolId = school.getId();
            var command = SchoolUpdateCommand.builder()
                .name(school.getName())
                .domain("teco.ac.kr")
                .region(SchoolRegion.서울)
                .build();

            // when
            schoolCommandService.updateSchool(schoolId, command);

            // then
            School updatedSchool = schoolRepository.getOrThrow(schoolId);
            assertThat(updatedSchool.getName()).isEqualTo(school.getName());
        }

        @Test
        void 수정할_도메인이_수정할_학교의_도메인과_같으면_도메인은_수정되지_않는다() {
            // given
            Long schoolId = school.getId();
            var command = SchoolUpdateCommand.builder()
                .name("테코대학교")
                .domain(school.getDomain())
                .region(SchoolRegion.서울)
                .build();

            // when
            schoolCommandService.updateSchool(schoolId, command);

            // then
            School updatedSchool = schoolRepository.getOrThrow(schoolId);
            assertThat(updatedSchool.getDomain()).isEqualTo(school.getDomain());
        }

        @Test
        void 예외가_발생하지_않으면_학교가_수정된다() {
            // given
            Long schoolId = school.getId();

            // when
            schoolCommandService.updateSchool(schoolId, command);

            // then
            School updatedSchool = schoolRepository.getOrThrow(schoolId);
            assertSoftly(softly -> {
                softly.assertThat(updatedSchool.getName()).isEqualTo("테코대학교");
                softly.assertThat(updatedSchool.getDomain()).isEqualTo("teco.ac.kr");
                softly.assertThat(updatedSchool.getRegion()).isEqualTo(SchoolRegion.서울);
                softly.assertThat(updatedSchool.getLogoUrl()).isEqualTo("https://image.com/newLogo.png");
                softly.assertThat(updatedSchool.getBackgroundUrl())
                    .isEqualTo("https://image.com/newBackgroundImage.png");
            });
        }
    }
}
