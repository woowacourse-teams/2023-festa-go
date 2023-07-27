package com.festago.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.domain.TicketType;
import com.festago.dto.TicketCreateRequest;
import com.festago.exception.NotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketServiceTest extends ApplicationTest {

    @Autowired
    TicketService ticketService;

    @Test
    void 공연이_없으면_예외() {
        // given
        String entryTime = "2023-07-26T18:00:00";
        long invliadStageId = 1L;
        int totalAmount = 100;

        TicketCreateRequest request = new TicketCreateRequest(invliadStageId, TicketType.VISITOR,
            totalAmount, LocalDateTime.parse(entryTime));

        // when && then
        assertThatThrownBy(() -> ticketService.create(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("존재하지 않은 공연입니다.");
    }
}
