package com.festago.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.FestivalService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.domain.TicketType;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import com.festago.dto.StageCreateRequest;
import com.festago.dto.StageResponse;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @MockBean
    StageService stageService;

    @MockBean
    TicketService ticketService;

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

    @Test
    void 존재_하지_않는_축제_무대_생성_예외() throws Exception {
        // given
        String startTime = "2023-07-27T18:00:00";
        String lineUp = "글렌, 애쉬, 오리, 푸우";
        String ticketOpenTime = "2023-07-26T18:00:00";
        long festivalId = 1L;

        given(stageService.create(any()))
            .willThrow(new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));

        StageCreateRequest request = new StageCreateRequest(
            LocalDateTime.parse(startTime),
            lineUp,
            LocalDateTime.parse(ticketOpenTime),
            festivalId);

        // when && then
        mockMvc.perform(post("/admin/stages")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("FESTIVAL_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("존재하지 않는 축제입니다."));
    }

    @Test
    void 무대_생성() throws Exception {
        // given
        String startTime = "2023-07-27T18:00:00";
        String lineUp = "글렌, 애쉬, 오리, 푸우";
        String ticketOpenTime = "2023-07-26T18:00:00";
        long festivalId = 1L;

        StageCreateRequest request = new StageCreateRequest(
            LocalDateTime.parse(startTime),
            lineUp,
            LocalDateTime.parse(ticketOpenTime),
            festivalId);

        given(stageService.create(any()))
            .willReturn(new StageResponse(festivalId, LocalDateTime.parse(startTime)));

        // when && then
        mockMvc.perform(post("/admin/stages")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(festivalId))
            .andExpect(jsonPath("$.startTime").value(startTime));
    }

    @Test
    void 존재_하지_않는_무대_티켓_예외() throws Exception {
        // given
        String entryTime = "2023-07-27T18:00:00";

        TicketCreateRequest request = new TicketCreateRequest(1L,
            TicketType.VISITOR,
            100,
            LocalDateTime.parse(entryTime)
        );

        given(ticketService.create(any()))
            .willThrow(new NotFoundException(ErrorCode.STAGE_NOT_FOUND));

        // when && then
        mockMvc.perform(post("/admin/tickets")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("STAGE_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("존재하지 않은 공연입니다."));
    }

    @Test
    void 티켓_생성() throws Exception {
        // given
        String entryTime = "2023-07-27T18:00:00";
        int totalAmount = 100;
        long stageId = 1L;
        TicketType ticketType = TicketType.VISITOR;

        TicketCreateRequest request = new TicketCreateRequest(stageId,
            ticketType,
            totalAmount,
            LocalDateTime.parse(entryTime)
        );

        long ticketId = 1L;

        given(ticketService.create(any()))
            .willReturn(new TicketResponse(ticketId,
                stageId,
                ticketType,
                totalAmount,
                LocalDateTime.parse(entryTime)));

        // when && then
        mockMvc.perform(post("/admin/tickets")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ticketId))
            .andExpect(jsonPath("$.stageId").value(stageId))
            .andExpect(jsonPath("$.ticketType").value(ticketType.name()))
            .andExpect(jsonPath("$.totalAmount").value(totalAmount));
    }
}
