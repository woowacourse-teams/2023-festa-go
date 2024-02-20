package com.festago.festival.presentation.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.support.CustomWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
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
}
