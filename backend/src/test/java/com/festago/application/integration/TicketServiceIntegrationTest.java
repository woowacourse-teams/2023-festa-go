package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.festago.application.TicketService;
import com.festago.domain.Festival;
import com.festago.domain.FestivalRepository;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicketRepository;
import com.festago.domain.Stage;
import com.festago.domain.StageRepository;
import com.festago.domain.TicketRepository;
import com.festago.domain.TicketType;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketingRequest;
import com.festago.exception.BadRequestException;
import com.festago.exception.NotFoundException;
import com.festago.support.FestivalFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketServiceIntegrationTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberTicketRepository memberTicketRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Test
    void 공연이_없으면_예외() {
        // given
        String entryTime = "2023-07-26T18:00:00";
        long invliadStageId = 0L;
        int totalAmount = 100;

        TicketCreateRequest request = new TicketCreateRequest(invliadStageId, TicketType.VISITOR,
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

    @Test
    void 티켓_최초_생성시_입장시간이_유효하지_않으면_티켓도_생성되지않는다() {
        // given
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        LocalDateTime now = LocalDateTime.now();
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).startTime(now.plusHours(10))
            .ticketOpenTime(now.plusHours(1)).build());
        LocalDateTime entryTime = stage.getStartTime().plusHours(1);
        Long stageId = stage.getId();
        TicketCreateRequest request = new TicketCreateRequest(stageId, TicketType.STUDENT, 100, entryTime);

        // when & then
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> ticketService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("입장 시간은 공연 시간보다 빨라야합니다.");
            softly.assertThat(ticketRepository.findAllByStageId(stageId).size()).isEqualTo(0);
        });
    }
}
