package com.festago.presentation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.student.application.StudentService;
import com.festago.student.dto.StudentResponse;
import com.festago.student.dto.StudentSchoolResponse;
import com.festago.student.dto.StudentSendMailRequest;
import com.festago.student.dto.StudentVerificateRequest;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StudentService studentService;

    @Nested
    class 학생_인증_메일_전송 {

        @Test
        void 인증이_되지_않으면_401() throws Exception {
            // given
            StudentSendMailRequest request = new StudentSendMailRequest("user", 1L);

            // when & then
            mockMvc.perform(post("/students/send-verification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockAuth
        void 학교_인증_요청() throws Exception {
            // given
            StudentSendMailRequest request = new StudentSendMailRequest("user", 1L);

            // when & then
            mockMvc.perform(post("/students/send-verification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        }
    }

    @Nested
    class 학생_인증 {

        @Test
        void 인증이_되지_않으면_401() throws Exception {
            // given
            StudentVerificateRequest request = new StudentVerificateRequest("123456");

            // when & then
            mockMvc.perform(post("/students/verification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockAuth
        void 학교_인증_요청() throws Exception {
            // given
            StudentVerificateRequest request = new StudentVerificateRequest("123456");

            // when & then
            mockMvc.perform(post("/students/verification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        }
    }

    @Nested
    class 학생_인증_정보_조회 {

        @Test
        void 인증이_되지_않으면_401() throws Exception {
            // given
            StudentVerificateRequest request = new StudentVerificateRequest("123456");

            // when & then
            mockMvc.perform(get("/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockAuth
        void 학생_인증_조회() throws Exception {
            // given
            StudentResponse expected = new StudentResponse(true, new StudentSchoolResponse(1L, "테코대학교", "teco.ac.kr"));
            given(studentService.findVerification(anyLong()))
                .willReturn(expected);

            // when & then
            String content = mockMvc.perform(get("/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
            StudentResponse actual = objectMapper.readValue(content, StudentResponse.class);
            assertThat(actual).isEqualTo(expected);
        }
    }
}
