package com.festago.school.intergration;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.NotFoundException;
import com.festago.school.application.SchoolService;
import com.festago.school.domain.School;
import com.festago.school.dto.SchoolCreateRequest;
import com.festago.school.dto.SchoolResponse;
import com.festago.school.dto.SchoolUpdateRequest;
import com.festago.school.dto.SchoolsResponse;
import com.festago.school.repository.SchoolRepository;
import com.festago.support.ApplicationIntegrationTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class SchoolServiceIntegration extends ApplicationIntegrationTest {

    @Autowired
    SchoolService schoolService;

    @Autowired
    SchoolRepository schoolRepository;

    @Nested
    class 학교_생성시 {

        @Test
        void 성공() {
            // given
            SchoolCreateRequest expected = new SchoolCreateRequest("domain.com", "name");

            // when
            SchoolResponse actual = schoolService.create(expected);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(actual.id()).isPositive();
                softly.assertThat(actual.domain()).isEqualTo(expected.domain());
                softly.assertThat(actual.name()).isEqualTo(expected.name());
            });
        }

        @Test
        void 중복된_도메인이_존재하면_예외() {
            // given
            School savedSchool = schoolRepository.save(new School("domain.com", "name"));

            // when && then
            assertThatThrownBy(() -> schoolService.create(new SchoolCreateRequest(savedSchool.getDomain(), "otherName")))
                    .isInstanceOf(BadRequestException.class);
        }

        @Test
        void 중복된_이름이_존재하면_예외() {
            // given
            School savedSchool = schoolRepository.save(new School("domain.com", "name"));

            // when && then
            assertThatThrownBy(() -> schoolService.create(new SchoolCreateRequest("otherDomain.com", savedSchool.getName())))
                    .isInstanceOf(BadRequestException.class);
        }
    }

    @Test
    void 모든_학교를_조회한다() {
        // given
        schoolRepository.save(new School("domain.com", "name1"));
        schoolRepository.save(new School("domain.kr", "name2"));
        schoolRepository.save(new School("domain.jp", "name3"));

        // when
        SchoolsResponse actual = schoolService.findAll();

        // then
        assertThat(actual.schools()).hasSize(3);
    }

    @Test
    void 단건_학교를_조회한다() {
        // given
        School expected = schoolRepository.save(new School("domain.com", "name"));

        // when
        SchoolResponse actual = schoolService.findById(expected.getId());

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.id()).isPositive();
            softly.assertThat(actual.domain()).isEqualTo(expected.getDomain());
            softly.assertThat(actual.name()).isEqualTo(expected.getName());
        });
    }

    @Test
    void 단건_조회시_없으면_예외() {
        // when && then
        assertThatThrownBy(() -> schoolService.findById(-1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 학교_정보를_수정한다() {
        // given
        Long savedSchoolId = schoolRepository.save(new School("domain.com", "name")).getId();
        SchoolUpdateRequest expected = new SchoolUpdateRequest("newDomain.com", "newName");

        // when
        schoolService.update(savedSchoolId, expected);

        // then
        School actual = schoolRepository.findById(savedSchoolId).get();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(savedSchoolId);
            softly.assertThat(actual.getDomain()).isEqualTo(expected.domain());
            softly.assertThat(actual.getName()).isEqualTo(expected.name());
        });
    }

    @Test
    void 학교를_삭제한다() {
        // given
        Long savedSchoolId = schoolRepository.save(new School("domain.com", "name")).getId();

        // when
        schoolService.delete(savedSchoolId);

        // then
        assertThat(schoolRepository.findById(savedSchoolId)).isEmpty();
    }
}
