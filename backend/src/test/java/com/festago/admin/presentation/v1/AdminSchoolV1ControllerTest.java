package com.festago.admin.presentation.v1;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.admin.presentation.v1.dto.SchoolV1CreateRequest;
import com.festago.admin.presentation.v1.dto.SchoolV1UpdateRequest;
import com.festago.auth.domain.Role;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.application.SchoolDeleteService;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.support.CustomWebMvcTest;
import com.festago.support.WithMockAuth;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminSchoolV1ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SchoolCommandService schoolCommandService;

    @Autowired
    SchoolDeleteService schoolDeleteService;

    @Nested
    class 학교_생성 {

        final String uri = "/admin/api/v1/schools";

        @Nested
        @DisplayName("POST " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_201_응답과_Location_헤더에_식별자가_반환된다() throws Exception {
                // given
                var request = new SchoolV1CreateRequest("테코대학교", "teco.ac.kr", SchoolRegion.서울);
                given(schoolCommandService.createSchool(any(SchoolCreateCommand.class)))
                    .willReturn(1L);

                // when & then
                mockMvc.perform(post(uri)
                        .cookie(new Cookie("token", "Bearer token"))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "/api/v1/schools/1"));
            }
        }
    }

    @Nested
    class 학교_수정 {

        final String uri = "/admin/api/v1/schools/{schoolId}";

        @Nested
        @DisplayName("PATCH " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_200_응답이_반환된다() throws Exception {
                // given
                var request = new SchoolV1UpdateRequest("테코대학교", "teco.ac.kr", SchoolRegion.서울);

                // when & then
                mockMvc.perform(patch(uri, 1L)
                        .cookie(new Cookie("token", "Bearer token"))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            }
        }
    }

    @Nested
    class 학교_삭제 {

        final String uri = "/admin/api/v1/schools/{schoolId}";

        @Nested
        @DisplayName("DELETE " + uri)
        class 올바른_주소로 {

            @Test
            @WithMockAuth(role = Role.ADMIN)
            void 요청을_보내면_204_응답이_반환된다() throws Exception {
                // when & then
                mockMvc.perform(delete(uri, 1L)
                        .cookie(new Cookie("token", "Bearer token"))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
            }
        }
    }
}
