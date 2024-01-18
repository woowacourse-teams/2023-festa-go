package com.festago.school.presentation.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.application.SchoolV1QueryService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.presentation.v1.dto.SchoolV1Response;
import com.festago.support.CustomWebMvcTest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SchoolV1QueryService schoolV1QueryService;

    @Nested
    class 모든_학교_정보_조회 {

        final String uri = "/api/v1/schools";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_하면_200_응답과_학교_정보_목록이_반환된다() throws Exception {
                // given
                var expected = List.of(
                    new SchoolV1Response(1L, "teco.ac.kr", "테코대학교", SchoolRegion.서울),
                    new SchoolV1Response(2L, "wote.ac.kr", "우테대학교", SchoolRegion.부산)
                );
                given(schoolV1QueryService.findAll(any(SearchCondition.class)))
                    .willReturn(new PageImpl<>(expected));

                // when & then
                mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.size()").value(2));
            }
        }
    }

    @Nested
    class 단일_학교_정보_조회 {

        final String uri = "/api/v1/schools/{schoolId}";

        @Nested
        @DisplayName("GET " + uri)
        class 올바른_주소로 {

            @Test
            void 요청을_하면_200_응답과_학교_정보가_반환된다() throws Exception {
                // given
                var expected = new SchoolV1Response(1L, "teco.ac.kr", "테코대학교", SchoolRegion.서울);
                given(schoolV1QueryService.findById(anyLong()))
                    .willReturn(expected);

                // when & then
                String content = mockMvc.perform(get(uri, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);
                var actual = objectMapper.readValue(content, SchoolV1Response.class);

                assertThat(actual).isEqualTo(expected);
            }
        }
    }
}
