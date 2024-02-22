package com.festago.school.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.school.dto.SchoolUpdateCommand;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import com.festago.support.SchoolFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SchoolCommandServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    SchoolRepository schoolRepository;

    @Nested
    class createSchool {

        SchoolCreateCommand command = SchoolCreateCommand.builder()
            .name("테코대학교")
            .domain("teco.ac.kr")
            .region(SchoolRegion.서울)
            .build();

        @Test
        void 같은_도메인의_학교가_저장되어_있으면_예외가_발생한다() {
            // given
            schoolRepository.save(SchoolFixture.school().domain("teco.ac.kr").build());

            // when & then
            assertThatThrownBy(() -> schoolCommandService.createSchool(command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.DUPLICATE_SCHOOL_DOMAIN.getMessage());
        }

        @Test
        void 같은_이름의_학교가_저장되어_있으면_예외가_발생한다() {
            // given
            schoolRepository.save(SchoolFixture.school().name("테코대학교").build());

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
            .build();

        @BeforeEach
        void setUp() {
            school = schoolRepository.save(new School("wote.ac.kr", "우테대학교", SchoolRegion.대구));
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
        void 같은_도메인의_학교가_저장되어_있으면_예외가_발생한다() {
            // given
            Long schoolId = school.getId();
            schoolRepository.save(SchoolFixture.school().domain("teco.ac.kr").build());

            // when & then
            assertThatThrownBy(() -> schoolCommandService.updateSchool(schoolId, command))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.DUPLICATE_SCHOOL_DOMAIN.getMessage());
        }

        @Test
        void 같은_이름의_학교가_저장되어_있으면_예외가_발생한다() {
            // given
            Long schoolId = school.getId();
            schoolRepository.save(SchoolFixture.school().name("테코대학교").build());

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
            });
        }
    }
}
