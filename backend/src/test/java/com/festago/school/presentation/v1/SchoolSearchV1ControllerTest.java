package com.festago.school.presentation.v1;

import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.school.application.v1.SchoolTotalSearchV1QueryService;
import com.festago.school.dto.v1.SchoolSearchRecentFestivalV1Response;
import com.festago.school.dto.v1.SchoolTotalSearchV1Response;
import com.festago.support.CustomWebMvcTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolSearchV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SchoolTotalSearchV1QueryService schoolTotalSearchV1QueryService;

    @Nested
    class 학교_상세_조회 {

        final String uri = "/api/v1/search/schools";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_보내면_200_응답과_학교_목록이_반환된다() throws Exception {
                // given
                LocalDate festivalStartDate = LocalDate.now();

                var response = List.of(
                    new SchoolTotalSearchV1Response(1L, "테코대학교", "https://image.com/logo1.png",
                        new SchoolSearchRecentFestivalV1Response(
                            1L,
                            festivalStartDate
                        )),
                    new SchoolTotalSearchV1Response(2L, "우테대학교", "https://image.com/logo2.png", null)
                );
                given(schoolTotalSearchV1QueryService.searchSchools(anyString()))
                    .willReturn(response);

                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("keyword", "테코대학교"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
            }

            @Test
            void 쿼리_파라미터_keyword가_2글자_미만이면_400_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("keyword", "1"))
                    .andExpect(status().isBadRequest());
            }

            @Test
            void 쿼리_파라미터_keyword가_blank_이면_400_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("keyword", " "))
                    .andExpect(status().isBadRequest());
            }
        }
    }
}
