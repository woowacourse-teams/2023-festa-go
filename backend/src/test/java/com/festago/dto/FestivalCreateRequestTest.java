package com.festago.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.school.domain.School;
import com.festago.support.SchoolFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalCreateRequestTest {

    @Test
    void 섬네일이_없는_요청은_이미지가_디폴트로_설정된다() {
        // given
        FestivalCreateRequest request = new FestivalCreateRequest(
            "name",
            LocalDate.now(),
            LocalDate.now().plusDays(2L),
            "",
            1L);
        School school = SchoolFixture.school().build();

        // when
        Festival festival = request.toEntity(school);

        // then
        assertThat(festival.getThumbnail()).isEqualTo("https://picsum.photos/536/354");
    }

    @Test
    void 섬네일이_없는_요청은_이미지를_요청_정보로_생성한다() {
        // given
        FestivalCreateRequest request = new FestivalCreateRequest(
            "name",
            LocalDate.now(),
            LocalDate.now().plusDays(2L),
            "img",
            1L);
        School school = SchoolFixture.school().build();

        // when
        Festival festival = request.toEntity(school);

        // then
        assertThat(festival.getThumbnail()).isEqualTo("img");
    }

}
