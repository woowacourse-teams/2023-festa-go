package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.application.AdminService;
import com.festago.auth.application.AdminAuthService;
import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.Role;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.exception.dto.ErrorResponse;
import com.festago.festival.application.FestivalService;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalResponse;
import com.festago.school.application.SchoolService;
import com.festago.school.dto.SchoolCreateRequest;
import com.festago.school.dto.SchoolResponse;
import com.festago.school.dto.SchoolUpdateRequest;
import com.festago.stage.application.StageService;
import com.festago.stage.dto.StageCreateRequest;
import com.festago.stage.dto.StageResponse;
import com.festago.stage.dto.StageUpdateRequest;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import com.festago.ticket.application.TicketService;
import com.festago.ticket.domain.TicketType;
import com.festago.ticket.dto.TicketCreateRequest;
import com.festago.ticket.dto.TicketCreateResponse;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

    @MockBean
    SchoolService schoolService;

    @SpyBean
    AuthExtractor authExtractor;

    @Test
    @WithMockAuth
    void 토큰의_Role이_어드민이_아니면_404_NotFound() throws Exception {
        // when & then
        mockMvc.perform(get("/admin")
                .cookie(new Cookie("token", "token")))
            .andExpect(status().isNotFound());
    }

    @Test
    void 쿠키에_토큰이_없으면_404_NotFound() throws Exception {
        // when & then
        mockMvc.perform(get("/admin"))
            .andExpect(status().isNotFound());
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
        String content = mockMvc.perform(post("/admin/api/festivals")
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
        String content = mockMvc.perform(post("/admin/api/stages")
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

        StageResponse expected = new StageResponse(festivalId, festivalId, LocalDateTime.parse(startTime),
            LocalDateTime.parse(ticketOpenTime), lineUp);

        given(stageService.create(any()))
            .willReturn(expected);

        // when && then
        String content = mockMvc.perform(post("/admin/api/stages")
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
    void 무대_수정() throws Exception {
        // given
        String startTime = "2023-07-27T18:00:00";
        String ticketOpenTime = "2023-07-26T18:00:00";
        String lineUp = "글렌, 애쉬, 오리, 푸우";

        StageUpdateRequest request = new StageUpdateRequest(LocalDateTime.parse(startTime),
            LocalDateTime.parse(ticketOpenTime), lineUp);

        // when & then
        mockMvc.perform(patch("/admin/api/stages/{id}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 무대_삭제() throws Exception {
        // when & then
        mockMvc.perform(delete("/admin/api/stages/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk());
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
        String content = mockMvc.perform(post("/admin/api/tickets")
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
        String content = mockMvc.perform(post("/admin/api/tickets")
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

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_생성() throws Exception {
        // given
        String domain = "teco.ac.kr";
        String name = "테코대학교";

        SchoolCreateRequest request = new SchoolCreateRequest(domain, name);
        SchoolResponse expected = new SchoolResponse(1L, domain, name);
        given(schoolService.create(any(SchoolCreateRequest.class)))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(post("/admin/api/schools")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        SchoolResponse actual = objectMapper.readValue(content, SchoolResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_생성_name_null이면_에외() throws Exception {
        // given
        SchoolCreateRequest request = new SchoolCreateRequest("teco.ac.kr", null);

        // when & then
        mockMvc.perform(post("/admin/api/schools")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_생성_domain_null이면_에외() throws Exception {
        // given
        SchoolCreateRequest request = new SchoolCreateRequest(null, "테코대학교");

        // when & then
        mockMvc.perform(post("/admin/api/schools")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_수정() throws Exception {
        // given
        SchoolUpdateRequest request = new SchoolUpdateRequest("teco.ac.kr", "테코대학교");

        // when & then
        mockMvc.perform(patch("/admin/api/schools/{id}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_수정_name_null이면_에외() throws Exception {
        // given
        SchoolUpdateRequest request = new SchoolUpdateRequest("teco.ac.kr", null);

        // when & then
        mockMvc.perform(patch("/admin/api/schools/{id}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_수정_domain_null이면_에외() throws Exception {
        // given
        SchoolUpdateRequest request = new SchoolUpdateRequest(null, "테코대학교");

        // when & then
        mockMvc.perform(patch("/admin/api/schools/{id}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 존재_하지_않는_학교_수정_예외() throws Exception {
        // given
        SchoolUpdateRequest request = new SchoolUpdateRequest("teco.ac.kr", "테코대학교");

        willThrow(new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND))
            .given(schoolService).update(anyLong(), any(SchoolUpdateRequest.class));

        // when & then
        mockMvc.perform(patch("/admin/api/schools/{id}", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockAuth(role = Role.ADMIN)
    void 학교_삭제() throws Exception {
        // when & then
        mockMvc.perform(delete("/admin/api/schools/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", "token")))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
