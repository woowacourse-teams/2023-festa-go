package com.festago.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.application.FestivalService;
import com.festago.application.StageService;
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
import com.festago.support.DatabaseClearExtension;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberFixture;
import com.festago.support.StageFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@ExtendWith(DatabaseClearExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketServiceIntegrationTest extends ApplicationIntegrationTest {

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

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    PlatformTransactionManager tm;

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
    @Sql(scripts = "/ticketing-test-data.sql",
        config = @SqlConfig(transactionMode = TransactionMode.ISOLATED))
    void 동시에_100명이_예약() {
        // given
        int memberCount = 100;
        List<Member> members = createMember(memberCount);
        TicketingRequest request = new TicketingRequest(1L);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<CompletableFuture<Void>> futures = members.stream()
            .map(member -> CompletableFuture.runAsync(() -> {
                ticketService.ticketing(member.getId(), request);
            }, executor).exceptionally(e -> null))
            .toList();

        futures.forEach(CompletableFuture::join);

        assertThat(memberTicketRepository.count()).isEqualTo(50);
    }

    List<Member> createMember(int count) {
        TransactionStatus transaction = tm.getTransaction(
            new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        List<Member> member = IntStream.rangeClosed(1, count)
            .mapToObj(i -> memberRepository.save(MemberFixture.member().build()))
            .toList();
        tm.commit(transaction);
        return member;
    }

    @Test
    @Sql(scripts = "/ticketing-test-data.sql",
        config = @SqlConfig(transactionMode = TransactionMode.ISOLATED))
    void 중복으로_티켓을_예매하면_예외() {
        // given
        Member member = memberRepository.save(MemberFixture.member().build());
        TicketingRequest request = new TicketingRequest(1L);
        Long memberId = member.getId();
        ticketService.ticketing(memberId, request);

        // when & then
        assertThatThrownBy(() -> ticketService.ticketing(memberId, request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("예매 가능한 수량을 초과했습니다.");
    }

    @Test
    void 티켓_생성시_입장_시간이_공연_시간보다_빠르면_예외() {
        // given
        Festival festival = festivalRepository.save(FestivalFixture.festival().build());
        LocalDateTime now = LocalDateTime.now();
        Stage stage = stageRepository.save(StageFixture.stage().festival(festival).startTime(now.plusHours(10))
            .ticketOpenTime(now.plusHours(1)).build());
        LocalDateTime entryTime = stage.getStartTime().plusHours(1);
        Long stageId = stage.getId();
        TicketCreateRequest request = new TicketCreateRequest(stageId, TicketType.STUDENT, 100, entryTime);

        // when & then
        assertThatThrownBy(() -> ticketService.create(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("입장 시간은 공연 시간보다 빨라야합니다.");
    }
}
