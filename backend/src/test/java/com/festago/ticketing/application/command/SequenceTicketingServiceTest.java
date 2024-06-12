package com.festago.ticketing.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import com.festago.common.infrastructure.FakeTicketingRateLimiter;
import com.festago.ticket.domain.FakeTicket;
import com.festago.ticketing.domain.TicketSequence;
import com.festago.ticketing.dto.command.TicketingCommand;
import com.festago.ticketing.repository.MemoryTicketSequenceRepository;
import com.festago.ticketing.repository.TicketSequenceRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SequenceTicketingServiceTest {

    TicketSequenceRepository ticketSequenceRepository;

    @BeforeEach
    void setUp() {
        ticketSequenceRepository = new MemoryTicketSequenceRepository();
    }

    @Nested
    class ticketing {

        Long ticketId = 1L;
        TicketSequence ticketSequence;
        AtomicLong reserveCount;
        SequenceTicketingService sequenceTicketingService;
        TicketingCommandService ticketingCommandService;
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        TicketingCommand command = TicketingCommand.builder()
            .ticketId(ticketId)
            .build();

        @BeforeEach
        void setUp() {
            ticketingCommandService = mock();
            reserveCount = new AtomicLong();
            sequenceTicketingService = new SequenceTicketingService(
                ticketingCommandService,
                ticketSequenceRepository,
                new FakeTicketingRateLimiter(false)
            );
        }

        @Test
        void 티켓팅은_동시성_문제가_발생하지_않아야_한다() {
            // given
            int ticketAmount = 50;
            long tryCount = 100;
            ticketSequence = ticketSequenceRepository.put(new FakeTicket(ticketId, ticketAmount));
            given(ticketingCommandService.ticketing(any(), anyInt())).willAnswer(invoke -> {
                reserveCount.incrementAndGet();
                return null;
            });

            // when
            List<CompletableFuture<Void>> futures = LongStream.rangeClosed(1, tryCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    sequenceTicketingService.ticketing(command);
                }, executorService).exceptionally(throwable -> null))
                .toList();
            futures.forEach(CompletableFuture::join);

            // then
            assertThat(ticketSequence.getQuantity()).isZero();
            assertThat(reserveCount).hasValue(ticketAmount);
        }

        @Test
        void 티켓팅_도중_예외가_발생하면_재고가_복구되고_동시성_문제가_발생하지_않아야_한다() {
            // given
            int ticketAmount = 50;
            long tryCount = 100;
            AtomicLong counter = new AtomicLong();
            ticketSequence = ticketSequenceRepository.put(new FakeTicket(ticketId, ticketAmount));
            given(ticketingCommandService.ticketing(any(), anyInt())).willAnswer(invoke -> {
                long count = counter.incrementAndGet();
                if (count <= ticketAmount / 2 && count % 5 == 0) {
                    throw new IllegalArgumentException();
                }
                reserveCount.incrementAndGet();
                return null;
            });

            // when
            List<CompletableFuture<Void>> futures = LongStream.rangeClosed(1, tryCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    sequenceTicketingService.ticketing(command);
                }, executorService).exceptionally(throwable -> null))
                .toList();
            futures.forEach(CompletableFuture::join);

            // then
            assertThat(ticketSequence.getQuantity()).isZero();
            assertThat(reserveCount).hasValue(ticketAmount);
        }
    }
}
