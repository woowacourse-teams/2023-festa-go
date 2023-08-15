package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.AdminService;
import com.festago.application.FestivalService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.auth.application.AdminAuthService;
import com.festago.auth.domain.Role;
import com.festago.domain.TicketType;
import com.festago.dto.ErrorResponse;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import com.festago.dto.StageCreateRequest;
import com.festago.dto.StageResponse;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketCreateResponse;
import com.festago.exception.ErrorCode;
import com.festago.exception.NotFoundException;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest(AdminController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FestivalService festivalService;

    @MockBean
    StageService stageService;

    @MockBean
    TicketService ticketService;

    @MockBean
    AdminService adminService;

    @MockBean
    AdminAuthService adminAuthService;

    @Test
    @WithMockAuth
    void 토큰의_Role이_어드민이_아니면_404_NotFound() throws Exception {
        mockMvc.perform(get("/admin")
                .cookie(new Cookie("token", "token")))
            .andExpect(status().isNotFound());
    }

    @Test
    void 쿠키에_토큰이_없으면_로그인_페이지로_리다이렉트() throws Exception {
        mockMvc.perform(get("/admin"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    void 권한이_없어도_로그인_페이지_접속_가능() throws Exception {
        mockMvc.perform(get("/admin/login"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 축제_생성() throws Exception {
        // given
        String festivalName = "테코 대학교";
        String startDate = "2023-08-02";
        String endDate = "2023-08-03";
        String thumbnail = "https://picsum.photos/536/354";

        FestivalCreateRequest request = new FestivalCreateRequest(
            festivalName,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate),
            "");

        FestivalResponse expected = new FestivalResponse(
            1L,
            festivalName,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate),
            thumbnail);

        given(festivalService.create(any()))
            .willReturn(expected);

        // when && then
        String content = mockMvc.perform(post("/admin/festivals")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        FestivalResponse actual = objectMapper.readValue(content, FestivalResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 존재_하지_않는_축제_무대_생성_예외() throws Exception {
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

        NotFoundException exception = new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND);
        ErrorResponse expected = ErrorResponse.from(exception);

        given(stageService.create(any()))
            .willThrow(exception);

        // when && then
        String content = mockMvc.perform(post("/admin/stages")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        ErrorResponse actual = objectMapper.readValue(content, ErrorResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
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

        StageResponse expected = new StageResponse(festivalId, LocalDateTime.parse(startTime));

        given(stageService.create(any()))
            .willReturn(expected);

        // when && then
        String content = mockMvc.perform(post("/admin/stages")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        StageResponse actual = objectMapper.readValue(content, StageResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 존재_하지_않는_무대_티켓_예외() throws Exception {
        // given
        String entryTime = "2023-07-27T18:00:00";

        TicketCreateRequest request = new TicketCreateRequest(1L,
            TicketType.VISITOR,
            100,
            LocalDateTime.parse(entryTime)
        );

        NotFoundException exception = new NotFoundException(ErrorCode.STAGE_NOT_FOUND);

        ErrorResponse expected = ErrorResponse.from(exception);

        given(ticketService.create(any()))
            .willThrow(exception);

        // when && then
        String content = mockMvc.perform(post("/admin/tickets")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        ErrorResponse actual = objectMapper.readValue(content, ErrorResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 티켓_생성() throws Exception {
        // given
        long ticketId = 1L;
        String entryTime = "2023-07-27T18:00:00";
        int totalAmount = 100;
        long stageId = 1L;
        TicketType ticketType = TicketType.VISITOR;

        TicketCreateRequest request = new TicketCreateRequest(stageId,
            ticketType,
            totalAmount,
            LocalDateTime.parse(entryTime)
        );

        TicketCreateResponse expected = new TicketCreateResponse(ticketId);

        given(ticketService.create(any()))
            .willReturn(expected);

        // when && then
        String content = mockMvc.perform(post("/admin/tickets")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        TicketCreateResponse actual = objectMapper.readValue(content, TicketCreateResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
