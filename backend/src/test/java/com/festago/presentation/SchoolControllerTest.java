package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.SchoolService;
import com.festago.dto.SchoolResponse;
import com.festago.dto.SchoolsResponse;
import com.festago.support.CustomWebMvcTest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest(SchoolController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SchoolControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SchoolService schoolService;

    @Test
    void 모든_학교_정보_조회() throws Exception {
        // given
        SchoolsResponse expected = new SchoolsResponse(
            List.of(
                new SchoolResponse(1L, "pooh.ac.kr", "푸우대학"),
                new SchoolResponse(2L, "ash.ac.kr", "애쉬대학")
            ));

        given(schoolService.findAll())
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(get("/schools")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        SchoolsResponse actual = objectMapper.readValue(content, SchoolsResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
