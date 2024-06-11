package com.festago.ticketing.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.common.infrastructure.FakeTicketingRateLimiter;
import com.festago.ticket.domain.FakeTicket;
import com.festago.ticketing.domain.TicketQuantity;
import com.festago.ticketing.dto.command.TicketingCommand;
import com.festago.ticketing.repository.MemoryTicketQuantityRepository;
import com.festago.ticketing.repository.TicketQuantityRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuantityTicketingServiceTest {

    QuantityTicketingService quantityTicketingService;

    TicketQuantityRepository ticketQuantityRepository;

    ExecutorService executorService = Executors.newFixedThreadPool(8);

    FakeTicketingRateLimiter fakeMemberRateLimiter = new FakeTicketingRateLimiter(false);

    Long ticketId = 1L;

    @BeforeEach
    void setUp() {
        ticketQuantityRepository = new MemoryTicketQuantityRepository();
    }

    @Test
    void 티켓팅은_동시성_문제가_발생하지_않아야_한다() {
        // given
        int ticketAmount = 50;
        long tryCount = 100;
        TicketQuantity ticketQuantity = ticketQuantityRepository.put(new FakeTicket(1L, ticketAmount));
        AtomicLong reserveCount = new AtomicLong();
        quantityTicketingService = new QuantityTicketingService(ticketQuantityRepository, command -> {
            reserveCount.incrementAndGet();
            return null;
        }, fakeMemberRateLimiter);
        var command = TicketingCommand.builder()
            .ticketId(ticketId)
            .build();

        // when
        List<CompletableFuture<Void>> futures = LongStream.rangeClosed(1, tryCount)
            .mapToObj(i -> CompletableFuture.runAsync(() -> {
                quantityTicketingService.ticketing(command);
            }, executorService).exceptionally(throwable -> null))
            .toList();
        futures.forEach(CompletableFuture::join);

        // then
        assertThat(ticketQuantity.getQuantity()).isZero();
        assertThat(reserveCount).hasValue(ticketAmount);
    }

    @Test
    void 티켓팅_도중_예외가_발생하면_재고가_복구되고_동시성_문제가_발생하지_않아야_한다() {
        // given
        int ticketAmount = 50;
        long tryCount = 100;
        TicketQuantity ticketQuantity = ticketQuantityRepository.put(new FakeTicket(1L, ticketAmount));
        AtomicLong reserveCount = new AtomicLong();
        AtomicLong counter = new AtomicLong();
        quantityTicketingService = new QuantityTicketingService(ticketQuantityRepository, command -> {
            long count = counter.incrementAndGet();
            if (count <= 25 && count % 5 == 0) { // 5번 예외 발생
                throw new IllegalArgumentException();
            }
            reserveCount.incrementAndGet();
            return null;
        }, fakeMemberRateLimiter);
        var command = TicketingCommand.builder()
            .ticketId(ticketId)
            .build();

        // when
        List<CompletableFuture<Void>> futures = LongStream.rangeClosed(1, tryCount)
            .mapToObj(i -> CompletableFuture.runAsync(() -> {
                quantityTicketingService.ticketing(command);
            }, executorService).exceptionally(throwable -> null))
            .toList();
        futures.forEach(CompletableFuture::join);

        // then
        assertThat(ticketQuantity.getQuantity()).isZero();
        assertThat(reserveCount).hasValue(ticketAmount);
    }
}
