package com.festago.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.FestivalService;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FestivalService festivalService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 축제_생성() throws Exception {
        // given
        String festivalName = "테코 대학교";
        String startDate = "2023-08-02";
        String endDate = "2023-08-03";
        String thumbnail = "https://picsum.photos/536/354";

        given(festivalService.create(any()))
            .willReturn(new FestivalResponse(
                1L,
                festivalName,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate),
                thumbnail));

        FestivalCreateRequest request = new FestivalCreateRequest(
            festivalName,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate),
            "");

        // when && then
        mockMvc.perform(post("/admin/festivals")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(festivalName))
            .andExpect(jsonPath("$.startDate").value(startDate))
            .andExpect(jsonPath("$.endDate").value(endDate))
            .andExpect(jsonPath("$.thumbnail").value(thumbnail));
    }
}
