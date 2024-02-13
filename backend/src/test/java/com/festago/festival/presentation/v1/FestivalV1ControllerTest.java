package com.festago.festival.presentation.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.festival.application.FestivalDetailV1QueryService;
import com.festago.festival.dto.FestivalDetailV1Response;
import com.festago.festival.dto.SchoolV1Response;
import com.festago.festival.dto.SocialMediaV1Response;
import com.festago.festival.dto.StageV1Response;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.support.CustomWebMvcTest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalV1ControllerTest {

    private static final String LAST_FESTIVAL_ID = "lastFestivalId";
    private static final String LAST_START_DATE = "lastStartDate";
    private static final String SIZE = "size";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FestivalDetailV1QueryService festivalDetailV1QueryService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 축제의_식별자와_마지막_축제_시작일 {

        @Test
        void 은_들다_보내면_성공한다() throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals")
                    .param(LAST_FESTIVAL_ID, "1")
                    .param(LAST_START_DATE, "1999-10-01"))
                .andExpect(status().isOk());
        }

        @Test
        void 은_들다_안_보내면_성공한다() throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals"))
                .andExpect(status().isOk());
        }

        @Test
        void 은_하나만_보내면_실패한다() throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals")
                    .param(LAST_FESTIVAL_ID, "1"))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class 축제_페이지_갯수는 {

        @ParameterizedTest
        @ValueSource(longs = {21})
        void _21_이상이면_실패한다(long value) throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals")
                    .param(SIZE, String.valueOf(value)))
                .andExpect(status().isBadRequest());
        }

        // PageableDefault 때문에 default 값으로 설정됨
        @ParameterizedTest
        @ValueSource(longs = {-1, 0})
        void 음수_또는_0이면_성공한다(long value) throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals")
                    .param(SIZE, String.valueOf(value)))
                .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 20})
        void 최소_1과_최대_20_사이면_성공한다(long value) throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals")
                    .param(SIZE, String.valueOf(value)))
                .andExpect(status().isOk());
        }
    }

    @Nested
    class 축제_상세_조회 {

        final String uri = "/api/v1/festivals/{festivalId}";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_축제_상세_정보가_반환된다() throws Exception {
                // given
                var expect = new FestivalDetailV1Response(
                    1L,
                    "테코대학교 축제",
                    new SchoolV1Response(
                        1L,
                        "테코대학교"
                    ),
                    LocalDate.parse("2077-06-30"),
                    LocalDate.parse("2077-06-30"),
                    "https://image.com/schoolImage.png",
                    Set.of(
                        new SocialMediaV1Response(
                            SocialMediaType.INSTAGRAM,
                            "총학 인스타",
                            "https://example.com/instagram.png",
                            "https://www.instagram.com/example_university"
                        )
                    ),
                    Set.of(
                        new StageV1Response(
                            1L,
                            LocalDateTime.parse("2077-06-30T00:00:00"),
                            null // @JsonRawValue 때문에 직렬화된 JSON을 다시 역직렬화 할 때 문제가 발생함
                        )
                    )
                );
                given(festivalDetailV1QueryService.findFestivalDetail(anyLong()))
                    .willReturn(expect);
                // when & then
                String content = mockMvc.perform(get(uri, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

                // 이렇게 검증해도 괜찮은지 의문. 마치 거울보고 가위바위보를 하는 느낌이 강하게 듦
                // jsonPath를 사용하여 json 명세가 정확한지(오타, 누락) 명시적으로 검사가 필요할까??
                // 많은 andExpect(jsonPath("$.id").value(1L)) 절이 호출되어 보기에 불편하지만
                // 코드 리뷰시 DTO 내부를 헤집을 필요 없이, 테스트 코드만 보고 JSON 명세를 확인 가능한게 장점인듯
                // 또한, @JsonRawValue를 사용하여 역직렬화된 JSON을 다시 직렬화하는 것이 불가능함.
                var actual = objectMapper.readValue(content, FestivalDetailV1Response.class);
                assertThat(actual).isEqualTo(expect);
            }
        }
    }
}
