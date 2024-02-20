package com.festago.festival.presentation.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalV1ControllerTest {

    private static final String LAST_FESTIVAL_ID_KEY = "lastFestivalId";
    private static final String LAST_START_DATE_KEY = "lastStartDate";
    private static final String SIZE_KEY = "size";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FestivalDetailV1QueryService festivalDetailV1QueryService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 축제_목록_커서_기반_페이징_조회 {

        final String uri = "/api/v1/festivals";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 쿼리_파라미터에_festivalId와_lastStartDate를_모두_보내면_200_응답이_반환된다() throws Exception {
                mockMvc.perform(get(uri)
                        .param(LAST_FESTIVAL_ID_KEY, "1")
                        .param(LAST_START_DATE_KEY, "1999-10-01"))
                    .andExpect(status().isOk());
            }

            @Test
            void 쿼리_파라미터에_festivalId와_lastStartDate를_보내지_않아도_200_응답이_반환된다() throws Exception {
                mockMvc.perform(get(uri))
                    .andExpect(status().isOk());
            }

            @CsvSource(value = {"1,''", "'',2077-06-30"})
            @ParameterizedTest
            void 쿼리_파라미터에_festivalId_또는_lastStartDate_중_하나만_보내면_400_응답이_반환된다(
                String festivalId, String lastStartDate
            ) throws Exception {
                mockMvc.perform(get(uri)
                        .param(LAST_FESTIVAL_ID_KEY, festivalId)
                        .param(LAST_START_DATE_KEY, lastStartDate))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("festivalId, lastStartDate 두 값 모두 요청하거나 요청하지 않아야합니다."));
            }

            @ParameterizedTest
            @ValueSource(strings = {"21"})
            void 쿼리_파라미터에_size가_20을_초과하면_400_응답이_반환된다(String size) throws Exception {
                mockMvc.perform(get(uri)
                        .param(SIZE_KEY, size))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("최대 size 값을 초과했습니다."));
            }

            @ParameterizedTest
            @ValueSource(strings = {"1", "20"})
            void 쿼리_파라미터에_size가_1_에서_20_사이면_200_응답이_반환된다(String size) throws Exception {
                // given && when && then
                mockMvc.perform(get(uri)
                        .param(SIZE_KEY, size))
                    .andExpect(status().isOk());
            }
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
