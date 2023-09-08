package com.festago.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.application.StudentService;
import com.festago.dto.StudentSendMailRequest;
import com.festago.dto.StudentVerificateRequest;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@CustomWebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
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
}
