package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.application.FestivalService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketingRequest;
import com.festago.exception.NotFoundException;
import com.festago.support.DatabaseClearExtension;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(DatabaseClearExtension.class)
@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketServiceIntegrationTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FestivalService festivalService;

    @Autowired
    StageService stageService;

    @Autowired
    MemberTicketRepository memberTicketRepository;

    @Test
    void 공연이_없으면_예외() {
        // given
        String entryTime = "2023-07-26T18:00:00";
        long invalidStageId = 0L;
        int totalAmount = 100;

        TicketCreateRequest request = new TicketCreateRequest(invalidStageId, TicketType.VISITOR,
            totalAmount, LocalDateTime.parse(entryTime));

        // when && then
        assertThatThrownBy(() -> ticketService.create(request))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("존재하지 않은 공연입니다.");
    }

    @Test
    @Sql("/ticketing-test-data.sql")
    void 예약() throws InterruptedException {
        // given
        Member member = memberRepository.findById(1L).get();
        TicketingRequest request = new TicketingRequest(1L);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ticketService.ticketing(member.getId(), request);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        assertThat(memberTicketRepository.count()).isEqualTo(100);
    }

}
