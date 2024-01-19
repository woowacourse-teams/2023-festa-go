package com.festago.festival.presentation.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.support.CustomWebMvcTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestivalV1ControllerTest {

    private static final String LAST_FESTIVAL_ID = "lastFestivalId";
    private static final String LAST_START_DATE = "lastStartDate";
    private static final String LIMIT = "limit";

    @Autowired
    MockMvc mockMvc;

    @Nested
    class 페스티벌_아이디와_마지막_축제_시작일 {

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
        @ValueSource(longs = {0, 21})
        void 최소_1과_20값_사이가_아닐_경우_실패한다(long value) throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals")
                    .param(LIMIT, String.valueOf(value)))
                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 20})
        void 최소_1과_최대_20_사이면_성공한다(long value) throws Exception {
            // given && when && then
            mockMvc.perform(get("/api/v1/festivals")
                    .param(LIMIT, String.valueOf(value)))
                .andExpect(status().isOk());
        }
    }
}
