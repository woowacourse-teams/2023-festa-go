package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.festago.application.TicketingService;
import com.festago.domain.Member;
import com.festago.domain.MemberRepository;
import com.festago.domain.MemberTicketRepository;
import com.festago.dto.TicketingRequest;
import com.festago.exception.BadRequestException;
import com.festago.stage.domain.Stage;
import com.festago.support.MemberFixture;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketingServiceIntegrationTest extends ApplicationIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TicketingService ticketingService;

    @SpyBean
    MemberTicketRepository memberTicketRepository;

    @SpyBean
    Clock clock;

    @Test
    @Sql("/ticketing-test-data.sql")
    void 동시에_100명이_예약() {
        // given
        int tryCount = 100;
        Member member = memberRepository.save(MemberFixture.member().build());
        TicketingRequest request = new TicketingRequest(1L);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        doReturn(false)
            .when(memberTicketRepository)
            .existsByOwnerAndStage(any(Member.class), any(Stage.class));
        doReturn(Instant.parse("2023-07-24T03:21:31Z"))
            .when(clock)
            .instant();

        // when
        List<CompletableFuture<Void>> futures = IntStream.range(0, tryCount)
            .mapToObj(i -> CompletableFuture.runAsync(() -> {
                ticketingService.ticketing(member.getId(), request);
            }, executor).exceptionally(e -> null))
            .toList();
        futures.forEach(CompletableFuture::join);

        // then
        assertThat(memberTicketRepository.count()).isEqualTo(50);
    }

    @Test
    @Sql("/ticketing-test-data.sql")
    void 하나의_공연에_중복으로_티켓을_예매하면_예외() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());
        TicketingRequest request = new TicketingRequest(1L);
        Long memberId = member.getId();
        doReturn(Instant.parse("2023-07-24T03:21:31Z"))
            .when(clock)
            .instant();

        ticketingService.ticketing(memberId, request);

        // when & then
        assertThatThrownBy(() -> ticketingService.ticketing(memberId, request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("예매 가능한 수량을 초과했습니다.");
    }
}
