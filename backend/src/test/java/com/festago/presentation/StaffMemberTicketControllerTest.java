package com.festago.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.festago.domain.EntryState;
import com.festago.dto.TicketValidationRequest;
import com.festago.dto.TicketValidationResponse;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StaffMemberTicketControllerTest extends ControllerTest {

    @Test
    void QR을_검사한다() throws Exception {
        // given
        TicketValidationRequest request = new TicketValidationRequest("anyCode");
        TicketValidationResponse expected = new TicketValidationResponse(EntryState.AFTER_ENTRY);
        given(entryService.validate(eq(request)))
            .willReturn(expected);

        // when & then
        String content = mockMvc.perform(post("/staff/member-tickets/validation")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
        TicketValidationResponse actual = objectMapper.readValue(content,
            TicketValidationResponse.class);
        assertThat(actual).isEqualTo(expected);
    }
}
